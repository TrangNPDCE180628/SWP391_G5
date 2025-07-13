package Controllers;

import DAOs.*;
import Models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PaymentController", urlPatterns = {"/PaymentController"})
public class PaymentController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                response.sendRedirect("CartController?action=view");
                return;
            }

            switch (action) {
                case "create":
                    createOrder(request, response);
                    break;
                case "confirm":
                    confirmPayment(request, response);
                    break;
                default:
                    response.sendRedirect("CartController?action=view");
            }
        } catch (Exception ex) {
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + ex.getMessage());
            response.sendRedirect("CartController?action=view");
        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        HttpSession session = request.getSession(false);
        int orderId = -1;

        try {
            /* 1. Kiểm tra đăng nhập */
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            /* 2. Lấy sản phẩm & voucher gửi từ cart.jsp */
            String[] selectedIds = request.getParameterValues("selectedProductIds");
            String voucherCode = request.getParameter("voucherCode");

            if (selectedIds == null || selectedIds.length == 0) {
                session.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
                response.sendRedirect("CartController?action=view");
                return;
            }

            @SuppressWarnings("unchecked")
            Map<String, ViewCartCustomer> cart
                    = (Map<String, ViewCartCustomer>) session.getAttribute("cart");
            if (cart == null || cart.isEmpty()) {
                session.setAttribute("error", "Giỏ hàng trống.");
                response.sendRedirect("CartController?action=view");
                return;
            }

            /* 3. Kiểm tra tồn kho & tính tiền */
            StockDAO stockDAO = new StockDAO();
            List<OrderDetail> orderDetails = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (String proId : selectedIds) {
                ViewCartCustomer item = cart.get(proId);
                if (item == null) {
                    continue;                               // không tồn tại trong cart
                }
                if (stockDAO.getQuantity(proId) < item.getQuantity()) {
                    session.setAttribute("error",
                            "Sản phẩm " + item.getProName() + " không đủ hàng.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }

                BigDecimal unitPrice = BigDecimal.valueOf(item.getProPrice());
                BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(lineTotal);

                OrderDetail od = new OrderDetail();
                od.setProId(proId);
                od.setQuantity(item.getQuantity());
                od.setUnitPrice(unitPrice.doubleValue());
                orderDetails.add(od);
            }

            /* 4. Xử lý voucher */
            BigDecimal discount = BigDecimal.ZERO;
            Integer voucherId = null;
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                Voucher v = new VoucherDAO().getByCode(voucherCode);
                if (v == null || totalAmount.compareTo(v.getMinOrderAmount()) < 0) {
                    session.setAttribute("error", "Voucher không hợp lệ hoặc không đủ điều kiện.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }
                voucherId = v.getVoucherId();
                discount = "percentage".equalsIgnoreCase(v.getDiscountType())
                        ? totalAmount.multiply(v.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        : v.getDiscountValue();
            }

            /* 5. Tạo Order */
            Order order = new Order();
            order.setCusId(user.getId());
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(discount);
            order.setFinalAmount(totalAmount.subtract(discount));
            order.setOrderStatus("pending");
            order.setVoucherId(voucherId);

            OrderDAO orderDAO = new OrderDAO();
            orderId = orderDAO.createOrder(order);             // trả về khóa
            order.setOrderId(orderId);

            /* 6. Lưu OrderDetail */
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            for (OrderDetail d : orderDetails) {
                d.setOrderId(orderId);
                if (!detailDAO.createOrderDetail(d)) {
                    throw new SQLException("Không thể lưu chi tiết đơn hàng.");
                }
            }

            /* 7. Trừ kho */
            for (String proId : selectedIds) {
                ViewCartCustomer i = cart.get(proId);
                stockDAO.decreaseStockAfterOrder(proId, i.getQuantity());
                cart.remove(proId);                       // xóa item khỏi cart
            }
            session.setAttribute("cart", cart);
            session.setAttribute("cartSize",
                    cart.values().stream().mapToInt(ViewCartCustomer::getQuantity).sum());

            /* 8. Chuyển tới trang payment.jsp */
            request.setAttribute("order", order);
            request.getRequestDispatcher("payment.jsp").forward(request, response);

        } catch (Exception ex) {
            if (orderId > 0) {
                new OrderDAO().delete(orderId);  // rollback Order + cascading FK
            }
            ex.printStackTrace();
            session.setAttribute("error", "Lỗi xử lý đơn hàng: " + ex.getMessage());
            response.sendRedirect("CartController?action=view");
        }
    }

    private void confirmPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        try {
            /* 1. Check đăng nhập */
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            /* 2. Lấy thông tin từ form */
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String paymentMethod = request.getParameter("paymentMethod");
            String cardNumber = request.getParameter("cardNumber");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");
            String shippingAddress = request.getParameter("shippingAddress");

            /* 3. Validate đầu vào */
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn phương thức thanh toán.");
            }
            if (shippingAddress == null || shippingAddress.trim().length() < 10) {
                throw new IllegalArgumentException("Vui lòng nhập địa chỉ giao hàng chi tiết.");
            }
            if ("creditcard".equals(paymentMethod)) {
                if (cardNumber == null || !cardNumber.replaceAll("\\s+", "").matches("\\d{8,16}")) {
                    throw new IllegalArgumentException("Số thẻ tín dụng không hợp lệ.");
                }
                if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
                    throw new IllegalArgumentException("Ngày hết hạn không hợp lệ.");
                }
                if (cvv == null || !cvv.matches("\\d{3,4}")) {
                    throw new IllegalArgumentException("Mã CVV không hợp lệ.");
                }
            }

            /* 4. Lấy đơn hàng */
            OrderDAO dao = new OrderDAO();
            Order order = dao.getById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng #" + orderId);
            }

            /* 5. Quyền sở hữu đơn hàng */
            if (!order.getCusId().equals(user.getId())) {
                throw new IllegalArgumentException("Bạn không có quyền thanh toán đơn hàng này.");
            }

            /* 6. Trạng thái đơn */
            if ("paid".equals(order.getOrderStatus())) {
                session.setAttribute("order", order);
                session.setAttribute("message", "Đơn hàng đã được thanh toán trước đó.");
                response.sendRedirect("payment_success.jsp");
                return;
            }
            if (!"pending".equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Đơn hàng không hợp lệ để thanh toán.");
            }

            /* 7. Cập nhật đơn hàng -> paid */
            order.setPaymentMethod(paymentMethod);
            order.setShippingAddress(shippingAddress);
            order.setOrderStatus("paid");

            if (!dao.updateOrder(order)) {
                throw new RuntimeException("Cập nhật đơn hàng thất bại.");
            }

            /* 8. Lấy chi tiết đơn và xóa khỏi giỏ hàng */
            List<OrderDetail> details = new OrderDetailDAO().getByOrderId(orderId);
            @SuppressWarnings("unchecked")
            Map<String, ViewCartCustomer> cart
                    = (Map<String, ViewCartCustomer>) session.getAttribute("cart");

            CartDAO cartDAO = new CartDAO();
            if (cart != null) {
                for (OrderDetail d : details) {
                    cart.remove(d.getProId());                              // Xóa khỏi session
                    cartDAO.removeItem(user.getId(), d.getProId());         // Xóa khỏi DB
                }
                session.setAttribute("cart", cart);
                session.setAttribute("cartSize",
                        cart.values().stream().mapToInt(ViewCartCustomer::getQuantity).sum());
            }

            /* 9. Lưu thông tin & redirect success */
            session.setAttribute("order", order);
            session.setAttribute("orderDetails", details);
            session.setAttribute("message", "Thanh toán thành công đơn hàng #" + orderId);
            response.sendRedirect("payment_success.jsp");

        } catch (Exception e) {
            e.printStackTrace();

            /* Trường hợp lỗi -> quay lại payment.jsp với thông báo */
            request.setAttribute("error", "Lỗi khi xác nhận thanh toán: " + e.getMessage());

            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = new OrderDAO().getById(orderId);
                if (order != null && "paid".equals(order.getOrderStatus())) {
                    // Nếu đơn đã thanh toán vẫn chuyển đến success
                    session.setAttribute("order", order);
                    session.setAttribute("message", "Đơn hàng đã được thanh toán trước đó.");
                    session.setAttribute("orderDetails",
                            new OrderDetailDAO().getByOrderId(order.getOrderId()));
                    response.sendRedirect("payment_success.jsp");
                    return;
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("CartController?action=view");
    }
}
