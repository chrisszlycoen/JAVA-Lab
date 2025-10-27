package Activity.File_Manipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

public class RwandaData {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/RwandaData";
        String username = "root";
        String password = "callm3wh3ny0uw1";
        String basePath = "/home/code888/Documents/JAVA/Activity/File_Manipulation/admin_entities/";

        // Map table names to their insert statements and expected number of columns
        Map<String, String> tables = new LinkedHashMap<>();
        tables.put("province", "INSERT INTO province (province_code, province_name) VALUES (?, ?)");
        tables.put("district", "INSERT INTO district (district_code, province_code, district_name) VALUES (?, ?, ?)");
        tables.put("sector", "INSERT INTO sector (sector_code, district_code, sector_name) VALUES (?, ?, ?)");
        tables.put("cell", "INSERT INTO cell (cell_code, sector_code, cell_name) VALUES (?, ?, ?)");
        tables.put("village", "INSERT INTO village (village_code, cell_code, village_name) VALUES (?, ?, ?)");

        try (Connection conn = DriverManager.getConnection(jdbcURL, username, password)) {
            conn.setAutoCommit(false);

            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String table = entry.getKey();
                String sql = entry.getValue();
                String filePath = basePath + capitalize(table) + ".txt";

                System.out.println("\nLoading data from: " + filePath);

                try (BufferedReader br = new BufferedReader(new FileReader(filePath));
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    String line;
                    int count = 0, batchSize = 1000;

                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length < 2) continue; // Skip invalid lines

                        // Bind parameters dynamically based on columns in the line
                        for (int i = 0; i < parts.length; i++) {
                            ps.setString(i + 1, parts[i].trim());
                        }

                        ps.addBatch();
                        if (++count % batchSize == 0) ps.executeBatch();
                    }

                    ps.executeBatch();
                    System.out.println(count + " records inserted into " + table);
                } catch (Exception e) {
                    System.err.println("Failed loading " + table + ": " + e.getMessage());
                }
            }

            conn.commit();
            System.out.println("\nAll data imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
