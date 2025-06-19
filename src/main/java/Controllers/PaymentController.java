package Controllers;

import DAOs.OrderDAO;
import Models.Order;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PaymentController", urlPatterns = {"/PaymentController"})
public class PaymentController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                System.out.println("User not logged in. Redirecting to login page.");
                response.sendRedirect("login.jsp");
                return;
            }
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            System.out.println("GET request - orderId: " + orderId);

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getById(orderId);
            if (order == null || order.getUserId() != user.getId()) {
                System.out.println("Order not found or user ID mismatch. Order: " + order);
                request.setAttribute("error", "Order not found or access denied.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            if (!"pending".equals(order.getStatus())) {
                System.out.println("Order status not pending. Current status: " + order.getStatus());
                request.setAttribute("error", "Order is not available for payment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            System.out.println("Order found and ready for payment. Order ID: " + order.getId());
            request.setAttribute("order", order);
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Error in GET: " + e.getMessage());
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");
            if (user == null) {
                System.out.println("User not logged in. Redirecting to login page.");
                response.sendRedirect("login.jsp");
                return;
            }
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String paymentMethod = request.getParameter("paymentMethod");
            String cardNumber = request.getParameter("cardNumber");

            System.out.println("POST request - orderId: " + orderId);
            System.out.println("POST request - paymentMethod: " + paymentMethod);
            System.out.println("POST request - cardNumber: " + (cardNumber != null ? cardNumber : "null"));

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                System.out.println("Order not found.");
                request.setAttribute("error", "Order not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            if (order.getUserId() != user.getId()) {
                System.out.println("User ID mismatch. User ID: " + user.getId() + ", Order User ID: " + order.getUserId());
                request.setAttribute("error", "You do not have permission to pay for this order.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            if (!"pending".equals(order.getStatus())) {
                System.out.println("Order status not pending. Current status: " + order.getStatus());
                request.setAttribute("error", "Order is not available for payment. Current status: " + order.getStatus());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            boolean paymentSuccess = false;
            String paymentError = null;

            if ("creditcard".equals(paymentMethod)) {
                if (cardNumber == null || cardNumber.trim().isEmpty()) {
                    paymentError = "Credit card number is required.";
                    System.out.println("Credit card number is missing.");
                } else if (cardNumber.length() < 8) {
                    paymentError = "Credit card number must be at least 8 digits.";
                    System.out.println("Credit card number too short: " + cardNumber.length() + " digits.");
                } else {
                    paymentSuccess = true;
                    System.out.println("Credit card payment success.");
                }
            } else if ("paypal".equals(paymentMethod)) {
                paymentSuccess = true;
                System.out.println("PayPal payment success (simulated).");
            } else {
                paymentError = "Invalid payment method.";
                System.out.println("Invalid payment method: " + paymentMethod);
            }

            if (paymentSuccess) {
                orderDAO.updateOrderStatus(orderId, "completed");
                System.out.println("Order payment completed. Order ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/payment_success.jsp?message=" + java.net.URLEncoder.encode("Payment successful! Your order is now completed.", "UTF-8"));
                return;
            } else {
                System.out.println("Payment failed. Reason: " + (paymentError != null ? paymentError : "Unknown error"));
                request.setAttribute("order", order);
                request.setAttribute("error", paymentError != null ? paymentError : "Payment failed. Please check your details and try again.");
                request.getRequestDispatcher("payment.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("Error in POST: " + e.getMessage());
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
