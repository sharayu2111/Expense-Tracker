package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.model.Income;
import com.expensetracker.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements ITransactionDAO {

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void addTransaction(Transaction t) {
        String query = "INSERT INTO transactions (type, amount, category, date, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, t.getTransactionType());
            pstmt.setDouble(2, t.getAmount());
            pstmt.setString(3, t.getCategory());
            pstmt.setString(4, df.format(t.getDate()));
            pstmt.setString(5, t.getDescription());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTransaction(Transaction t) {
        String query = "UPDATE transactions SET amount=?, category=?, date=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setDouble(1, t.getAmount());
            pstmt.setString(2, t.getCategory());
            pstmt.setString(3, df.format(t.getDate()));
            pstmt.setString(4, t.getDescription());
            pstmt.setInt(5, t.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTransaction(int id) {
        String query = "DELETE FROM transactions WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE type=? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, type);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractFromResultSet(rs));
                }
            }
            
        } catch (Exception e) {
             e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getTotalBalance() {
        double balance = 0.0;
        String query = "SELECT type, amount FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
             
             while(rs.next()) {
                 if (rs.getString("type").equalsIgnoreCase("INCOME")) {
                     balance += rs.getDouble("amount");
                 } else {
                     balance -= rs.getDouble("amount");
                 }
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }
    
    private Transaction extractFromResultSet(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String type = rs.getString("type");
        double amount = rs.getDouble("amount");
        String category = rs.getString("category");
        java.util.Date date = df.parse(rs.getString("date"));
        String desc = rs.getString("description");
        
        if ("INCOME".equalsIgnoreCase(type)) {
            return new Income(id, amount, category, date, desc);
        } else {
            return new Expense(id, amount, category, date, desc);
        }
    }
}
