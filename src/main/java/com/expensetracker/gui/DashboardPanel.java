package com.expensetracker.gui;

import com.expensetracker.dao.AdvancedDAO;
import com.expensetracker.dao.ITransactionDAO;
import com.expensetracker.dao.TransactionDAOImpl;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.Expense;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

public class DashboardPanel extends JPanel {
    private MainFrame parent;
    private ITransactionDAO dao;
    private AdvancedDAO advDao;
    
    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private DefaultTableModel tableModel;
    private JPanel chartContainer;
    
    private JLabel aiPredictionLabel;
    private JLabel smartBudgetLabel;
    private boolean isUSD = false;

    // Light sleek UI palette
    private final Color SIDEBAR_BG = new Color(248, 249, 250); 
    private final Color MAIN_BG = Color.WHITE;
    private final Color TEXT_DARK = new Color(33, 37, 41);
    private final Color BRAND_PURPLE = new Color(81, 45, 168);
    private final Color CARD_GREEN = new Color(64, 192, 87);
    private final Color CARD_BLUE = new Color(52, 152, 219);
    private final Color CARD_RED = new Color(250, 82, 82);
    
    // Tab Data holders
    private JPanel cashFlowTab;
    private JPanel goalsTab;
    private JPanel splitsTab;
    private JPanel subsTab;
    private JPanel trendsTab;
    
