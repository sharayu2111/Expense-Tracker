package com.expensetracker.dao;

import java.sql.*;
import java.util.*;

public class AdvancedDAO {
    
    // Validate PIN
    public boolean validatePIN(String pin) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE pin = ?")) {
            ps.setString(1, pin);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Add/Get Goals
    public void addGoal(String name, double target, double saved) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO goals (name, target, saved) VALUES (?,?,?)")) {
            ps.setString(1, name);
            ps.setDouble(2, target);
            ps.setDouble(3, saved);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Map<String, Object>> getGoals() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM goals")) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", rs.getString("name"));
                map.put("target", rs.getDouble("target"));
                map.put("saved", rs.getDouble("saved"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    // Add/Get Subscriptions
    public void addSubscription(String name, double cost, int dueDay) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO subscriptions (name, cost, due_day) VALUES (?,?,?)")) {
            ps.setString(1, name);
            ps.setDouble(2, cost);
            ps.setInt(3, dueDay);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public List<Map<String, Object>> getSubscriptions() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM subscriptions")) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("cost", rs.getDouble("cost"));
                map.put("due_day", rs.getInt("due_day"));
                map.put("status", rs.getString("status"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    // Add/Get Splits
    public void addSplit(String group, String desc, double total, int parts, double youOwe) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO splits (group_name, description, total_amount, participants, you_owe) VALUES (?,?,?,?,?)")) {
            ps.setString(1, group);
            ps.setString(2, desc);
            ps.setDouble(3, total);
            ps.setInt(4, parts);
            ps.setDouble(5, youOwe);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public List<Map<String, Object>> getSplits() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM splits")) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("group", rs.getString("group_name"));
                map.put("desc", rs.getString("description"));
                map.put("total", rs.getDouble("total_amount"));
                map.put("parts", rs.getInt("participants"));
                map.put("owe", rs.getDouble("you_owe"));
                map.put("status", rs.getString("status"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public void toggleSplitStatus(int id) {
        String currentStatus = "DUE"; double owe = 0; String desc = "";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM splits WHERE id = ?")) {
            ps.setInt(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) { currentStatus = rs.getString("status"); owe = rs.getDouble("you_owe"); desc = rs.getString("group_name") + ": " + rs.getString("description"); }
        } catch(SQLException e) {}
        
        String newStatus = "DUE".equals(currentStatus) ? "PAID" : "DUE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE splits SET status = ? WHERE id = ?")) {
            ps.setString(1, newStatus); ps.setInt(2, id); ps.executeUpdate();
        } catch (SQLException e) {}
        
        if ("PAID".equals(newStatus)) {
            new TransactionDAOImpl().addTransaction(new com.expensetracker.model.Expense(0, owe, "Splitwise", new java.util.Date(), desc + " (Logged as Paid)"));
        }
    }

    public void toggleSubStatus(int id) {
        String currentStatus = "DUE"; double cost = 0; String name = "";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM subscriptions WHERE id = ?")) {
            ps.setInt(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) { currentStatus = rs.getString("status"); cost = rs.getDouble("cost"); name = rs.getString("name"); }
        } catch(SQLException e) {}
        
        String newStatus = "DUE".equals(currentStatus) ? "PAID" : "DUE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE subscriptions SET status = ? WHERE id = ?")) {
            ps.setString(1, newStatus); ps.setInt(2, id); ps.executeUpdate();
        } catch (SQLException e) {}
        
        if ("PAID".equals(newStatus)) {
            new TransactionDAOImpl().addTransaction(new com.expensetracker.model.Expense(0, cost, "Subscription", new java.util.Date(), name + " (Logged as Paid)"));
        }
    }
}
