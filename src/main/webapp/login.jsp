<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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

            <%-- Hiển thị thông báo logout thành công --%>
            <% 
                String logoutParam = request.getParameter("logout");
                if ("success".equals(logoutParam)) {
            %>
            <div class="alert alert-success">
                <i class="fas fa-check-circle me-2"></i>
                Bạn đã đăng xuất thành công!
            </div>
            <% } %>

            <%-- Hiển thị lỗi đăng nhập --%>
            <% if (request.getAttribute("ERROR") != null) {%>
            <div class="alert alert-danger">
                <%= request.getAttribute("ERROR")%>
            </div>
            <% } %>

            <%-- Hiển thị thông báo khi cần đăng nhập để truy cập trang được yêu cầu --%>
            <% 
                String redirectURL = (String) session.getAttribute("REDIRECT_URL");
                if (redirectURL != null) {
            %>
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                Bạn cần đăng nhập để truy cập trang này.
                <br><small>Trang đích: <%= redirectURL %></small>
            </div>
            <% } %>

            <%-- Hiển thị cảnh báo khi truy cập giỏ hàng mà chưa đăng nhập --%>
            <%
                String loginMsg = (String) session.getAttribute("LOGIN_MESSAGE");
                if (loginMsg != null) {
                    session.removeAttribute("LOGIN_MESSAGE");
            %>
            <div class="alert alert-warning">
                <%= loginMsg%>
            </div>
            <%
                }
            %>

            <%-- Hiển thị thông báo thành công (ví dụ đăng ký xong) --%>
            <%
                String successMsg = (String) session.getAttribute("SUCCESS");
                if (successMsg != null) {
                    session.removeAttribute("SUCCESS"); // Remove after displaying
            %>
            <div class="alert alert-success">
                <%= successMsg%>
            </div>
            <% }%>

            <%-- Hiển thị message thông thường (nếu có) --%>
            <div class="mt-3 text-success">
                <%= request.getAttribute("message") != null ? request.getAttribute("message") : ""%>
            </div>

            <form action="LoginController" method="post">
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
                    <a href="register.jsp" class="text-decoration-none">Don't have an account? Register</a> <br>
                    <a href="forgot-password.jsp" class="text-decoration-none">Do you forget your password? Forget Password</a>
                </div>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
