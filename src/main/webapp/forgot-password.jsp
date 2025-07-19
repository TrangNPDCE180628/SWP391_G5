<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Forgot Password</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5" style="max-width: 500px">
            <h3 class="mb-4 text-center">Forgot Password</h3>

            <!-- Form gửi email -->
            <form id="otpForm">
                <div class="mb-3">
                    <label for="email">Enter your email</label>
                    <input type="email" class="form-control" name="email" id="email" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Send OTP</button>
            </form>

            <div class="mt-3 text-danger" id="errorMsg" style="display:none;"></div>

            <!-- Form nhập OTP ẩn -->
            <div class="mt-4" id="otpSection" style="display:none;">
                <form id="otpVerifyForm">
                    <input type="hidden" name="action" value="verifyOtp">
                    <input type="hidden" name="email" id="hiddenEmail">
                    <div class="mb-3">
                        <label for="otp">Enter OTP</label>
                        <input type="text" class="form-control" name="otp" required>
                    </div>
                    <button type="submit" class="btn btn-success w-100">Verify OTP</button>
                </form>
                <div class="mt-3" id="countdownSection" style="display: none;">
                    <p class="text-primary">
                        OTP expires in <span id="countdownTimer">05:00</span>
                    </p>

                    <button id="resendOtpBtn" class="btn btn-outline-secondary btn-sm" disabled>
                        <i class="bi bi-arrow-repeat"></i> Resend OTP
                    </button>
                </div>

            </div>
        </div>

        <!-- Nhúng script -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/forgot-password.js"></script>
    </body>
</html>
