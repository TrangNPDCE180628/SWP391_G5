package Ultis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author DBContext
 */
public class DBContext {
    public static final Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=TechStore1;"
                + "encrypt=true;"
                + "trustServerCertificate=true";
        conn = DriverManager.getConnection(url, "sa", "1");
        return conn;
    }
}
