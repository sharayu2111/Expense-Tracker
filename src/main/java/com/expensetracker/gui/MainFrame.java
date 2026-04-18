package com.expensetracker.gui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Personal Expense Tracker");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Show Login Panel first
        setContentPane(new LoginPanel(this));
    }
    
    public void loadDashboard() {
        setContentPane(new DashboardPanel(this));
        revalidate();
        repaint();
    }

    public static void startGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ex) {
                System.err.println("Failed to initialize Flatlaf");
            }
            new MainFrame().setVisible(true);
        });
    }
}
