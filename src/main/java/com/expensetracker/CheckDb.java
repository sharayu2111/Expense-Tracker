package com.expensetracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckDb {
    public static void main(String[] args) {
        System.out.println("\n==================================");
        System.out.println("FETCHING ALL DATA DIRECTLY FROM MYSQL:");
        System.out.println("==================================");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/expensetracker?createDatabaseIfNotExist=true", "root", "root");
            Statement stmt = conn.createStatement();
            
            // Just double checking if table exists, create if not to avoid crash
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "type VARCHAR(50) NOT NULL," +
                         "amount DOUBLE NOT NULL," +
                         "category VARCHAR(100) NOT NULL," +
                         "date VARCHAR(50) NOT NULL," +
                         "description TEXT" +
                         ");");

            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
            
            boolean found = false;
            while(rs.next()) {
                found = true;
                System.out.println(">> Row " + rs.getInt("id") + ": " 
                                   + rs.getString("type") + " | Rs " 
                                   + rs.getDouble("amount") + " | " 
                                   + rs.getString("category") + " | " 
                                   + rs.getString("description"));
            }
            if(!found) {
                System.out.println(">> NO DATA RETURNED! The database is empty.");
                System.out.println(">> If you added something on the web, it did not save successfully! Check your form inputs.");
            }
        } catch (Exception e) {
            System.out.println("ERROR Connecting to MySQL: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("==================================\n");
    }
}
