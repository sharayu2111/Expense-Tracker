package com.expensetracker.gui;

import com.expensetracker.dao.ITransactionDAO;
import com.expensetracker.dao.TransactionDAOImpl;
import com.expensetracker.model.Expense;
import com.expensetracker.model.Income;
import com.expensetracker.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTransactionDialog extends JDialog {
    
    private ITransactionDAO dao = new TransactionDAOImpl();
    
    public AddTransactionDialog(JFrame parent, DashboardPanel dashboard) {
        super(parent, "Add Transaction", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        JTextField amountField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Salary", "Food", "Travel", "Shopping", "Bills", "Other"
        });
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField descField = new JTextField();
        
        panel.add(new JLabel("Type:")); panel.add(typeCombo);
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Category:")); panel.add(categoryCombo);
        panel.add(new JLabel("Date (yyyy-MM-dd):")); panel.add(dateField);
        panel.add(new JLabel("Description:")); panel.add(descField);
        
        JButton addBtn = new JButton("Save");
        addBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
                String type = typeCombo.getSelectedItem().toString();
                String category = categoryCombo.getSelectedItem().toString();
                String desc = descField.getText();
                
                Transaction t;
                if(type.equals("INCOME")) {
                    t = new Income(0, amount, category, date, desc);
                } else {
                    t = new Expense(0, amount, category, date, desc);
                }
                
                dao.addTransaction(t);
                dashboard.refreshData();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input: " + ex.getMessage());
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        panel.add(addBtn);
        panel.add(cancelBtn);
        
        add(panel);
    }
}
