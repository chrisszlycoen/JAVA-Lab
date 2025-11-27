import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.*;
import java.sql.*;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UltraFastWebsiteDownloader
 *
 * - Java 21+ recommended (virtual threads)
 * - Uses HttpClient (HTTP/2), async file I/O, and batched DB writes.
 *
 * Usage:
 *  java -Xms512m -Xmx4g UltraFastWebsiteDownloader
 *
 * Edit CONFIG section below to tune for your environment.
 */
public class UltraFastWebsiteDownloader {

    // ==================== CONFIG ====================
    private static final String START_URL = "https://example.com/"; // replace
    private static final Path OUTPUT_DIR = Paths.get("downloaded_site");
    private static final int MAX_CONCURRENT_REQUESTS = 400;    // large number for IO-bound tasks
    private static final int DB_BATCH_SIZE = 200;
    private static final int LINK_EXTRACT_LIMIT_PER_PAGE = 1000;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    private static final int MAX_RETRIES = 3;
    private static final Duration INITIAL_BACKOFF = Duration.ofMillis(200);

    // MySQL config (edit)
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/website_downloader?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "downloader";
    private static final String DB_PASS = "StrongPass123";

    // ==================== INTERNALS ====================
    private final HttpClient http;
    private final ExecutorService workers; // will be virtual-thread executor in Java 21
    private final Semaphore concurrencyLimiter;
    private final BlockingQueue<String> linkQueue = new LinkedBlockingQueue<>();
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final BlockingQueue<DownloadRecord> dbQueue = new LinkedBlockingQueue<>();
    private final AtomicLong downloadedBytes = new AtomicLong();
    private final AtomicLong downloadedFiles = new AtomicLong();

    // regex to quickly extract href/src (fast, not full HTML parser)
    private static final Pattern LINK_PAT = Pattern.compile(
            "(?i)(?:href|src)\\s*=\\s*(['\\\"])(.*?)\\1|" + // quoted
                    "(?i)(?:href|src)\\s*=\\s*([^\\s>]+)");         // unquoted

    // record to send to DB writer
    private static record DownloadRecord(String url, long timeMs, long kb) {}

    public UltraFastWebsiteDownloader() {
        // Build HttpClient with HTTP/2 and reasonable timeouts
        this.http = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newFixedThreadPool(8)) // small pool for HttpClient callbacks
                .build();

        // virtual threads (Java 21). If running older JDK, replace with: Executors.newFixedThreadPool(<size>)
        this.workers = Executors.newVirtualThreadPerTaskExecutor();

