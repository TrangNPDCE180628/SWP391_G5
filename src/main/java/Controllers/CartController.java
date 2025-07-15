package Controllers;

import DAOs.CartDAO;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                case "view":
                    loadCartFromDatabase(request);
                    request.getRequestDispatcher("cart.jsp").forward(request, response);
                    break;

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
                    response.sendRedirect("cart.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing the shopping cart!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void loadCartFromDatabase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String cusId = (String) session.getAttribute("cusId");

        try {
            CartDAO cartDAO = new CartDAO();
            List<ViewCartCustomer> cartItems = cartDAO.getViewCartByCusId(cusId);
            LinkedHashMap<String, ViewCartCustomer> cartMap = new LinkedHashMap<>();

            StockDAO stockDAO = new StockDAO();
            Map<String, Integer> stockMap = new HashMap<>();

            for (ViewCartCustomer item : cartItems) {
                int stockQuantity = stockDAO.getStockProductByProductId(item.getProId());
                stockMap.put(item.getProId(), stockQuantity);
                cartMap.put(item.getProId(), item);
            }

            session.setAttribute("cart", cartMap);
            session.setAttribute("stockMap", stockMap);
            session.setAttribute("cartSize", cartMap.size());

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Unable to load cart from database!");
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();

        String productId = request.getParameter("productId");
        String cusId = (String) session.getAttribute("cusId");

        try {
            if (cusId == null) {
                session.setAttribute("error", "You need to login to add products to cart!");
                response.sendRedirect("login.jsp");
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            StockDAO stockDAO = new StockDAO();
            CartDAO cartDAO = new CartDAO();

            Product product = productDAO.getById(productId);
            int stockQuantity = stockDAO.getStockProductByProductId(productId);

            if (product == null || stockQuantity <= 0) {
                session.setAttribute("error", "Product is invalid or out of stock!");
                response.sendRedirect("HomeController");
                return;
            }

            // ❗❗ Thêm vào DB (dùng addToCart để tránh trùng lỗi)
            cartDAO.addToCart(cusId, productId, 1);

            // 🔄 Load lại toàn bộ cart từ DB
            List<ViewCartCustomer> cartList = cartDAO.getViewCartByCusId(cusId);

            // Cập nhật session
            LinkedHashMap<String, ViewCartCustomer> updatedCart = new LinkedHashMap<>();
            for (ViewCartCustomer item : cartList) {
                updatedCart.put(item.getProId(), item);
            }

            session.setAttribute("cart", updatedCart);
            session.setAttribute("cartSize", updatedCart.size());
            session.setAttribute("message", "Product added to cart!");
            response.sendRedirect("HomeController");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error adding to cart!");
            response.sendRedirect("HomeController");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String productId = request.getParameter("productId");
        String cusId = (String) session.getAttribute("cusId");

        try {
            if (cusId == null) {
                session.setAttribute("error", "You need to login to operate!");
                response.sendRedirect("login.jsp");
                return;
            }

            int change = Integer.parseInt(request.getParameter("change"));

            LinkedHashMap<String, ViewCartCustomer> cart
                    = (LinkedHashMap<String, ViewCartCustomer>) session.getAttribute("cart");

            ViewCartCustomer item = null;
            for (Map.Entry<String, ViewCartCustomer> entry : cart.entrySet()) {
                if (entry.getKey().trim().equals(productId.trim())) {
                    item = entry.getValue();
                    break;
                }
            }

            if (item != null) {
                int newQuantity = item.getQuantity() + change;

                StockDAO stockDAO = new StockDAO();
                int stock = stockDAO.getStockProductByProductId(productId);

                CartDAO cartDAO = new CartDAO();

                if (newQuantity > 0 && newQuantity <= stock) {
                    cartDAO.updateQuantity(item.getCartId(), newQuantity);
                    session.setAttribute("message", "Quantity update successful!");
                } else if (newQuantity <= 0) {
                    cartDAO.deleteCartItem(item.getCartId());
                    session.setAttribute("message", "Product removed!");
                } else {
                    session.setAttribute("error", "Quantity exceeds inventory!");
                }

                // Cập nhật lại cart
                List<ViewCartCustomer> updatedCartList = cartDAO.getViewCartByCusId(cusId);
                LinkedHashMap<String, ViewCartCustomer> updatedCart = new LinkedHashMap<>();
                for (ViewCartCustomer vc : updatedCartList) {
                    updatedCart.put(vc.getProId(), vc);
                }

                session.setAttribute("cart", updatedCart);
                session.setAttribute("cartSize", updatedCart.size());

            } else {
                session.setAttribute("error", "No products found in the cart!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Cart update failed: " + e.getMessage());
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
                ViewCartCustomer item = cart.get(productId);

                if (item != null) {
                    CartDAO cartDAO = new CartDAO();
                    cartDAO.deleteCartItem(item.getCartId());
                    cart.remove(productId);
                    session.setAttribute("message", "Product removed from cart!");
                }

                session.setAttribute("cart", cart);
                session.setAttribute("cartSize", cart.size());
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Removing product from cart failed!");
            }
        }

        response.sendRedirect("cart.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO voucherDAO = new VoucherDAO();
        List<Voucher> vouchers = null;
        try {
            vouchers = voucherDAO.getAll();
        } catch (SQLException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getSession().setAttribute("vouchers", vouchers);
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
