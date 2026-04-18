<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense Tracker - Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(135deg, #f4f7fa 0%, #e2e8f0 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            background: white;
            border-radius: 24px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: flex;
            border: none;
        }

        .login-left {
            background: linear-gradient(135deg, #4318ff 0%, #8f60ff 100%);
            color: white;
            padding: 50px;
            width: 45%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            text-align: center;
        }

        .login-right {
            padding: 60px;
            width: 55%;
        }

        .form-control {
            border-radius: 12px;
            padding: 12px 20px;
            border: 1px solid #e2e8f0;
            background: #f8fafc;
        }

        .form-control:focus {
            box-shadow: none;
            border-color: #4318ff;
            background: white;
        }

        .btn-primary {
            background: #4318ff;
            border: none;
            border-radius: 12px;
            padding: 12px;
            font-weight: 600;
            transition: 0.3s;
        }

        .btn-primary:hover {
            background: #320ddb;
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(67, 24, 255, 0.2);
        }
        
        .alert-error {
            background-color: #fef2f2;
            color: #ef4444;
            border-radius: 12px;
            padding: 12px;
            font-size: 0.9rem;
            font-weight: 500;
            margin-bottom: 20px;
            border: 1px solid #fecaca;
        }
    </style>
</head>
<body>

    <div class="login-card">
        <!-- Left Side Presentation -->
        <div class="login-left d-none d-md-flex">
            <i class="fa-solid fa-wallet mb-4" style="font-size: 4rem;"></i>
            <h2 class="fw-bold mb-3">FinTrack Platform</h2>
            <p class="opacity-75" style="font-size: 1rem;">Take control of your personal finances with our intelligent analytical dashboard.</p>
        </div>
        
        <!-- Right Side Form -->
        <div class="login-right">
            <div class="mb-5 text-center">
                <h3 class="fw-bold" style="color: #1e293b;">Welcome Back</h3>
                <p class="text-muted">Please sign in to access your dashboard.</p>
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert-error">
                    <i class="fa-solid fa-circle-exclamation me-2"></i> <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if ("success".equals(request.getParameter("logout"))) { %>
                <div class="alert-error" style="background: #ecfdf5; color: #10b981; border-color: #a7f3d0;">
                    <i class="fa-solid fa-check-circle me-2"></i> Successfully logged out.
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/login" method="POST">
                <div class="mb-4">
                    <label class="form-label text-muted fw-bold small">Email Address</label>
                    <input type="text" name="email" class="form-control" placeholder="sharayu@tracker.com" value="sharayu@tracker.com" required>
                </div>
                
                <div class="mb-4">
                    <label class="form-label text-muted fw-bold small d-flex justify-content-between">
                        Password
                    </label>
                    <input type="password" name="password" class="form-control" placeholder="••••••••" value="password123" required>
                    <div class="form-text mt-2"><small>Demo password is <strong>password123</strong></small></div>
                </div>
                
                <button type="submit" class="btn btn-primary w-100 mt-2">Sign In to Dashboard</button>
            </form>
        </div>
    </div>

</body>
</html>
