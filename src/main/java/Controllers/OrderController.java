package Controllers;

import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.UserDAO;
import Models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;
import com.google.gson.Gson;

@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            if (action == null) {
                loadOrdersPage(request, response);
                return;
            }

            switch (action) {
                case "checkout":
                    checkout(request, response);
                    break;
                case "getOrderDetails":
                    getOrderDetails(request, response);
                    break;
                default:
                    loadOrdersPage(request, response);
            }
        } catch (Exception e) {
            log("Error at OrderController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void loadOrdersPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");

            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();

            List<Order> orders = orderDAO.getByUserId(user.getId());

            Map<Integer, List<OrderDetailWithProductName>> orderDetailsMap = new HashMap<>();
            for (Order order : orders) {
                List<OrderDetail> details = orderDetailDAO.getByOrderId(order.getId());
                List<OrderDetailWithProductName> detailsWithProductName = new ArrayList<>();
                for (OrderDetail detail : details) {
                    Product product = productDAO.getById(String.valueOf(detail.getProductId())); // FIXED
                    OrderDetailWithProductName detailWithName = new OrderDetailWithProductName(detail,
                            product != null ? product.getProName() : "");
                    detailsWithProductName.add(detailWithName);
                }
                orderDetailsMap.put(order.getId(), detailsWithProductName);
            }

            request.setAttribute("orders", orders);
            request.setAttribute("orderDetailsMap", orderDetailsMap);
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");
            Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if (cart == null || cart.isEmpty()) {
                session.setAttribute("error", "Your cart is empty");
                response.sendRedirect("cart.jsp");
                return;
            }

            Order order = new Order();
            order.setUserId(user.getId());
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("pending");

            double totalPrice = 0;
            for (CartItem item : cart.values()) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
            order.setTotalPrice(totalPrice);

            OrderDAO orderDAO = new OrderDAO();
            orderDAO.create(order);

            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            for (CartItem item : cart.values()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setProductId(item.getProductId());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(item.getPrice());
                detail.setTotalPrice(item.getPrice() * item.getQuantity());
                orderDetailDAO.create(detail);
            }

            session.removeAttribute("cart");
            session.removeAttribute("cartSize");

            response.sendRedirect("OrderController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void getOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();

            Order order = orderDAO.getById(orderId);
            List<OrderDetail> orderDetails = orderDetailDAO.getByOrderId(orderId);

            OrderDetailsResponse responseObj = new OrderDetailsResponse();
            responseObj.order = order;
            responseObj.orderItems = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                Product product = productDAO.getById(String.valueOf(detail.getProductId())); // FIXED
                OrderItem item = new OrderItem();
                item.productName = product != null ? product.getProName() : "";
                item.quantity = detail.getQuantity();
                item.unitPrice = detail.getUnitPrice();
                item.totalPrice = detail.getTotalPrice();
                responseObj.orderItems.add(item);
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(responseObj));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching order details: " + e.getMessage());
        }
    }

    private static class OrderDetailsResponse {

        Order order;
        List<OrderItem> orderItems;
    }

    private static class OrderItem {

        String productName;
        int quantity;
        double unitPrice;
        double totalPrice;
    }

    public static class OrderDetailWithProductName extends OrderDetail {

        private String productName;

        public OrderDetailWithProductName(OrderDetail detail, String productName) {
            super.setOrderId(detail.getOrderId());
            super.setProductId(detail.getProductId());
            super.setQuantity(detail.getQuantity());
            super.setUnitPrice(detail.getUnitPrice());
            super.setTotalPrice(detail.getTotalPrice());
            this.productName = productName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
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
