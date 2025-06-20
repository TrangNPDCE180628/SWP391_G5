package Controllers;

import DAOs.AdminDAO;
import DAOs.CategoryDAO;
import DAOs.CustomerDAO;
import DAOs.OrderDAO;
import DAOs.ProductDAO;
import DAOs.StaffDAO;
import DAOs.DiscountDAO;
import DAOs.VoucherDAO;
import DAOs.ProductSpecDAO;

import Models.Admin;
import Models.Category;
import Models.Customer;
import Models.Order;
import Models.Product;
import Models.Staff;
import Models.Discount;
import Models.Voucher;
import Models.User;
import Models.ProductSpecification;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
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
            if (action == null) {
                loadAdminPage(request, response);
                return;
            }

            switch (action) {

                /* Manage Profile*/
                case "editProfile":
                    editProfile(request, response);
                    break;
                case "addProductType":
                    addProductType(request, response);
                    break;
                case "updateProductType":
                    updateProductType(request, response);
                    break;
                case "deleteProductType":
                    deleteProductType(request, response);
                    break;

                /*Manage Product*/
                case "addProduct":
                    addProduct(request, response);
                    break;
                case "updateProduct":
                    updateProduct(request, response);
                    break;
                case "deleteProduct":
                    deleteProduct(request, response);
                    break;

                /*Manage Customer*/
                case "addUser":
                    addUser(request, response);
                    break;
                case "updateUser":
                    updateUser(request, response);
                    break;
                case "deleteUser":
                    deleteUser(request, response);
                    break;

                /*Manage Discount*/
                case "addDiscount":
                    addDiscount(request, response);
                    break;
                case "updateDiscount":
                    updateDiscount(request, response);
                    break;
                case "deleteDiscount":
                    deleteDiscount(request, response);
                    break;

                /*Manage Voucher*/
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

                /*Manage Product Specification*/
                case "addProductSpec":
                    addProductSpec(request, response);
                    break;
                case "updateProductSpec":
                    updateProductSpec(request, response);
                    break;
                case "deleteProductSpec":
                    deleteProductSpec(request, response);
                    break;
                case "getProductSpec":
                    getProductSpec(request, response);
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
            // Load common data
            CategoryDAO productTypeDAO = new CategoryDAO();
            ProductDAO productDAO = new ProductDAO();
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            DiscountDAO discountDAO = new DiscountDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            ProductSpecDAO productSpecDAO = new ProductSpecDAO();

            List<Category> productTypes = productTypeDAO.getAllCategories();
            List<Product> products = productDAO.getAllProducts();
            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Discount> discounts = discountDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<ProductSpecification> productSpecs = productSpecDAO.getAll();

            request.setAttribute("productTypes", productTypes);
            request.setAttribute("products", products);
            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("discounts", discounts);
            request.setAttribute("vouchers", vouchers);
            request.setAttribute("productSpecs", productSpecs);

            // Load profile info
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser != null) {
                String role = loginUser.getRole();

                if ("Admin".equals(role)) {
                    AdminDAO adminDAO = new AdminDAO();
                    Admin adminProfile = adminDAO.getAdminById(String.valueOf(loginUser.getId()));
                    request.setAttribute("profile", adminProfile);
                } else if ("Staff".equals(role)) {
                    StaffDAO staffDAO2 = new StaffDAO();
                    Staff staffProfile = staffDAO2.getById(String.valueOf(loginUser.getId()));
                    request.setAttribute("profile", staffProfile);
                }
            }
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void editProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String role = request.getParameter("userRole");
            String id = request.getParameter("userId");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            Part imagePart = request.getPart("image");
            String currentImage = request.getParameter("currentImage");

            String imageFileName = "";
            if (imagePart != null && imagePart.getSize() > 0) {
                imageFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                String savePath = getServletContext().getRealPath("/images") + File.separator + imageFileName;
                imagePart.write(savePath);
            } else {
                imageFileName = currentImage;
            }

            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("LOGIN_USER");

            if ("Admin".equals(role)) {
                AdminDAO adminDAO = new AdminDAO();
                Admin admin = adminDAO.getAdminById(id);
                if (admin != null) {
                    admin.setAdminFullName(fullName);
                    admin.setAdminGmail(email);
                    admin.setAdminImage(imageFileName);
                    adminDAO.updateProfileInfo(admin);

                    loginUser.setFullName(fullName);
                    loginUser.setEmail(email);
                    loginUser.setImage(imageFileName);
                    session.setAttribute("LOGIN_USER", loginUser);
                }

            } else if ("Staff".equals(role)) {
                String gender = request.getParameter("gender");
                String phone = request.getParameter("phone");
                String position = request.getParameter("position");

                StaffDAO staffDAO = new StaffDAO();
                Staff staff = staffDAO.getById(id);
                if (staff != null) {
                    staff.setStaffFullName(fullName);
                    staff.setStaffGmail(email);
                    staff.setStaffGender(gender);
                    staff.setStaffPhone(phone);
                    staff.setStaffPosition(position);
                    staff.setStaffImage(imageFileName);
                    staffDAO.updateProfileInfo(staff);

                    loginUser.setFullName(fullName);
                    loginUser.setEmail(email);
                    loginUser.setGender(gender);
                    loginUser.setPhone(phone);
                    loginUser.setImage(imageFileName);
                    session.setAttribute("LOGIN_USER", loginUser);
                }
            }

            loadAdminPage(request, response);

        } catch (Exception e) {
            log("Error in editProfile(): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR", "Lỗi cập nhật hồ sơ: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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

    private void addDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            String discountType = request.getParameter("discountType");
            double discountValue = Double.parseDouble(request.getParameter("discountValue"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
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

            response.sendRedirect("AdminController?tab=discounts");
    } catch (Exception e) {
        throw new ServletException("Error adding discount: " + e.getMessage());
    }
}

    private void updateDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String proId = request.getParameter("proId");
            String discountType = request.getParameter("discountType");
            double discountValue = Double.parseDouble(request.getParameter("discountValue"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
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
            throw new ServletException("Error updating discount: " + e.getMessage());
        }
    }

    private void deleteDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            DiscountDAO dao = new DiscountDAO();
            dao.delete(id);

            response.sendRedirect("AdminController?tab=discounts");
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
            int id = Integer.parseInt(request.getParameter("voucherId"));
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription"); // Optional, null nếu không có
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

    private void addProductSpec(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productId = request.getParameter("productId");
            String cpu = request.getParameter("cpu");
            String ram = request.getParameter("ram");
            String storage = request.getParameter("storage");
            String screen = request.getParameter("screen");
            String os = request.getParameter("os");
            String battery = request.getParameter("battery");
            String camera = request.getParameter("camera");
            String graphic = request.getParameter("graphic");

            ProductSpecification spec = new ProductSpecification(0, productId, cpu, ram, storage, screen, os, battery, camera, graphic);
            ProductSpecDAO dao = new ProductSpecDAO();
            dao.create(spec);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException("Error adding product specification", e);
        }
    }

    private void updateProductSpec(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int specId = Integer.parseInt(request.getParameter("specId"));
            String productId = request.getParameter("productId");
            String cpu = request.getParameter("cpu");
            String ram = request.getParameter("ram");
            String storage = request.getParameter("storage");
            String screen = request.getParameter("screen");
            String os = request.getParameter("os");
            String battery = request.getParameter("battery");
            String camera = request.getParameter("camera");
            String graphic = request.getParameter("graphic");

            ProductSpecification spec = new ProductSpecification(specId, productId, cpu, ram, storage, screen, os, battery, camera, graphic);
            ProductSpecDAO dao = new ProductSpecDAO();
            dao.update(spec);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException("Error updating product specification", e);
        }
    }

    private void deleteProductSpec(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int specId = Integer.parseInt(request.getParameter("specId"));
            ProductSpecDAO dao = new ProductSpecDAO();
            dao.delete(specId);

            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException("Error deleting product specification", e);
        }
    }

    private void getProductSpec(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productId = request.getParameter("productId");
            ProductSpecDAO dao = new ProductSpecDAO();
            ProductSpecification spec = dao.getByProductId(productId);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            if (spec != null) {
                out.print(new Gson().toJson(spec));
            } else {
                out.print("{}");
            }
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching product specification");
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
