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
            request.getSession().setAttribute("error", "System error:" + ex.getMessage());
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
                session.setAttribute("error", "Please select at least one product to checkout.");
                response.sendRedirect("CartController?action=view");
                return;
            }

            @SuppressWarnings("unchecked")
            Map<String, ViewCartCustomer> cart
                    = (Map<String, ViewCartCustomer>) session.getAttribute("cart");
            if (cart == null || cart.isEmpty()) {
                session.setAttribute("error", "Cart is empty.");
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
                            "Product " + item.getProName() + " out of stock");
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
                    session.setAttribute("error", "Voucher is invalid or ineligible.");
                    response.sendRedirect("CartController?action=view");
                    return;
                }
                voucherId = v.getVoucherId();
                discount = "percentage".equalsIgnoreCase(v.getDiscountType())
                        ? totalAmount.multiply(v.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        : v.getDiscountValue();
                if (v.getMaxDiscountValue() != null && v.getMaxDiscountValue().compareTo(BigDecimal.ZERO) > 0
                        && discount.compareTo(v.getMaxDiscountValue()) >= 0) {
                    discount = v.getMaxDiscountValue();
                }
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
                    throw new SQLException("Unable to save order details.");
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
            session.setAttribute("error", "Order processing error: " + ex.getMessage());
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
                throw new IllegalArgumentException("Please select payment method.");
            }
            if (shippingAddress == null || shippingAddress.trim().length() < 10) {
                throw new IllegalArgumentException("Please enter detailed shipping address.");
            }
            if ("creditcard".equals(paymentMethod)) {
                if (cardNumber == null || !cardNumber.replaceAll("\\s+", "").matches("\\d{8,16}")) {
                    throw new IllegalArgumentException("Invalid credit card number.");
                }
                if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
                    throw new IllegalArgumentException("Invalid expiration date.");
                }
                if (cvv == null || !cvv.matches("\\d{3,4}")) {
                    throw new IllegalArgumentException("Invalid CVV code.");
                }
            }

            /* 4. Lấy đơn hàng */
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found #" + orderId);
            }

            /* 5. Quyền sở hữu đơn hàng */
            if (!order.getCusId().equals(user.getId())) {
                throw new IllegalArgumentException("You are not authorized to pay for this order.");
            }

            /* 6. Trạng thái đơn */
            if ("paid".equals(order.getOrderStatus())) {
                session.setAttribute("order", order);
                session.setAttribute("message", "The order has been paid in advance.");
                response.sendRedirect("payment_success.jsp");
                return;
            }
            if (!"pending".equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Order is not valid for payment.");
            }

            /* 7. Cập nhật đơn hàng -> paid */
            order.setPaymentMethod(paymentMethod);
            order.setShippingAddress(shippingAddress);
            order.setOrderStatus("completed");

            if (!dao.updateOrder(order)) {
                throw new RuntimeException("Order update failed.");
            }
            /* giảm quantity voucher (nếu có)*/
            if (order.getVoucherId() != null) {
                VoucherDAO vDao = new VoucherDAO();
                Voucher usedV = vDao.getById(order.getVoucherId());
                if (usedV != null && usedV.getQuantity() > 0) {
                    int newQty = usedV.getQuantity() - 1;
                    vDao.updateQuantity(usedV.getVoucherId(), newQty);

                    if (newQty == 0) {
                        usedV.setQuantity(0); // Đảm bảo đồng bộ
                        usedV.setVoucherActive(false);
                        vDao.update(usedV);  // update trạng thái inactive
                    }
                }
            }
            /* 8. Lấy chi tiết đơn và xóa khỏi giỏ hàng */
            List<OrderDetail> details = new OrderDetailDAO().getByOrderId(orderId);
            ProductDAO productDAO = new ProductDAO();
            Map<String, String> productNames = new HashMap<>();

            for (OrderDetail d : details) {
                Product p = productDAO.getById(d.getProId());
                if (p != null) {
                    productNames.put(d.getProId(), p.getProName());
                }
            }

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
            session.setAttribute("productNames", productNames);
            session.setAttribute("message", "Order payment successful #" + orderId);
            response.sendRedirect("payment_success.jsp");

        } catch (Exception e) {
            e.printStackTrace();

            /* Trường hợp lỗi -> quay lại payment.jsp với thông báo */
            request.setAttribute("error", "Error confirming payment: " + e.getMessage());

            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = new OrderDAO().getById(orderId);
                if (order != null && "paid".equals(order.getOrderStatus())) {
                    // Nếu đơn đã thanh toán vẫn chuyển đến success
                    session.setAttribute("order", order);
                    session.setAttribute("message", "The order has been paid in advance.");
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
