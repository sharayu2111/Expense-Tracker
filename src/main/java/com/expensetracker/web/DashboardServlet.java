package com.expensetracker.web;

import com.expensetracker.dao.ITransactionDAO;
import com.expensetracker.dao.TransactionDAOImpl;
import com.expensetracker.dao.AdvancedDAO;
import com.expensetracker.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardServlet extends HttpServlet {
    
    private ITransactionDAO dao;
    private static final double PREDEFINED_BUDGET = 50000.0;
    
    @Override
    public void init() throws ServletException {
        dao = new TransactionDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        List<Transaction> transactions = dao.getAllTransactions();
        
        double totalIncome = 0;
        double totalExpense = 0;
        double monthlyIncome = 0;
        double monthlyExpense = 0;
        double todayExpense = 0;
        
        Map<String, Double> categoryBreakdown = new HashMap<>();
        
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String todayString = sdf.format(new Date());

        for(Transaction t : transactions) {
            boolean isIncome = "INCOME".equalsIgnoreCase(t.getTransactionType());
            
            cal.setTime(t.getDate());
            boolean isCurrentMonth = (cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear);
            boolean isToday = sdf.format(t.getDate()).equals(todayString);
            
            if(isIncome) {
                totalIncome += t.getAmount();
                if (isCurrentMonth) monthlyIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
                if (isCurrentMonth) monthlyExpense += t.getAmount();
                if (isToday) todayExpense += t.getAmount();
                
                // Category breakdown
                categoryBreakdown.put(t.getCategory(), categoryBreakdown.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        
        double balance = totalIncome - totalExpense;
        double savingsRate = totalIncome > 0 ? ((balance) / totalIncome) * 100.0 : 0;
        
        // Highest spending category
        String highestCategory = "None";
        double highestCategoryVal = 0;
        for (Map.Entry<String, Double> entry : categoryBreakdown.entrySet()) {
            if (entry.getValue() > highestCategoryVal) {
                highestCategoryVal = entry.getValue();
                highestCategory = entry.getKey();
            }
        }
        
        // Pass data to JSP properly
        List<Transaction> recentTransactions = transactions.size() > 5 ? transactions.subList(0, 5) : transactions;
        
        req.setAttribute("transactions", transactions);
        req.setAttribute("recentTransactions", recentTransactions);
        req.setAttribute("totalIncome", totalIncome);
        req.setAttribute("totalExpense", totalExpense);
        req.setAttribute("balance", balance);
        
        req.setAttribute("monthlyIncome", monthlyIncome);
        req.setAttribute("monthlyExpense", monthlyExpense);
        req.setAttribute("todayExpense", todayExpense);
        req.setAttribute("savingsRate", savingsRate);
        req.setAttribute("budgetAlert", monthlyExpense > PREDEFINED_BUDGET);
        req.setAttribute("budgetRemaining", PREDEFINED_BUDGET - monthlyExpense);
        req.setAttribute("highestCategory", highestCategory);
        
        // Prepare Category JSON
        String labelsJson = categoryBreakdown.keySet().stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(",", "[", "]"));
        String dataJson = categoryBreakdown.values().stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
        req.setAttribute("chartLabels", labelsJson.equals("[]") ? "[\"No Data\"]" : labelsJson);
        req.setAttribute("chartData", dataJson.equals("[]") ? "[1]" : dataJson);
        
        // --- Add PRO Features Data ---
        AdvancedDAO advDao = new AdvancedDAO();
        req.setAttribute("goalsList", advDao.getGoals());
        req.setAttribute("splitsList", advDao.getSplits());
        req.setAttribute("subsList", advDao.getSubscriptions());
        
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    dao.deleteTransaction(Integer.parseInt(idStr));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
        String type = req.getParameter("type");
        if (type != null) {
            String amountStr = req.getParameter("amount");
            String category = req.getParameter("category");
            String description = req.getParameter("description");
            
            try {
                double amount = Double.parseDouble(amountStr);
                Transaction t;
                if ("INCOME".equalsIgnoreCase(type)) {
                    t = new com.expensetracker.model.Income(0, amount, category, new Date(), description);
                } else {
                    t = new com.expensetracker.model.Expense(0, amount, category, new Date(), description);
                }
                dao.addTransaction(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        } 
        
        // PRO Features Post Actions
        AdvancedDAO advDao = new AdvancedDAO();
        if ("toggleSplit".equals(action)) {
            advDao.toggleSplitStatus(Integer.parseInt(req.getParameter("id")));
        } else if ("toggleSub".equals(action)) {
            advDao.toggleSubStatus(Integer.parseInt(req.getParameter("id")));
        } else if ("addGoal".equals(action)) {
            advDao.addGoal(req.getParameter("name"), Double.parseDouble(req.getParameter("target")), Double.parseDouble(req.getParameter("saved")));
        } else if ("addSplit".equals(action)) {
            double total = Double.parseDouble(req.getParameter("total"));
            int parts = Integer.parseInt(req.getParameter("parts"));
            advDao.addSplit(req.getParameter("group"), req.getParameter("desc"), total, parts, total/parts);
        } else if ("addSub".equals(action)) {
            advDao.addSubscription(req.getParameter("name"), Double.parseDouble(req.getParameter("cost")), Integer.parseInt(req.getParameter("day")));
        }
        
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
