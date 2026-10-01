package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {
    private static final String URL = "jdbc:postgresql://localhost:5432/sidney";
    private static final String USER = "sidney";
    private static final String PASSWORD = System.getenv("123456789");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null) {
            throw new SQLException("Variável de ambiente DB_PASSWORD não definida.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}