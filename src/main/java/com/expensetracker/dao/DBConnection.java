package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // MySQL URL with createDatabaseIfNotExist flag so it acts somewhat like
    // SQLite's auto-file creation
    private static final String URL = "jdbc:mysql://localhost:3306/expensetracker?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Update this with your actual MySQL password

    private DBConnection() {
    } // Utility class

    public static Connection getConnection() {
        try {
            // Ensure the MySQL driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Always return a new connection to support try-with-resources
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }
}
