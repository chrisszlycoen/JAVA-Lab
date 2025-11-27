import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Insert10MillionMultiThread {

    static final String URL  = "jdbc:mysql://localhost:3306/testDB";
    static final String USER = "root";
    static final String PASS = "callm3wh3ny0uw1";

    static final int TOTAL = 10_000_000;
    static final int THREADS = 8;
    static final int BATCH_SIZE = 20_000;

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        int perThread = TOTAL / THREADS;
        Thread[] workers = new Thread[THREADS];

        for (int t = 0; t < THREADS; t++) {
            int threadId = t;
            int startId = t * perThread + 1;
            int endId   = (t + 1) * perThread;

            workers[t] = new Thread(() -> insertRange(threadId, startId, endId));
            workers[t].start();
        }

        for (Thread w : workers) w.join();

        long sec = (System.currentTimeMillis() - start) / 1000;
        System.out.println("\n==========================================");
        System.out.println("INSERT COMPLETE: 10,000,000 rows");
        System.out.println("Total time: " + sec + " sec  (" + (sec / 60) + " minutes)");
        System.out.println("==========================================");
    }


    static void insertRange(int threadId, int startId, int endId) {
        String sql = "INSERT INTO Person (firstname, lastname, dob, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = startId; i <= endId; i++) {

                // FAST synthetic data
                ps.setString(1, "FN" + i);
                ps.setString(2, "LN" + i);
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis() - (i % 20000L * 86400000L)));
                ps.setString(4, "user" + i + "@example.com");

                ps.addBatch();

                if (i % BATCH_SIZE == 0) {
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("Thread %d: inserted %,d/%d%n",
                            threadId, (i - startId), (endId - startId));
                }
            }

            ps.executeBatch();
            conn.commit();

            System.out.printf("Thread %d: FINISHED (%,d records)\n",
                    threadId, (endId - startId));
        }
        catch (Exception e) {
            System.out.println("Thread " + threadId + " ERROR: " + e.getMessage());
        }
    }
}
