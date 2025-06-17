package Controllers;

import DAOs.ProductDAO;
import Models.CartItem;
import Models.Product;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        System.out.println("CartController - Action: " + action);

        try {
            if (action == null) {
                System.out.println("No action specified, redirecting to cart.jsp");
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
                default:
                    System.out.println("Unknown action: " + action + ", redirecting to cart.jsp");
                    response.sendRedirect("cart.jsp");
            }
        } catch (Exception e) {
            System.out.println("Error at CartController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Get cart from session or create new one
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            System.out.println("Created new cart");
        }

        // Get product details
        String productIdStr = request.getParameter("productId");
        System.out.println("Adding to cart - ProductID: " + productIdStr);

        try {
            int productId = Integer.parseInt(productIdStr); // vẫn dùng để lưu key trong cart

            // Check if product already exists in cart
            if (cart.containsKey(productId)) {
                System.out.println("Product already in cart");
                session.setAttribute("error", "This product is already in your cart. Please adjust quantity in the cart page.");
                response.sendRedirect("HomeController");
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            // ✅ Fix: ép kiểu về String để khớp với ProductDAO
            Product product = productDAO.getById(productIdStr);

            if (product != null) {
                System.out.println("Found product: " + product.getProName() + ", Stock: " + product.getProStockQuantity());

                // Check if product is in stock
                if (product.getProStockQuantity() <= 0) {
                    System.out.println("Product out of stock");
                    session.setAttribute("error", "Sorry, this product is out of stock.");
                    response.sendRedirect("HomeController");
                    return;
                }

                // Create new cart item with quantity = 1
                CartItem item = new CartItem(
                        productId,
                        product.getProName(),
                        product.getProPrice(),
                        1, // Always set quantity to 1 for new items
                        product.getProImageMain()
                );
                System.out.println("Created new cart item: " + item.getProductName());

                // Update cart in session
                cart.put(productId, item);
                session.setAttribute("cart", cart);
                session.setAttribute("cartSize", cart.size());

                // Debug cart contents
                System.out.println("Cart contents after update:");
                for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
                    System.out.println("Product ID: " + entry.getKey()
                            + ", Name: " + entry.getValue().getProductName()
                            + ", Quantity: " + entry.getValue().getQuantity());
                }

                session.setAttribute("message", "Product added to cart successfully!");
                response.sendRedirect("HomeController");
            } else {
                System.out.println("Product not found for ID: " + productId);
                session.setAttribute("error", "Product not found.");
                response.sendRedirect("HomeController");
            }
        } catch (Exception e) {
            System.out.println("Error in addToCart: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Failed to add item to cart.");
            response.sendRedirect("HomeController");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int change = Integer.parseInt(request.getParameter("change"));

                System.out.println("Updating cart - ProductID: " + productId + ", Change: " + change);

                CartItem item = cart.get(productId);
                if (item != null) {
                    ProductDAO productDAO = new ProductDAO();
                    // ✅ Ép kiểu sang String để khớp với DAO
                    Product product = productDAO.getById(String.valueOf(productId));

                    int newQuantity = item.getQuantity() + change;
                    System.out.println("Current quantity: " + item.getQuantity() + ", New quantity: " + newQuantity);

                    // ✅ Dùng đúng getter: getProStockQuantity() thay vì getProQuantity()
                    if (newQuantity > 0 && newQuantity <= product.getProStockQuantity()) {
                        item.setQuantity(newQuantity);
                        System.out.println("Updated quantity to: " + newQuantity);
                    } else if (newQuantity <= 0) {
                        cart.remove(productId);
                        System.out.println("Removed item from cart (quantity <= 0)");
                    }

                    session.setAttribute("cart", cart);
                    session.setAttribute("cartSize", cart.size());
                } else {
                    System.out.println("Item not found in cart");
                }
            } catch (Exception e) {
                System.out.println("Error updating cart: " + e.getMessage());
                session.setAttribute("error", "Failed to update cart item.");
            }
        } else {
            System.out.println("Cart is null");
        }

        response.sendRedirect("cart.jsp");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                System.out.println("Removing from cart - ProductID: " + productId);

                CartItem removedItem = cart.remove(productId);
                if (removedItem != null) {
                    System.out.println("Successfully removed item: " + removedItem.getProductName());
                    session.setAttribute("message", "Item removed from cart.");
                } else {
                    System.out.println("Item not found in cart");
                }

                session.setAttribute("cart", cart);
                session.setAttribute("cartSize", cart.size());
            } catch (Exception e) {
                System.out.println("Error removing from cart: " + e.getMessage());
                session.setAttribute("error", "Failed to remove item from cart.");
            }
        } else {
            System.out.println("Cart is null");
        }

        response.sendRedirect("cart.jsp");
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
