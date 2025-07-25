<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Verify OTP</title>
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
            .btn-verify {
                background-color: #198754;
                border: none;
            }
            .btn-verify:hover {
                background-color: #157347;
            }
            .otp-input {
                text-align: center;
                font-size: 1.5rem;
                letter-spacing: 0.5rem;
            }
        </style>
    </head>
    <body>

        <div class="card">
            <h3 class="text-center mb-4">Verify Your Email</h3>
            
            <div class="text-center mb-4">
                <i class="fas fa-envelope-open-text fa-3x text-primary"></i>
                <p class="mt-3 text-muted">
                    We've sent a 6-digit verification code to<br>
                    <strong><%= request.getAttribute("email") %></strong><br>
                    <small class="text-info">Enter the code to complete your registration</small>
                </p>
            </div>

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
                <input type="hidden" name="action" value="verifyOtp">
                <input type="hidden" name="email" value="<%= request.getAttribute("email") %>">
                
                <!-- OTP Input -->
                <div class="mb-4">
                    <label for="otp" class="form-label text-center w-100">Enter Verification Code</label>
                    <input type="text" name="otp" id="otp" class="form-control otp-input" 
                           maxlength="6" pattern="[0-9]{6}" 
                           placeholder="000000" required
                           title="Please enter the 6-digit code">
                </div>

                <!-- Submit Button -->
                <button type="submit" class="btn btn-verify w-100 text-white mb-3">
                    Verify & Complete Registration
                </button>

                <!-- Resend OTP -->
                <div class="text-center">
                    <p class="text-muted mb-2">Didn't receive the code?</p>
                    <a href="javascript:void(0);" onclick="resendOTP()" class="text-decoration-none">
                        Resend Code
                    </a>
                </div>

                <!-- Back to Register -->
                <div class="text-center mt-3">
                    <a href="register.jsp" class="text-decoration-none text-muted">
                        <i class="fas fa-arrow-left"></i> Back to Registration
                    </a>
                </div>
            </form>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/js/all.min.js"></script>
        
        <script>
            // Auto-focus on OTP input
            document.getElementById('otp').focus();
            
            // Auto-submit when 6 digits are entered
            document.getElementById('otp').addEventListener('input', function(e) {
                if (this.value.length === 6 && /^[0-9]{6}$/.test(this.value)) {
                    // Auto-submit after a brief delay
                    setTimeout(() => {
                        this.form.submit();
                    }, 500);
                }
            });
            
            // Only allow numbers
            document.getElementById('otp').addEventListener('keypress', function(e) {
                if (!/[0-9]/.test(e.key) && !['Backspace', 'Delete', 'Tab', 'Escape', 'Enter'].includes(e.key)) {
                    e.preventDefault();
                }
            });
            
            function resendOTP() {
                // Redirect back to registration with same data to trigger new OTP
                alert('Please go back to registration form and submit again to receive a new OTP.');
                window.location.href = 'register.jsp';
            }
            
            // Countdown timer (optional)
            let countdown = 300; // 5 minutes
            function updateCountdown() {
                const minutes = Math.floor(countdown / 60);
                const seconds = countdown % 60;
                document.title = `Verify OTP (${minutes}:${seconds.toString().padStart(2, '0')})`;
                
                if (countdown > 0) {
                    countdown--;
                    setTimeout(updateCountdown, 1000);
                } else {
                    document.title = 'Verify OTP - Expired';
                    document.querySelector('.card').innerHTML = 
                        '<div class="text-center">' +
                        '<h4 class="text-danger">OTP Expired</h4>' +
                        '<p>Your verification code has expired.</p>' +
                        '<a href="register.jsp" class="btn btn-primary">Register Again</a>' +
                        '</div>';
                }
            }
            updateCountdown();
        </script>
    </body>
</html>
