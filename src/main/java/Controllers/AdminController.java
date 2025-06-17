package Controllers;

import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.UserDAO;
import DAOs.VoucherDAO;
// UPDATED: Added import for DiscountDAO
import DAOs.DiscountDAO;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.ProductTypes;
import Models.User;
import Models.Voucher;
// UPDATED: Added import for Discount model
import Models.Discount;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
//import java.util.Date; // UPDATED: Added for date handling

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
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
                // UPDATED: Added discount-related actions
                case "addDiscount":
                    addDiscount(request, response);
                    break;
                case "updateDiscount":
                    updateDiscount(request, response);
                    break;
                case "deleteDiscount":
                    deleteDiscount(request, response);
                    break;

                // --- Voucher management ---
                case "addVoucher":
                    addVoucher(request, response);
                    break;
                case "updateVoucher":
                    updateVoucher(request, response);
                    break;
                case "deleteVoucher":
                    deleteVoucher(request, response);
                    break;
                case "getVoucherDetails":
                    getVoucherDetails(request, response);
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
            // NEW: Initialize DiscountDAO and load discounts
            DiscountDAO discountDAO = new DiscountDAO();

            VoucherDAO voucherDAO = new VoucherDAO();

            List<ProductTypes> productTypes = productTypeDAO.getAll();
            List<Product> products = productDAO.getAll();
            List<User> users = userDAO.getAll();
            List<Order> orders = orderDAO.getAll();
            // NEW: Retrieve all discounts
            List<Discount> discounts = discountDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();

            request.setAttribute("productTypes", productTypes);
            request.setAttribute("products", products);
            request.setAttribute("users", users);
            request.setAttribute("orders", orders);
            // NEW: Set discounts attribute for JSP
            request.setAttribute("discounts", discounts);

            request.setAttribute("vouchers", vouchers);

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

            response.sendRedirect(" AdminController");
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
            product.setProStockQuantity(quantity); // sửa lại
            product.setCateId(typeId);             // sửa lại
            product.setProImageMain(fileName != null ? UPLOAD_DIR + "/" + fileName : null); // sửa lại

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
            String proId = request.getParameter("id"); // sửa từ int → String
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            double price = Double.parseDouble(request.getParameter("productPrice"));
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int cateId = Integer.parseInt(request.getParameter("productType")); // sửa tên cho đúng

            // Optional additional fields
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            int warrantyMonths = Integer.parseInt(request.getParameter("warrantyMonths"));
            String model = request.getParameter("model");
            String color = request.getParameter("color");
            double weight = Double.parseDouble(request.getParameter("weight"));
            String dimensions = request.getParameter("dimensions");
            String origin = request.getParameter("origin");
            String material = request.getParameter("material");
            String connectivity = request.getParameter("connectivity");
            int status = Integer.parseInt(request.getParameter("status")); // 1 or 0

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

            // Khởi tạo product và set dữ liệu
            Product product = new Product();
            product.setProId(proId);
            product.setProName(name);
            product.setProDescription(description);
            product.setProPrice(price);
            product.setProStockQuantity(quantity);
            product.setCateId(cateId);
            product.setBrandId(brandId);
            product.setProWarrantyMonths(warrantyMonths);
            product.setProModel(model);
            product.setProColor(color);
            product.setProWeight(weight);
            product.setProDimensions(dimensions);
            product.setProOrigin(origin);
            product.setProMaterial(material);
            product.setProConnectivity(connectivity);
            product.setStatus(status);

            if (fileName != null) {
                product.setProImageMain(fileName);
            }

            // Cập nhật
            ProductDAO dao = new ProductDAO();
            dao.update(product);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("id"); // sửa từ int → String
            ProductDAO dao = new ProductDAO();
            dao.delete(proId);

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
                Product product = productDAO.getById(String.valueOf(detail.getProductId()));

                OrderItem item = new OrderItem();
                item.productName = product.getProName();
                item.quantity = detail.getQuantity();
                item.unitPrice = detail.getUnitPrice();
                item.totalPrice = detail.getTotalPrice();
                responseObj.orderItems.add(item);
            }

            request.setAttribute("orderDetails", responseObj);
            request.getRequestDispatcher("admin-order-detail.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
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

    // UPDATED: Added methods for discount management
    private void addDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            String discountType = request.getParameter("discountType");
            double discountValue = Double.parseDouble(request.getParameter("discountValue"));

            // Parse ngày từ input -> java.util.Date -> java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedStart = sdf.parse(request.getParameter("startDate"));
            java.util.Date parsedEnd = sdf.parse(request.getParameter("endDate"));
            Date startDate = new Date(parsedStart.getTime());
            Date endDate = new Date(parsedEnd.getTime());

            boolean active = Boolean.parseBoolean(request.getParameter("active"));
            String adminId = request.getParameter("adminId");

            Discount discount = new Discount();
            discount.setProId(proId);
            discount.setDiscountType(discountType);
            discount.setDiscountValue(discountValue);
            discount.setStartDate(startDate);
            discount.setEndDate(endDate);
            discount.setActive(active);
            discount.setAdminId(adminId);

            DiscountDAO dao = new DiscountDAO();
            dao.create(discount);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String proId = request.getParameter("proId");
            String discountType = request.getParameter("discountType");
            double discountValue = Double.parseDouble(request.getParameter("discountValue"));

            // Parse java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedStart = sdf.parse(request.getParameter("startDate"));
            java.util.Date parsedEnd = sdf.parse(request.getParameter("endDate"));

            // Convert sang java.sql.Date
            Date startDate = new Date(parsedStart.getTime());
            Date endDate = new Date(parsedEnd.getTime());

            boolean active = Boolean.parseBoolean(request.getParameter("active"));
            String adminId = request.getParameter("adminId");

            Discount discount = new Discount();
            discount.setDiscountId(id);
            discount.setProId(proId);
            discount.setDiscountType(discountType);
            discount.setDiscountValue(discountValue);
            discount.setStartDate(startDate);
            discount.setEndDate(endDate);
            discount.setActive(active);
            discount.setAdminId(adminId);

            DiscountDAO dao = new DiscountDAO();
            dao.update(discount);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            DiscountDAO dao = new DiscountDAO();
            dao.delete(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            Voucher voucher = new Voucher(0, codeName, description, discountType,
                    discountValue, minOrderAmount, startDate, endDate, isActive);

            VoucherDAO dao = new VoucherDAO();
            dao.create(voucher);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            Voucher voucher = new Voucher(id, codeName, description, discountType,
                    discountValue, minOrderAmount, startDate, endDate, isActive);

            VoucherDAO dao = new VoucherDAO();
            dao.update(voucher);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VoucherDAO dao = new VoucherDAO();
            dao.delete(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void getVoucherDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VoucherDAO dao = new VoucherDAO();
            Voucher voucher = dao.getById(id);

            if (voucher == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Voucher not found");
                return;
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(voucher));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching voucher: " + e.getMessage());
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
