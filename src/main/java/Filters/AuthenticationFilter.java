package Filters;

import Models.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*") // Apply to all URLs
public class AuthenticationFilter implements Filter {
    
    // URLs that require admin authentication
    private static final List<String> ADMIN_URLS = Arrays.asList(
        "/AdminController", 
        "/admin.jsp",
        "/productManager.jsp",
        "/voucher-manager.jsp",
        "/Revenue.jsp",
        "/Stock.jsp"
        
    );
    
    // URLs that require user authentication (admin, staff, or customer)
    private static final List<String> PROTECTED_URLS = Arrays.asList(
        "/orders.jsp",
        "/orderDetails.jsp",
        "/profileCustomer.jsp",
        "/order-history.jsp",
        "/cart.jsp"
    );
    
    // URLs that don't require authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/login.jsp",
        "/register.jsp",
        "/home.jsp",
        "/product-list.jsp",
        "/product-detail.jsp",
        "/product-detail",
        "/forgot-password.jsp",
        "/reset-password.jsp",
        "/LoginController",
        "/RegisterController",
        "/HomeController",
        "/ProductListServlet",
        "/ProductDetailServlet",
        "/ForgotPasswordServlet",
        "/css/",
        "/js/",
        "/images/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Get current user from session
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute("LOGIN_USER");
        }
        
        // Check if it's a public resource (CSS, JS, Images)
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if it's a public URL
        if (isPublicURL(path)) {
            // If user is already logged in and trying to access login page, redirect to appropriate dashboard
            if (currentUser != null && (path.equals("/login.jsp") || path.equals("/LoginController"))) {
                redirectToDashboard(currentUser, httpResponse, contextPath);
                return;
            }
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is authenticated
        if (currentUser == null) {
            // Store the original URL for redirect after login
            String originalURL = requestURI;
            String queryString = httpRequest.getQueryString();
            if (queryString != null) {
                originalURL += "?" + queryString;
            }
            session = httpRequest.getSession(true); // Create session if doesn't exist
            session.setAttribute("REDIRECT_URL", originalURL);
            
            // Redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }
        
        // Check admin access
        if (isAdminURL(path)) {
            if (!"Admin".equals(currentUser.getRole()) && !"Staff".equals(currentUser.getRole())) {
                // User doesn't have admin/staff privileges
                httpResponse.sendRedirect(contextPath + "/HomeController?error=access_denied");
                return;
            }
        }
        
        // Check if customer is trying to access admin pages
        if ("Customer".equals(currentUser.getRole()) && isAdminURL(path)) {
            httpResponse.sendRedirect(contextPath + "/HomeController?error=access_denied");
            return;
        }
        
        // User is authenticated and has proper permissions, continue with request
        chain.doFilter(request, response);
    }
    
    private boolean isPublicResource(String path) {
        return path.startsWith("/css/") || 
               path.startsWith("/js/") || 
               path.startsWith("/images/") ||
               path.startsWith("/META-INF/") ||
               path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico");
    }
    
    private boolean isPublicURL(String path) {
        return PUBLIC_URLS.stream().anyMatch(publicUrl -> 
            path.equals(publicUrl) || path.startsWith(publicUrl)
        );
    }
    
    private boolean isAdminURL(String path) {
        return ADMIN_URLS.stream().anyMatch(adminUrl -> 
            path.equals(adminUrl) || path.startsWith(adminUrl)
        );
    }
    
    private boolean isProtectedURL(String path) {
        return PROTECTED_URLS.stream().anyMatch(protectedUrl -> 
            path.equals(protectedUrl) || path.startsWith(protectedUrl)
        );
    }
    
    private void redirectToDashboard(User user, HttpServletResponse response, String contextPath) 
            throws IOException {
        String role = user.getRole();
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
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
