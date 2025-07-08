package Controllers;

import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.StockDAO;
import DAOs.VoucherDAO;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.ViewCartCustomer;
import Models.Voucher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            if (action == null) {
                response.sendRedirect("cart.jsp");
                return;
            }

            switch (action) {
                case "add":
                    addToCart(request, response);
                    break;
                case "update":
                    updateCart(request, response);
                    break;
                case "remove":
                    removeFromCart(request, response);
                    break;
                case "makePayment":
                    makePayment(request, response);
                    break;
                default:
                    response.sendRedirect("cart.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý giỏ hàng.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        LinkedHashMap<String, ViewCartCustomer> cart
                = (LinkedHashMap<String, ViewCartCustomer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new LinkedHashMap<>();
        }

        String productId = request.getParameter("productId");
        String cusId = (String) session.getAttribute("cusId");

        try {
            if (cart.containsKey(productId)) {
                session.setAttribute("error", "Sản phẩm đã có trong giỏ. Vui lòng chỉnh sửa số lượng tại giỏ hàng.");
                response.sendRedirect("HomeController");
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            StockDAO stockDAO = new StockDAO();

            Product product = productDAO.getProductById(productId);
            int stockQuantity = stockDAO.getStockByProductId(productId);

            if (product == null) {
                session.setAttribute("error", "Không tìm thấy sản phẩm.");
                response.sendRedirect("HomeController");
                return;
            }

            if (stockQuantity <= 0) {
                session.setAttribute("error", "Sản phẩm đã hết hàng.");
                response.sendRedirect("HomeController");
                return;
            }

            ViewCartCustomer item = new ViewCartCustomer(
                    0,
                    cusId,
                    productId,
                    product.getProName(),
                    product.getProPrice().doubleValue(),
                    product.getProImageMain(),
                    1
            );

            // Đưa sản phẩm mới vào đầu giỏ hàng (LinkedHashMap preserve order)
            LinkedHashMap<String, ViewCartCustomer> updatedCart = new LinkedHashMap<>();
            updatedCart.put(productId, item);
            for (Map.Entry<String, ViewCartCustomer> entry : cart.entrySet()) {
                updatedCart.put(entry.getKey(), entry.getValue());
            }

            session.setAttribute("cart", updatedCart);
            session.setAttribute("cartSize", updatedCart.size());
            session.setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng.");
            response.sendRedirect("HomeController");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Thêm sản phẩm vào giỏ thất bại.");
            response.sendRedirect("HomeController");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Map<String, ViewCartCustomer> cart = (Map<String, ViewCartCustomer>) session.getAttribute("cart");

        if (cart != null) {
            try {
                String productId = request.getParameter("productId");
                int change = Integer.parseInt(request.getParameter("change"));

                ViewCartCustomer item = cart.get(productId);
                if (item != null) {
                    StockDAO stockDAO = new StockDAO();
                    int stockQuantity = stockDAO.getStockByProductId(productId);
                    int newQuantity = item.getQuantity() + change;

                    if (newQuantity > 0 && newQuantity <= stockQuantity) {
                        item.setQuantity(newQuantity);
                    } else if (newQuantity <= 0) {
                        cart.remove(productId);
                    } else {
                        session.setAttribute("error", "Vượt quá số lượng tồn kho.");
                    }

                    session.setAttribute("cart", cart);
                    session.setAttribute("cartSize", cart.size());
                }

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Cập nhật giỏ hàng thất bại.");
            }
        }

        response.sendRedirect("cart.jsp");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Map<String, ViewCartCustomer> cart = (Map<String, ViewCartCustomer>) session.getAttribute("cart");

        if (cart != null) {
            try {
                String productId = request.getParameter("productId");
                if (cart.remove(productId) != null) {
                    session.setAttribute("message", "Đã xóa sản phẩm khỏi giỏ.");
                }

                session.setAttribute("cart", cart);
                session.setAttribute("cartSize", cart.size());
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Xóa sản phẩm khỏi giỏ thất bại.");
            }
        }

        response.sendRedirect("cart.jsp");
    }

    private void makePayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String voucherCode = request.getParameter("voucherCode");
        String cusId = (String) session.getAttribute("cusId");

        Map<String, ViewCartCustomer> cart = (Map<String, ViewCartCustomer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("error", "Your cart is empty.");
            response.sendRedirect("cart.jsp");
            return;
        }

        try {
            double subtotal = cart.values().stream()
                    .mapToDouble(ViewCartCustomer::getTotalPrice).sum();

            double discount = 0;
            if (voucherCode != null && !voucherCode.isEmpty()) {
                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher matched = voucherDAO.getAll().stream()
                        .filter(v -> v.getCodeName().equalsIgnoreCase(voucherCode) && v.isVoucherActive())
                        .findFirst().orElse(null);

                if (matched != null && subtotal >= matched.getMinOrderAmount().doubleValue()) {
                    if (matched.getDiscountType().equalsIgnoreCase("percentage")) {
                        discount = subtotal * matched.getDiscountValue().doubleValue() / 100.0;
                    } else if (matched.getDiscountType().equalsIgnoreCase("fixed")) {
                        discount = matched.getDiscountValue().doubleValue();
                    }
                } else {
                    session.setAttribute("warning", "Voucher không hợp lệ hoặc không đủ điều kiện.");
                }
            }

            double totalPrice = subtotal - discount;

            OrderDAO orderDAO = new OrderDAO();
            Order order = new Order();
            order.setUserId(Integer.parseInt(cusId));
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
            order.setTotalPrice(totalPrice);
            orderDAO.create(order);

            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            StockDAO stockDAO = new StockDAO();

            for (ViewCartCustomer item : cart.values()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setProductId(Integer.parseInt(item.getProId()));
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(item.getProPrice());
                detail.setTotalPrice(item.getTotalPrice());
                orderDetailDAO.create(detail);
                stockDAO.decreaseStockAfterOrder(item.getProId(), item.getQuantity());
            }

            session.removeAttribute("cart");
            session.setAttribute("cartSize", 0);
            session.setAttribute("message", "Thanh toán thành công. Mã đơn hàng: #" + order.getId());
            response.sendRedirect("order_success.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Đã xảy ra lỗi khi thanh toán.");
            response.sendRedirect("cart.jsp");
        }
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
}
