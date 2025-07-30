package Controllers;

import DAOs.CustomerDAO;
import DAOs.PasswordResetDAO;
import Ultis.MD5Util;
import Ultis.EmailUtility;

import Models.Customer;
import Models.PasswordReset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {

    private static final String ERROR = "register.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        
        if ("verifyOtp".equals(action)) {
            handleOtpVerification(request, response);
            return;
        }

        try {
            // Lấy dữ liệu từ form
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            // Validate input với điều kiện chi tiết hơn
            String validationError = validateRegistrationInput(username, password, fullname, gender, gmail, phone, address);
            if (validationError != null) {
                request.setAttribute("ERROR", validationError);
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            CustomerDAO dao = new CustomerDAO();

            // Check if username already exists
            if (dao.checkUsernameExists(username)) {
                request.setAttribute("ERROR", "Username already exists!");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            // Check if email already exists
            if (isEmailAlreadyExists(gmail)) {
                request.setAttribute("ERROR", "Email already exists!");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            // Generate OTP and send email - KHÔNG TẠO CUSTOMER Ở ĐÂY
            String otp = generateOTP();
            PasswordResetDAO otpDAO = new PasswordResetDAO();
            
            System.out.println("=== REGISTRATION OTP GENERATION ===");
            System.out.println("Generated OTP: " + otp);
            System.out.println("Email: " + gmail);
            
            // Clear any existing OTP for this email
            otpDAO.deleteOtp(gmail);
            
            // Insert new OTP - chỉ lưu OTP, chưa tạo customer
            PasswordReset passwordReset = new PasswordReset(gmail, otp, LocalDateTime.now().plusMinutes(10));
            otpDAO.insertOtp(passwordReset);

            // Send OTP email
            String subject = "Email Verification - Registration";
            String messageContent = "<h3>Email Verification</h3>" +
                    "<p>Your OTP for registration is: <strong>" + otp + "</strong></p>" +
                    "<p>This OTP will expire in 10 minutes.</p>" +
                    "<p>Please enter this OTP to complete your registration.</p>";
            
            EmailUtility.sendEmail(gmail, subject, messageContent);
            System.out.println("Email sent successfully to: " + gmail);

            // Store registration data in session - chờ verify OTP
            HttpSession session = request.getSession();
            session.setAttribute("regUsername", username);
            session.setAttribute("regPassword", password);
            session.setAttribute("regFullname", fullname);
            session.setAttribute("regGender", gender);
            session.setAttribute("regGmail", gmail);
            session.setAttribute("regPhone", phone);
            session.setAttribute("regAddress", address);
            
            System.out.println("Session data stored, waiting for OTP verification");
            System.out.println("=== END REGISTRATION DEBUG ===");

            // Forward to OTP verification page - CHƯA TẠO CUSTOMER
            request.setAttribute("SUCCESS", "OTP has been sent to your email. Please check and verify to complete registration.");
            request.setAttribute("email", gmail);
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);

        } catch (Exception e) {
            log("Error at RegisterController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
    }

    private void handleOtpVerification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String otpCode = request.getParameter("otp");

            System.out.println("=== OTP VERIFICATION REQUEST ===");
            System.out.println("Email from request: " + email);
            System.out.println("OTP from request: " + otpCode);
            
            if (email == null || otpCode == null || email.trim().isEmpty() || otpCode.trim().isEmpty()) {
                System.out.println("Email or OTP is null/empty");
                request.setAttribute("ERROR", "Email and OTP are required");
                request.setAttribute("email", email);
                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                return;
            }

            // Trim whitespace
            email = email.trim();
            otpCode = otpCode.trim();
            
            System.out.println("Trimmed Email: " + email);
            System.out.println("Trimmed OTP: " + otpCode);

            PasswordResetDAO otpDAO = new PasswordResetDAO();
            
            if (otpDAO.verifyOtp(email, otpCode)) {
                System.out.println("OTP verification successful! Now creating customer...");
                
                // OTP is valid, NOW create customer (chỉ tạo khi OTP đúng)
                HttpSession session = request.getSession();
                String username = (String) session.getAttribute("regUsername");
                String password = (String) session.getAttribute("regPassword");
                String fullname = (String) session.getAttribute("regFullname");
                String gender = (String) session.getAttribute("regGender");
                String gmail = (String) session.getAttribute("regGmail");
                String phone = (String) session.getAttribute("regPhone");
                String address = (String) session.getAttribute("regAddress");

                // Kiểm tra session data
                if (username == null || password == null || fullname == null || 
                    gender == null || gmail == null || phone == null || address == null) {
                    System.out.println("Session data missing! User needs to register again.");
                    request.setAttribute("ERROR", "Session expired. Please register again.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                System.out.println("Session data - Username: " + username + ", Gmail: " + gmail);

                // Validate again before creating customer (double check)
                String validationError = validateRegistrationInput(username, password, fullname, gender, gmail, phone, address);
                if (validationError != null) {
                    System.out.println("Validation failed during OTP verification: " + validationError);
                    request.setAttribute("ERROR", "Registration data invalid: " + validationError);
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                // Create customer with retry mechanism for duplicate ID
                CustomerDAO dao = new CustomerDAO();
                
                // Double check username và email trước khi tạo
                if (dao.checkUsernameExists(username)) {
                    System.out.println("Username already exists during OTP verification: " + username);
                    request.setAttribute("ERROR", "Username already exists! Please choose another username.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                if (dao.checkEmailExists(gmail)) {
                    System.out.println("Email already exists during OTP verification: " + gmail);
                    request.setAttribute("ERROR", "Email already exists! Please use another email.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }

                Customer user = new Customer();
                boolean customerInserted = false;
                int retryCount = 0;
                int maxRetries = 3;
                
                while (!customerInserted && retryCount < maxRetries) {
                    try {
                        String cusId = dao.generateNextCusId();
                        user.setCusId(cusId);
                        user.setUsername(username);
                        user.setCusPassword(MD5Util.hashPassword(password));
                        user.setCusFullName(fullname);
                        user.setCusGender(gender);
                        user.setCusGmail(gmail);
                        user.setCusPhone(phone);
                        user.setCusAddress(address);
                        user.setCusImage("default.png");

                        dao.insertCustomer(user);
                        customerInserted = true;
                        System.out.println("Customer created successfully with ID: " + cusId + " after OTP verification!");
                        
                    } catch (SQLException e) {
                        retryCount++;
                        System.out.println("Customer insert attempt " + retryCount + " failed: " + e.getMessage());
                        
                        if (e.getMessage().contains("PRIMARY KEY constraint") || 
                            e.getMessage().contains("duplicate key")) {
                            
                            if (retryCount >= maxRetries) {
                                throw new Exception("Unable to generate unique customer ID after " + maxRetries + " attempts");
                            }
                            // Continue to retry with new ID
                        } else {
                            // Other SQL errors, don't retry
                            throw e;
                        }
                    }
                }

                // Clear OTP and session data sau khi tạo customer thành công
                otpDAO.deleteOtp(email);
                session.removeAttribute("regUsername");
                session.removeAttribute("regPassword");
                session.removeAttribute("regFullname");
                session.removeAttribute("regGender");
                session.removeAttribute("regGmail");
                session.removeAttribute("regPhone");
                session.removeAttribute("regAddress");

                // Set success message
                session.setAttribute("SUCCESS", "Registration completed successfully! Please login with your new account.");
                response.sendRedirect("login.jsp");

            } else {
                System.out.println("OTP verification failed!");
                request.setAttribute("ERROR", "Invalid or expired OTP. Please try again.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("Exception in OTP verification: " + e.getMessage());
            e.printStackTrace();
            log("Error at OTP verification: " + e.toString());
            request.setAttribute("ERROR", "An error occurred during OTP verification.");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        }
    }

    private String validateRegistrationInput(String username, String password, String fullname, 
                                           String gender, String gmail, String phone, String address) {
        // Check if any field is null or empty
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (fullname == null || fullname.trim().isEmpty()) {
            return "Full name is required";
        }
        if (gender == null || gender.trim().isEmpty()) {
            return "Gender is required";
        }
        if (gmail == null || gmail.trim().isEmpty()) {
            return "Email is required";
        }
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone is required";
        }
        if (address == null || address.trim().isEmpty()) {
            return "Address is required";
        }

        // Validate username (only letters and numbers, 3-20 characters)
        if (!username.matches("^[a-zA-Z0-9]{3,20}$")) {
            return "Username must contain only letters and numbers, 3-20 characters";
        }

        // Validate password (minimum 6 characters, must contain letters and numbers)
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            return "Password must contain both letters and numbers";
        }

        // Validate full name (only letters and spaces, 2-50 characters)
        if (!fullname.matches("^[a-zA-Z\\s]{2,50}$")) {
            return "Full name must contain only letters and spaces, 2-50 characters";
        }

        // Validate gender
        if (!"male".equalsIgnoreCase(gender) && !"female".equalsIgnoreCase(gender)) {
            return "Gender must be either 'male' or 'female'";
        }

        // Validate email format
        if (!gmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return "Please enter a valid email address";
        }

        // Validate phone (10-12 digits)
        if (!phone.matches("^0[0-9]{9,11}$")) {
            return "Phone number must start with 0 and contain 10-12 digits";
        }

        // Validate address (2-100 characters)
        if (address.length() < 2 || address.length() > 100) {
            return "Address must be between 2 and 100 characters";
        }

        return null; // No validation errors
    }

    private boolean isEmailAlreadyExists(String email) {
        try {
            CustomerDAO dao = new CustomerDAO();
            return dao.checkEmailExists(email);
        } catch (Exception e) {
            log("Error checking email existence: " + e.toString());
            return false;
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(ERROR); // Redirect GET requests to register page
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Register Controller";
    }
}
