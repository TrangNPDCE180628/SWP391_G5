package Controllers;

import DAOs.CategoryDAO;
import DAOs.CustomerDAO;
import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductDAO;
import DAOs.StaffDAO;
import Models.Category;
import Models.Customer;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.Staff;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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
                case "viewOrders":
                    viewOrders(request, response);
                    break;
                case "updateOrderStatus":
                    updateOrderStatus(request, response);
                    break;
                case "filterOrders":
                    filterOrders(request, response);
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
            CategoryDAO productTypeDAO = new CategoryDAO();
            ProductDAO productDAO = new ProductDAO();
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            OrderDAO orderDAO = new OrderDAO();

            List<Category> productTypes = productTypeDAO.getAllCategories();
            List<Product> products = productDAO.getAllProducts();
            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Order> orders = orderDAO.getAll();

            request.setAttribute("productTypes", productTypes);
            request.setAttribute("products", products);
            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("orders", orders);

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void viewOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.getAll();
            request.setAttribute("orders", orders);
            request.setAttribute("activeTab", "orders"); // NEW: Set active tab
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // UPDATED: Forward to admin.jsp instead of redirecting
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            String newStatus = request.getParameter("status");
            String currentStatus = request.getParameter("currentStatus"); // NEW: Get current filter status
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.updateOrderStatus(orderId, newStatus);

            // Reload orders with the current filter
            List<Order> orders;
            if (currentStatus == null || currentStatus.isEmpty() || currentStatus.equals("All")) {
                orders = orderDAO.getAll();
            } else {
                orders = orderDAO.getByStatus(currentStatus);
            }
            request.setAttribute("orders", orders);
            request.setAttribute("selectedStatus", currentStatus);
            request.setAttribute("activeTab", "orders"); // NEW: Set active tab
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // UPDATED: Set activeTab attribute
    private void filterOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders;
            if (status == null || status.isEmpty() || status.equals("All")) {
                orders = orderDAO.getAll();
            } else {
                orders = orderDAO.getByStatus(status);
            }
            request.setAttribute("orders", orders);
            request.setAttribute("selectedStatus", status);
            request.setAttribute("activeTab", "orders"); // NEW: Set active tab
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            CustomerDAO cusDAO = new CustomerDAO();

            Order order = orderDAO.getById(orderId);
            List<OrderDetail> orderDetails = orderDetailDAO.getByOrderId(orderId);
            Customer customer = cusDAO.getCustomerById(order.getCusId());

            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.setAttribute("customer", customer);

            request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("typeName");
            Category productType = new Category();
            productType.setCateName(name);

            CategoryDAO dao = new CategoryDAO();
            dao.insertCategory(productType);

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

            Category productType = new Category(id, name);
            CategoryDAO dao = new CategoryDAO();
            dao.updateCategory(productType);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CategoryDAO dao = new CategoryDAO();
            dao.deleteCategory(id);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("productId");
            int cateId = Integer.parseInt(request.getParameter("categoryId"));
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            BigDecimal price = new BigDecimal(request.getParameter("productPrice"));
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int warranty = Integer.parseInt(request.getParameter("warrantyMonths"));
            String model = request.getParameter("productModel");
            String color = request.getParameter("productColor");
            BigDecimal weight = new BigDecimal(request.getParameter("productWeight"));
            String dimensions = request.getParameter("productDimensions");
            String origin = request.getParameter("productOrigin");
            String material = request.getParameter("productMaterial");
            String connectivity = request.getParameter("productConnectivity");
            int status = Integer.parseInt(request.getParameter("productStatus"));

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
            product.setProId(proId);
            product.setCateId(cateId);
            product.setBrandId(brandId);
            product.setProName(name);
            product.setProDescription(description);
            product.setProPrice(price);
            product.setProStockQuantity(quantity);
            product.setProWarrantyMonths(warranty);
            product.setProModel(model);
            product.setProColor(color);
            product.setProWeight(weight);
            product.setProDimensions(dimensions);
            product.setProOrigin(origin);
            product.setProMaterial(material);
            product.setProConnectivity(connectivity);
            product.setProImageMain(fileName != null ? UPLOAD_DIR + "/" + fileName : null);
            product.setStatus(status);

            ProductDAO dao = new ProductDAO();
            dao.insertProduct(product);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("productId");
            int cateId = Integer.parseInt(request.getParameter("categoryId"));
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            BigDecimal price = new BigDecimal(request.getParameter("productPrice"));
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int warranty = Integer.parseInt(request.getParameter("warrantyMonths"));
            String model = request.getParameter("productModel");
            String color = request.getParameter("productColor");
            BigDecimal weight = new BigDecimal(request.getParameter("productWeight"));
            String dimensions = request.getParameter("productDimensions");
            String origin = request.getParameter("productOrigin");
            String material = request.getParameter("productMaterial");
            String connectivity = request.getParameter("productConnectivity");
            int status = Integer.parseInt(request.getParameter("productStatus"));

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
            product.setProId(proId);
            product.setCateId(cateId);
            product.setBrandId(brandId);
            product.setProName(name);
            product.setProDescription(description);
            product.setProPrice(price);
            product.setProStockQuantity(quantity);
            product.setProWarrantyMonths(warranty);
            product.setProModel(model);
            product.setProColor(color);
            product.setProWeight(weight);
            product.setProDimensions(dimensions);
            product.setProOrigin(origin);
            product.setProMaterial(material);
            product.setProConnectivity(connectivity);
            if (fileName != null) {
                product.setProImageMain(UPLOAD_DIR + "/" + fileName);
            }
            product.setStatus(status);

            ProductDAO dao = new ProductDAO();
            dao.updateProduct(product);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            ProductDAO dao = new ProductDAO();
            dao.deleteProduct(id);

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
            String gender = request.getParameter("gender");
            String image = request.getParameter("image");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");

            if ("customer".equalsIgnoreCase(role)) {
                Customer customer = new Customer();
                customer.setUsername(username);
                customer.setCusPassword(password);
                customer.setCusFullName(fullname);
                customer.setCusGender(gender);
                customer.setCusImage(image);
                customer.setCusGmail(gmail);
                customer.setCusPhone(phone);

                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.insertCustomer(customer);

            } else if ("staff".equalsIgnoreCase(role)) {
                Staff staff = new Staff();
                staff.setStaffName(username);
                staff.setStaffPassword(password);
                staff.setStaffFullName(fullname);
                staff.setStaffGender(gender);
                staff.setStaffImage(image);
                staff.setStaffGmail(gmail);
                staff.setStaffPhone(phone);
                staff.setStaffPosition("staff");

                StaffDAO staffDAO = new StaffDAO();
                staffDAO.create(staff);
            }

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String role = request.getParameter("role");

            if ("customer".equalsIgnoreCase(role)) {
                Customer customer = new Customer();
                customer.setCusId(id);
                customer.setUsername(username);
                customer.setCusPassword(password);
                customer.setCusFullName(fullname);
                customer.setCusGender(gender);

                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.updateCustomer(customer);
            } else if ("staff".equalsIgnoreCase(role)) {
                Staff staff = new Staff();
                staff.setStaffId(id);
                staff.setStaffName(username);
                staff.setStaffPassword(password);
                staff.setStaffFullName(fullname);
                staff.setStaffGender(gender);

                StaffDAO staffDAO = new StaffDAO();
                staffDAO.update(staff);
            }

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            String role = request.getParameter("role");

            if ("customer".equalsIgnoreCase(role)) {
                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.deleteCustomer(id);
            } else if ("staff".equalsIgnoreCase(role)) {
                StaffDAO staffDAO = new StaffDAO();
                staffDAO.delete(id);
            }

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