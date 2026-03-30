import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:2006/school_db";
    private static final String username = "StrongUser";
    private static final String password = "StrongPass123";//this is my real password lol;

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,password);
    }
}
