<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.expensetracker.model.Transaction" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
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
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense Tracker - Premium Dashboard</title>
    <!-- Modern Font -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        :root {
            --bg-color: #f4f7fa;
            --card-bg: #ffffff;
            --text-primary: #1e293b;
            --text-secondary: #64748b;
            --primary: #4318ff;
            --success: #05cd99;
            --danger: #ee5d50;
            --warning: #ffce20;
            --info: #39b8ff;
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--bg-color);
            color: var(--text-primary);
        }

        .navbar {
            background-color: var(--card-bg);
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
            padding: 15px 0;
            margin-bottom: 30px;
        }

        .navbar-brand {
            font-weight: 700;
            color: var(--primary) !important;
            font-size: 1.5rem;
        }

        .user-profile {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--primary), #8f60ff);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
        }

        .card-custom {
            background: var(--card-bg);
            border: none;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            transition: transform 0.2s, box-shadow 0.2s;
            height: 100%;
            overflow: hidden;
        }

        .card-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.07);
        }

        .stat-card {
            color: white;
            padding: 25px;
            position: relative;
        }

        .bg-gradient-balance { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
        .bg-gradient-income { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        .bg-gradient-expense { background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%); }
        .bg-gradient-savings { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }

        .stat-icon {
            position: absolute;
            right: 20px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 3rem;
            opacity: 0.2;
        }

        .stat-title {
            font-size: 1rem;
            font-weight: 500;
            opacity: 0.9;
            margin-bottom: 5px;
        }

        .stat-value {
            font-size: 2.2rem;
            font-weight: 700;
            margin-bottom: 0;
        }

        .section-title {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 20px;
            color: var(--text-primary);
        }

        .table-custom {
            margin-bottom: 0;
        }

        .table-custom th {
            border-bottom: 1px solid #edf2f7;
            color: var(--text-secondary);
            font-weight: 500;
            padding: 15px;
            font-size: 0.9rem;
        }

        .table-custom td {
            border-bottom: 1px solid #edf2f7;
            padding: 15px;
            vertical-align: middle;
            font-weight: 500;
        }

        .table-custom tr:last-child td {
            border-bottom: none;
        }

        .badge-income { background-color: rgba(5, 205, 153, 0.1); color: var(--success); }
        .badge-expense { background-color: rgba(238, 93, 80, 0.1); color: var(--danger); }

        .btn-action {
            border-radius: 12px;
            font-weight: 500;
            padding: 10px 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            width: 100%;
            margin-bottom: 10px;
            transition: all 0.3s;
        }
        
        .insight-box {
            padding: 15px;
            border-radius: 12px;
            background: #f8fafc;
            margin-bottom: 15px;
            border-left: 4px solid var(--primary);
        }

        .insight-box p { margin: 0; font-size: 0.9rem; color: var(--text-secondary); }
        .insight-box h5 { margin: 5px 0 0 0; font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .animate-fade {
            animation: fadeIn 0.4s ease forwards;
        }
    </style>
</head>
<body>

    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="fa-solid fa-wallet me-2"></i>FinTrack</a>
            
            <div class="ms-auto d-flex align-items-center">
                <!-- User Profile Section -->
                <div class="user-profile">
                    <div class="text-end d-none d-md-block">
                        <div class="fw-bold" style="font-size: 0.95rem;">Sharayu Pathare</div>
                        <div class="text-muted" style="font-size: 0.8rem;">sharayu@tracker.com</div>
                    </div>
                    <div class="user-avatar">SP</div>
                    <a href="<%= request.getContextPath() %>/login?action=logout" class="btn btn-sm btn-outline-danger ms-3"><i class="fa-solid fa-arrow-right-from-bracket"></i></a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container pb-5">
        
        <!-- Budget Alert -->
        <% if(budgetAlert) { %>
        <div class="alert alert-danger d-flex align-items-center animate-fade" role="alert" style="border-radius: 15px; border: none; box-shadow: 0 4px 15px rgba(238, 93, 80, 0.1);">
            <i class="fa-solid fa-triangle-exclamation fs-3 me-3"></i>
            <div>
                <h5 class="alert-heading mb-1 fw-bold">Budget Exceeded!</h5>
                <p class="mb-0">You have exceeded your predefined monthly budget (₹50,000). Please review your recent expenses.</p>
            </div>
        </div>
        <% } %>

        <!-- Smart Summary Cards -->
        <div class="row g-4 mb-4">
            <div class="col-xl-3 col-md-6 animate-fade" style="animation-delay: 0.1s;">
                <div class="card card-custom stat-card bg-gradient-balance">
                    <i class="fa-solid fa-vault stat-icon"></i>
                    <div class="stat-title">Total Balance</div>
                    <h2 class="stat-value">₹<%= String.format("%.2f", balance) %></h2>
                </div>
            </div>
            <div class="col-xl-3 col-md-6 animate-fade" style="animation-delay: 0.2s;">
                <div class="card card-custom stat-card bg-gradient-income">
                    <i class="fa-solid fa-arrow-trend-up stat-icon"></i>
                    <div class="stat-title">Monthly Income</div>
                    <h2 class="stat-value">₹<%= String.format("%.2f", monthlyIncome) %></h2>
                </div>
            </div>
            <div class="col-xl-3 col-md-6 animate-fade" style="animation-delay: 0.3s;">
                <div class="card card-custom stat-card bg-gradient-expense">
                    <i class="fa-solid fa-arrow-trend-down stat-icon"></i>
                    <div class="stat-title">Monthly Expense</div>
                    <h2 class="stat-value">₹<%= String.format("%.2f", monthlyExpense) %></h2>
                </div>
            </div>
            <div class="col-xl-3 col-md-6 animate-fade" style="animation-delay: 0.4s;">
                <div class="card card-custom stat-card bg-gradient-savings">
                    <i class="fa-solid fa-piggy-bank stat-icon"></i>
                    <div class="stat-title">Savings Rate</div>
                    <h2 class="stat-value"><%= String.format("%.1f", savingsRate) %>%</h2>
                </div>
            </div>
        </div>

        <!-- Charts and Insights Row -->
        <div class="row g-4 mb-4">
            
            <!-- Category Breakdown -->
            <div class="col-lg-5 animate-fade" style="animation-delay: 0.5s;">
                <div class="card card-custom p-4">
                    <h5 class="section-title">Expense Category Breakdown</h5>
                    <div style="position: relative; height: 260px; display: flex; justify-content: center;">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Expense Insights & Actions -->
            <div class="col-lg-3 animate-fade" style="animation-delay: 0.6s;">
                <div class="card card-custom p-4">
                    <h5 class="section-title">Smart Insights</h5>
                    
                    <div class="insight-box" style="border-left-color: var(--info);">
                        <p>Today's Spending</p>
                        <h5>₹<%= String.format("%.2f", todayExpense) %></h5>
                    </div>
                    
                    <div class="insight-box" style="border-left-color: var(--danger);">
                        <p>Highest Spending Category</p>
                        <h5><%= highestCategory != null ? highestCategory : "None" %></h5>
                    </div>
                    
                    <div class="insight-box" style="border-left-color: var(--success);">
                        <p>Budget Remaining</p>
                        <h5><%= budgetRemaining > 0 ? "₹" + String.format("%.2f", budgetRemaining) : "Budget Overrun" %></h5>
                    </div>
                </div>
            </div>
            
            <!-- Quick Actions -->
            <div class="col-lg-4 animate-fade" style="animation-delay: 0.7s;">
                <div class="card card-custom p-4">
                    <h5 class="section-title">Quick Actions & Filters</h5>
                    
                    <button class="btn btn-primary btn-action text-white mb-3" data-bs-toggle="modal" data-bs-target="#addTransactionModal" style="background: var(--primary); border: none;">
                        <i class="fa-solid fa-plus"></i> Record New Transaction
                    </button>
                    
                    <hr class="text-muted my-4">
                    
                    <h6 class="text-secondary mb-3"><i class="fa-solid fa-filter me-2"></i>Filter Transactions</h6>
                    <select id="categoryFilter" class="form-select mb-3" style="border-radius: 10px;" onchange="applyFilters()">
                        <option value="">All Categories</option>
                        <option value="Food">Food & Dining</option>
                        <option value="Travel">Transportation</option>
                        <option value="Shopping">Shopping</option>
                        <option value="Bills">Bills & Utilities</option>
                        <option value="Salary">Salary / Income</option>
                        <option value="Other">Other</option>
                    </select>
                    
                    <div class="d-flex gap-2">
                        <select id="typeFilter" class="form-select" style="border-radius: 10px;" onchange="applyFilters()">
                            <option value="">Any Type</option>
                            <option value="INCOME">Income</option>
                            <option value="EXPENSE">Expense</option>
                        </select>
                        <button class="btn btn-light" style="border-radius: 10px;" onclick="applyFilters()"><i class="fa-solid fa-search"></i></button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Transactions Library -->
        <div class="col-12 animate-fade" style="animation-delay: 0.8s;">
            <div class="card card-custom p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="section-title mb-0">Transactions</h5>
                </div>
                
                <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                    <table class="table table-custom table-hover" id="transactionsTable">
                        <thead>
                            <tr>
                                <th>Transaction</th>
                                <th>Category</th>
                                <th>Date</th>
                                <th class="text-end">Amount</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (transactions.isEmpty()) { %>
                                <tr><td colspan="5" class="text-center text-muted py-4">No transactions found.</td></tr>
                            <% } else { %>
                                <% for(Transaction t : transactions) { %>
                                <tr class="transaction-row" data-type="<%= t.getTransactionType() %>" data-category="<%= t.getCategory() %>">
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="badge <%= t.getTransactionType().equals("INCOME") ? "badge-income" : "badge-expense" %> p-2 rounded-circle">
                                                <i class="fa-solid <%= t.getTransactionType().equals("INCOME") ? "fa-arrow-down" : "fa-arrow-up" %>"></i>
                                            </div>
                                            <div>
                                                <div class="fw-bold"><%= t.getDescription() != null && !t.getDescription().isEmpty() ? t.getDescription() : t.getCategory() %></div>
                                                <div class="text-muted small"><%= t.getTransactionType() %></div>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="badge bg-light text-dark shadow-sm border"><%= t.getCategory() %></span></td>
                                    <td class="text-muted"><%= df.format(t.getDate()) %></td>
                                    <td class="text-end fw-bold <%= t.getTransactionType().equals("INCOME") ? "text-success" : "text-dark" %>">
                                        <%= t.getTransactionType().equals("INCOME") ? "+" : "-" %>₹<%= String.format("%.2f", t.getAmount()) %>
                                    </td>
                                    <td class="text-center">
                                        <form action="<%= request.getContextPath() %>/dashboard" method="POST" class="d-inline" onsubmit="return confirm('Are you sure you want to delete this transaction?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="<%= t.getId() %>">
                                            <button type="submit" class="btn btn-sm btn-light text-danger" style="border-radius: 8px;" title="Delete">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <% } %>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>

    <!-- Add Transaction Modal -->
    <div class="modal fade" id="addTransactionModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="border-radius: 16px; border: none;">
          <div class="modal-header border-bottom-0">
            <h5 class="modal-title fw-bold text-primary"><i class="fa-solid fa-plus-circle me-2"></i>New Transaction</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form action="<%= request.getContextPath() %>/dashboard" method="POST">
            <div class="modal-body pb-0">
                <div class="row g-3">
                    <div class="col-6">
                        <label class="form-label text-muted small fw-bold">Type</label>
                        <select name="type" class="form-select" style="border-radius: 10px;" required>
                            <option value="EXPENSE">Expense</option>
                            <option value="INCOME">Income</option>
                        </select>
                    </div>
                    <div class="col-6">
                        <label class="form-label text-muted small fw-bold">Amount (₹)</label>
                        <input type="number" step="0.01" name="amount" class="form-control" style="border-radius: 10px;" placeholder="0.00" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label text-muted small fw-bold">Category</label>
                        <input type="text" name="category" class="form-control" style="border-radius: 10px;" placeholder="e.g. Food, Travel, Salary" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label text-muted small fw-bold">Description (Optional)</label>
                        <input type="text" name="description" class="form-control" style="border-radius: 10px;" placeholder="What was this for?">
                    </div>
                </div>
            </div>
            <div class="modal-footer border-top-0 pt-4">
              <button type="button" class="btn btn-light" data-bs-dismiss="modal" style="border-radius: 10px;">Cancel</button>
              <button type="submit" class="btn btn-primary" style="background: var(--primary); border: none; border-radius: 10px;">Save Transaction</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Chart.js & Filter Logic -->
    <script>
        // Filter functionality
        function applyFilters() {
            var categorySelect = document.getElementById("categoryFilter").value.toLowerCase();
            var typeSelect = document.getElementById("typeFilter").value.toUpperCase();
            var rows = document.querySelectorAll(".transaction-row");
            
            rows.forEach(function(row) {
                var rowCat = row.getAttribute('data-category').toLowerCase();
                var rowType = row.getAttribute('data-type').toUpperCase();
                
                var matchCategory = categorySelect === "" || rowCat.includes(categorySelect);
                var matchType = typeSelect === "" || rowType === typeSelect;
                
                if (matchCategory && matchType) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            });
        }

        document.addEventListener('DOMContentLoaded', function() {
            var ctx = document.getElementById('categoryChart').getContext('2d');
            
            var labels = <%= chartLabels %>;
            var data = <%= chartData %>;
            
            var chart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        backgroundColor: [
                            '#4318ff', '#05cd99', '#ffce20', '#ee5d50', 
                            '#39b8ff', '#8f60ff', '#ff8b39'
                        ],
                        borderWidth: 0,
                        hoverOffset: 4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    cutout: '75%',
                    plugins: {
                        legend: {
                            position: 'right',
                            labels: {
                                usePointStyle: true,
                                padding: 20,
                                font: { family: "'Outfit', sans-serif" }
                            }
                        }
                    }
                }
            });
        });
    </script>
</body>
</html>
