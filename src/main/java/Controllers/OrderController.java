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

@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // Check authentication
        HttpSession session = request.getSession(false);
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute("LOGIN_USER");
        }
        
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }
        
        try {
            switch (action.toLowerCase()) {
                case "cancel":
                    cancelOrder(request, response, loginUser);
                    break;
                case "update-status":
                    updateOrderStatus(request, response, loginUser);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/order-history");
                    break;
            }
        } catch (Exception e) {
            log("Error in OrderController: " + e.toString());
            e.printStackTrace();
            
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/order-history");
        }
    }
    
    /**
     * Cancel order - only allowed for pending orders and by the order owner
     */
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "ID đơn hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/order-history");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = new OrderDAO();
            
            // Get order details to verify ownership and status
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                session.setAttribute("errorMessage", "Không tìm thấy đơn hàng #" + orderId);
                response.sendRedirect(request.getContextPath() + "/order-history");
                return;
            }
            
            // Check if user owns this order
            if (!order.getCusId().equals(loginUser.getId())) {
                session.setAttribute("errorMessage", "Bạn không có quyền hủy đơn hàng này.");
                response.sendRedirect(request.getContextPath() + "/order-history");
                return;
            }
            
            // Check if order can be cancelled (only pending orders)
            if (!"pending".equalsIgnoreCase(order.getOrderStatus())) {
                session.setAttribute("errorMessage", "Chỉ có thể hủy đơn hàng đang chờ thanh toán. Trạng thái hiện tại: " + order.getOrderStatus());
                response.sendRedirect(request.getContextPath() + "/order-history");
                return;
            }
            
            // Cancel the order
            boolean success = orderDAO.updateOrderStatus(String.valueOf(orderId), "cancel");
            
            if (success) {
                session.setAttribute("successMessage", "Đơn hàng #" + orderId + " đã được hủy thành công.");
                log("Order cancelled successfully: OrderID=" + orderId + ", UserID=" + loginUser.getId());
            } else {
                session.setAttribute("errorMessage", "Không thể hủy đơn hàng. Vui lòng thử lại sau.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID đơn hàng không hợp lệ.");
        } catch (Exception e) {
            log("Error cancelling order: " + e.getMessage());
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi hủy đơn hàng: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/order-history?tab=pending");
    }
    
    /**
     * Update order status - for admin/staff use or customer confirming receipt
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String orderIdStr = request.getParameter("orderId");
        String newStatus = request.getParameter("status");
        String redirectUrl = "/order-history"; // Default redirect URL
        
        if (orderIdStr == null || orderIdStr.trim().isEmpty() || 
            newStatus == null || newStatus.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Thông tin cập nhật không hợp lệ.");
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = new OrderDAO();
            
            // Get order details to verify ownership and current status
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                session.setAttribute("errorMessage", "Không tìm thấy đơn hàng #" + orderId);
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
            
            // Check permissions based on user role and action
            boolean isAuthorized = false;
            
            if ("Admin".equals(loginUser.getRole()) || "Staff".equals(loginUser.getRole())) {
                // Admin/Staff can update any order status
                isAuthorized = true;
                redirectUrl = "/AdminController?tab=orders";
            } else if (order.getCusId().equals(loginUser.getId())) {
                // Customer can only update their own order from "processing" to "completed"
                if ("processing".equalsIgnoreCase(order.getOrderStatus())) {
                    isAuthorized = true;
                    redirectUrl = "/order-history?tab=shipped";
                }
            }
            
            if (!isAuthorized) {
                session.setAttribute("errorMessage", "Bạn không có quyền thực hiện thao tác này.");
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
            
            // Validate status
            String[] validStatuses = {"pending", "processing", "shipped", "completed", "cancel"};
            boolean isValidStatus = false;
            for (String status : validStatuses) {
                if (status.equals(newStatus)) {
                    isValidStatus = true;
                    break;
                }
            }
            
            if (!isValidStatus) {
                session.setAttribute("errorMessage", "Trạng thái đơn hàng không hợp lệ: " + newStatus);
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(String.valueOf(orderId), newStatus);
            
            if (success) {
                if ("completed".equals(newStatus) && !"Admin".equals(loginUser.getRole()) && !"Staff".equals(loginUser.getRole())) {
                    session.setAttribute("successMessage", "Cảm ơn bạn đã xác nhận đã nhận hàng! Đơn hàng #" + orderId + " đã hoàn thành.");
                } else {
                    session.setAttribute("successMessage", "Cập nhật trạng thái đơn hàng #" + orderId + " thành công.");
                }
                log("Order status updated: OrderID=" + orderId + ", NewStatus=" + newStatus + ", UpdatedBy=" + loginUser.getId());
            } else {
                session.setAttribute("errorMessage", "Không thể cập nhật trạng thái đơn hàng.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID đơn hàng không hợp lệ.");
        } catch (Exception e) {
            log("Error updating order status: " + e.getMessage());
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật trạng thái: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + redirectUrl);
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
        return "Order Controller for handling order operations";
    }
}
