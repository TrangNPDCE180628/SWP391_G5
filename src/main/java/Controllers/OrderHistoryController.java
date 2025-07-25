package Controllers;

import Models.User;
import Models.Order;
import DAOs.OrderDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderHistoryController", urlPatterns = {"/order-history", "/OrderHistoryController"})
public class OrderHistoryController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // Check authentication
        HttpSession session = request.getSession(false);
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute("LOGIN_USER");
        }
        
        // If not authenticated, store current URL and redirect to login
        if (loginUser == null) {
            String originalURL = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                originalURL += "?" + queryString;
            }
            
            session = request.getSession(true);
            session.setAttribute("REDIRECT_URL", originalURL);
            
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Only customers can access order history
        if (!"Customer".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/AdminController");
            return;
        }
        
        try {
            OrderDAO orderDAO = new OrderDAO();
            String customerId = loginUser.getId();
            
            // Get tab parameter to determine which orders to load
            String tab = request.getParameter("tab");
            if (tab == null || tab.isEmpty()) {
                tab = "all";
            }
            
            List<Order> orders;
            
            switch (tab.toLowerCase()) {
                case "pending":
                    orders = orderDAO.getOrdersByCustomerIdAndStatus(customerId, "pending");
                    break;
                    
                case "processing":
                    orders = orderDAO.getOrdersByCustomerIdAndStatus(customerId, "processing");
                    break;
                    
                case "shipped":
                    orders = orderDAO.getOrdersByCustomerIdAndStatus(customerId, "shipped");
                    break;
                    
                case "completed":
                    orders = orderDAO.getOrdersByCustomerIdAndStatus(customerId, "completed");
                    break;
                    
                case "cancelled":
                    orders = orderDAO.getOrdersByCustomerIdAndStatus(customerId, "cancel");
                    break;
                    
                
                    
                default: // "all"
                    orders = orderDAO.getOrdersByCustomerId(customerId);
                    break;
            }
            
            // Count orders by status for tab badges
            List<Order> allOrders = orderDAO.getOrdersByCustomerId(customerId);
            int allCount = allOrders.size();
            int pendingCount = (int) allOrders.stream().filter(o -> 
                "pending".equals(o.getOrderStatus())).count();
            int processingCount = (int) allOrders.stream().filter(o -> 
                "processing".equals(o.getOrderStatus())).count();
            int shippedCount = (int) allOrders.stream().filter(o -> 
                "shipped".equals(o.getOrderStatus())).count();
            int completedCount = (int) allOrders.stream().filter(o -> 
                "completed".equals(o.getOrderStatus())).count();
            int cancelledCount = (int) allOrders.stream().filter(o -> 
                "cancel".equals(o.getOrderStatus())).count();
            
            
            // Set attributes for JSP
            request.setAttribute("orders", orders);
            request.setAttribute("currentTab", tab);
            request.setAttribute("allCount", allCount);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("processingCount", processingCount);
            request.setAttribute("shippedCount", shippedCount);
            request.setAttribute("completedCount", completedCount);
            request.setAttribute("cancelledCount", cancelledCount);
            
            
            request.getRequestDispatcher("order-history.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error at OrderHistoryController: " + e.toString());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/HomeController?error=system_error");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
