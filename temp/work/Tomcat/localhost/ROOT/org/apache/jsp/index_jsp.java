
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.List;
import com.expensetracker.model.Transaction;
import java.text.SimpleDateFormat;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("java.text.SimpleDateFormat");
    _jspx_imports_classes.add("com.expensetracker.model.Transaction");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    List<Transaction> recentTransactions = (List<Transaction>) request.getAttribute("recentTransactions");
    
    Double totalIncome = (Double) request.getAttribute("totalIncome");
    Double totalExpense = (Double) request.getAttribute("totalExpense");
    Double balance = (Double) request.getAttribute("balance");
    Double monthlyIncome = (Double) request.getAttribute("monthlyIncome");
    Double monthlyExpense = (Double) request.getAttribute("monthlyExpense");
    Double todayExpense = (Double) request.getAttribute("todayExpense");
    Double savingsRate = (Double) request.getAttribute("savingsRate");
    
    Boolean budgetAlert = (Boolean) request.getAttribute("budgetAlert");
    Double budgetRemaining = (Double) request.getAttribute("budgetRemaining");
    String highestCategory = (String) request.getAttribute("highestCategory");
    
    String chartLabels = (String) request.getAttribute("chartLabels");
    String chartData = (String) request.getAttribute("chartData");

    if (transactions == null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
    SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("<head>\n");
      out.write("    <meta charset=\"UTF-8\">\n");
      out.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("    <title>Expense Tracker - Premium Dashboard</title>\n");
      out.write("    <!-- Modern Font -->\n");
      out.write("    <link href=\"https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap\" rel=\"stylesheet\">\n");
      out.write("    <link href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css\" rel=\"stylesheet\">\n");
      out.write("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
      out.write("    <!-- Chart.js -->\n");
      out.write("    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n");
      out.write("\n");
      out.write("    <style>\n");
      out.write("        :root {\n");
      out.write("            --bg-color: #f4f7fa;\n");
      out.write("            --card-bg: #ffffff;\n");
      out.write("            --text-primary: #1e293b;\n");
      out.write("            --text-secondary: #64748b;\n");
      out.write("            --primary: #4318ff;\n");
      out.write("            --success: #05cd99;\n");
      out.write("            --danger: #ee5d50;\n");
      out.write("            --warning: #ffce20;\n");
      out.write("            --info: #39b8ff;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        body {\n");
      out.write("            font-family: 'Outfit', sans-serif;\n");
      out.write("            background-color: var(--bg-color);\n");
      out.write("            color: var(--text-primary);\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .navbar {\n");
      out.write("            background-color: var(--card-bg);\n");
      out.write("            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);\n");
      out.write("            padding: 15px 0;\n");
      out.write("            margin-bottom: 30px;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .navbar-brand {\n");
      out.write("            font-weight: 700;\n");
      out.write("            color: var(--primary) !important;\n");
      out.write("            font-size: 1.5rem;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .user-profile {\n");
      out.write("            display: flex;\n");
      out.write("            align-items: center;\n");
      out.write("            gap: 12px;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .user-avatar {\n");
      out.write("            width: 40px;\n");
      out.write("            height: 40px;\n");
      out.write("            border-radius: 50%;\n");
      out.write("            background: linear-gradient(135deg, var(--primary), #8f60ff);\n");
      out.write("            color: white;\n");
      out.write("            display: flex;\n");
      out.write("            align-items: center;\n");
      out.write("            justify-content: center;\n");
      out.write("            font-weight: 600;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .card-custom {\n");
      out.write("            background: var(--card-bg);\n");
      out.write("            border: none;\n");
      out.write("            border-radius: 20px;\n");
      out.write("            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);\n");
      out.write("            transition: transform 0.2s, box-shadow 0.2s;\n");
      out.write("            height: 100%;\n");
      out.write("            overflow: hidden;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .card-custom:hover {\n");
      out.write("            transform: translateY(-3px);\n");
      out.write("            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.07);\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .stat-card {\n");
      out.write("            color: white;\n");
      out.write("            padding: 25px;\n");
      out.write("            position: relative;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .bg-gradient-balance { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }\n");
      out.write("        .bg-gradient-income { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }\n");
      out.write("        .bg-gradient-expense { background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%); }\n");
      out.write("        .bg-gradient-savings { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }\n");
      out.write("\n");
      out.write("        .stat-icon {\n");
      out.write("            position: absolute;\n");
      out.write("            right: 20px;\n");
      out.write("            top: 50%;\n");
      out.write("            transform: translateY(-50%);\n");
      out.write("            font-size: 3rem;\n");
      out.write("            opacity: 0.2;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .stat-title {\n");
      out.write("            font-size: 1rem;\n");
      out.write("            font-weight: 500;\n");
      out.write("            opacity: 0.9;\n");
      out.write("            margin-bottom: 5px;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .stat-value {\n");
      out.write("            font-size: 2.2rem;\n");
      out.write("            font-weight: 700;\n");
      out.write("            margin-bottom: 0;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .section-title {\n");
      out.write("            font-size: 1.2rem;\n");
      out.write("            font-weight: 600;\n");
      out.write("            margin-bottom: 20px;\n");
      out.write("            color: var(--text-primary);\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .table-custom {\n");
      out.write("            margin-bottom: 0;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .table-custom th {\n");
      out.write("            border-bottom: 1px solid #edf2f7;\n");
      out.write("            color: var(--text-secondary);\n");
      out.write("            font-weight: 500;\n");
      out.write("            padding: 15px;\n");
      out.write("            font-size: 0.9rem;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .table-custom td {\n");
      out.write("            border-bottom: 1px solid #edf2f7;\n");
      out.write("            padding: 15px;\n");
      out.write("            vertical-align: middle;\n");
      out.write("            font-weight: 500;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .table-custom tr:last-child td {\n");
      out.write("            border-bottom: none;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .badge-income { background-color: rgba(5, 205, 153, 0.1); color: var(--success); }\n");
      out.write("        .badge-expense { background-color: rgba(238, 93, 80, 0.1); color: var(--danger); }\n");
      out.write("\n");
      out.write("        .btn-action {\n");
      out.write("            border-radius: 12px;\n");
      out.write("            font-weight: 500;\n");
      out.write("            padding: 10px 20px;\n");
      out.write("            display: flex;\n");
      out.write("            align-items: center;\n");
      out.write("            justify-content: center;\n");
      out.write("            gap: 8px;\n");
      out.write("            width: 100%;\n");
      out.write("            margin-bottom: 10px;\n");
      out.write("            transition: all 0.3s;\n");
      out.write("        }\n");
      out.write("        \n");
      out.write("        .insight-box {\n");
      out.write("            padding: 15px;\n");
      out.write("            border-radius: 12px;\n");
      out.write("            background: #f8fafc;\n");
      out.write("            margin-bottom: 15px;\n");
      out.write("            border-left: 4px solid var(--primary);\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        .insight-box p { margin: 0; font-size: 0.9rem; color: var(--text-secondary); }\n");
      out.write("        .insight-box h5 { margin: 5px 0 0 0; font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }\n");
      out.write("\n");
      out.write("        /* Animation */\n");
      out.write("        @keyframes fadeIn {\n");
      out.write("            from { opacity: 0; transform: translateY(10px); }\n");
      out.write("            to { opacity: 1; transform: translateY(0); }\n");
      out.write("        }\n");
      out.write("        .animate-fade {\n");
      out.write("            animation: fadeIn 0.4s ease forwards;\n");
      out.write("        }\n");
      out.write("    </style>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("    <!-- Navigation -->\n");
      out.write("    <nav class=\"navbar navbar-expand-lg\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <a class=\"navbar-brand\" href=\"#\"><i class=\"fa-solid fa-wallet me-2\"></i>FinTrack</a>\n");
      out.write("            \n");
      out.write("            <div class=\"ms-auto d-flex align-items-center\">\n");
      out.write("                <!-- User Profile Section -->\n");
      out.write("                <div class=\"user-profile\">\n");
      out.write("                    <div class=\"text-end d-none d-md-block\">\n");
      out.write("                        <div class=\"fw-bold\" style=\"font-size: 0.95rem;\">Sharayu Pathare</div>\n");
      out.write("                        <div class=\"text-muted\" style=\"font-size: 0.8rem;\">sharayu@tracker.com</div>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"user-avatar\">SP</div>\n");
      out.write("                    <a href=\"");
      out.print( request.getContextPath() );
      out.write("/login?action=logout\" class=\"btn btn-sm btn-outline-danger ms-3\"><i class=\"fa-solid fa-arrow-right-from-bracket\"></i></a>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </nav>\n");
      out.write("\n");
      out.write("    <div class=\"container pb-5\">\n");
      out.write("        \n");
      out.write("        <!-- Budget Alert -->\n");
      out.write("        ");
 if(budgetAlert) { 
      out.write("\n");
      out.write("        <div class=\"alert alert-danger d-flex align-items-center animate-fade\" role=\"alert\" style=\"border-radius: 15px; border: none; box-shadow: 0 4px 15px rgba(238, 93, 80, 0.1);\">\n");
      out.write("            <i class=\"fa-solid fa-triangle-exclamation fs-3 me-3\"></i>\n");
      out.write("            <div>\n");
      out.write("                <h5 class=\"alert-heading mb-1 fw-bold\">Budget Exceeded!</h5>\n");
      out.write("                <p class=\"mb-0\">You have exceeded your predefined monthly budget (₹50,000). Please review your recent expenses.</p>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("        ");
 } 
      out.write("\n");
      out.write("\n");
      out.write("        <!-- Smart Summary Cards -->\n");
      out.write("        <div class=\"row g-4 mb-4\">\n");
      out.write("            <div class=\"col-xl-3 col-md-6 animate-fade\" style=\"animation-delay: 0.1s;\">\n");
      out.write("                <div class=\"card card-custom stat-card bg-gradient-balance\">\n");
      out.write("                    <i class=\"fa-solid fa-vault stat-icon\"></i>\n");
      out.write("                    <div class=\"stat-title\">Total Balance</div>\n");
      out.write("                    <h2 class=\"stat-value\">₹");
      out.print( String.format("%.2f", balance) );
      out.write("</h2>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-xl-3 col-md-6 animate-fade\" style=\"animation-delay: 0.2s;\">\n");
      out.write("                <div class=\"card card-custom stat-card bg-gradient-income\">\n");
      out.write("                    <i class=\"fa-solid fa-arrow-trend-up stat-icon\"></i>\n");
      out.write("                    <div class=\"stat-title\">Monthly Income</div>\n");
      out.write("                    <h2 class=\"stat-value\">₹");
      out.print( String.format("%.2f", monthlyIncome) );
      out.write("</h2>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-xl-3 col-md-6 animate-fade\" style=\"animation-delay: 0.3s;\">\n");
      out.write("                <div class=\"card card-custom stat-card bg-gradient-expense\">\n");
      out.write("                    <i class=\"fa-solid fa-arrow-trend-down stat-icon\"></i>\n");
      out.write("                    <div class=\"stat-title\">Monthly Expense</div>\n");
      out.write("                    <h2 class=\"stat-value\">₹");
      out.print( String.format("%.2f", monthlyExpense) );
      out.write("</h2>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-xl-3 col-md-6 animate-fade\" style=\"animation-delay: 0.4s;\">\n");
      out.write("                <div class=\"card card-custom stat-card bg-gradient-savings\">\n");
      out.write("                    <i class=\"fa-solid fa-piggy-bank stat-icon\"></i>\n");
      out.write("                    <div class=\"stat-title\">Savings Rate</div>\n");
      out.write("                    <h2 class=\"stat-value\">");
      out.print( String.format("%.1f", savingsRate) );
      out.write("%</h2>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Charts and Insights Row -->\n");
      out.write("        <div class=\"row g-4 mb-4\">\n");
      out.write("            \n");
      out.write("            <!-- Category Breakdown -->\n");
      out.write("            <div class=\"col-lg-5 animate-fade\" style=\"animation-delay: 0.5s;\">\n");
      out.write("                <div class=\"card card-custom p-4\">\n");
      out.write("                    <h5 class=\"section-title\">Expense Category Breakdown</h5>\n");
      out.write("                    <div style=\"position: relative; height: 260px; display: flex; justify-content: center;\">\n");
      out.write("                        <canvas id=\"categoryChart\"></canvas>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <!-- Expense Insights & Actions -->\n");
      out.write("            <div class=\"col-lg-3 animate-fade\" style=\"animation-delay: 0.6s;\">\n");
      out.write("                <div class=\"card card-custom p-4\">\n");
      out.write("                    <h5 class=\"section-title\">Smart Insights</h5>\n");
      out.write("                    \n");
      out.write("                    <div class=\"insight-box\" style=\"border-left-color: var(--info);\">\n");
      out.write("                        <p>Today's Spending</p>\n");
      out.write("                        <h5>₹");
      out.print( String.format("%.2f", todayExpense) );
      out.write("</h5>\n");
      out.write("                    </div>\n");
      out.write("                    \n");
      out.write("                    <div class=\"insight-box\" style=\"border-left-color: var(--danger);\">\n");
      out.write("                        <p>Highest Spending Category</p>\n");
      out.write("                        <h5>");
      out.print( highestCategory != null ? highestCategory : "None" );
      out.write("</h5>\n");
      out.write("                    </div>\n");
      out.write("                    \n");
      out.write("                    <div class=\"insight-box\" style=\"border-left-color: var(--success);\">\n");
      out.write("                        <p>Budget Remaining</p>\n");
      out.write("                        <h5>");
      out.print( budgetRemaining > 0 ? "₹" + String.format("%.2f", budgetRemaining) : "Budget Overrun" );
      out.write("</h5>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            \n");
      out.write("            <!-- Quick Actions -->\n");
      out.write("            <div class=\"col-lg-4 animate-fade\" style=\"animation-delay: 0.7s;\">\n");
      out.write("                <div class=\"card card-custom p-4\">\n");
      out.write("                    <h5 class=\"section-title\">Quick Actions & Filters</h5>\n");
      out.write("                    \n");
      out.write("                    <button class=\"btn btn-primary btn-action text-white mb-3\" data-bs-toggle=\"modal\" data-bs-target=\"#addTransactionModal\" style=\"background: var(--primary); border: none;\">\n");
      out.write("                        <i class=\"fa-solid fa-plus\"></i> Record New Transaction\n");
      out.write("                    </button>\n");
      out.write("                    \n");
      out.write("                    <hr class=\"text-muted my-4\">\n");
      out.write("                    \n");
      out.write("                    <h6 class=\"text-secondary mb-3\"><i class=\"fa-solid fa-filter me-2\"></i>Filter Transactions</h6>\n");
      out.write("                    <select id=\"categoryFilter\" class=\"form-select mb-3\" style=\"border-radius: 10px;\" onchange=\"applyFilters()\">\n");
      out.write("                        <option value=\"\">All Categories</option>\n");
      out.write("                        <option value=\"Food\">Food & Dining</option>\n");
      out.write("                        <option value=\"Travel\">Transportation</option>\n");
      out.write("                        <option value=\"Shopping\">Shopping</option>\n");
      out.write("                        <option value=\"Bills\">Bills & Utilities</option>\n");
      out.write("                        <option value=\"Salary\">Salary / Income</option>\n");
      out.write("                        <option value=\"Other\">Other</option>\n");
      out.write("                    </select>\n");
      out.write("                    \n");
      out.write("                    <div class=\"d-flex gap-2\">\n");
      out.write("                        <select id=\"typeFilter\" class=\"form-select\" style=\"border-radius: 10px;\" onchange=\"applyFilters()\">\n");
      out.write("                            <option value=\"\">Any Type</option>\n");
      out.write("                            <option value=\"INCOME\">Income</option>\n");
      out.write("                            <option value=\"EXPENSE\">Expense</option>\n");
      out.write("                        </select>\n");
      out.write("                        <button class=\"btn btn-light\" style=\"border-radius: 10px;\" onclick=\"applyFilters()\"><i class=\"fa-solid fa-search\"></i></button>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Transactions Library -->\n");
      out.write("        <div class=\"col-12 animate-fade\" style=\"animation-delay: 0.8s;\">\n");
      out.write("            <div class=\"card card-custom p-4\">\n");
      out.write("                <div class=\"d-flex justify-content-between align-items-center mb-3\">\n");
      out.write("                    <h5 class=\"section-title mb-0\">Transactions</h5>\n");
      out.write("                </div>\n");
      out.write("                \n");
      out.write("                <div class=\"table-responsive\" style=\"max-height: 400px; overflow-y: auto;\">\n");
      out.write("                    <table class=\"table table-custom table-hover\" id=\"transactionsTable\">\n");
      out.write("                        <thead>\n");
      out.write("                            <tr>\n");
      out.write("                                <th>Transaction</th>\n");
      out.write("                                <th>Category</th>\n");
      out.write("                                <th>Date</th>\n");
      out.write("                                <th class=\"text-end\">Amount</th>\n");
      out.write("                                <th class=\"text-center\">Action</th>\n");
      out.write("                            </tr>\n");
      out.write("                        </thead>\n");
      out.write("                        <tbody>\n");
      out.write("                            ");
 if (transactions.isEmpty()) { 
      out.write("\n");
      out.write("                                <tr><td colspan=\"5\" class=\"text-center text-muted py-4\">No transactions found.</td></tr>\n");
      out.write("                            ");
 } else { 
      out.write("\n");
      out.write("                                ");
 for(Transaction t : transactions) { 
      out.write("\n");
      out.write("                                <tr class=\"transaction-row\" data-type=\"");
      out.print( t.getTransactionType() );
      out.write("\" data-category=\"");
      out.print( t.getCategory() );
      out.write("\">\n");
      out.write("                                    <td>\n");
      out.write("                                        <div class=\"d-flex align-items-center gap-3\">\n");
      out.write("                                            <div class=\"badge ");
      out.print( t.getTransactionType().equals("INCOME") ? "badge-income" : "badge-expense" );
      out.write(" p-2 rounded-circle\">\n");
      out.write("                                                <i class=\"fa-solid ");
      out.print( t.getTransactionType().equals("INCOME") ? "fa-arrow-down" : "fa-arrow-up" );
      out.write("\"></i>\n");
      out.write("                                            </div>\n");
      out.write("                                            <div>\n");
      out.write("                                                <div class=\"fw-bold\">");
      out.print( t.getDescription() != null && !t.getDescription().isEmpty() ? t.getDescription() : t.getCategory() );
      out.write("</div>\n");
      out.write("                                                <div class=\"text-muted small\">");
      out.print( t.getTransactionType() );
      out.write("</div>\n");
      out.write("                                            </div>\n");
      out.write("                                        </div>\n");
      out.write("                                    </td>\n");
      out.write("                                    <td><span class=\"badge bg-light text-dark shadow-sm border\">");
      out.print( t.getCategory() );
      out.write("</span></td>\n");
      out.write("                                    <td class=\"text-muted\">");
      out.print( df.format(t.getDate()) );
      out.write("</td>\n");
      out.write("                                    <td class=\"text-end fw-bold ");
      out.print( t.getTransactionType().equals("INCOME") ? "text-success" : "text-dark" );
      out.write("\">\n");
      out.write("                                        ");
      out.print( t.getTransactionType().equals("INCOME") ? "+" : "-" );
      out.write('₹');
      out.print( String.format("%.2f", t.getAmount()) );
      out.write("\n");
      out.write("                                    </td>\n");
      out.write("                                    <td class=\"text-center\">\n");
      out.write("                                        <form action=\"");
      out.print( request.getContextPath() );
      out.write("/dashboard\" method=\"POST\" class=\"d-inline\" onsubmit=\"return confirm('Are you sure you want to delete this transaction?');\">\n");
      out.write("                                            <input type=\"hidden\" name=\"action\" value=\"delete\">\n");
      out.write("                                            <input type=\"hidden\" name=\"id\" value=\"");
      out.print( t.getId() );
      out.write("\">\n");
      out.write("                                            <button type=\"submit\" class=\"btn btn-sm btn-light text-danger\" style=\"border-radius: 8px;\" title=\"Delete\">\n");
      out.write("                                                <i class=\"fa-solid fa-trash\"></i>\n");
      out.write("                                            </button>\n");
      out.write("                                        </form>\n");
      out.write("                                    </td>\n");
      out.write("                                </tr>\n");
      out.write("                                ");
 } 
      out.write("\n");
      out.write("                            ");
 } 
      out.write("\n");
      out.write("                        </tbody>\n");
      out.write("                    </table>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Add Transaction Modal -->\n");
      out.write("    <div class=\"modal fade\" id=\"addTransactionModal\" tabindex=\"-1\" aria-hidden=\"true\">\n");
      out.write("      <div class=\"modal-dialog modal-dialog-centered\">\n");
      out.write("        <div class=\"modal-content\" style=\"border-radius: 16px; border: none;\">\n");
      out.write("          <div class=\"modal-header border-bottom-0\">\n");
      out.write("            <h5 class=\"modal-title fw-bold text-primary\"><i class=\"fa-solid fa-plus-circle me-2\"></i>New Transaction</h5>\n");
      out.write("            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n");
      out.write("          </div>\n");
      out.write("          <form action=\"");
      out.print( request.getContextPath() );
      out.write("/dashboard\" method=\"POST\">\n");
      out.write("            <div class=\"modal-body pb-0\">\n");
      out.write("                <div class=\"row g-3\">\n");
      out.write("                    <div class=\"col-6\">\n");
      out.write("                        <label class=\"form-label text-muted small fw-bold\">Type</label>\n");
      out.write("                        <select name=\"type\" class=\"form-select\" style=\"border-radius: 10px;\" required>\n");
      out.write("                            <option value=\"EXPENSE\">Expense</option>\n");
      out.write("                            <option value=\"INCOME\">Income</option>\n");
      out.write("                        </select>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"col-6\">\n");
      out.write("                        <label class=\"form-label text-muted small fw-bold\">Amount (₹)</label>\n");
      out.write("                        <input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" style=\"border-radius: 10px;\" placeholder=\"0.00\" required>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"col-12\">\n");
      out.write("                        <label class=\"form-label text-muted small fw-bold\">Category</label>\n");
      out.write("                        <input type=\"text\" name=\"category\" class=\"form-control\" style=\"border-radius: 10px;\" placeholder=\"e.g. Food, Travel, Salary\" required>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"col-12\">\n");
      out.write("                        <label class=\"form-label text-muted small fw-bold\">Description (Optional)</label>\n");
      out.write("                        <input type=\"text\" name=\"description\" class=\"form-control\" style=\"border-radius: 10px;\" placeholder=\"What was this for?\">\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"modal-footer border-top-0 pt-4\">\n");
      out.write("              <button type=\"button\" class=\"btn btn-light\" data-bs-dismiss=\"modal\" style=\"border-radius: 10px;\">Cancel</button>\n");
      out.write("              <button type=\"submit\" class=\"btn btn-primary\" style=\"background: var(--primary); border: none; border-radius: 10px;\">Save Transaction</button>\n");
      out.write("            </div>\n");
      out.write("          </form>\n");
      out.write("        </div>\n");
      out.write("      </div>\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Bootstrap JS Bundle -->\n");
      out.write("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>\n");
      out.write("    \n");
      out.write("    <!-- Chart.js & Filter Logic -->\n");
      out.write("    <script>\n");
      out.write("        // Filter functionality\n");
      out.write("        function applyFilters() {\n");
      out.write("            var categorySelect = document.getElementById(\"categoryFilter\").value.toLowerCase();\n");
      out.write("            var typeSelect = document.getElementById(\"typeFilter\").value.toUpperCase();\n");
      out.write("            var rows = document.querySelectorAll(\".transaction-row\");\n");
      out.write("            \n");
      out.write("            rows.forEach(function(row) {\n");
      out.write("                var rowCat = row.getAttribute('data-category').toLowerCase();\n");
      out.write("                var rowType = row.getAttribute('data-type').toUpperCase();\n");
      out.write("                \n");
      out.write("                var matchCategory = categorySelect === \"\" || rowCat.includes(categorySelect);\n");
      out.write("                var matchType = typeSelect === \"\" || rowType === typeSelect;\n");
      out.write("                \n");
      out.write("                if (matchCategory && matchType) {\n");
      out.write("                    row.style.display = \"\";\n");
      out.write("                } else {\n");
      out.write("                    row.style.display = \"none\";\n");
      out.write("                }\n");
      out.write("            });\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.addEventListener('DOMContentLoaded', function() {\n");
      out.write("            var ctx = document.getElementById('categoryChart').getContext('2d');\n");
      out.write("            \n");
      out.write("            var labels = ");
      out.print( chartLabels );
      out.write(";\n");
      out.write("            var data = ");
      out.print( chartData );
      out.write(";\n");
      out.write("            \n");
      out.write("            var chart = new Chart(ctx, {\n");
      out.write("                type: 'doughnut',\n");
      out.write("                data: {\n");
      out.write("                    labels: labels,\n");
      out.write("                    datasets: [{\n");
      out.write("                        data: data,\n");
      out.write("                        backgroundColor: [\n");
      out.write("                            '#4318ff', '#05cd99', '#ffce20', '#ee5d50', \n");
      out.write("                            '#39b8ff', '#8f60ff', '#ff8b39'\n");
      out.write("                        ],\n");
      out.write("                        borderWidth: 0,\n");
      out.write("                        hoverOffset: 4\n");
      out.write("                    }]\n");
      out.write("                },\n");
      out.write("                options: {\n");
      out.write("                    responsive: true,\n");
      out.write("                    maintainAspectRatio: false,\n");
      out.write("                    cutout: '75%',\n");
      out.write("                    plugins: {\n");
      out.write("                        legend: {\n");
      out.write("                            position: 'right',\n");
      out.write("                            labels: {\n");
      out.write("                                usePointStyle: true,\n");
      out.write("                                padding: 20,\n");
      out.write("                                font: { family: \"'Outfit', sans-serif\" }\n");
      out.write("                            }\n");
      out.write("                        }\n");
      out.write("                    }\n");
      out.write("                }\n");
      out.write("            });\n");
      out.write("        });\n");
      out.write("    </script>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
