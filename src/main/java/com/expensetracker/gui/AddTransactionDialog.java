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
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        JTextField amountField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Salary", "Food", "Travel", "Shopping", "Bills", "Other"
        });
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField descField = new JTextField();
        
        JButton scannerBtn = new JButton("📷 Scan Receipt");
        scannerBtn.addActionListener(e -> simulateReceiptScan(amountField, categoryCombo, descField));
        
        panel.add(new JLabel("Smart Feature:")); panel.add(scannerBtn);
        
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
    
    // Receipt Scanner (Simulated via File Chooser & Delays)
    private void simulateReceiptScan(JTextField amountField, JComboBox<String> catBox, JTextField descField) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Upload Receipt Image");
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Using a background thread to simulate AI parsing delay!
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1500); // simulate 1.5 seconds AI scanning
                    return null;
                }
                @Override
                protected void done() {
                    // Better Parsing Logic: Try to extract actual numbers from the uploaded filename!
                    String filename = jfc.getSelectedFile().getName();
                    String extractedAmount = "250.00"; // fallback
                    
                    // Parse logic: If filename has numbers (like "receipt_500.png" -> extracts "500")
                    String parsedNumbers = filename.replaceAll("[^0-9]", "");
                    if (!parsedNumbers.isEmpty()) {
                        extractedAmount = parsedNumbers;
                    } else {
                        // Or use file size bytes to simulate a specific static amount
                        long sizeBytes = jfc.getSelectedFile().length();
                        extractedAmount = String.valueOf((sizeBytes % 1500) + 10);
                    }
                    
                    amountField.setText(extractedAmount);
                    catBox.setSelectedItem("Food");
                    descField.setText("Scanned from: " + filename);
                    JOptionPane.showMessageDialog(AddTransactionDialog.this, "Receipt parsed successfully! Found amount based on file properties.");
                }
            };
            worker.execute();
        }
    }
}
