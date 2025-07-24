package Ultis;

import Models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutHelper {
    
    /**
     * Perform logout and clear all session data
     */
    public static void performLogout(HttpServletRequest request, HttpServletResponse response, String redirectUrl) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Log the logout action
            User loginUser = (User) session.getAttribute("LOGIN_USER");
            if (loginUser != null) {
                System.out.println("User logout: " + loginUser.getUsername() + " (Role: " + loginUser.getRole() + ")");
            }
            
            // Clear specific session attributes
            session.removeAttribute("LOGIN_USER");
            session.removeAttribute("cusId");
            session.removeAttribute("REDIRECT_URL");
            
            // Clear cart data if exists
            session.removeAttribute("cart");
            session.removeAttribute("cartCount");
            
            // Invalidate the entire session
            session.invalidate();
        }
        
        // Redirect to specified URL or default
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?logout=success");
        }
    }
    
    /**
     * Quick logout - redirect to login page
     */
    public static void quickLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        performLogout(request, response, null);
    }
    
    /**
     * Logout and redirect to home page
     */
    public static void logoutToHome(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        performLogout(request, response, request.getContextPath() + "/home.jsp");
    }
    
    /**
     * Check if user is logged in and get user info
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("LOGIN_USER");
        }
        return null;
    }
    
    /**
     * Generate logout URL with optional redirect
     */
    public static String getLogoutUrl(HttpServletRequest request, String redirectAfterLogout) {
        String logoutUrl = request.getContextPath() + "/LogoutController";
        if (redirectAfterLogout != null && !redirectAfterLogout.isEmpty()) {
            logoutUrl += "?redirect=" + redirectAfterLogout;
        }
        return logoutUrl;
    }
}
