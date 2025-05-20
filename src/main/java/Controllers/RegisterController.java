package Controllers;

import DAOs.UserDAO;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {
    private static final String ERROR = "register.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            
            // Validate input
            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                fullname == null || fullname.trim().isEmpty() ||
                gender == null || gender.trim().isEmpty()) {
                    
                request.setAttribute("ERROR", "All fields are required");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }
            
            UserDAO dao = new UserDAO();
            
            // Check if username already exists
            if (dao.checkUsernameExists(username)) {
                request.setAttribute("ERROR", "Username already exists!");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }
            
            // Create new user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setGender(gender);
            user.setRole("customer");
            
            dao.create(user);
            
            // Set success message in session instead of request
            HttpSession session = request.getSession();
            session.setAttribute("SUCCESS", "Registration successful! Please login.");
            
            // Redirect instead of forward
            response.sendRedirect(SUCCESS);
            
        } catch (Exception e) {
            log("Error at RegisterController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
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