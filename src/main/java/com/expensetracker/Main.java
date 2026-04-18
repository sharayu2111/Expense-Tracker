package com.expensetracker;

import com.expensetracker.dao.SetupDB;
import com.expensetracker.gui.MainFrame;
import com.expensetracker.web.WebServer;

public class Main {
    public static void main(String[] args) {

        System.out.println("Initializing Database...");
        SetupDB.initialize();

        
        System.out.println("Starting Web Server Thread...");
        WebServer.start();

        
        System.out.println("Starting Swing Application...");
        MainFrame.startGUI();
    }
}