        this.concurrencyLimiter = new Semaphore(MAX_CONCURRENT_REQUESTS);
    }

    public void start(String startUrl) throws Exception {
        Files.createDirectories(OUTPUT_DIR);

        // push start url
        String normalizedStart = normalize(startUrl);
        visited.add(normalizedStart);
        linkQueue.add(normalizedStart);

        // start DB writer thread
        Thread dbWriter = new Thread(this::dbWriterLoop, "DB-Writer");
        dbWriter.setDaemon(true);
        dbWriter.start();

        // start main downloader manager threads (one per virtual task)
        // We'll run many worker tasks that take links from queue and process
        int managerCount = Math.max(4, Runtime.getRuntime().availableProcessors());
        CountDownLatch managers = new CountDownLatch(managerCount);

        for (int m = 0; m < managerCount; m++) {
            workers.submit(() -> {
                try {
                    while (true) {
                        String url = linkQueue.poll(5, TimeUnit.SECONDS);
                        if (url == null) {
                            // if queue idle for 5s, we try to stop (but ensure DB queue emptied)
                            if (linkQueue.isEmpty()) break;
                            else continue;
                        }
                        processUrl(url);
                    }
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    managers.countDown();
                }
            });
        }

        // optional progress reporter
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        reporter.scheduleAtFixedRate(() -> {
            System.out.printf("Progress: files=%d  bytes=%,d KB  queue=%d%n",
                    downloadedFiles.get(),
                    downloadedBytes.get() / 1024,
                    linkQueue.size());
        }, 2, 2, TimeUnit.SECONDS);

        managers.await(); // wait until manager tasks finish
        // wait for DB queue flush
        while (!dbQueue.isEmpty()) Thread.sleep(200);
        // stop db writer
        dbWriter.interrupt();

        // shutdown reporter + workers
        reporter.shutdownNow();
        workers.shutdown();
        workers.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("All done. Downloaded files: " + downloadedFiles.get() +
                "  total KB: " + (downloadedBytes.get() / 1024));
    }

    private void processUrl(String url) {
        // concurrency limiter to avoid too many simultaneous requests
        boolean permit = false;
        try {
            concurrencyLimiter.acquire();
            permit = true;
            fetchAndSave(url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (permit) concurrencyLimiter.release();
        }
    }

    private void fetchAndSave(String url) {
        int attempt = 0;
        Duration backoff = INITIAL_BACKOFF;
        while (attempt <= MAX_RETRIES) {
            attempt++;
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(REQUEST_TIMEOUT)
                        .header("User-Agent", "UltraFastWebsiteDownloader/1.0")
                        .GET()
                        .build();

                // async send; we block only to await response body bytes (but underlying IO is async)
                HttpResponse<byte[]> resp = http.send(req, HttpResponse.BodyHandlers.ofByteArray());

                int status = resp.statusCode();
                if (status >= 200 && status < 300) {
                    byte[] body = resp.body();
                    long start = System.currentTimeMillis();
                    Path out = mapUrlToPath(url, resp);
                    writeFileAsync(out, body).get(); // wait for write completion
                    long timeMs = System.currentTimeMillis() - start;

                    long kb = Math.max(1, body.length / 1024);
                    downloadedBytes.addAndGet(body.length);
                    downloadedFiles.incrementAndGet();

                    // enqueue DB record
                    dbQueue.offer(new DownloadRecord(url, timeMs, kb));

                    // fast link extraction and enqueue discovered links
                    String contentType = resp.headers().firstValue("Content-Type").orElse("");
                    if (contentType.toLowerCase().contains("text/html")) {
                        extractLinksAndEnqueue(url, new String(body)); // use default charset — works for many pages
                    }

                    // done
                    return;
                } else {
                    // for 3xx or 4xx/5xx we don't retry for many codes, but allow a few attempts
                    if (attempt > MAX_RETRIES) {
                        System.err.println("Failed " + status + " -> " + url);
                        return;
                    }
                }
            } catch (Exception e) {
                // retry with backoff up to MAX_RETRIES
                if (attempt > MAX_RETRIES) {
                    System.err.println("GIVEUP " + url + " -> " + e.getClass().getSimpleName() + " : " + e.getMessage());
                    return;
                }
                try { Thread.sleep(backoff.toMillis()); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
                backoff = backoff.multipliedBy(2);
            }
        }
    }

    private Future<Void> writeFileAsync(Path out, byte[] content) throws IOException {
        Files.createDirectories(out.getParent());
        AsynchronousFileChannel ch = AsynchronousFileChannel.open(out,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        ByteBuffer buf = ByteBuffer.wrap(content);
        CompletableFuture<Void> future = new CompletableFuture<>();
        ch.write(buf, 0, null, new CompletionHandler<Integer, Void>() {
            @Override public void completed(Integer result, Void attachment) {
                try { ch.close(); } catch (IOException ignored) {}
                future.complete(null);
            }
            @Override public void failed(Throwable exc, Void attachment) {
                try { ch.close(); } catch (IOException ignored) {}
                future.completeExceptionally(exc);
            }
        });
        return future;
    }

    // Map URL to local path inside OUTPUT_DIR
    private Path mapUrlToPath(String urlStr, HttpResponse<byte[]> resp) {
        try {
            URI uri = URI.create(urlStr);
            String path = uri.getPath();
            if (path == null || path.isEmpty()) path = "/index.html";
            if (path.endsWith("/")) path += "index.html";
            if (!path.contains(".")) path += ".html";

            // include query string as safe filename (replace ? and &)
            String qs = uri.getQuery();
            if (qs != null && !qs.isEmpty()) {
                String safeQs = qs.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
                path = path + "__" + safeQs + ".html";
            }
            Path p = OUTPUT_DIR.resolve(uri.getHost()).resolve(path.substring(1));
            return p;
        } catch (Exception e) {
            // fallback
            String safe = urlStr.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
            return OUTPUT_DIR.resolve("misc").resolve(safe);
        }
    }

    // Very fast link extraction (regex). Not a perfect HTML parser, but much faster than DOM parse.
    private void extractLinksAndEnqueue(String baseUrl, String content) {
        Matcher m = LINK_PAT.matcher(content);
        int count = 0;
        while (m.find() && count < LINK_EXTRACT_LIMIT_PER_PAGE) {
            String raw = m.group(2); // quoted
            if (raw == null) raw = m.group(3);
            if (raw == null || raw.isBlank()) continue;

            String resolved = resolveUrl(baseUrl, raw);
            if (resolved == null) continue;

            // normalization: remove fragment, trailing hash, ignore mailto/tel
            if (resolved.startsWith("mailto:") || resolved.startsWith("tel:")) continue;
            int hash = resolved.indexOf('#');
            if (hash != -1) resolved = resolved.substring(0, hash);
            if (resolved.endsWith("/")) resolved = resolved; // keep trailing slash

            // stay on the same host only
            try {
                URI base = URI.create(baseUrl);
                URI r = URI.create(resolved);
                if (!base.getHost().equalsIgnoreCase(r.getHost())) continue;
            } catch (Exception ex) { continue; }

            String normalized = normalize(resolved);
            if (visited.add(normalized)) {
                linkQueue.offer(normalized);
            }
            count++;
        }
    }

    private String resolveUrl(String base, String relative) {
        try {
            URI baseUri = URI.create(base);
            URI resolved = baseUri.resolve(relative);
            return resolved.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String normalize(String url) {
        // remove fragment, trim
        int h = url.indexOf('#');
        if (h >= 0) url = url.substring(0, h);
        return url.trim();
    }

    // Dedicated DB writer: batch writes records to DB to avoid per-file round-trips
    private void dbWriterLoop() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);
            // ensure tables exist
            try (Statement s = conn.createStatement()) {
                s.execute("""
                CREATE TABLE IF NOT EXISTS website (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    website_name VARCHAR(255) UNIQUE NOT NULL,
                    download_start_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                    download_end_date_time DATETIME NULL,
                    total_elapsed_time BIGINT NULL,
                    total_downloaded_kilobytes BIGINT NULL
                )""");
                s.execute("""
                CREATE TABLE IF NOT EXISTS link (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    link_name VARCHAR(2000) NOT NULL,
                    website_id BIGINT,
                    total_elapsed_time BIGINT,
                    total_downloaded_kilobytes BIGINT,
                    FOREIGN KEY (website_id) REFERENCES website(id) ON DELETE CASCADE
                )""");
            }

            // website id upsert
            long websiteId = upsertWebsite(conn, START_URL);

            String insertSql = "INSERT INTO link (link_name, website_id, total_elapsed_time, total_downloaded_kilobytes) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            int batchCount = 0;

            while (!Thread.currentThread().isInterrupted()) {
                DownloadRecord rec = dbQueue.poll(2, TimeUnit.SECONDS);
                if (rec == null) {
                    // flush if batch present
                    if (batchCount > 0) {
                        ps.executeBatch();
                        conn.commit();
                        batchCount = 0;
                    }
                    // if both queues empty and workers done, exit
                    if (linkQueue.isEmpty() && dbQueue.isEmpty() && Thread.currentThread().isInterrupted()) break;
                    // continue waiting
                    continue;
                }
                ps.setString(1, rec.url());
                ps.setLong(2, websiteId);
                ps.setLong(3, rec.timeMs());
                ps.setLong(4, rec.kb());
                ps.addBatch();
                batchCount++;

                if (batchCount >= DB_BATCH_SIZE) {
                    ps.executeBatch();
                    conn.commit();
                    batchCount = 0;
                }
            }
            // final flush
            if (batchCount > 0) {
                ps.executeBatch();
                conn.commit();
            }
            // update website summary
            updateWebsiteSummary(conn, websiteId, downloadedFiles.get(), downloadedBytes.get() / 1024);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("DB writer failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private long upsertWebsite(Connection c, String name) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT IGNORE INTO website (website_name) VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
        try (PreparedStatement ps2 = c.prepareStatement("SELECT id FROM website WHERE website_name = ?")) {
            ps2.setString(1, name);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) return rs.getLong(1);
        }
        throw new SQLException("Unable to upsert website");
    }

    private void updateWebsiteSummary(Connection c, long id, long files, long kb) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE website SET download_end_date_time=NOW(), total_elapsed_time=?, total_downloaded_kilobytes=? WHERE id=?")) {
            ps.setLong(1, 0L);
            ps.setLong(2, kb);
            ps.setLong(3, id);
            ps.executeUpdate();
        }
    }

    // ========== MAIN ==========
    public static void main(String[] args) throws Exception {
        // Optional: allow overriding START_URL by CLI arg
        String start = START_URL;
        if (args.length > 0) start = args[0];

        System.out.println("Starting downloader for: " + start);
        long t0 = System.currentTimeMillis();
        UltraFastWebsiteDownloader d = new UltraFastWebsiteDownloader();
        d.start(start);
        long elapsed = System.currentTimeMillis() - t0;
        System.out.printf("Finished in %,d ms%n", elapsed);
    }
}
