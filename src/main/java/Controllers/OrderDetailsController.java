package Controllers;

import Models.User;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.Voucher;
import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.VoucherDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderDetailsController", urlPatterns = {"/order-details", "/OrderDetailsController"})
public class OrderDetailsController extends HttpServlet {

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

        // If not authenticated, redirect to login
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order-history");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);

            // Initialize DAOs
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();
            VoucherDAO voucherDAO = new VoucherDAO();

            // Get order information
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn hàng");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Security check: ensure user can only view their own orders (or admin)
            if (!"Admin".equals(loginUser.getRole())
                    && !loginUser.getId().equals(order.getCusId())) {
                response.sendRedirect(request.getContextPath() + "/order-history");
                return;
            }

            // Get order details with product information
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailByOrderId(orderId);
            List<OrderDetailWithProduct> orderDetailsWithProducts = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                try {
                    Product product = productDAO.getById(detail.getProId());
                    OrderDetailWithProduct detailWithProduct = new OrderDetailWithProduct();
                    detailWithProduct.setOrderDetail(detail);
                    detailWithProduct.setProduct(product);

                    // Calculate subtotal for this item
                    double subtotal = detail.getQuantity() * detail.getUnitPrice();
                    detailWithProduct.setSubtotal(subtotal);

                    orderDetailsWithProducts.add(detailWithProduct);
                } catch (Exception e) {
                    log("Error loading product " + detail.getProId() + ": " + e.getMessage());
                    // Continue with other products even if one fails
                }
            }

            // Get voucher information if exists
            Voucher voucher = null;
            if (order.getVoucherId() != null) {
                try {
                    voucher = voucherDAO.getById(order.getVoucherId());
                } catch (Exception e) {
                    log("Error loading voucher: " + e.getMessage());
                }
            }

            // Calculate totals
            double subtotalAmount = orderDetailsWithProducts.stream()
                    .mapToDouble(OrderDetailWithProduct::getSubtotal)
                    .sum();

            double discountAmount = order.getDiscountAmount() != null
                    ? order.getDiscountAmount().doubleValue() : 0.0;

            double shippingFee = 30000.0; // hoặc lấy từ order nếu đã lưu

            double finalAmount = subtotalAmount - discountAmount + shippingFee;

            // Set attributes for JSP
            request.setAttribute("order", order);
            request.setAttribute("orderDetailsWithProducts", orderDetailsWithProducts);
            request.setAttribute("voucher", voucher);
            request.setAttribute("subtotalAmount", subtotalAmount);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("finalAmount", finalAmount);

            // Forward to JSP
            request.getRequestDispatcher("order-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/order-history?error=invalid_order_id");
        } catch (Exception e) {
            log("Error at OrderDetailsController: " + e.toString());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin đơn hàng");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Inner class to hold order detail with product information
    public static class OrderDetailWithProduct {

        private OrderDetail orderDetail;
        private Product product;
        private double subtotal;

        // Getters and setters
        public OrderDetail getOrderDetail() {
            return orderDetail;
        }

        public void setOrderDetail(OrderDetail orderDetail) {
            this.orderDetail = orderDetail;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }
}
