package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDB {
    public static void initialize() {
        String[] tables = {
            "CREATE TABLE IF NOT EXISTS transactions (id INT AUTO_INCREMENT PRIMARY KEY, type VARCHAR(50) NOT NULL, amount DOUBLE NOT NULL, category VARCHAR(100) NOT NULL, date VARCHAR(50) NOT NULL, description TEXT);",
            "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, pin VARCHAR(255) NOT NULL);",
            "CREATE TABLE IF NOT EXISTS goals (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), target DOUBLE, saved DOUBLE);",
            "CREATE TABLE IF NOT EXISTS subscriptions (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), cost DOUBLE, due_day INT, status VARCHAR(20) DEFAULT 'DUE');",
            "CREATE TABLE IF NOT EXISTS splits (id INT AUTO_INCREMENT PRIMARY KEY, group_name VARCHAR(100), description VARCHAR(200), total_amount DOUBLE, participants INT, you_owe DOUBLE, status VARCHAR(20) DEFAULT 'DUE');"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
            // Try to force add status columns if they didn't exist 
            try { stmt.execute("ALTER TABLE subscriptions ADD COLUMN status VARCHAR(20) DEFAULT 'DUE'"); } catch(Exception ignored){}
            try { stmt.execute("ALTER TABLE splits ADD COLUMN status VARCHAR(20) DEFAULT 'DUE'"); } catch(Exception ignored){}
            
            for (String sql : tables) {
                stmt.execute(sql);
            }
            
            // Insert default PIN '1234' if no user exists
            java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (pin) VALUES ('1234')");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
