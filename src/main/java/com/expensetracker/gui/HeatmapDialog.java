package com.expensetracker.gui;

import com.expensetracker.model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HeatmapDialog extends JDialog {
    public HeatmapDialog(JFrame parent, List<Transaction> transactions) {
        super(parent, "Expense Heatmap (Last 35 Days)", true);
        setSize(600, 350);
        setLocationRelativeTo(parent);
        
        JPanel grid = new JPanel(new GridLayout(5, 7, 5, 5));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Group expenses by Date
        Map<String, Double> expensesByDate = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        double maxExpense = 0.0;

        for (Transaction t : transactions) {
            if ("EXPENSE".equals(t.getTransactionType())) {
                String d = sdf.format(t.getDate());
                double newAmt = expensesByDate.getOrDefault(d, 0.0) + t.getAmount();
                expensesByDate.put(d, newAmt);
                if (newAmt > maxExpense) maxExpense = newAmt;
            }
        }

        // Draw 35 squares representing the last 35 days heatmap
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -34); 
        
        for (int i = 0; i < 35; i++) {
            String d = sdf.format(cal.getTime());
            double amt = expensesByDate.getOrDefault(d, 0.0);
            
            JPanel dayPanel = new JPanel();
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayPanel.setToolTipText(d + " | Spent: " + amt);
            
            if (amt == 0) {
                dayPanel.setBackground(new Color(245, 245, 245));
            } else {
                // Color intensity calculation (pure Red)
                float intensity = (float)(amt / maxExpense);
                if (intensity < 0.2f) intensity = 0.2f; // Ensure at least a little red
                int red = 255;
                int gb = (int)(255 * (1 - intensity)); // Decreases green and blue to make it pure darker red
                dayPanel.setBackground(new Color(Math.max(100, red - (int)(intensity*100)), Math.max(0, gb), Math.max(0, gb)));
            }
            grid.add(dayPanel);
            
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        add(new JLabel("Expense Heatmap (Hover boxes to view daily totals)", SwingConstants.CENTER), BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }
}
