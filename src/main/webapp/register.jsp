<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
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
                           placeholder="Enter your username" pattern="[a-zA-Z0-9]{3,20}"
                           title="Username must contain only letters and numbers, 3-20 characters"
                           minlength="3" maxlength="20"
                           value="<%= request.getParameter("username") != null ? request.getParameter("username") : ""%>">
                    <div class="form-text">Only letters and numbers, 3-20 characters</div>
                </div>

                <!-- Password -->
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required
                           placeholder="Enter your password" minlength="6"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$"
                           title="Password must be at least 6 characters and contain both letters and numbers">
                    <div class="form-text">At least 6 characters with letters and numbers</div>
                </div>

                <!-- Full Name -->
                <div class="mb-3">
                    <label for="fullname" class="form-label">Full Name</label>
                    <input type="text" name="fullname" id="fullname" class="form-control" required
                           placeholder="Enter your full name" pattern="[a-zA-Z\s]{2,50}"
                           title="Full name must contain only letters and spaces, 2-50 characters"
                           minlength="2" maxlength="50"
                           value="<%= request.getParameter("fullname") != null ? request.getParameter("fullname") : ""%>">
                    <div class="form-text">Only letters and spaces, 2-50 characters</div>
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
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
                           title="Please enter a valid email address"
                           value="<%= request.getParameter("gmail") != null ? request.getParameter("gmail") : ""%>">
                    <div class="form-text">We'll send a verification code to this email</div>
                </div>

                <!-- Phone -->
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" name="phone" id="phone" class="form-control" required pattern="0[0-9]{9,11}"
                           title="Phone number must start with 0 and contain 10-12 digits"
                           placeholder="Enter your phone number" minlength="10" maxlength="12"
                           value="<%= request.getParameter("phone") != null ? request.getParameter("phone") : ""%>">
                    <div class="form-text">Must start with 0, 10-12 digits total</div>
                </div>

                <!-- Address -->
                <div class="mb-3">
                    <label for="address" class="form-label">Address</label>
                    <textarea name="address" id="address" class="form-control" required
                              placeholder="Enter your address" minlength="2" maxlength="100" rows="3"
                              title="Address must be between 2 and 100 characters"><%= request.getParameter("address") != null ? request.getParameter("address") : ""%></textarea>
                    <div class="form-text">2-100 characters</div>
                </div>

                <!-- Submit -->
                <button type="submit" class="btn btn-register w-100 text-white">Send Verification Code</button>

                <!-- Redirect -->
                <div class="mt-3 text-center">
                    <a href="login.jsp" class="text-decoration-none">Already have an account? Login</a>
                </div>
            </form>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Real-time validation
            document.addEventListener('DOMContentLoaded', function() {
                const form = document.querySelector('form');
                const username = document.getElementById('username');
                const password = document.getElementById('password');
                const fullname = document.getElementById('fullname');
                const gmail = document.getElementById('gmail');
                const phone = document.getElementById('phone');
                const address = document.getElementById('address');

                // Username validation
                username.addEventListener('input', function() {
                    const value = this.value;
                    const isValid = /^[a-zA-Z0-9]{3,20}$/.test(value);
                    toggleValidation(this, isValid, 'Username must contain only letters and numbers, 3-20 characters');
                });

                // Password validation
                password.addEventListener('input', function() {
                    const value = this.value;
                    const hasLength = value.length >= 6;
                    const hasLetter = /[a-zA-Z]/.test(value);
                    const hasNumber = /[0-9]/.test(value);
                    const isValid = hasLength && hasLetter && hasNumber;
                    
                    let message = '';
                    if (!hasLength) message = 'Password must be at least 6 characters';
                    else if (!hasLetter) message = 'Password must contain letters';
                    else if (!hasNumber) message = 'Password must contain numbers';
                    
                    toggleValidation(this, isValid, message);
                });

                // Full name validation
                fullname.addEventListener('input', function() {
                    const value = this.value;
                    const isValid = /^[a-zA-Z\s]{2,50}$/.test(value);
                    toggleValidation(this, isValid, 'Full name must contain only letters and spaces, 2-50 characters');
                });

                // Email validation
                gmail.addEventListener('input', function() {
                    const value = this.value;
                    const isValid = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value);
                    toggleValidation(this, isValid, 'Please enter a valid email address');
                });

                // Phone validation
                phone.addEventListener('input', function() {
                    const value = this.value;
                    const isValid = /^0[0-9]{9,11}$/.test(value);
                    toggleValidation(this, isValid, 'Phone number must start with 0 and contain 10-12 digits');
                });

                // Address validation
                address.addEventListener('input', function() {
                    const value = this.value;
                    const isValid = value.length >= 2 && value.length <= 100;
                    let message = '';
                    if (value.length < 2) message = 'Address must be at least 2 characters';
                    else if (value.length > 100) message = 'Address must be at most 100 characters';
                    
                    toggleValidation(this, isValid, message);
                });

                // Only allow numbers in phone field
                phone.addEventListener('keypress', function(e) {
                    if (!/[0-9]/.test(e.key) && !['Backspace', 'Delete', 'Tab', 'Escape', 'Enter'].includes(e.key)) {
                        e.preventDefault();
                    }
                });

                function toggleValidation(field, isValid, message) {
                    const formText = field.nextElementSibling;
                    
                    if (field.value === '') {
                        field.classList.remove('is-valid', 'is-invalid');
                        formText.className = 'form-text';
                        return;
                    }
                    
                    if (isValid) {
                        field.classList.remove('is-invalid');
                        field.classList.add('is-valid');
                        formText.className = 'form-text text-success';
                        formText.textContent = '✓ Valid';
                    } else {
                        field.classList.remove('is-valid');
                        field.classList.add('is-invalid');
                        formText.className = 'form-text text-danger';
                        formText.textContent = message;
                    }
                }

                // Form submission validation
                form.addEventListener('submit', function(e) {
                    const fields = [username, password, fullname, gmail, phone, address];
                    let isFormValid = true;

                    fields.forEach(field => {
                        if (!field.checkValidity()) {
                            isFormValid = false;
                            field.classList.add('is-invalid');
                        }
                    });

                    if (!isFormValid) {
                        e.preventDefault();
                        alert('Please correct the errors in the form before submitting.');
                    }
                });
            });
        </script>
    </body>
</html> 