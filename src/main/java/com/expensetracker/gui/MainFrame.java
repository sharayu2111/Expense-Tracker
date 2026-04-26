package com.expensetracker.gui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Personal Expense Tracker");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize the first panel (Standard Login Screen)
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
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                UIManager.put("Button.arc", 10);
                UIManager.put("Component.arc", 10);
                UIManager.put("ProgressBar.arc", 10);
                UIManager.put("TextComponent.arc", 10);
            } catch (Exception ex) {
                System.err.println("Failed to initialize Flatlaf");
            }
            new MainFrame().setVisible(true);
        });
    }
}
