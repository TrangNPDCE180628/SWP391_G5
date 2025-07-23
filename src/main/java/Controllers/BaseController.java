package Controllers;

import Models.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    
    /**
     * Check if user is authenticated
     * @param request HttpServletRequest
     * @return User object if authenticated, null otherwise
     */
    protected User getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("LOGIN_USER");
        }
        return null;
    }
    
    /**
     * Require authentication. If not authenticated, redirect to login page
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return User object if authenticated, null if redirected to login
     * @throws IOException if redirect fails
     */
    protected User requireAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        User user = getAuthenticatedUser(request);
        if (user == null) {
            // Store current URL for redirect after login
            String originalURL = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                originalURL += "?" + queryString;
            }
            
            HttpSession session = request.getSession(true);
            session.setAttribute("REDIRECT_URL", originalURL);
            
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return null;
        }
        return user;
    }
    
    /**
     * Require admin authentication
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return User object if authenticated as admin/staff, null if access denied
     * @throws IOException if redirect fails
     */
    protected User requireAdminAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        User user = requireAuthentication(request, response);
        if (user == null) {
            return null; // Already redirected to login
        }
        
        String role = user.getRole();
        if (!"Admin".equals(role) && !"Staff".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/HomeController?error=access_denied");
            return null;
        }
        
        return user;
    }
    
    /**
     * Require customer authentication
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return User object if authenticated as customer, null if access denied
     * @throws IOException if redirect fails
     */
    protected User requireCustomerAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        User user = requireAuthentication(request, response);
        if (user == null) {
            return null; // Already redirected to login
        }
        
        if (!"Customer".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/AdminController");
            return null;
        }
        
        return user;
    }
    
    /**
     * Send JSON error response
     * @param response HttpServletResponse
     * @param message Error message
     * @param statusCode HTTP status code
     * @throws IOException if writing response fails
     */
    protected void sendJsonError(HttpServletResponse response, String message, int statusCode) 
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
    
    /**
     * Check if request is AJAX
     * @param request HttpServletRequest
     * @return true if AJAX request, false otherwise
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
