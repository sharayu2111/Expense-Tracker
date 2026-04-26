package com.expensetracker.gui;

import com.expensetracker.dao.AdvancedDAO;
import com.expensetracker.dao.TransactionDAOImpl;
import com.expensetracker.model.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdvancedFeaturesDialog extends JDialog {
    private AdvancedDAO advDao = new AdvancedDAO();
    private List<Transaction> allTx;
    
    private final Color MAIN_BG = new Color(245, 247, 250); 
    private final Color BRAND_PURPLE = new Color(94, 53, 177);
    private final Color ACCENT_BLUE = new Color(41, 128, 185);
    private final Color CARD_GREEN = new Color(39, 174, 96);

    public AdvancedFeaturesDialog(JFrame parent) {
        super(parent, "🚀 Advanced & Pro Features Hub", true);
        setSize(1000, 750);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(MAIN_BG);
        
        try {
            allTx = new TransactionDAOImpl().getAllTransactions();
        } catch(Exception e) {}

        UIManager.put("TabbedPane.selected", Color.WHITE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabs.setBackground(new Color(220, 225, 230));
        tabs.setForeground(new Color(50, 50, 50));
        
        tabs.addTab("📈 Cash Flow Graph", createCashFlowTab());
        tabs.addTab("🎯 Savings Goals", createGoalsTab());
        tabs.addTab("🤝 Split Expenses", createSplitsTab());
        tabs.addTab("🔁 Subscriptions", createSubsTab());
        
        add(tabs);
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setSelectionBackground(new Color(225, 235, 245));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(BRAND_PURPLE);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 45));
    }
    
    private JButton createActionBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        return btn;
    }

    private JPanel createCashFlowTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double totalInc = 0;
        double totalExp = 0;
        
        for (Transaction t : allTx) {
            String shortDate = new java.text.SimpleDateFormat("dd MMM").format(t.getDate());
            if (t.getTransactionType().equals("INCOME")) {
                totalInc += t.getAmount();
                dataset.addValue(t.getAmount(), "Income", shortDate);
            } else {
                totalExp += t.getAmount();
                dataset.addValue(t.getAmount(), "Expense", shortDate);
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart("Income vs Expense Trendline", "Date", "Amount (₹)", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(MAIN_BG);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, CARD_GREEN);
        renderer.setSeriesPaint(1, new Color(231, 76, 60)); 
        
        ChartPanel cp = new ChartPanel(chart);
        cp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        JLabel ratioLabel = new JLabel();
        ratioLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        if (totalInc > totalExp) {
            ratioLabel.setText("🟢 FINANCES HEALTHY: You have a Surplus of ₹" + (totalInc-totalExp));
            ratioLabel.setForeground(CARD_GREEN);
        } else {
            ratioLabel.setText("🔴 OVERSPENDING: You have a Deficit of ₹" + (totalExp-totalInc));
            ratioLabel.setForeground(new Color(231, 76, 60));
        }
        header.add(ratioLabel, BorderLayout.CENTER);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(cp, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createGoalsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(MAIN_BG);
        
        for (Map<String, Object> goal : advDao.getGoals()) {
            JPanel item = new JPanel(new BorderLayout(0, 10));
            item.setBackground(Color.WHITE);
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            double target = (double) goal.get("target");
            double saved = (double) goal.get("saved");
            int pct = (int)((saved / target) * 100);
            
            JLabel name = new JLabel("🎯 " + goal.get("name") + "  (Saved: ₹" + saved + " / Target: ₹" + target + ")");
            name.setFont(new Font("Segoe UI", Font.BOLD, 18));
            name.setForeground(BRAND_PURPLE);
            
            JProgressBar pb = new JProgressBar(0, 100);
            pb.setValue(pct);
            pb.setStringPainted(true);
            pb.setString(pct + "% Completed");
            pb.setFont(new Font("Segoe UI", Font.BOLD, 14));
            pb.setForeground(ACCENT_BLUE);
            pb.setBackground(new Color(230, 230, 230));
            pb.setPreferredSize(new Dimension(800, 35));
            
            item.add(name, BorderLayout.NORTH);
            item.add(pb, BorderLayout.CENTER);
            
            listPanel.add(item);
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        JButton addBtn = createActionBtn("➕ Add New Financial Goal", ACCENT_BLUE);
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Goal Name (e.g., Buy iPhone):");
            if (name != null) {
                double target = Double.parseDouble(JOptionPane.showInputDialog("Target Amount:"));
                double saved = Double.parseDouble(JOptionPane.showInputDialog("Currently Saved:"));
                advDao.addGoal(name, target, saved);
                dispose(); new AdvancedFeaturesDialog((JFrame)getParent()).setVisible(true);
            }
        });
        
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(addBtn, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createSplitsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        String[] cols = {"Group", "Description", "Total Bill", "People", "Your Split Amount"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        
        for (Map<String, Object> s : advDao.getSplits()) {
            model.addRow(new Object[]{s.get("group"), s.get("desc"), "₹"+s.get("total"), s.get("parts"), "₹"+s.get("owe")});
        }
        JTable table = new JTable(model);
        styleTable(table);
        
        JScrollPane jsp = new JScrollPane(table);
        jsp.getViewport().setBackground(Color.WHITE);
        jsp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JButton addBtn = createActionBtn("🤝 Split New Expense", BRAND_PURPLE);
        addBtn.addActionListener(e -> {
            String group = JOptionPane.showInputDialog("Group Name:");
            if (group != null) {
                String desc = JOptionPane.showInputDialog("Description:");
                double total = Double.parseDouble(JOptionPane.showInputDialog("Total Bill:"));
                int parts = Integer.parseInt(JOptionPane.showInputDialog("How many people sharing?"));
                double owe = total / parts;
                advDao.addSplit(group, desc, total, parts, owe);
                dispose(); new AdvancedFeaturesDialog((JFrame)getParent()).setVisible(true);
            }
        });
        
        panel.add(jsp, BorderLayout.CENTER);
        panel.add(addBtn, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createSubsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        String[] cols = {"Service Name", "Monthly Cost", "Recurring Due Day"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        
        double monthlyTotal = 0;
        for (Map<String, Object> s : advDao.getSubscriptions()) {
            double c = (double)s.get("cost");
            monthlyTotal += c;
            model.addRow(new Object[]{s.get("name"), "₹"+c, s.get("due_day") + "th of month"});
        }
        
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane jsp = new JScrollPane(table);
        jsp.getViewport().setBackground(Color.WHITE);
        jsp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(new Color(41, 128, 185)); // Solid blue header
        JLabel totalLabel = new JLabel("💸 Total Monthly Subscriptions Overhead: ₹" + monthlyTotal);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.add(totalLabel);
        
        JButton addBtn = createActionBtn("➕ Register New Subscription", new Color(46, 204, 113));
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Subscription Name (e.g. Netflix):");
            if (name != null) {
                double cost = Double.parseDouble(JOptionPane.showInputDialog("Monthly Cost:"));
                int day = Integer.parseInt(JOptionPane.showInputDialog("Due Day (1-31):"));
                advDao.addSubscription(name, cost, day);
                dispose(); new AdvancedFeaturesDialog((JFrame)getParent()).setVisible(true);
            }
        });
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(jsp, BorderLayout.CENTER);
        panel.add(addBtn, BorderLayout.SOUTH);
        return panel;
    }
}
