<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #e9ecef;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .card {
            width: 100%;
            max-width: 400px;
            padding: 2rem;
            border-radius: 1rem;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .btn-login {
            background-color: #198754;
            border: none;
        }
        .btn-login:hover {
            background-color: #157347;
        }
        .form-check-input:checked {
            background-color: #198754;
            border-color: #198754;
        }
    </style>
</head>
<body>

<div class="card">
    <h3 class="text-center mb-4">Login</h3>

    <% if (request.getAttribute("ERROR") != null) { %>
        <div class="alert alert-danger">
            <%= request.getAttribute("ERROR") %>
        </div>
    <% } %>

    <% 
        String successMsg = (String) session.getAttribute("SUCCESS");
        if (successMsg != null) {
            session.removeAttribute("SUCCESS"); // Remove after displaying
    %>
        <div class="alert alert-success">
            <%= successMsg %>
        </div>
    <% } %>

    <form action="MainController" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input type="text" name="username" id="username" class="form-control" required placeholder="Enter your username">
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" name="password" id="password" class="form-control" required placeholder="Enter your password">
        </div>
        <div class="form-check mb-3">
            <input type="checkbox" class="form-check-input" id="remember">
            <label class="form-check-label" for="remember">Remember me</label>
        </div>
        <input type="submit" name="action" class="btn btn-login w-100 text-white" value="Login"/>
        <div class="mt-3 text-center">
            <a href="register.jsp" class="text-decoration-none">Don't have an account? Register</a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
