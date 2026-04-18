package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:expensetracker.db";

    private DBConnection() {} // Utility class

    public static Connection getConnection() {
        try {
            // Ensure the driver is loaded
            Class.forName("org.sqlite.JDBC");
            // Always return a new connection to support try-with-resources
            return DriverManager.getConnection(URL);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }
}
