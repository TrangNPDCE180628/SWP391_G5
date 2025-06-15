package Controllers;

import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.UserDAO;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.ProductTypes;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class AdminController extends HttpServlet {

    private static final String UPLOAD_DIR = "images/products";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        
        try {
            // If action is null, load admin page by default
            if (action == null) {
                loadAdminPage(request, response);
                return;
            }
            
            switch (action) {
                case "addProductType":
                    addProductType(request, response);
                    break;
                case "updateProductType":
                    updateProductType(request, response);
                    break;
                case "deleteProductType":
                    deleteProductType(request, response);
                    break;
                case "addProduct":
                    addProduct(request, response);
                    break;
                case "updateProduct":
                    updateProduct(request, response);
                    break;
                case "deleteProduct":
                    deleteProduct(request, response);
                    break;
                case "addUser":
                    addUser(request, response);
                    break;
                case "updateUser":
                    updateUser(request, response);
                    break;
                case "deleteUser":
                    deleteUser(request, response);
                    break;
                case "getOrderDetails":
                    getOrderDetails(request, response);
                    break;
                case "updateOrderStatus":
                    updateOrderStatus(request, response);
                    break;
                case "viewOrderDetails":
                    viewOrderDetails(request, response);
                    break;
                default:
                    loadAdminPage(request, response);
            }


        } catch (Exception e) {
            log("Error at AdminController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void loadAdminPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load all data needed for the admin page
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            ProductDAO productDAO = new ProductDAO();
            UserDAO userDAO = new UserDAO();
            OrderDAO orderDAO = new OrderDAO();

            List<ProductTypes> productTypes = productTypeDAO.getAll();
            List<Product> products = productDAO.getAll();
            List<User> users = userDAO.getAll();
            List<Order> orders = orderDAO.getAll();

            request.setAttribute("productTypes", productTypes);
            request.setAttribute("products", products);
            request.setAttribute("users", users);
            request.setAttribute("orders", orders);

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("typeName");
            ProductTypes productType = new ProductTypes();
            productType.setName(name);

            ProductTypeDAO dao = new ProductTypeDAO();
            dao.create(productType);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("typeName");

            ProductTypes productType = new ProductTypes(id, name);
            ProductTypeDAO dao = new ProductTypeDAO();
            dao.update(productType);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductTypeDAO dao = new ProductTypeDAO();
            dao.delete(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            double price = Double.parseDouble(request.getParameter("productPrice"));
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int typeId = Integer.parseInt(request.getParameter("productType"));

            // Handle file upload
            Part filePart = request.getPart("productImage");
            String fileName = null;
            if (filePart != null && filePart.getSize() > 0) {
                fileName = getFileName(filePart);
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
            }

            Product product = new Product();
            product.setProName(name);
            product.setProDescription(description);
            product.setProPrice(price);
            product.setProQuantity(quantity);
            product.setProTypeId(typeId);
            product.setProImage(fileName != null ? UPLOAD_DIR + "/" + fileName : null);

            ProductDAO dao = new ProductDAO();
            dao.create(product);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            double price = Double.parseDouble(request.getParameter("productPrice"));
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int typeId = Integer.parseInt(request.getParameter("productType"));

            // Handle file upload
            Part filePart = request.getPart("productImage");
            String fileName = null;
            if (filePart != null && filePart.getSize() > 0) {
                fileName = getFileName(filePart);
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
            }

            Product product = new Product();
            product.setProId(id);
            product.setProName(name);
            product.setProDescription(description);
            product.setProPrice(price);
            product.setProQuantity(quantity);
            product.setProTypeId(typeId);
            
            

            ProductDAO dao = new ProductDAO();
            
            if (fileName != null) {
                product.setProImage(fileName);
            }
            
            dao.update(product);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductDAO dao = new ProductDAO();
            dao.delete(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String role = request.getParameter("role");

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setRole(role);

            UserDAO dao = new UserDAO();
            dao.create(user);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String role = request.getParameter("role");

            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setRole(role);

            UserDAO dao = new UserDAO();
            dao.update(user);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            UserDAO dao = new UserDAO();
            dao.delete(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private void getOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            UserDAO userDAO = new UserDAO();
            ProductDAO productDAO = new ProductDAO();

            Order order = orderDAO.getById(orderId);
            User customer = userDAO.getById(order.getUserId());
            List<OrderDetail> orderDetails = orderDetailDAO.getByOrderId(orderId);

            // Create a response object with all the necessary information
            OrderDetailsResponse responseObj = new OrderDetailsResponse();
            responseObj.order = order;
            responseObj.customerName = customer.getFullname();
            responseObj.orderItems = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                Product product = productDAO.getById(detail.getProductId());
                OrderItem item = new OrderItem();
                item.productName = product.getProName();
                item.quantity = detail.getQuantity();
                item.unitPrice = detail.getUnitPrice();
                item.totalPrice = detail.getTotalPrice();
                responseObj.orderItems.add(item);
            }

            // Convert to JSON and send response
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(responseObj));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching order details: " + e.getMessage());
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            System.out.println("Received order ID parameter: " + idParam);

            if (idParam == null || idParam.trim().isEmpty()) {
                System.out.println("Order ID is missing or empty.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Order ID is missing or invalid.");
                return;
            }

            int orderId = Integer.parseInt(idParam);
            String newStatus = request.getParameter("status");
            System.out.println("Parsed orderId: " + orderId + ", newStatus: " + newStatus);

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getById(orderId);

            if (order == null) {
                System.out.println("Order not found for ID: " + orderId);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Order not found.");
                return;
            }

            System.out.println("Current order status: " + order.getStatus());

            // Validate status transition
            if (order.getStatus().equals("completed") && newStatus.equals("pending")) {
                System.out.println("Invalid transition: completed -> pending");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Cannot change a completed order back to pending");
                return;
            }

            if (order.getStatus().equals("cancelled") && !newStatus.equals("cancelled")) {
                System.out.println("Invalid transition: cancelled -> " + newStatus);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Cannot change a cancelled order's status");
                return;
            }

            orderDAO.updateOrderStatus(orderId, newStatus);
            System.out.println("Order status updated successfully for Order ID: " + orderId + " to status: " + newStatus);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating order status: " + e.getMessage());
        }
    }

    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            UserDAO userDAO = new UserDAO();
            ProductDAO productDAO = new ProductDAO();

            Order order = orderDAO.getById(orderId);
            User customer = userDAO.getById(order.getUserId());
            List<OrderDetail> orderDetails = orderDetailDAO.getByOrderId(orderId);

            request.setAttribute("order", order);
            request.setAttribute("customer", customer);
            request.setAttribute("orderDetails", orderDetails);

            request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("ERROR", "Error loading order details: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Helper classes for JSON response
    private static class OrderDetailsResponse {
        Order order;
        String customerName;
        List<OrderItem> orderItems;
    }

    private static class OrderItem {
        String productName;
        int quantity;
        double unitPrice;
        double totalPrice;
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