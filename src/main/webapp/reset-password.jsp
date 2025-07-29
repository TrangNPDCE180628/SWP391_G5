<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Reset Password</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    </head>
    <body>
        
        <div class="container mt-5" style="max-width: 500px">
            <h3 class="mb-4 text-center">Reset Password</h3>

            <form action="ForgotPasswordServlet" method="post">
                <input type="hidden" name="action" value="resetPassword">
                <input type="hidden" name="email" value="<%= request.getAttribute("email")%>">

                <div class="mb-3">
                    <label for="newPassword">New Password</label>
                    <input type="password" name="newPassword" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Reset Password</button>
            </form>

            <div class="mt-3 text-success"><%= request.getAttribute("message") != null ? request.getAttribute("message") : ""%></div>
            <div class="mt-3 text-danger"><%= request.getAttribute("error") != null ? request.getAttribute("error") : ""%></div>
        </div>
    </body>
</html>
