package Controllers;

import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.StockDAO;
import DAOs.VoucherDAO;
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

@WebServlet(name = "PaymentController", urlPatterns = {"/PaymentController"})
public class PaymentController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        try {
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String[] selectedIds = request.getParameterValues("selectedProductIds");
            String voucherCode = request.getParameter("voucherCode");

            if (selectedIds == null || selectedIds.length == 0) {
                session.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
                response.sendRedirect("CartController?action=view");
                return;
            }

            @SuppressWarnings("unchecked")
            Map<String, ViewCartCustomer> cart = (Map<String, ViewCartCustomer>) session.getAttribute("cart");
            if (cart == null || cart.isEmpty()) {
                session.setAttribute("error", "Giỏ hàng của bạn đang trống.");
                response.sendRedirect("CartController?action=view");
                return;
            }
            StockDAO stockDAO = new StockDAO();  // <-- Thay vì ProductDAO
            for (String proId : selectedIds) {
                ViewCartCustomer item = cart.get(proId);
                if (item == null) {
                    session.setAttribute("error", "Sản phẩm không tồn tại trong giỏ hàng.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }

                int availableStock = stockDAO.getQuantity(proId);
                if (availableStock < item.getQuantity()) {
                    session.setAttribute("error", "Sản phẩm " + item.getProName() + " không đủ số lượng tồn kho.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }
            }

            List<OrderDetail> orderDetails = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            // Tính tổng tiền
            for (String proId : selectedIds) {
                ViewCartCustomer item = cart.get(proId);
                double unitPrice = item.getProPrice();
                int quantity = item.getQuantity();
                BigDecimal lineTotal = BigDecimal.valueOf(unitPrice).multiply(BigDecimal.valueOf(quantity));

                OrderDetail detail = new OrderDetail();
                detail.setProId(proId);
                detail.setQuantity(quantity);
                detail.setUnitPrice(unitPrice);
                detail.setVoucherId(null);
                orderDetails.add(detail);

                totalAmount = totalAmount.add(lineTotal);
            }

            // Xử lý voucher
            BigDecimal discountAmount = BigDecimal.ZERO;
            Integer voucherId = null;
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher v = voucherDAO.getByCode(voucherCode);
                if (v == null) {
                    session.setAttribute("error", "Mã voucher không hợp lệ.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }
                if (totalAmount.compareTo(v.getMinOrderAmount()) >= 0) {
                    voucherId = v.getVoucherId();
                    if (v.getDiscountType().equalsIgnoreCase("percentage")) {
                        discountAmount = totalAmount.multiply(v.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    } else {
                        discountAmount = v.getDiscountValue();
                    }
                } else {
                    session.setAttribute("error", "Đơn hàng không đủ điều kiện để áp dụng voucher.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }
            }

            // Lấy địa chỉ giao hàng
            String shippingAddress = request.getParameter("shippingAddress");
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                shippingAddress = "Địa chỉ chưa được cung cấp.";
            }

            // Tạo đơn hàng
            Order order = new Order();
            order.setCusId(user.getId());
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(discountAmount);
            order.setVoucherId(voucherId);
            order.setOrderStatus("pending");
            order.setPaymentMethod(null);
            order.setShippingAddress(shippingAddress);

            // Lưu đơn hàng vào database
            OrderDAO orderDAO = new OrderDAO();
            orderId = orderDAO.create(order);
            if (orderId <= 0) {
                throw new SQLException("Không thể tạo đơn hàng.");
            }

            // Lưu chi tiết đơn hàng
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            try {
                for (OrderDetail detail : orderDetails) {
                    detail.setOrderId(orderId);
                    if (!detailDAO.create(detail)) {
                        throw new SQLException("Không thể tạo chi tiết đơn hàng.");
                    }
                }

                // Cập nhật số lượng tồn kho
                for (String proId : selectedIds) {
                    ViewCartCustomer item = cart.get(proId);
                    boolean updated = stockDAO.decreaseStockAfterOrder(proId, item.getQuantity());
                    if (!updated) {
                        throw new SQLException("Không thể cập nhật tồn kho cho sản phẩm: " + proId);
                    }
                }

                // Cập nhật giỏ hàng
                for (String proId : selectedIds) {
                    cart.remove(proId);
                }
                session.setAttribute("cart", cart);

                // Cập nhật số lượng trong giỏ hàng
                int cartSize = cart.values().stream().mapToInt(ViewCartCustomer::getQuantity).sum();
                session.setAttribute("cartSize", cartSize);

                // Lấy thông tin đơn hàng đầy đủ
                Order fullOrder = orderDAO.getById(orderId);
                if (fullOrder == null) {
                    throw new SQLException("Không thể tải thông tin đơn hàng.");
                }

                request.setAttribute("order", fullOrder);
                request.getRequestDispatcher("payment.jsp").forward(request, response);
            } catch (Exception e) {
                // Rollback: Xóa đơn hàng nếu có lỗi
                orderDAO.delete(orderId);
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi xử lý đơn hàng: " + e.getMessage());
            response.sendRedirect("CartController?action=view");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi không xác định: " + e.getMessage());
            response.sendRedirect("CartController?action=view");
        }
    }

    private void confirmPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        try {
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String orderIdParam = request.getParameter("orderId");
            String paymentMethod = request.getParameter("paymentMethod");
            String cardNumber = request.getParameter("cardNumber");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");
            String shippingAddress = request.getParameter("shippingAddress");

            // Validate input
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy mã đơn hàng.");
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn phương thức thanh toán.");
            }

            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập địa chỉ giao hàng.");
            }

            if ("creditcard".equals(paymentMethod)) {
                if (cardNumber == null || cardNumber.trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập số thẻ tín dụng.");
                }
                String cleanCardNumber = cardNumber.replaceAll("\\s+", "");
                if (!cleanCardNumber.matches("\\d{8,16}")) {
                    throw new IllegalArgumentException("Số thẻ tín dụng không hợp lệ (8-16 chữ số).");
                }
                if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
                    throw new IllegalArgumentException("Ngày hết hạn không hợp lệ (MM/YY).");
                }
                if (cvv == null || !cvv.matches("\\d{3,4}")) {
                    throw new IllegalArgumentException("Mã CVV không hợp lệ (3-4 chữ số).");
                }
            }

            // Parse order ID
            int orderId;
            try {
                orderId = Integer.parseInt(orderIdParam);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID đơn hàng không hợp lệ.");
            }

            // Get order
            OrderDAO dao = new OrderDAO();
            Order order = dao.getById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng #" + orderId);
            }

            // Validate order ownership
            if (order.getCusId() == null || !order.getCusId().equals(user.getId())) {
                throw new IllegalArgumentException("Bạn không có quyền thanh toán đơn hàng này.");
            }

            // Check order status
            String currentStatus = order.getOrderStatus();
            if ("paid".equals(currentStatus)) {
                session.setAttribute("message", "Đơn hàng #" + orderId + " đã được thanh toán.");
                request.setAttribute("order", order);
                request.getRequestDispatcher("payment-success.jsp").forward(request, response);
                return;
            }

            if (!"pending".equals(currentStatus)) {
                throw new IllegalArgumentException("Đơn hàng #" + orderId + " không thể thanh toán ở trạng thái: " + currentStatus);
            }

            // Update order
            order.setPaymentMethod(paymentMethod);
            order.setShippingAddress(shippingAddress);
            order.setOrderStatus("paid");

            boolean updated = dao.update(order);
            if (!updated) {
                throw new SQLException("Không thể cập nhật đơn hàng.");
            }

            // Success
            session.setAttribute("message", "Thanh toán thành công đơn hàng #" + orderId);
            request.setAttribute("order", order);
            request.getRequestDispatcher("payment-success.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi cập nhật đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi không xác định: " + e.getMessage());
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("CartController?action=view");
    }
}
