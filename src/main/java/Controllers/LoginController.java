package Controllers;

import DAOs.ViewUserDAO;
import DAOs.AdminDAO;
import DAOs.CustomerDAO;
import DAOs.StaffDAO;
import Models.Admin;
import Models.Staff;
import Models.Customer;

import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String AD = "Admin";
    private static final String ST = "Staff";
    private static final String ADMIN_PAGE = "AdminController";
    private static final String CS = "Customer";
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

            ViewUserDAO viewDao = new ViewUserDAO();
            User loginUser = viewDao.getUserByUsername(username);

            if (loginUser != null) {
                // Check password
                if (password.equals(loginUser.getPassword())) {
                    System.out.println("Password correct - Role: " + loginUser.getRole());

                    HttpSession session = request.getSession();
                    session.setAttribute("LOGIN_USER", loginUser);
                    session.setAttribute("cusId", loginUser.getId()); // hoặc getCusId() nếu tên khác

                    // Check if there's a redirect URL stored
                    String redirectURL = (String) session.getAttribute("REDIRECT_URL");
                    System.out.println("Redirect URL from session: " + redirectURL);

                    if (redirectURL != null && !redirectURL.isEmpty()) {
                        // Remove the redirect URL from session
                        session.removeAttribute("REDIRECT_URL");
                        System.out.println("Redirecting to original URL: " + redirectURL);
                        // Redirect to original URL (full URL) - FIX: don't use sendRedirect in finally block
                        response.sendRedirect(redirectURL);
                        return; // Important: return here to avoid further processing
                    } else {
                        // Role-based redirection
                        String role = loginUser.getRole();
                        if (AD.equals(role) || ST.equals(role)) {
                            response.sendRedirect(ADMIN_PAGE);
                            return;
                        } else if (CS.equals(role)) {
                            response.sendRedirect(CUSTOMER_PAGE);
                            return;
                        } else {
                            request.setAttribute("ERROR", "Your role is not supported!");
                            url = ERROR;
                        }
                    }
                } else {
                    System.out.println("Password incorrect");
                    request.setAttribute("ERROR", "Incorrect username or password");
                }
            } else {
                System.out.println("User not found");
                request.setAttribute("ERROR", "Incorrect username or password");
            }
        } catch (Exception e) {
            log("Error at LoginController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred during login. Please try again.");
            url = ERROR;
        } finally {
            // Only redirect if we haven't already redirected and url is set
            if (!response.isCommitted() && url != null && !url.isEmpty()) {
                try {
                    response.sendRedirect(url);
                } catch (Exception e) {
                    log("Error redirecting: " + e.toString());
                }
            }
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
