package com.expensetracker.gui;

import com.expensetracker.dao.AdvancedDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PinLoginPanel extends JPanel {
    
    private MainFrame parent;
    private JPasswordField pinField;
    
    public PinLoginPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 50, 40, 50)
        ));
        
        JLabel lockIcon = new JLabel("🔒", SwingConstants.CENTER);
        lockIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("Enter 4-Digit PIN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pinField = new JPasswordField(4);
        pinField.setFont(new Font("Segoe UI", Font.BOLD, 32));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setMaximumSize(new Dimension(150, 50));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinField.setEchoChar('●');
        
        JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
        keypad.setBackground(Color.WHITE);
        keypad.setMaximumSize(new Dimension(250, 300));
        
        ActionListener numListener = e -> {
            String current = new String(pinField.getPassword());
            if(current.length() < 4) {
                pinField.setText(current + ((JButton)e.getSource()).getText());
            }
        };
        
        for (int i = 1; i <= 9; i++) {
            JButton btn = createKeypadBtn(String.valueOf(i));
            btn.addActionListener(numListener);
            keypad.add(btn);
        }
        
        JButton clearBtn = createKeypadBtn("C");
        clearBtn.setForeground(Color.RED);
        clearBtn.addActionListener(e -> pinField.setText(""));
        keypad.add(clearBtn);
        
        JButton zeroBtn = createKeypadBtn("0");
        zeroBtn.addActionListener(numListener);
        keypad.add(zeroBtn);
        
        JButton enterBtn = createKeypadBtn("✔");
        enterBtn.setForeground(new Color(46, 204, 113));
        enterBtn.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            if (new AdvancedDAO().validatePIN(pin)) {
                parent.loadDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Access Denied", JOptionPane.ERROR_MESSAGE);
                pinField.setText("");
            }
        });
        keypad.add(enterBtn);
        
        card.add(lockIcon);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(pinField);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(keypad);
        
        add(card);
    }
    
    private JButton createKeypadBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
