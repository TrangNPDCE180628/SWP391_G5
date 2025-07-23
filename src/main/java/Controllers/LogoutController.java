package Controllers;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutController", urlPatterns = {"/LogoutController"})
public class LogoutController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = SUCCESS;
        
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                // Get user info before logout for logging
                Models.User loginUser = (Models.User) session.getAttribute("LOGIN_USER");
                if (loginUser != null) {
                    System.out.println("User logout: " + loginUser.getUsername() + " (Role: " + loginUser.getRole() + ")");
                }
                
                // Remove specific session attributes
                session.removeAttribute("LOGIN_USER");
                session.removeAttribute("cusId");
                session.removeAttribute("REDIRECT_URL"); // Also clear any stored redirect URL
                
                // Optionally invalidate entire session
                session.invalidate();
                
                System.out.println("Session invalidated successfully");
            }
            
            // Check if there's a specific redirect URL requested
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // Validate redirect URL for security (prevent open redirect attacks)
                if (redirectUrl.startsWith("/") || redirectUrl.startsWith(request.getContextPath())) {
                    url = redirectUrl;
                } else {
                    // If external URL, redirect to home for security
                    url = request.getContextPath() + "/home.jsp";
                }
            } else {
                // Default redirect to login page with logout success message
                url = SUCCESS + "?logout=success";
            }
            
        } catch (Exception e) {
            log("Error at LogoutController: " + e.toString());
            e.printStackTrace();
        } finally {
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
}
