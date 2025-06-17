<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
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
            .btn-register {
                background-color: #198754;
                border: none;
            }
            .btn-register:hover {
                background-color: #157347;
            }
        </style>
    </head>
    <body>

        <div class="card">
            <h3 class="text-center mb-4">Register</h3>

            <% if (request.getAttribute("ERROR") != null) {%>
            <div class="alert alert-danger">
                <%= request.getAttribute("ERROR")%>
            </div>
            <% } %>

            <% if (request.getAttribute("SUCCESS") != null) {%>
            <div class="alert alert-success">
                <%= request.getAttribute("SUCCESS")%>
            </div>
            <% }%>

            <form action="RegisterController" method="post">
                <!-- Username -->
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" name="username" id="username" class="form-control" required
                           placeholder="Enter your username" pattern="[a-zA-Z0-9]+"
                           title="Username can only contain letters and numbers"
                           value="<%= request.getParameter("username") != null ? request.getParameter("username") : ""%>">
                </div>

                <!-- Password -->
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required
                           placeholder="Enter your password" minlength="6">
                </div>

                <!-- Full Name -->
                <div class="mb-3">
                    <label for="fullname" class="form-label">Full Name</label>
                    <input type="text" name="fullname" id="fullname" class="form-control" required
                           placeholder="Enter your full name"
                           value="<%= request.getParameter("fullname") != null ? request.getParameter("fullname") : ""%>">
                </div>

                <!-- Gender -->
                <div class="mb-3">
                    <label class="form-label">Gender</label>
                    <div class="d-flex gap-3">
                        <div class="form-check">
                            <input type="radio" name="gender" value="male" id="male" class="form-check-input" required
                                   <%= (request.getParameter("gender") == null || "male".equals(request.getParameter("gender"))) ? "checked" : ""%>>
                            <label class="form-check-label" for="male">Male</label>
                        </div>
                        <div class="form-check">
                            <input type="radio" name="gender" value="female" id="female" class="form-check-input" required
                                   <%= "female".equals(request.getParameter("gender")) ? "checked" : ""%>>
                            <label class="form-check-label" for="female">Female</label>
                        </div>
                    </div>
                </div>

                <!-- Gmail -->
                <div class="mb-3">
                    <label for="gmail" class="form-label">Email</label>
                    <input type="email" name="gmail" id="gmail" class="form-control" required
                           placeholder="Enter your email"
                           value="<%= request.getParameter("gmail") != null ? request.getParameter("gmail") : ""%>">
                </div>

                <!-- Phone -->
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" name="phone" id="phone" class="form-control" required pattern="[0-9]{10,12}"
                           title="Phone number should contain 10 to 12 digits"
                           placeholder="Enter your phone number"
                           value="<%= request.getParameter("phone") != null ? request.getParameter("phone") : ""%>">
                </div>

                <!-- Submit -->
                <button type="submit" class="btn btn-register w-100 text-white">Register</button>

                <!-- Redirect -->
                <div class="mt-3 text-center">
                    <a href="login.jsp" class="text-decoration-none">Already have an account? Login</a>
                </div>
            </form>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 