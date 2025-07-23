//package Controllers;
//
//import Models.User;
//import Models.Order;
//import DAOs.OrderDAO;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(name = "OrderHistoryController", urlPatterns = {"/order-history", "/OrderHistoryController"})
//public class OrderHistoryController extends HttpServlet {
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        
//        response.setContentType("text/html;charset=UTF-8");
//        request.setCharacterEncoding("UTF-8");
//        
//        // Check authentication
//        HttpSession session = request.getSession(false);
//        User loginUser = null;
//        if (session != null) {
//            loginUser = (User) session.getAttribute("LOGIN_USER");
//        }
//        
//        // If not authenticated, store current URL and redirect to login
//        if (loginUser == null) {
//            String originalURL = request.getRequestURI();
//            String queryString = request.getQueryString();
//            if (queryString != null) {
//                originalURL += "?" + queryString;
//            }
//            
//            session = request.getSession(true);
//            session.setAttribute("REDIRECT_URL", originalURL);
//            
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }
//        
//        // Only customers can access order history
//        if (!"Customer".equals(loginUser.getRole())) {
//            response.sendRedirect(request.getContextPath() + "/AdminController");
//            return;
//        }
//        
//        try {
//            // Load customer's orders
//            OrderDAO orderDAO = new OrderDAO();
//            List<Order> orders = orderDAO.getOrdersByCustomerId(loginUser.getId());
//            
//            request.setAttribute("orders", orders);
//            request.getRequestDispatcher("order-history.jsp").forward(request, response);
//            
//        } catch (Exception e) {
//            log("Error at OrderHistoryController: " + e.toString());
//            response.sendRedirect(request.getContextPath() + "/HomeController?error=system_error");
//        }
//    }
//    
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doGet(request, response);
//    }
//}
