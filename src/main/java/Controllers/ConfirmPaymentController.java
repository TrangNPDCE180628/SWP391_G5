//package Controllers;
//
//import DAOs.OrderDAO;
//import Models.Order;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//
//@WebServlet(name = "ConfirmPaymentController", urlPatterns = {"/ConfirmPaymentController"})
//public class ConfirmPaymentController extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        try {
//            // Kiểm tra session và user
//            HttpSession session = request.getSession(false);
//            if (session == null || session.getAttribute("LOGIN_USER") == null) {
//                response.sendRedirect("login.jsp");
//                return;
//            }
//
//            // Lấy dữ liệu từ form
//            String orderIdParam = request.getParameter("orderId");
//            String paymentMethod = request.getParameter("paymentMethod");
//            String cardNumber = request.getParameter("cardNumber");
//            String shippingAddress = request.getParameter("shippingAddress");
//
//            // Validation chi tiết hơn
//            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
//                request.setAttribute("error", "Thiếu thông tin ID đơn hàng.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
//                request.setAttribute("error", "Vui lòng chọn phương thức thanh toán.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
//                request.setAttribute("error", "Vui lòng nhập địa chỉ giao hàng.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // Validate card number nếu chọn credit card
//            if ("creditcard".equals(paymentMethod)) {
//                if (cardNumber == null || cardNumber.trim().isEmpty()) {
//                    request.setAttribute("error", "Vui lòng nhập số thẻ tín dụng.");
//                    request.getRequestDispatcher("error.jsp").forward(request, response);
//                    return;
//                }
//
//                // Validate card number format (chỉ chấp nhận số, từ 8-16 ký tự)
//                String cleanCardNumber = cardNumber.replaceAll("\\s+", ""); // Remove spaces
//                if (!cleanCardNumber.matches("\\d{8,16}")) {
//                    request.setAttribute("error", "Số thẻ tín dụng không hợp lệ (8-16 chữ số).");
//                    request.getRequestDispatcher("error.jsp").forward(request, response);
//                    return;
//                }
//            }
//
//            int orderId;
//            try {
//                orderId = Integer.parseInt(orderIdParam);
//            } catch (NumberFormatException e) {
//                request.setAttribute("error", "ID đơn hàng không hợp lệ.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // Lấy và kiểm tra order
//            OrderDAO dao = new OrderDAO();
//            Order order = dao.getById(orderId);
//
//            if (order == null) {
//                request.setAttribute("error", "Không tìm thấy đơn hàng #" + orderId);
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // FIX: Xử lý các trạng thái order khác nhau
//            String currentStatus = order.getOrderStatus();
//
//            // Nếu order đã được thanh toán
//            if ("paid".equals(currentStatus)) {
//                request.setAttribute("message", "Đơn hàng #" + orderId + " đã được thanh toán thành công trước đó.");
//                request.setAttribute("order", order);
//                request.getRequestDispatcher("success.jsp").forward(request, response);
//                return;
//            }
//
//            // Nếu order bị hủy hoặc trạng thái không hợp lệ
//            if ("cancelled".equals(currentStatus) || "completed".equals(currentStatus)) {
//                request.setAttribute("error", "Đơn hàng #" + orderId + " đã được "
//                        + ("cancelled".equals(currentStatus) ? "hủy" : "hoàn thành")
//                        + " và không thể thanh toán.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // Chỉ xử lý thanh toán nếu order đang pending
//            if (!"pending".equals(currentStatus)) {
//                request.setAttribute("error", "Đơn hàng #" + orderId + " có trạng thái '" + currentStatus
//                        + "' và không thể thanh toán lúc này.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // Cập nhật order
//            order.setPaymentMethod(paymentMethod);
//            order.setOrderStatus("paid");
//            order.setShippingAddress(shippingAddress);
//
//            boolean updated = dao.update(order);
//
//            if (!updated) {
//                request.setAttribute("error", "Không thể cập nhật đơn hàng. Vui lòng thử lại.");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//                return;
//            }
//
//            // Hiển thị trang xác nhận thành công
//            request.setAttribute("message", "Bạn đã thanh toán thành công đơn hàng #" + orderId);
//            request.setAttribute("order", order);
//            request.getRequestDispatcher("success.jsp").forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            // FIX: Đảm bảo error message không rỗng
//            String errorMessage = e.getMessage();
//            if (errorMessage == null || errorMessage.trim().isEmpty()) {
//                errorMessage = "Đã xảy ra lỗi không xác định trong quá trình xử lý.";
//            }
//            request.setAttribute("error", "Lỗi hệ thống khi xác nhận thanh toán: " + errorMessage);
//            request.getRequestDispatcher("error.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.sendRedirect("HomeController");
//    }
//}