    public DashboardPanel(MainFrame parent) {
        this.parent = parent;
        this.dao = new TransactionDAOImpl();
        this.advDao = new AdvancedDAO();
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                initUI();
                return null;
            }
            @Override
            protected void done() {
                refreshData();
            }
        };
        worker.execute();
        
        // Timer for auto refresh
        Timer timer = new Timer(5000, e -> refreshData());
        timer.start();
        
        // Smart Reminder System for Bills & Subs using Background Threads
        Thread bgThread = new Thread(() -> {
            try {
                Thread.sleep(3000); 
                List<Map<String, Object>> subs = advDao.getSubscriptions();
                int today = java.time.LocalDate.now().getDayOfMonth();
                for (Map<String, Object> sub : subs) {
                    if ((int)sub.get("due_day") == today) {
                        JOptionPane.showMessageDialog(this, "🔔 Subscription renewal due today: " + sub.get("name") + " (₹" + sub.get("cost") + ")", "Payment Reminder", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                if(today == 1 || today == 5) {
                    JOptionPane.showMessageDialog(this, "🔔 Reminder: Rent & Electricity bills are due this week!", "Bill Reminder", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ignored) {}
        });
        bgThread.start();
    }
    
    private void initUI() {
        // --- 0. Top Navigation Bar (Header) ---
        JPanel topNavBar = new JPanel(new BorderLayout());
        topNavBar.setBackground(Color.WHITE);
        topNavBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            new EmptyBorder(10, 30, 10, 30)
        ));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setBackground(Color.WHITE);
        JLabel logoIcon = new JLabel("💳", SwingConstants.CENTER); 
        logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoIcon.setForeground(BRAND_PURPLE);
        JLabel brandName = new JLabel("FinTrack");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandName.setForeground(BRAND_PURPLE);
        brandPanel.add(logoIcon);
        brandPanel.add(brandName);

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        profilePanel.setBackground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        JLabel userName = new JLabel("Sharayu Pathare", SwingConstants.RIGHT);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setForeground(TEXT_DARK);
        JLabel userEmail = new JLabel("sharayu@tracker.com", SwingConstants.RIGHT);
        userEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userEmail.setForeground(Color.GRAY);
        textPanel.add(userName);
        textPanel.add(userEmail);
        
        JComponent avatarCircle = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(113, 88, 226)); 
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("SP")) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("SP", x, y);
                g2.dispose();
            }
        };
        avatarCircle.setPreferredSize(new Dimension(42, 42));
        avatarCircle.setMinimumSize(new Dimension(42, 42));
        
        JButton topLogoutBtn = new JButton("🚪");
        topLogoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        topLogoutBtn.setBorderPainted(false);
        topLogoutBtn.setContentAreaFilled(false);
        topLogoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topLogoutBtn.setForeground(CARD_RED);
        topLogoutBtn.setToolTipText("Logout");
        topLogoutBtn.addActionListener(e -> parent.setContentPane(new PinLoginPanel(parent)));
        
        profilePanel.add(textPanel);
        profilePanel.add(avatarCircle);
        profilePanel.add(topLogoutBtn);

        topNavBar.add(brandPanel, BorderLayout.WEST);
        topNavBar.add(profilePanel, BorderLayout.EAST);
        add(topNavBar, BorderLayout.NORTH); 

        // Base Panel to hold Sidebar and Content together below Header
        JPanel baseWrapper = new JPanel(new BorderLayout());

        // --- 1. Beautiful Sidebar (WEST) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));

        JButton addBtn = createSidebarButton("➕ Add Transaction");
        addBtn.addActionListener(e -> new AddTransactionDialog(parent, this).setVisible(true));
        
        JButton deleteBtn = createSidebarButton("🗑 Delete Selected");
        deleteBtn.setForeground(CARD_RED);
        
        JButton exportBtn = createSidebarButton("💾 Export to CSV");
        exportBtn.addActionListener(e -> exportCSV());
        
        JToggleButton currencyBtn = new JToggleButton("🌎 Currency: ₹ / $");
        currencyBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currencyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        currencyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyBtn.setFocusPainted(false);
        currencyBtn.addActionListener(e -> { isUSD = currencyBtn.isSelected(); refreshData(); });
        
        sidebar.add(addBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(deleteBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(exportBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(currencyBtn);
        sidebar.add(Box.createVerticalGlue()); 
        
        baseWrapper.add(sidebar, BorderLayout.WEST);

        // --- 2. Main Content Area (CENTER) ---
        // Intead of a static visualPanel, we use a central JTabbedPane!
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(MAIN_BG);
        mainContent.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Summary Cards (Always visible on top)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(MAIN_BG);
        cardsPanel.setPreferredSize(new Dimension(0, 130));
        balanceLabel = createSummaryCard(cardsPanel, "Total Balance", "₹0.00", CARD_GREEN);
        incomeLabel = createSummaryCard(cardsPanel, "Total Income", "₹0.00", CARD_BLUE);
        expenseLabel = createSummaryCard(cardsPanel, "Total Expense", "₹0.00", CARD_RED);
        mainContent.add(cardsPanel, BorderLayout.NORTH);
        
        // Massive JTabbedPane for Dashboard + PRO Features natively embedded!
        UIManager.put("TabbedPane.selected", Color.WHITE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabs.setBackground(new Color(230,235,240));
        
        tabs.addTab("🏠 Main Dashboard", createMainDashboardTab(deleteBtn));
        cashFlowTab = new JPanel(new BorderLayout());
        goalsTab = new JPanel(new BorderLayout());
        splitsTab = new JPanel(new BorderLayout());
        subsTab = new JPanel(new BorderLayout());
        JPanel trendsTabHolder = new JPanel(new BorderLayout());
        
        tabs.addTab("📈 Cash Flow Analysis", cashFlowTab);
        tabs.addTab("🔥 Trends & Heatmap", trendsTabHolder);
        tabs.addTab("🎯 Saver Goals", goalsTab);
        tabs.addTab("🤝 Split Expenses", splitsTab);
        tabs.addTab("🔁 Subscriptions", subsTab);
        
        this.trendsTab = trendsTabHolder;
        
        tabs.addChangeListener(e -> refreshProTabs()); // re-render layout dynamically when clicked
        
        mainContent.add(tabs, BorderLayout.CENTER);
        
        baseWrapper.add(mainContent, BorderLayout.CENTER);
        add(baseWrapper, BorderLayout.CENTER);
    }
    
    // Core Dashboard Content
    private JPanel createMainDashboardTab(JButton deleteBtn) {
        JPanel wrapper = new JPanel(new BorderLayout(0,0));
        wrapper.setBackground(MAIN_BG);
        
        String[] columns = {"ID", "Type", "Amount", "Category", "Date", "Desc."};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(230, 230, 230)); 
        table.setBackground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(180);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(SIDEBAR_BG);
        header.setForeground(TEXT_DARK);
        
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(parent, "Delete transaction?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dao.deleteTransaction(id);
                    refreshData();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        scrollPane.getViewport().setBackground(MAIN_BG);
        
        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(MAIN_BG);
        chartContainer.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, chartContainer);
        splitPane.setDividerLocation(520);
        splitPane.setDividerSize(12);
        splitPane.setBorder(null);
        splitPane.setBackground(MAIN_BG);
        splitPane.setContinuousLayout(true);
        wrapper.add(splitPane, BorderLayout.CENTER);
        
        // AI Hints
        JPanel aiPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        aiPanel.setBackground(MAIN_BG);
        aiPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        aiPredictionLabel = createAILabel("🤖 AI Prediction: ...");
        smartBudgetLabel = createAILabel("💡 Smart Budget: ...");
        aiPanel.add(aiPredictionLabel);
        aiPanel.add(smartBudgetLabel);
        wrapper.add(aiPanel, BorderLayout.SOUTH);
        
        return wrapper;
    }
    
    // --- DYNAMIC RE-RENDERING FOR PRO TABS ---
    private void refreshProTabs() {
        try {
            List<Transaction> allTx = dao.getAllTransactions();
            double conversion = isUSD ? 1/83.0 : 1.0; 
            String symbol = isUSD ? "$" : "₹";
            
            // 1. Refresh Cash Flow
            cashFlowTab.removeAll();
            cashFlowTab.setLayout(new BorderLayout());
            cashFlowTab.setBackground(Color.WHITE);
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            double totalInc = 0; double totalExp = 0;
            for (Transaction t : allTx) {
                String d = new java.text.SimpleDateFormat("dd MMM").format(t.getDate());
                if (t.getTransactionType().equals("INCOME")) { totalInc += t.getAmount()*conversion; dataset.addValue(t.getAmount()*conversion, "Income", d); }
                else { totalExp += t.getAmount()*conversion; dataset.addValue(t.getAmount()*conversion, "Expense", d); }
            }
            JFreeChart chart = ChartFactory.createBarChart("Cash Flow Analyzer", "Date", "Amount ("+symbol+")", dataset, PlotOrientation.VERTICAL, true, true, false);
            chart.setBackgroundPaint(Color.WHITE);
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, CARD_GREEN); renderer.setSeriesPaint(1, new Color(231, 76, 60)); 
            
            JLabel rL = new JLabel(totalInc > totalExp ? "🟢 HEALTHY: Surplus of "+symbol+String.format("%.2f",(totalInc-totalExp)) : "🔴 DEFICIT: "+symbol+String.format("%.2f",(totalExp-totalInc)));
            rL.setFont(new Font("Segoe UI", Font.BOLD, 22)); rL.setForeground(totalInc > totalExp ? CARD_GREEN : CARD_RED); rL.setBorder(new EmptyBorder(10,10,10,10));
            cashFlowTab.add(rL, BorderLayout.NORTH); cashFlowTab.add(new ChartPanel(chart), BorderLayout.CENTER);
            
            // 1.5 Refresh Trends & Heatmap
            trendsTab.removeAll();
            trendsTab.setLayout(new BorderLayout(15, 15));
            trendsTab.setBackground(Color.WHITE);
            
            Map<Integer, Double> dailySpends = new HashMap<>();
            Calendar cal = Calendar.getInstance();
            for (Transaction t : allTx) {
                if (t.getTransactionType().equals("EXPENSE")) {
                    cal.setTime(t.getDate());
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dailySpends.put(day, dailySpends.getOrDefault(day, 0.0) + (t.getAmount() * conversion));
                }
            }
            DefaultCategoryDataset trendData = new DefaultCategoryDataset();
            for(int i=1; i<=31; i++) trendData.addValue(dailySpends.getOrDefault(i, 0.0), "Expenses", String.valueOf(i));
            JFreeChart trendChart = ChartFactory.createLineChart("Daily Spending Trend", "Day of Month", "Amount ("+symbol+")", trendData, PlotOrientation.VERTICAL, false, true, false);
            trendChart.setBackgroundPaint(Color.WHITE); trendChart.getPlot().setBackgroundPaint(Color.WHITE);
            LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)((org.jfree.chart.plot.CategoryPlot)trendChart.getPlot()).getRenderer();
            lineRenderer.setSeriesPaint(0, CARD_RED);
            lineRenderer.setDefaultShapesVisible(true);
            
            JPanel heatmapBody = new JPanel(new BorderLayout()); heatmapBody.setBackground(Color.WHITE); heatmapBody.setBorder(new EmptyBorder(10,0,0,0));
            JLabel hqInfo = new JLabel("🔥 Monthly Expense Heatmap Matrix", SwingConstants.CENTER); hqInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            JPanel heatmapGrid = new JPanel(new GridLayout(4, 8, 4, 4)); heatmapGrid.setBackground(Color.WHITE);
            double maxSpend = dailySpends.values().stream().max(Double::compareTo).orElse(1.0);
            for (int i = 1; i <= 31; i++) {
                double spent = dailySpends.getOrDefault(i, 0.0);
                float intensity = (float)(spent / maxSpend);
                JPanel cell = new JPanel(new BorderLayout());
                if (spent == 0) cell.setBackground(new Color(245, 245, 245));
                else cell.setBackground(new Color(255, (int)(255 - (200 * intensity)), (int)(255 - (200 * intensity))));
                JLabel dayL = new JLabel("D" + i); dayL.setHorizontalAlignment(SwingConstants.CENTER); dayL.setFont(new Font("Segoe UI", Font.BOLD, 14));
                JLabel valL = new JLabel(spent > 0 ? symbol + String.format("%.0f", spent) : ""); valL.setHorizontalAlignment(SwingConstants.CENTER); valL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                if (intensity > 0.6) { dayL.setForeground(Color.WHITE); valL.setForeground(Color.WHITE); }
                cell.add(dayL, BorderLayout.CENTER); cell.add(valL, BorderLayout.SOUTH); cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                heatmapGrid.add(cell);
            }
            heatmapBody.add(hqInfo, BorderLayout.NORTH); heatmapBody.add(heatmapGrid, BorderLayout.CENTER);
            
            JSplitPane trendsSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new ChartPanel(trendChart), heatmapBody);
            trendsSplit.setDividerLocation(300); trendsSplit.setBorder(null); trendsSplit.setBackground(Color.WHITE);
            trendsTab.add(trendsSplit, BorderLayout.CENTER);
            
            // 2. Refresh Goals
            goalsTab.removeAll();
            goalsTab.setLayout(new BorderLayout(15, 15));
            goalsTab.setBackground(Color.WHITE);
            JPanel listG = new JPanel(); listG.setLayout(new BoxLayout(listG, BoxLayout.Y_AXIS)); listG.setBackground(Color.WHITE);
            for (Map<String, Object> g : advDao.getGoals()) {
                JPanel item = new JPanel(new BorderLayout(0,10));
                item.setBackground(Color.WHITE);
                item.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(20,20,20,20)));
                double t = (double) g.get("target") * conversion; double s = (double) g.get("saved") * conversion;
                int pct = (int)((s/t) * 100);
                JLabel n = new JLabel("🎯 " + g.get("name") + "  (Saved: "+symbol + String.format("%.2f",s) + " / Target: "+symbol + String.format("%.2f",t) + ")");
                n.setFont(new Font("Segoe UI", Font.BOLD, 18)); n.setForeground(BRAND_PURPLE);
                JProgressBar pb = new JProgressBar(0, 100); pb.setValue(pct); pb.setStringPainted(true);
                pb.setString(pct + "% Completed"); pb.setForeground(CARD_BLUE); pb.setPreferredSize(new Dimension(800, 30));
                item.add(n, BorderLayout.NORTH); item.add(pb, BorderLayout.CENTER);
                listG.add(item); listG.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            JButton addG = createProBtn("➕ Add New Financial Goal", CARD_BLUE);
            addG.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Goal Name (e.g., Buy iPhone):");
                if (name != null) {
                    double t = Double.parseDouble(JOptionPane.showInputDialog("Target Amount ("+symbol+"):"));
                    double s = Double.parseDouble(JOptionPane.showInputDialog("Currently Saved ("+symbol+"):"));
                    advDao.addGoal(name, t / conversion, s / conversion); refreshProTabs();
                }
            });
            goalsTab.add(new JScrollPane(listG), BorderLayout.CENTER); goalsTab.add(addG, BorderLayout.SOUTH);
            
            // 3. Refresh Splits
            splitsTab.removeAll();
            splitsTab.setLayout(new BorderLayout(15, 15));
            splitsTab.setBackground(Color.WHITE);
            DefaultTableModel sModel = new DefaultTableModel(new String[]{"ID", "Group", "Desc", "Total Bill", "People", "Your Portion", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return column == 6; } 
            };
            for (Map<String, Object> s : advDao.getSplits()) {
                sModel.addRow(new Object[]{s.get("id"), s.get("group"), s.get("desc"), symbol+String.format("%.2f",(double)s.get("total") * conversion), s.get("parts"), symbol+String.format("%.2f",(double)s.get("owe") * conversion), s.get("status")});
            }
            JTable sT = new JTable(sModel); styleTable(sT);
            sT.getColumnModel().getColumn(0).setMaxWidth(0); sT.getColumnModel().getColumn(0).setMinWidth(0); sT.getColumnModel().getColumn(0).setPreferredWidth(0);
            
            sT.getColumn("Status").setCellRenderer(new ButtonRenderer());
            sT.getColumn("Status").setCellEditor(new ButtonEditor(new JCheckBox(), "SPLIT", advDao, this));
            
            JPanel splitsActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            splitsActionPanel.setBackground(Color.WHITE);
            JButton addS = createProBtn("➕ Add Splitwise Group Bill", BRAND_PURPLE);
            addS.addActionListener(e -> {
                String group = JOptionPane.showInputDialog("Group Name:");
                if (group != null) {
                    String desc = JOptionPane.showInputDialog("Description:");
                    double total = Double.parseDouble(JOptionPane.showInputDialog("Total Bill:"));
                    int parts = Integer.parseInt(JOptionPane.showInputDialog("How many people sharing?"));
                    advDao.addSplit(group, desc, total, parts, total / parts);
                    refreshProTabs();
                }
            });
            splitsActionPanel.add(addS);
            splitsTab.add(new JScrollPane(sT), BorderLayout.CENTER); splitsTab.add(splitsActionPanel, BorderLayout.SOUTH);
            
            // 4. Refresh Subs
            subsTab.removeAll();
            subsTab.setLayout(new BorderLayout(15, 15));
            subsTab.setBackground(Color.WHITE);
            DefaultTableModel suvModel = new DefaultTableModel(new String[]{"ID", "Service Name", "Monthly Cost", "Recurring Due Day", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return column == 4; }
            };
            double monthlyTotal = 0;
            for (Map<String, Object> s : advDao.getSubscriptions()) {
                double c = (double)s.get("cost") * conversion; monthlyTotal += c;
                suvModel.addRow(new Object[]{s.get("id"), s.get("name"), symbol+String.format("%.2f",c), s.get("due_day") + "th of month", s.get("status")});
            }
            JTable subT = new JTable(suvModel); styleTable(subT);
            subT.getColumnModel().getColumn(0).setMaxWidth(0); subT.getColumnModel().getColumn(0).setMinWidth(0); subT.getColumnModel().getColumn(0).setPreferredWidth(0);
            
            subT.getColumn("Status").setCellRenderer(new ButtonRenderer());
            subT.getColumn("Status").setCellEditor(new ButtonEditor(new JCheckBox(), "SUB", advDao, this));
            
            JLabel tL = new JLabel("💸 Total Monthly Subscriptions Overhead: " + symbol + String.format("%.2f", monthlyTotal)); tL.setFont(new Font("Segoe UI", Font.BOLD, 20));
            
            JPanel subsActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            subsActionPanel.setBackground(Color.WHITE);
            JButton addSub = createProBtn("➕ Register New Subscription", CARD_GREEN);
            addSub.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Subscription Name (e.g. Netflix):");
                if (name != null) {
                    double cost = Double.parseDouble(JOptionPane.showInputDialog("Monthly Cost:"));
                    int day = Integer.parseInt(JOptionPane.showInputDialog("Due Day (1-31):"));
                    advDao.addSubscription(name, cost, day);
                    refreshProTabs();
                }
            });
            subsActionPanel.add(addSub);
            
            subsTab.add(tL, BorderLayout.NORTH); subsTab.add(new JScrollPane(subT), BorderLayout.CENTER); subsTab.add(subsActionPanel, BorderLayout.SOUTH);
            
            cashFlowTab.revalidate(); goalsTab.revalidate(); splitsTab.revalidate(); subsTab.revalidate();
        } catch (Exception ignored) {}
    }
    
    private void styleTable(JTable t) {
        t.setRowHeight(40); t.setFont(new Font("Segoe UI", Font.PLAIN, 15)); t.setSelectionBackground(new Color(225, 235, 245));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16)); t.getTableHeader().setBackground(BRAND_PURPLE); t.getTableHeader().setForeground(Color.WHITE);
    }
    
    private JButton createProBtn(String text, Color bg) {
        JButton btn = new JButton(text); btn.setFont(new Font("Segoe UI", Font.BOLD, 16)); btn.setBackground(bg); btn.setForeground(Color.WHITE); btn.setFocusPainted(false); btn.setBorder(new EmptyBorder(15,20,15,20)); return btn;
    }
    
    // Core Helpers
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_DARK);
        btn.setBorder(BorderFactory.createLineBorder(SIDEBAR_BG, 1));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(225, 230, 235)); btn.setForeground(BRAND_PURPLE); }
            public void mouseExited(MouseEvent e) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_DARK); if(text.contains("Delete")) btn.setForeground(CARD_RED); }
        });
        return btn;
    }
    
    private JLabel createAILabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER); label.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 15));
        label.setForeground(TEXT_DARK); label.setOpaque(true); label.setBackground(SIDEBAR_BG); label.setBorder(new EmptyBorder(20, 10, 20, 10));
        return label;
    }

    private JLabel createSummaryCard(JPanel parentPanel, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()); card.setBackground(color); card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(color.brighter()); card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), new EmptyBorder(18, 23, 18, 23))); }
            public void mouseExited(MouseEvent e) { card.setBackground(color); card.setBorder(new EmptyBorder(20, 25, 20, 25)); }
        });
        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT); titleLabel.setForeground(new Color(255, 255, 255, 230)); titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel valueLabel = new JLabel(value, SwingConstants.LEFT); valueLabel.setForeground(Color.WHITE); valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        card.add(titleLabel, BorderLayout.NORTH); card.add(valueLabel, BorderLayout.CENTER); parentPanel.add(card);
        return valueLabel;
    }
    
    public void refreshData() {
        SwingWorker<List<Transaction>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Transaction> doInBackground() throws Exception { return dao.getAllTransactions(); }
            @Override
            protected void done() {
                try {
                    List<Transaction> tx = get();
                    updateTable(tx); updateSummary(tx); updateChart(tx);
                    refreshProTabs(); // Instantly update advanced tabs when balance changes!
                } catch (Exception e) {}
            }
        };
        worker.execute();
    }
    
    private void updateTable(List<Transaction> tx) {
        if(tableModel == null) return;
        tableModel.setRowCount(0);
        double conversion = isUSD ? 1/83.0 : 1.0; String symbol = isUSD ? "$" : "₹";
        for (Transaction t : tx) {
            tableModel.addRow(new Object[]{t.getId(), t.getTransactionType(), String.format("%s%.2f", symbol, t.getAmount() * conversion), t.getCategory(), new java.text.SimpleDateFormat("yyyy-MM-dd").format(t.getDate()), t.getDescription()});
        }
    }
    
    private void updateSummary(List<Transaction> tx) {
        if(incomeLabel == null) return;
        double income = 0; double exp = 0;
        for (Transaction t : tx) { if (t.getTransactionType().equals("INCOME")) income += t.getAmount(); else exp += t.getAmount(); }
        double conversion = isUSD ? 1/83.0 : 1.0; String symbol = isUSD ? "$" : "₹";
        incomeLabel.setText(String.format("%s%.2f", symbol, income * conversion));
        expenseLabel.setText(String.format("%s%.2f", symbol, exp * conversion));
        balanceLabel.setText(String.format("%s%.2f", symbol, (income - exp) * conversion));
        aiPredictionLabel.setText(String.format("🤖 AI Prediction: Likely to spend %s%.2f next month", symbol, (exp > 0 ? exp * 1.15 : 0) * conversion));
        smartBudgetLabel.setText(String.format("💡 Smart Budget: Ideal monthly budget is %s%.2f", symbol, (income > 0 ? income * 0.6 : (exp > 0 ? exp * 1.2 : 5000)) * conversion));
    }
    
    private void updateChart(List<Transaction> tx) {
        if(chartContainer == null) return;
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Transaction t : tx) {
            if (t.getTransactionType().equals("EXPENSE")) {
                double current = 0;
                try { current = dataset.getValue(t.getCategory()).doubleValue(); } catch (org.jfree.data.UnknownKeyException ignore) { current = 0; }
                dataset.setValue(t.getCategory(), current + t.getAmount());
            }
        }
        JFreeChart chart = ChartFactory.createPieChart("Expense Breakdown", dataset, true, true, false);
        chart.setBackgroundPaint(MAIN_BG); chart.getPlot().setBackgroundPaint(MAIN_BG); chart.getTitle().setPaint(TEXT_DARK); chart.getLegend().setBackgroundPaint(MAIN_BG); chart.getLegend().setItemPaint(TEXT_DARK);
        chartContainer.removeAll(); chartContainer.add(new ChartPanel(chart), BorderLayout.CENTER); chartContainer.revalidate(); chartContainer.repaint();
    }
    
    private void exportCSV() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter pw = new java.io.PrintWriter(jfc.getSelectedFile() + ".csv")) {
                pw.println("ID,Type,Amount,Category,Date,Description");
                for (Transaction t : dao.getAllTransactions()) { pw.println(t.getId()+","+t.getTransactionType()+","+t.getAmount()+","+t.getCategory()+","+new java.text.SimpleDateFormat("yyyy-MM-dd").format(t.getDate())+","+t.getDescription()); }
                JOptionPane.showMessageDialog(this, "Data exported successfully!");
            } catch (Exception ex) {}
        }
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() { setOpaque(true); setFont(new Font("Segoe UI", Font.BOLD, 12)); setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String status = (value == null) ? "DUE" : value.toString();
        setText("PAID".equals(status) ? "🟢 PAID" : "🔴 DUE");
        setBackground("PAID".equals(status) ? new Color(64, 192, 87) : new Color(250, 82, 82));
        setForeground(Color.WHITE);
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private int currentRowId;
    private String rowType;
    private AdvancedDAO advDao;
    private DashboardPanel parentPanel;
    
    public ButtonEditor(JCheckBox checkBox, String type, AdvancedDAO advDao, DashboardPanel parentPanel) {
        super(checkBox);
        this.rowType = type;
        this.advDao = advDao;
        this.parentPanel = parentPanel;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "DUE" : value.toString();
        button.setText("PAID".equals(label) ? "🟢 PAID" : "🔴 DUE");
        button.setBackground("PAID".equals(label) ? new Color(64, 192, 87) : new Color(250, 82, 82));
        button.setForeground(Color.WHITE);
        isPushed = true;
        currentRowId = (int) table.getModel().getValueAt(row, 0);
        return button;
    }
    public Object getCellEditorValue() {
        if (isPushed) {
            if ("SPLIT".equals(rowType)) advDao.toggleSplitStatus(currentRowId);
            else advDao.toggleSubStatus(currentRowId);
            
            // Fully update the Dashboard with the newly injected Expense natively!
            SwingUtilities.invokeLater(() -> parentPanel.refreshData());
        }
        isPushed = false;
        return label;
    }
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
