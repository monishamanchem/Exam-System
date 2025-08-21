package Online_exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/online_exam";
    private static final String USER = "root"; // ✅ Fix this
    private static final String PASSWORD = "Mmonisha.86";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
