package com.expensetracker.gui;

import com.expensetracker.dao.ITransactionDAO;
import com.expensetracker.dao.TransactionDAOImpl;
import com.expensetracker.model.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private MainFrame parent;
    private ITransactionDAO dao;
    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private DefaultTableModel tableModel;
    private JPanel chartContainer;
    
    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        this.dao = new TransactionDAOImpl();
        setLayout(new BorderLayout());
        
        // Ensure multithreading loads the UI without blocking
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Initialize components
                initUI();
                return null;
            }
            
            @Override
            protected void done() {
                refreshData();
            }
        };
        worker.execute();
    }
    
    private void initUI() {
        JPanel topPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        balanceLabel = createSummaryCard("Total Balance", "₹0.00", new Color(46, 204, 113));
        incomeLabel = createSummaryCard("Total Income", "₹0.00", new Color(52, 152, 219));
        expenseLabel = createSummaryCard("Total Expense", "₹0.00", new Color(231, 76, 60));
        
        JPanel actionCard = new JPanel(new GridLayout(2,1, 5,5));
        JButton addBtn = new JButton("Add Transaction");
        addBtn.addActionListener(e -> {
            new AddTransactionDialog(parent, this).setVisible(true);
        });
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> parent.setContentPane(new LoginPanel(parent)));
        
        actionCard.add(addBtn);
        actionCard.add(logoutBtn);
        
        topPanel.add((Component)balanceLabel.getParent());
        topPanel.add((Component)incomeLabel.getParent());
        topPanel.add((Component)expenseLabel.getParent());
        topPanel.add(actionCard);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center: Table and Chart
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        String[] columns = {"ID", "Type", "Amount", "Category", "Date", "Description"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        centerPanel.add(new JScrollPane(table));
        
        chartContainer = new JPanel(new BorderLayout());
        centerPanel.add(chartContainer);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JLabel createSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return valueLabel;
    }
    
    public void refreshData() {
        // Run database fetch in background
        SwingWorker<List<Transaction>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Transaction> doInBackground() throws Exception {
                return dao.getAllTransactions();
            }

            @Override
            protected void done() {
                try {
                    List<Transaction> transactions = get();
                    updateTable(transactions);
                    updateSummary(transactions);
                    updateChart(transactions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void updateTable(List<Transaction> tx) {
        tableModel.setRowCount(0);
        for (Transaction t : tx) {
            tableModel.addRow(new Object[]{
                t.getId(), t.getTransactionType(), t.getAmount(),
                t.getCategory(), new java.text.SimpleDateFormat("yyyy-MM-dd").format(t.getDate()),
                t.getDescription()
            });
        }
    }
    
    private void updateSummary(List<Transaction> tx) {
        double income = 0;
        double exp = 0;
        for (Transaction t : tx) {
            if (t.getTransactionType().equals("INCOME")) income += t.getAmount();
            else exp += t.getAmount();
        }
        incomeLabel.setText(String.format("₹%.2f", income));
        expenseLabel.setText(String.format("₹%.2f", exp));
        balanceLabel.setText(String.format("₹%.2f", (income - exp)));
        
        if ((income - exp) < 0) {
            JOptionPane.showMessageDialog(this, "Budget Alert: Your expenses exceed your income!");
        }
    }
    
    private void updateChart(List<Transaction> tx) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Transaction t : tx) {
            if (t.getTransactionType().equals("EXPENSE")) {
                Number current = dataset.getValue(t.getCategory());
                dataset.setValue(t.getCategory(), (current == null ? 0 : current.doubleValue()) + t.getAmount());
            }
        }
        
        JFreeChart chart = ChartFactory.createPieChart("Expense Breakdown", dataset, true, true, false);
        chartContainer.removeAll();
        chartContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }
}
