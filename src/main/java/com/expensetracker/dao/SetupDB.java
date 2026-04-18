package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDB {
    public static void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "type TEXT NOT NULL," +
                     "amount REAL NOT NULL," +
                     "category TEXT NOT NULL," +
                     "date TEXT NOT NULL," +
                     "description TEXT" +
                     ");";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
