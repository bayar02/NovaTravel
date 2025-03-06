package tn.esprit.monji.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://127.0.0.1:3306/nova_travel";
    private final String username = "root";
    private final String password = "";

    private DatabaseConnection() {
        try {
            connection = createConnection();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            throw new RuntimeException("Error connecting to the database: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if connection is closed or invalid
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking database connection: " + e.getMessage());
        }
    }

    private Connection createConnection() throws SQLException {
        // Debugging connection attempt
        System.out.println("Connecting to database with URL: " + url + " and username: " + username);
        return DriverManager.getConnection(url, username, password);
    }

    // Method to explicitly close connection when needed (e.g., application shutdown)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection: " + e.getMessage());
        }
    }
} 