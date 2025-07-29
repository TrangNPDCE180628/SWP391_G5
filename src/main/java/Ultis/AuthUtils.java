package Ultis;

import Models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthUtils {
    
    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("LOGIN_USER") != null;
    }
    
    /**
     * Get current authenticated user
     */
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("LOGIN_USER");
        }
        return null;
    }
    
    /**
     * Check if user has admin role
     */
    public static boolean isAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && ("Admin".equals(user.getRole()) || "Staff".equals(user.getRole()));
    }
    
    /**
     * Check if user has customer role
     */
    public static boolean isCustomer(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && "Customer".equals(user.getRole());
    }
    
    /**
     * Redirect to login page with return URL
     */
    public static void redirectToLogin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Store current URL for redirect after login
        String originalURL = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            originalURL += "?" + queryString;
        }
        
        HttpSession session = request.getSession(true);
        session.setAttribute("REDIRECT_URL", originalURL);
        
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    
    /**
     * Redirect to appropriate dashboard based on user role
     */
    public static void redirectToDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        User user = getCurrentUser(request);
        if (user != null) {
            String role = user.getRole();
            String contextPath = request.getContextPath();
            
            switch (role) {
                case "Admin":
                case "Staff":
                    response.sendRedirect(contextPath + "/AdminController");
                    break;
                case "Customer":
                    response.sendRedirect(contextPath + "/HomeController");
                    break;
                default:
                    response.sendRedirect(contextPath + "/login.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
    
    /**
     * Check access permission for a given URL
     */
    public static boolean hasAccess(HttpServletRequest request, String requiredRole) {
        User user = getCurrentUser(request);
        if (user == null) {
            return false;
        }
        
        String userRole = user.getRole();
        
        // Admin and Staff can access admin resources
        if ("Admin".equals(requiredRole) || "Staff".equals(requiredRole)) {
            return "Admin".equals(userRole) || "Staff".equals(userRole);
        }
        
        // Customer can access customer resources
        if ("Customer".equals(requiredRole)) {
            return "Customer".equals(userRole);
        }
        
        // Default: user role must match required role
        return requiredRole.equals(userRole);
    }
}
