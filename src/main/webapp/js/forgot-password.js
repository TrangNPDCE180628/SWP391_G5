let countdownInterval;
const otpDuration = 300; // 300 giây = 5 phút

function startCountdown(duration) {
    let timeLeft = duration;

    $("#countdownSection").show();
    $("#resendOtpBtn").prop("disabled", true);

    updateTimerDisplay(timeLeft);

    countdownInterval = setInterval(() => {
        timeLeft--;
        updateTimerDisplay(timeLeft);

        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            $("#resendOtpBtn").prop("disabled", false);
            $("#countdownTimer").text("Expired");
        }
    }, 1000);
}

function updateTimerDisplay(seconds) {
    const min = Math.floor(seconds / 60);
    const sec = seconds % 60;
    $("#countdownTimer").text(`${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`);
}

function sendOtp(email) {
    $.ajax({
        type: "POST",
        url: "ForgotPasswordServlet",
        data: {
            action: "sendOtp",
            email: email
        },
        success: function () {
            $("#successMsg").text("OTP sent to your email.").show();
            $("#errorMsg").hide();

            $("#hiddenEmail").val(email);
            $("#otpSection").slideDown();

            startCountdown(otpDuration);
        },
        error: function (xhr) {
            const err = xhr.responseText || "Error sending OTP.";
            $("#errorMsg").text(err).show();
            $("#successMsg").hide();
        }
    });
}

$(document).ready(function () {
    // Gửi OTP lần đầu
    $("#otpForm").submit(function (e) {
        e.preventDefault();
        const email = $("#email").val();
        sendOtp(email);
    });

    // Gửi lại OTP khi countdown hết
    $("#resendOtpBtn").click(function () {
        const email = $("#hiddenEmail").val();
        sendOtp(email);
    });

    // Xác thực OTP
    $("#otpVerifyForm").submit(function (e) {
        e.preventDefault();

        const email = $("#hiddenEmail").val();
        const otp = $("#otpVerifyForm input[name='otp']").val();

        $.ajax({
            type: "POST",
            url: "ForgotPasswordServlet",
            data: {
                action: "verifyOtp",
                email: email,
                otp: otp
            },
            success: function (html) {
                $("body").html(html); // replace toàn bộ nội dung
            },
            error: function (xhr) {
                const err = xhr.responseText || "Invalid OTP.";
                $("#errorMsg").text(err).show();
                $("#successMsg").hide();
            }
        });
    });
});
