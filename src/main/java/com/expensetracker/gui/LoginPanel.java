package com.expensetracker.gui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    
    private MainFrame parent;
    
    public LoginPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Personal Expense Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        loginBtn.addActionListener(e -> {
            String uname = usernameField.getText();
            String pwd = new String(passwordField.getPassword());
            
            if (uname.equals("admin") && pwd.equals("admin")) {
                parent.setContentPane(new PinLoginPanel(parent));
                parent.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        formPanel.add(title);
        formPanel.add(usernameField);
        formPanel.add(passwordField);
        formPanel.add(loginBtn);
        
        add(formPanel);
    }
}
