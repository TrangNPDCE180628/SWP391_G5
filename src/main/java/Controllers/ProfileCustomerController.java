package Controllers;

import DAOs.CustomerDAO;
import Models.Customer;
import Models.User;
import Ultis.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
@WebServlet(name = "ProfileCustomerController", urlPatterns = {"/ProfileCustomerController"})
public class ProfileCustomerController extends HttpServlet {

    private static final String PROFILE_PAGE = "profileCustomer.jsp";
    private static final String LOGIN_PAGE = "login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        // Load lại thông tin từ DB (đảm bảo dữ liệu mới nhất)
        Object loginUserObj = session.getAttribute("LOGIN_USER");
        String userId = null;
        
        // Handle both User and Customer objects in session
        if (loginUserObj instanceof User) {
            userId = ((User) loginUserObj).getId();
        } else if (loginUserObj instanceof Customer) {
            userId = ((Customer) loginUserObj).getCusId();
        } else {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }
        
        System.out.println("LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
        System.out.println("User ID: " + userId);

        try {
            CustomerDAO dao = new CustomerDAO();
            Customer freshCustomer = dao.getCustomerById(userId);
            request.setAttribute("customer", freshCustomer);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading customer information");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        String action = request.getParameter("action");
        Object loginUserObj = session.getAttribute("LOGIN_USER");
        String userId = null;
        
        // Handle both User and Customer objects in session
        if (loginUserObj instanceof User) {
            userId = ((User) loginUserObj).getId();
        } else if (loginUserObj instanceof Customer) {
            userId = ((Customer) loginUserObj).getCusId();
        } else {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }
        
        CustomerDAO dao = new CustomerDAO();
        Customer customer = null;
        
        try {
            customer = dao.getCustomerById(userId);
            if (customer == null) {
                request.setAttribute("error", "Customer not found.");
                response.sendRedirect(LOGIN_PAGE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading customer data.");
            response.sendRedirect(LOGIN_PAGE);
            return;
        }
        
        try {
            System.out.println("Before processing - Session LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
            
            if ("changePassword".equals(action)) {
                // Handle password change
                String oldPassword = request.getParameter("oldPasswordHidden");
                String newPassword = request.getParameter("newPasswordHidden");
                
                if (oldPassword == null || newPassword == null || 
                    oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
                    request.setAttribute("error", "Please provide both old and new passwords.");
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                    return;
                }
                
                // Verify old password using MD5 hash
                if (!MD5Util.verifyPassword(oldPassword, customer.getCusPassword())) {
                    request.setAttribute("error", "Old password is incorrect. Please try again.");
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                    return;
                }
                
                // Hash new password before storing
                customer.setCusPassword(MD5Util.hashPassword(newPassword));
                dao.updateCustomer(customer);
                
                // Update session - handle both User and Customer objects
                if (loginUserObj instanceof User) {
                    User sessionUser = (User) loginUserObj;
                    sessionUser.setPassword(MD5Util.hashPassword(newPassword));
                    // Keep the User object with updated password
                    session.setAttribute("LOGIN_USER", sessionUser);
                } else if (loginUserObj instanceof Customer) {
                    // If session contains Customer, update password but keep other fields
                    Customer sessionCustomer = (Customer) loginUserObj;
                    sessionCustomer.setCusPassword(MD5Util.hashPassword(newPassword));
                    session.setAttribute("LOGIN_USER", sessionCustomer);
                }
                
                System.out.println("After password update - Session LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
                
                request.setAttribute("customer", customer);
                request.setAttribute("message", "Password changed successfully!");
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                
            } else if ("updateImage".equals(action)) {
                // Handle image upload only
                String imagePath = request.getParameter("existingImage"); // Default to existing image
                
                try {
                    Part imagePart = request.getPart("imageFile");
                    if (imagePart != null && imagePart.getSize() > 0 && 
                        imagePart.getSubmittedFileName() != null && 
                        !imagePart.getSubmittedFileName().trim().isEmpty()) {
                        
                        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                        String uploadPath = getServletContext().getRealPath("/images/uploads");
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();
                        imagePart.write(uploadPath + "/" + fileName);
                        imagePath = "images/uploads/" + fileName;
                        
                        // Update only image in database
                        customer.setCusImage(imagePath);
                        dao.updateCustomer(customer);
                        
                        // Update session
                        if (loginUserObj instanceof Customer) {
                            Customer sessionCustomer = (Customer) loginUserObj;
                            sessionCustomer.setCusImage(imagePath);
                            session.setAttribute("LOGIN_USER", sessionCustomer);
                        }
                        
                        request.setAttribute("message", "Profile picture updated successfully!");
                    } else {
                        request.setAttribute("error", "Please select an image file.");
                    }
                } catch (Exception fileEx) {
                    System.out.println("File upload failed: " + fileEx.getMessage());
                    request.setAttribute("error", "Failed to upload image. Please try again.");
                }
                
                request.setAttribute("customer", customer);
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                
            } else {
                // Handle profile update (without image)
                String imagePath = request.getParameter("existingImage"); // Keep existing image
                String fullName = request.getParameter("cusFullName");
                String gender = request.getParameter("cusGender");
                String email = request.getParameter("cusGmail");
                String phone = request.getParameter("cusPhone");
                
                // Validation: Check if required fields are not null or empty
                if (fullName == null || fullName.trim().isEmpty()) {
                    request.setAttribute("error", "Full name is required.");
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                    return;
                }
                
                if (email == null || email.trim().isEmpty()) {
                    request.setAttribute("error", "Email is required.");
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                    return;
                }
                
                // Trim values to remove whitespace
                fullName = fullName.trim();
                gender = (gender != null) ? gender.trim() : "Male"; // Default gender
                email = email.trim();
                phone = (phone != null) ? phone.trim() : "";
                
                System.out.println("=== Profile Update Parameters ===");
                System.out.println("cusFullName: " + fullName);
                System.out.println("cusGender: " + gender);
                System.out.println("cusGmail: " + email);
                System.out.println("cusPhone: " + phone);
                System.out.println("existingImage: " + imagePath);
                System.out.println("================================");
                
                // Set values to customer object with null checks
                if (fullName != null && !fullName.isEmpty()) {
                    customer.setCusFullName(fullName);
                }
                if (gender != null && !gender.isEmpty()) {
                    customer.setCusGender(gender);
                }
                if (imagePath != null && !imagePath.isEmpty()) {
                    customer.setCusImage(imagePath);
                }
                if (email != null && !email.isEmpty()) {
                    customer.setCusGmail(email);
                }
                if (phone != null && !phone.isEmpty()) {
                    customer.setCusPhone(phone);
                }
                
                System.out.println("=== Customer object before update ===");
                System.out.println("Customer ID: " + customer.getCusId());
                System.out.println("Username: " + customer.getUsername());
                System.out.println("Full Name: " + customer.getCusFullName());
                System.out.println("Gender: " + customer.getCusGender());
                System.out.println("Email: " + customer.getCusGmail());
                System.out.println("Phone: " + customer.getCusPhone());
                System.out.println("Image: " + customer.getCusImage());
                System.out.println("====================================");
                
                dao.updateCustomer(customer);
                
                // Update session - handle both User and Customer objects properly
                if (loginUserObj instanceof User) {
                    User sessionUser = (User) loginUserObj;
                    // Update only the relevant fields in User object
                    sessionUser.setFullName(fullName);
                    // Keep other User properties intact
                    session.setAttribute("LOGIN_USER", sessionUser);
                } else if (loginUserObj instanceof Customer) {
                    // If session contains Customer, update it with fresh data
                    Customer sessionCustomer = (Customer) loginUserObj;
                    sessionCustomer.setCusFullName(fullName);
                    sessionCustomer.setCusGender(gender);
                    sessionCustomer.setCusImage(imagePath);
                    sessionCustomer.setCusGmail(email);
                    sessionCustomer.setCusPhone(phone);
                    session.setAttribute("LOGIN_USER", sessionCustomer);
                }
                
                System.out.println("After profile update - Session LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
                
                request.setAttribute("customer", customer);
                request.setAttribute("message", "Profile updated successfully!");
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred, session LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
            
            try {
                // Reload customer data for display
                Customer errorCustomer = dao.getCustomerById(userId);
                request.setAttribute("customer", errorCustomer);
            } catch (Exception ex) {
                ex.printStackTrace();
                // If we can't even load customer data, redirect to login
                response.sendRedirect(LOGIN_PAGE);
                return;
            }
            request.setAttribute("error", "An error occurred while updating. Please try again.");
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        }
    }
}
