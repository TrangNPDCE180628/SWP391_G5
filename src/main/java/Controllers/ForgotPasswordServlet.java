/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CustomerDAO;
import DAOs.PasswordResetDAO;
import DAOs.StaffDAO;
import DAOs.ViewUserDAO;
import Models.PasswordReset;
import Models.User;
import Ultis.EmailUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thanh Duan
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPasswordServlet"})
public class ForgotPasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("sendOtp".equals(action)) {
            handleSendOtp(request, response);
        } else if ("verifyOtp".equals(action)) {
            handleVerifyOtp(request, response);
        } else if ("resetPassword".equals(action)) {
            handleResetPassword(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void handleSendOtp(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Email cannot be empty.");
            return;
        }

        String otp = generateOTP();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        String subject = "Your OTP Code to Reset Password";
        String content = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                + "<h2 style='color: #2c3e50;'>"
                + " <img src=\"https://cdn-icons-png.flaticon.com/512/3064/3064197.png\" width=\"24\" style=\"vertical-align: middle; margin-right: 8px;\">"
                + "Reset Your Password"
                + "</h2>"
                + "<p>Dear user,</p>"
                + "<p>You requested to reset your password. Please use the following OTP to proceed:</p>"
                + "<h1 style='color: #e74c3c; letter-spacing: 5px;'>" + otp + "</h1>"
                + "<p><strong>This OTP will expire in 5 minutes.</strong></p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<hr>"
                + "<p style='font-size: 12px; color: #888;'>This is an automated message. Please do not reply.</p>"
                + "</div>";

        try {
            EmailUtility.sendEmail(email, subject, content);

            PasswordReset pr = new PasswordReset(email, otp, expiry);
            PasswordResetDAO dao = new PasswordResetDAO();
            dao.insertOtp(pr);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to send OTP.");
        }
    }

    private void handleVerifyOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        if (email == null || otp == null || email.trim().isEmpty() || otp.trim().isEmpty()) {
            request.setAttribute("error", "Email or OTP cannot be empty.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        try {
            PasswordResetDAO dao = new PasswordResetDAO();
            boolean valid = dao.verifyOtp(email, otp);

            if (valid) {
                // Nếu OTP hợp lệ → trả về HTML của reset-password.jsp
                request.setAttribute("email", email);
                request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid or expired OTP.");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("System error: " + e.getMessage());
        }

    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");

        if (email == null || newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Password cannot be empty.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        boolean updated = false;

        try {
            // TODO: update password in your User table (not PasswordReset table)
            ViewUserDAO viewuser = new ViewUserDAO();
            User user = viewuser.getUserByEmail(email);

            if (user.getRole().equals("Customer")) {
                CustomerDAO cusDAO = new CustomerDAO();
                updated = cusDAO.updatePassword(email, newPassword);
            } else if (user.getRole().equals("Staff")) {
                StaffDAO staffDAO = new StaffDAO();
                updated = staffDAO.updatePassword(email, newPassword);
            }

            if (updated) {
                // Xoá OTP sau khi thành công (optional)
                new PasswordResetDAO().deleteOtp(email);

                request.setAttribute("message", "Password reset successfully. Please login.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to reset password.");
                request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "System error.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }

    private String generateOTP() {
        int otp = 100000 + new SecureRandom().nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
