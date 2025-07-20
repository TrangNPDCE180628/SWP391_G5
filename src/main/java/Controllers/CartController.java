package Controllers;

import DAOs.CartDAO;
import DAOs.StockDAO;
import DAOs.ProductDAO;
import DAOs.VoucherDAO;
import Models.Product;
import Models.ViewCartCustomer;
import Models.Voucher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String cusId = (session != null) ? (String) session.getAttribute("cusId") : null;

        if (cusId == null) {
            session = request.getSession(); // tạo session nếu chưa có
            session.setAttribute("LOGIN_MESSAGE", "You must login to access your cart.");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null) {
                response.sendRedirect("cart.jsp");
                return;
            }

            switch (action) {
                case "view":
                    // Kiểm tra đăng nhập
                    if (!isLoggedIn(request)) {
                        request.setAttribute("error", "You must login to access your cart.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }
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

    // Hàm kiểm tra đăng nhập
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String cusId = (String) session.getAttribute("cusId");
        return cusId != null && !cusId.isEmpty();
    }

    private void loadCartFromDatabase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String cusId = (String) session.getAttribute("cusId");
        // Nếu chưa đăng nhập thì reset cart
        if (cusId == null) {
            session.setAttribute("cart", null);
            session.setAttribute("cartTotalQuantity", 0);
            return;
        }

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

            int totalQty = cartDAO.getTotalQuantityByCusId(cusId);
            session.setAttribute("cartTotalQuantity", totalQty);

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

            // Kiểm tra số lượng sản phẩm đã có trong giỏ
            int currentCartQty = 0;
            List<ViewCartCustomer> cartList = cartDAO.getViewCartByCusId(cusId);
            for (ViewCartCustomer item : cartList) {
                if (item.getProId().equals(productId)) {
                    currentCartQty = item.getQuantity();
                    break;
                }
            }
            if (currentCartQty + 1 > stockQuantity) {
                session.setAttribute("error", "Product '" + product.getProName() + "' exceeds stock quantity (" + stockQuantity + ")");
                response.sendRedirect("HomeController");
                return;
            }

            cartDAO.addToCart(cusId, productId, 1);

            cartList = cartDAO.getViewCartByCusId(cusId);

            LinkedHashMap<String, ViewCartCustomer> updatedCart = new LinkedHashMap<>();
            for (ViewCartCustomer item : cartList) {
                updatedCart.put(item.getProId(), item);
            }

            session.setAttribute("cart", updatedCart);
            session.setAttribute("cartSize", updatedCart.size());
            session.setAttribute("message", "Product added to cart!");

            int totalQty = cartDAO.getTotalQuantityByCusId(cusId);
            session.setAttribute("cartTotalQuantity", totalQty);

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

            if (cart == null) {
                session.setAttribute("error", "Không tìm thấy giỏ hàng để cập nhật!");
                response.sendRedirect("cart.jsp");
                return;
            }

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

                if (newQuantity > stock) {
                    session.setAttribute("error", "Product '" + item.getProName() + "' exceeds stock quantity (" + stock + ")");
                } else if (newQuantity > 0) {
                    cartDAO.updateQuantity(item.getCartId(), newQuantity);
                    session.setAttribute("message", "Quantity update successful!");
                } else if (newQuantity <= 0) {
                    cartDAO.deleteCartItem(item.getCartId());
                    session.setAttribute("message", "Product removed!");
                }

                List<ViewCartCustomer> updatedCartList = cartDAO.getViewCartByCusId(cusId);
                LinkedHashMap<String, ViewCartCustomer> updatedCart = new LinkedHashMap<>();
                for (ViewCartCustomer vc : updatedCartList) {
                    updatedCart.put(vc.getProId(), vc);
                }

                session.setAttribute("cart", updatedCart);
                session.setAttribute("cartSize", updatedCart.size());

                int totalQty = cartDAO.getTotalQuantityByCusId(cusId);
                session.setAttribute("cartTotalQuantity", totalQty);

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
        String cusId = (String) session.getAttribute("cusId");

        if (cart != null) {
            try {
                String productId = request.getParameter("productId");
                ViewCartCustomer item = cart.get(productId);

                if (item != null) {
                    CartDAO cartDAO = new CartDAO();
                    cartDAO.deleteCartItem(item.getCartId());
                    cart.remove(productId);
                    session.setAttribute("message", "Product removed from cart!");

                    int totalQty = cartDAO.getTotalQuantityByCusId(cusId);
                    session.setAttribute("cartTotalQuantity", totalQty);
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
            vouchers = voucherDAO.getAllActive();
        } catch (SQLException | ClassNotFoundException ex) {
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
