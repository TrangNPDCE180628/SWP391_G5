package Controllers;

import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.UserDAO;
import Models.Product;
import Models.ProductTypes;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name="LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String AD = "admin";
    private static final String ADMIN_PAGE = "AdminController";
    private static final String CS = "customer";
    private static final String CUSTOMER_PAGE = "HomeController";
    private static final int PRODUCTS_PER_PAGE = 8;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println("Login attempt - Username: " + username);

            // Authenticate user
            UserDAO dao = new UserDAO();
            User loginUser = dao.login(username, password);

            if (loginUser != null) {
                System.out.println("User found - Role: " + loginUser.getRole());
                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", loginUser);

                // Determine the URL based on the role
                String role = loginUser.getRole();
                if (AD.equals(role)) {
                    url = ADMIN_PAGE;
                    System.out.println("Redirecting to admin page");
                } else if (CS.equals(role)) {
                    url = CUSTOMER_PAGE;
                    System.out.println("Redirecting to customer page");
                } else {
                    System.out.println("Unsupported role: " + role);
                    request.setAttribute("ERROR", "Your role is not supported!");
                    url = ERROR;
                }


            } else {
                System.out.println("Login failed - User not found or incorrect password");
                request.setAttribute("ERROR", "Incorrect username or password");
            }
        } catch (Exception e) {
            log("Error at LoginController: " + e.toString());
            System.out.println("Login error: " + e.getMessage());
            request.setAttribute("ERROR", "An error occurred during login. Please try again.");
        } finally {
            // Use sendRedirect for client-side redirection
            response.sendRedirect(url);
        }
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

    @Override
    public String getServletInfo() {
        return "Login Controller";
    }
}
