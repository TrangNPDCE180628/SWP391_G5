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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
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
                case "paynow":
                    createDirectOrder(request, response);
                    break;
                case "cancel":
                    cancelOrder(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

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

            BigDecimal shippingFee = BigDecimal.valueOf(30000);
            BigDecimal finalAmount = totalAmount.subtract(discount).add(shippingFee);
            order.setFinalAmount(finalAmount);

            System.out.println("Final amount: " + finalAmount);

            order.setOrderStatus("pending");
            order.setVoucherId(voucherId);

            OrderDAO orderDAO = new OrderDAO();
            orderId = orderDAO.createOrder(order); // trả về khóa chính
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
            CustomerDAO customerDAO = new CustomerDAO();
            Customer profile = customerDAO.getCustomerById(user.getId());
            request.setAttribute("userProfile", profile);
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
            String receiverName = request.getParameter("receiverName");
            String receiverPhone = request.getParameter("receiverPhone");
            String shippingAddress = request.getParameter("shippingAddress");

            /* 3. Validate đầu vào */
            if (receiverName == null || receiverName.trim().length() < 2) {
                throw new IllegalArgumentException("Please enter receiver's name (at least 2 characters).");
            }
            if (receiverPhone == null || !receiverPhone.matches("\\d{10,15}")) {
                throw new IllegalArgumentException("Invalid receiver's phone number (10–15 digits).");
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                throw new IllegalArgumentException("Please select payment method.");
            }
            if (shippingAddress == null || shippingAddress.trim().length() < 10) {
                throw new IllegalArgumentException("Shipping address must be at least 10 characters.");
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
            order.setReceiverName(receiverName);
            order.setReceiverPhone(receiverPhone);
            order.setShippingAddress(shippingAddress);

            if ("vnpay".equalsIgnoreCase(paymentMethod)) {
                order.setPaymentMethod(paymentMethod);
                order.setReceiverName(receiverName);
                order.setReceiverPhone(receiverPhone);
                order.setShippingAddress(shippingAddress);

                dao.updateOrder(order); // chỉ update thông tin, chưa update trạng thái

                // Gửi thông tin đến ajaxServlet để xử lý redirect
                request.setAttribute("orderId", orderId);
                request.setAttribute("totalBill", order.getFinalAmount());
                request.getRequestDispatcher("ajaxRedirect.jsp").forward(request, response);
                return;
            }

            // Nếu không phải vnpay thì xử lý tiếp: COD chẳng hạn
            order.setOrderStatus("completed");
            if (!dao.updateOrder(order)) {
                order.setOrderStatus("failed");
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

            request.setAttribute("error", e.getMessage());

            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = new OrderDAO().getById(orderId);
                if (order == null) {
                    throw new IllegalArgumentException("Order not found.");
                }
                User user = (User) session.getAttribute("LOGIN_USER");
                if (user != null) {
                    CustomerDAO customerDAO = new CustomerDAO();
                    Customer profile = customerDAO.getCustomerById(user.getId());
                    request.setAttribute("userProfile", profile);
                }

                // Cập nhật lại thông tin user vừa nhập để hiển thị lại form
                order.setReceiverName(request.getParameter("receiverName"));
                order.setReceiverPhone(request.getParameter("receiverPhone"));
                order.setShippingAddress(request.getParameter("shippingAddress"));
                order.setPaymentMethod(request.getParameter("paymentMethod"));

                request.setAttribute("order", order);

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderById(orderId);

            if (order == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Nếu gọi từ sendBeacon mà không có session thì bỏ qua check user
            User user = session != null ? (User) session.getAttribute("LOGIN_USER") : null;
            if (user != null) {
                // Kiểm tra quyền sở hữu nếu có user
                if (!order.getCusId().equals(user.getId())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            // Chỉ huỷ đơn nếu trạng thái là pending
            if (!"pending".equals(order.getOrderStatus())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Huỷ đơn
            order.setOrderStatus("cancel");
            if (!dao.updateOrder(order)) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Cộng lại tồn kho
            List<OrderDetail> details = new OrderDetailDAO().getByOrderId(orderId);
            StockDAO stockDAO = new StockDAO();
            for (OrderDetail d : details) {
                stockDAO.increaseStockAfterCancel(d.getProId(), d.getQuantity());
            }

            // Trả HTTP 200 OK + redirect
            session.setAttribute("message", "Order #" + orderId + " has been cancelled successfully.");
            response.sendRedirect("CartController?action=view");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createDirectOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        int generatedOrderId = -1; // Dùng để rollback nếu có lỗi

        // Lấy proId ngay từ đầu để dùng trong trường hợp redirect lỗi
        String proId = request.getParameter("productId");

        try {
            /* 1. Kiểm tra đăng nhập */
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            /* 2. Lấy thông tin từ form Mua Ngay */
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String voucherCode = request.getParameter("voucherCode");

            /* 3. Lấy thông tin, kiểm tra tồn kho & tính toán ban đầu */
            StockDAO stockDAO = new StockDAO();
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getById(proId); // Giả định tên phương thức là getById

            if (product == null) {
                throw new Exception("Product with ID " + proId + " not found.");
            }

            if (stockDAO.getQuantity(proId) < quantity && stockDAO.getQuantity(proId) <= 0 && (stockDAO.getQuantity(proId) - quantity) < 0) {
                session.setAttribute("error", "Product " + product.getProName() + " is out of stock for the requested quantity.");
                response.sendRedirect("product-detail?id=" + proId);
                return;
            }

            BigDecimal unitPrice = product.getProPrice();
            BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

            // Chuẩn bị sẵn OrderDetail, chưa có orderId
            OrderDetail od = new OrderDetail();
            od.setProId(proId);
            od.setQuantity(quantity);
            od.setUnitPrice(unitPrice.doubleValue());

            /* 4. Xử lý voucher */
            BigDecimal discount = BigDecimal.ZERO;
            Integer voucherId = null;
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                Voucher v = new VoucherDAO().getByCode(voucherCode);
                if (v == null || totalAmount.compareTo(v.getMinOrderAmount()) < 0) {
                    session.setAttribute("error", "Voucher is invalid or ineligible.");
                    response.sendRedirect("product-detail?id=" + proId);
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

            /* 5. Tạo đối tượng Order */
            Order order = new Order();
            order.setCusId(String.valueOf(user.getId())); // Giả định phương thức là getId()
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(discount);

            BigDecimal shippingFee = new BigDecimal("30000"); // Phí ship cố định
            BigDecimal finalAmount = totalAmount.subtract(discount).add(shippingFee);
            order.setFinalAmount(finalAmount);

            order.setOrderStatus("pending");
            order.setVoucherId(voucherId);

            // Tạo order trong DB và lấy ID
            OrderDAO orderDAO = new OrderDAO();
            generatedOrderId = orderDAO.createOrder(order); // Giả sử createOrder trả về ID
            order.setOrderId(generatedOrderId);

            /* 6. Lưu OrderDetail với orderId đã có */
            od.setOrderId(generatedOrderId);
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            if (!detailDAO.createOrderDetail(od)) { // Giả định tên phương thức
                throw new SQLException("Unable to save order details for direct purchase.");
            }

            /* 7. Trừ kho và giảm số lượng voucher */
            stockDAO.decreaseStockAfterOrder(proId, quantity); // Dùng chung phương thức với createOrder
            if (voucherId != null) {
                new VoucherDAO().decreaseQuantity(voucherCode); // Giảm số lượng voucher đã dùng
            }

            /* 8. Chuyển tới trang thanh toán */
            CustomerDAO customerDAO = new CustomerDAO();
            Customer profile = customerDAO.getCustomerById(user.getId());
            request.setAttribute("userProfile", profile);
            request.setAttribute("order", order);
            request.getRequestDispatcher("payment.jsp").forward(request, response);

        } catch (Exception ex) {
            // 9. ROLLBACK: Nếu đã tạo Order nhưng có lỗi ở các bước sau
            if (generatedOrderId > 0) {
                try {
                    // Xóa Order, các OrderDetail sẽ được xóa theo (nhờ cascading delete trong DB)
                    new OrderDAO().delete(generatedOrderId);
                    // Hoàn lại số lượng voucher nếu có
                    String voucherCode = request.getParameter("voucherCode");
                    if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                        new VoucherDAO().increaseQuantity(voucherCode); // Bạn cần tạo phương thức này
                    }
                } catch (Exception e) {
                    Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, "Error during rollback", e);
                }
            }

            // Ghi log và báo lỗi cho người dùng
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, "DirectOrder Error", ex);
            session.setAttribute("error", "Order processing error: " + ex.getMessage());
            response.sendRedirect("product-detail?id=" + proId);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr != null) {
                int orderId = Integer.parseInt(orderIdStr);
                HttpSession session = request.getSession(false);
                User user = (User) session.getAttribute("LOGIN_USER");

                if (user == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                CustomerDAO customerDAO = new CustomerDAO();
                Customer profile = customerDAO.getCustomerById(user.getId());

                OrderDAO dao = new OrderDAO();
                Order order = dao.getOrderById(orderId);

                if (order != null && "pending".equalsIgnoreCase(order.getOrderStatus())
                        && order.getCusId().equals(user.getId())) {
                    request.setAttribute("userProfile", profile);
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("payment.jsp").forward(request, response);
                    return;
                }

                // Nếu không đúng trạng thái hoặc không thuộc về user
                session.setAttribute("error", "Order is not valid or you are not authorized.");
                response.sendRedirect("CartController?action=view");
            } else {
                response.sendRedirect("CartController?action=view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Unexpected error: " + e.getMessage());
            response.sendRedirect("CartController?action=view");
        }
    }

}
