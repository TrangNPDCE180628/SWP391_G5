package Controllers;

import DAOs.AdminDAO;
import DAOs.AttributeDAO;
import DAOs.CustomerDAO;
import DAOs.FeedbackDAO;
import DAOs.FeedbackReplyViewDAO;
import DAOs.OrderDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductAttributeDAO;
import DAOs.ProductDAO;
import DAOs.ReplyFeedbackDAO;
import DAOs.StaffDAO;
import DAOs.VoucherDAO;
import DAOs.ProductTypeDAO;

import Models.Admin;
import Models.Attribute;
import Models.Customer;
import Models.Feedback;
import Models.FeedbackReplyView;
import Models.Order;
import Models.OrderDetail;
import Models.Product;
import Models.ProductAttribute;
import Models.ReplyFeedback;
import Models.Staff;
import Models.Voucher;
import Models.User;
import Models.ProductTypes;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class AdminController extends HttpServlet {

    private static final String UPLOAD_DIR = "images/products";
    private static final String UPLOAD_DIR_STAFF = "images/staff";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            if (action == null) {
                loadAdminPage(request, response);
                return;
            }

            switch (action) {
                case "loadOrders":
                    loadOrdersPage(request, response);
                    break;
                case "updateOrderStatus":
                    updateOrderStatus(request, response);
                    break;
                case "deleteOrder":
                    deleteOrder(request, response);
                    break;
                case "viewOrderDetails":
                    viewOrderDetails(request, response);
                    break;
                /* Manage Profile */
                case "editProfile":
                    editProfile(request, response);
                    break;
                /* Product Type */
                case "addProductType":
                    addProductType(request, response);
                    break;
                case "updateProductType":
                    updateProductType(request, response);
                    break;
                case "deleteProductType":
                    deleteProductType(request, response);
                    break;
                /* Manage Voucher */
                case "addVoucher":
                    addVoucher(request, response);
                    break;
                case "updateVoucherURE":
                    updateVoucher(request, response);
                    break;
                case "deleteVoucher":
                    deleteVoucher(request, response);
                    break;
                case "getVoucherDetails":
                    getVoucherDetails(request, response);
                    break;
                /* Manage Staff */
                case "addStaff":
                    addStaff(request, response);
                    break;
                case "editStaff":
                    edit1Staff(request, response);
                    break;
                case "deleteStaff":
                    deleteStaff(request, response);
                    break;
                /* Manage Feedback */
                case "replyFeedback":
                    replyFeedback(request, response);
                    break;
                // ✅ FIXED: FILTER ORDER BY STATUS
                case "filterOrdersByStatus":
                    String statusFilter = request.getParameter("status");
                    List<Order> filteredOrders = filterOrdersByStatus(statusFilter);

                    // ✅ Dùng cùng tên với loadOrdersPage
                    request.setAttribute("orderList", filteredOrders);
                    request.setAttribute("statusFilter", statusFilter);
                    request.setAttribute("activeTab", "orders");
                    request.getRequestDispatcher("admin.jsp").forward(request, response);
                    break;

                /*Manage Feedback*/
                case "addAttribute":
                    addAttribute(request, response);
                    return;
                case "updateAttribute":
                    updateAttribute(request, response);
                    return;
                case "deleteAttribute":
                    deleteAttribute(request, response);
                    return;
                case "addProductAttribute":
                    addProductAttribute(request, response);
                    return;
                case "updateProductAttribute":
                    updateProductAttribute(request, response);
                    return;
                case "deleteProductAttribute":
                    deleteProductAttribute(request, response);
                    return;
                case "viewProductAttribute": {
                    String proId = request.getParameter("proId");
                    int attributeId = Integer.parseInt(request.getParameter("attributeId"));

                    ProductAttributeDAO dao = new ProductAttributeDAO();
                    ProductAttribute pa = dao.getByProductIdAndAttributeId(proId, attributeId);

                    Gson gson = new Gson();
                    String json = gson.toJson(pa);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                    return; // Kết thúc ở đây vì không cần forward
                }

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
            String activeTab = request.getParameter("tab");
            request.setAttribute("activeTab", activeTab);
            // Load common data
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            FeedbackReplyViewDAO viewfeedbackDAO = new FeedbackReplyViewDAO();
            OrderDAO orderDAO = new OrderDAO();
            AttributeDAO attributeDAO = new AttributeDAO();
            ProductAttributeDAO paDAO = new ProductAttributeDAO();

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<ProductTypes> productTypes = productTypeDAO.getAllProductTypes();
            List<Feedback> feedbacks = feedbackDAO.getAllFeedbacks();
            List<FeedbackReplyView> viewFeedbacks = viewfeedbackDAO.getAllFeedbackReplies();
            List<Order> orders = orderDAO.getAll();

            // Fetch customer names for each order
            for (Order order : orders) {
                Customer customer = cusDAO.getCustomerById(order.getUserId());
                order.setCustomerName(customer != null ? customer.getCusFullName() : "Unknown");
            }
            List<Attribute> attributes = attributeDAO.getAll();
            List<ProductAttribute> productAttributes = paDAO.getAll();

            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("vouchers", vouchers);
            request.setAttribute("productTypes", productTypes);
            request.setAttribute("feedbacks", feedbacks);
            request.setAttribute("viewFeedbacks", viewFeedbacks);
            request.setAttribute("orders", orders);
            request.setAttribute("attributes", attributes);
            request.setAttribute("productAttributes", productAttributes);
            
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

    private void loadOrdersPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String statusFilter = request.getParameter("statusFilter"); // <-- Lấy filter từ query
            List<Order> orders = filterOrdersByStatus(statusFilter);   // <-- Gọi hàm lọc đã có
            request.setAttribute("orderList", orders);
            request.setAttribute("statusFilter", statusFilter);        // <-- Gửi lại filter
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error loading orders: " + e.toString());
            request.setAttribute("ERROR", "Failed to load orders: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Filters orders by the given status, or returns all if status is
     * invalid/null.
     */
    private List<Order> filterOrdersByStatus(String statusFilter)
            throws SQLException, ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();

        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            String trimmed = statusFilter.trim();
            if (trimmed.equals("Done") || trimmed.equals("Pending") || trimmed.equals("Cancel")) {
                return orderDAO.getByStatus(trimmed);
            } else if (trimmed.equals("All")) {
                return orderDAO.getAll(); // Trả về toàn bộ khi chọn All
            }
        }

        return orderDAO.getAll(); // Trả về toàn bộ nếu null hoặc rỗng
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        String orderId = request.getParameter("orderId");
        String status = request.getParameter("status");

        // Validate status
        if (status != null && (status.equals("Done") || status.equals("Pending") || status.equals("Cancel"))) {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.updateOrderStatus(orderId, status);
        }

        response.sendRedirect("AdminController?action=loadOrders&tab=orders");
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        String orderId = request.getParameter("orderId");
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

        // Delete order details first due to foreign key constraint
        orderDetailDAO.deleteByOrderId(orderId);
        orderDAO.delete(orderId);

        response.sendRedirect("AdminController?action=loadOrders&tab=orders");
    }

    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        String orderId = request.getParameter("orderId");
        if (orderId == null || orderId.trim().isEmpty()) {
            request.setAttribute("ERROR", "Invalid Order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        ProductDAO productDAO = new ProductDAO();

        Order order = orderDAO.getById(orderId);
        if (order == null) {
            request.setAttribute("ERROR", "Order not found");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        List<OrderDetail> orderDetails = orderDetailDAO.getByOrderId(orderId);
        Customer customer = customerDAO.getCustomerById(order.getUserId());

        // Fetch product names for each order detail
        for (OrderDetail detail : orderDetails) {
            Product product = productDAO.getProductById(detail.getProductId());
            detail.setProductName(product != null ? product.getProName() : "Unknown");
        }

        // Set attributes for JSP
        request.setAttribute("order", order);
        request.setAttribute("customer", customer != null ? customer : new Customer()); // Avoid null customer
        request.setAttribute("orderDetails", orderDetails != null ? orderDetails : new ArrayList<>());
        request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
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

            response.sendRedirect("AdminController?tab=profile");
        } catch (Exception e) {
            log("Error in editProfile(): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR", "Lỗi cập nhật hồ sơ: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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

    private void addProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("typeName");
            if (name != null && !name.trim().isEmpty()) {
                ProductTypeDAO dao = new ProductTypeDAO();
                ProductTypes type = new ProductTypes();
                type.setName(name);
                dao.addProductType(type);
            }
            response.sendRedirect("AdminController?tab=productTypes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("typeId"));
            String name = request.getParameter("typeName");
            if (name != null && !name.trim().isEmpty()) {
                ProductTypeDAO dao = new ProductTypeDAO();
                ProductTypes type = new ProductTypes();
                type.setId(id);
                type.setName(name);
                dao.updateProductType(type);
            }
            response.sendRedirect("AdminController?tab=productTypes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductTypeDAO dao = new ProductTypeDAO();
            dao.deleteProductType(id);
            response.sendRedirect("AdminController?tab=productTypes");
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

            response.sendRedirect("AdminController?tab=vouchers");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("voucherId"));
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

            response.sendRedirect("AdminController?tab=vouchers");
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

            response.sendRedirect("AdminController?tab=vouchers");
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

    private void addStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");

            Part filePart = request.getPart("image");
            String fileName = "";

            if (filePart != null && filePart.getSize() > 0) {
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/") + "images" + File.separator + "staff";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
            } else {
                fileName = "default.jpg";
            }

            String position = request.getParameter("position");
            Staff staff = new Staff();
            staff.setStaffId(id);
            staff.setStaffName(username);
            staff.setStaffPassword(password);
            staff.setStaffFullName(fullname);
            staff.setStaffGender(gender);
            staff.setStaffImage(fileName);
            staff.setStaffGmail(gmail);
            staff.setStaffPhone(phone);
            staff.setStaffPosition(position);

            StaffDAO staffDAO = new StaffDAO();
            staffDAO.create(staff);
            response.sendRedirect("AdminController");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void edit1Staff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");
            String position = request.getParameter("position");

            // Xử lý file ảnh
            Part imagePart = request.getPart("image");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

            String imageFileName = null;

            if (fileName != null && !fileName.trim().isEmpty()) {
                // Có upload ảnh mới
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR_STAFF;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                imagePart.write(uploadPath + File.separator + fileName);
                imageFileName = fileName;
            } else {
                // Không có ảnh mới => giữ ảnh cũ từ database
                StaffDAO dao = new StaffDAO();
                Staff existing = dao.getById(id);
                imageFileName = existing.getStaffImage();
            }
            // Cập nhật dữ liệu
            Staff updatedStaff = new Staff(id, username, fullname, password,
                    gender, imageFileName, gmail, phone, position);

            StaffDAO dao = new StaffDAO();
            dao.update(updatedStaff);
            response.sendRedirect("AdminController?tab=staff");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String staffId = request.getParameter("id");
            if (staffId != null && !staffId.trim().isEmpty()) {
                StaffDAO staffDAO = new StaffDAO();
                staffDAO.delete(staffId);
            }
            response.sendRedirect("AdminController");
        } catch (Exception e) {
            request.setAttribute("error", "Error while deleting staff: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void replyFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int feedbackId = Integer.parseInt(request.getParameter("feedbackId"));
            String cusId = request.getParameter("cusId");
            String staffId = request.getParameter("staffId");
            String contentReply = request.getParameter("contentReply");

            if (staffId == null || staffId.trim().isEmpty() || contentReply == null || contentReply.trim().isEmpty()) {
                request.setAttribute("ERROR", "Missing required reply data.");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            }

            ReplyFeedbackDAO dao = new ReplyFeedbackDAO();
            ReplyFeedback reply = new ReplyFeedback();
            reply.setFeedbackId(feedbackId);
            reply.setCusId(cusId);
            reply.setStaffId(staffId);
            reply.setContentReply(contentReply);

            dao.insertReplyFeedback(reply);

            loadAdminPage(request, response);
        } catch (Exception e) {
            log("Error in replyFeedback: " + e.toString());
            request.setAttribute("ERROR", "Failed to reply feedback.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // == ATTRIBUTE ==
    private void addAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("attributeName");
            String unit = request.getParameter("unit");
            int proTypeId = Integer.parseInt(request.getParameter("proTypeId"));
            new AttributeDAO().create(new Attribute(0, name, unit, proTypeId));
            response.sendRedirect("AdminController?tab=attributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("attributeId"));
            String name = request.getParameter("attributeName");
            String unit = request.getParameter("unit");
            int proTypeId = Integer.parseInt(request.getParameter("proTypeId"));
            new AttributeDAO().update(new Attribute(id, name, unit, proTypeId));
            response.sendRedirect("AdminController?tab=attributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("attributeId"));
            new AttributeDAO().delete(id);
            response.sendRedirect("AdminController?tab=attributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

// == PRODUCT ATTRIBUTE ==
    private void addProductAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int attributeId = Integer.parseInt(request.getParameter("attributeId"));
            String value = request.getParameter("value");
            new ProductAttributeDAO().create(new ProductAttribute(proId, attributeId, value));
            response.sendRedirect("AdminController?tab=productAttributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProductAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int attributeId = Integer.parseInt(request.getParameter("attributeId"));
            String value = request.getParameter("value");
            new ProductAttributeDAO().update(new ProductAttribute(proId, attributeId, value));
            response.sendRedirect("AdminController?tab=productAttributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProductAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int attributeId = Integer.parseInt(request.getParameter("attributeId"));
            new ProductAttributeDAO().delete(proId, attributeId);
            response.sendRedirect("AdminController?tab=productAttributes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void filterProductAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String filterProductId = request.getParameter("filterProductId");
            String filterAttributeName = request.getParameter("filterAttributeName");
            String filterValue = request.getParameter("filterValue");

            ProductAttributeDAO paDAO = new ProductAttributeDAO();

            List<ProductAttribute> productAttributes = paDAO.getAll();

            if (filterProductId != null && !filterProductId.trim().isEmpty()) {
                productAttributes.removeIf(pa -> !pa.getProId().toLowerCase().contains(filterProductId.toLowerCase()));
            }
            if (filterAttributeName != null && !filterAttributeName.trim().isEmpty()) {
                productAttributes.removeIf(pa -> pa.getAttributeName() == null || !pa.getAttributeName().toLowerCase().contains(filterAttributeName.toLowerCase()));
            }
            if (filterValue != null && !filterValue.trim().isEmpty()) {
                productAttributes.removeIf(pa -> pa.getValue() == null || !pa.getValue().toLowerCase().contains(filterValue.toLowerCase()));
            }

            request.setAttribute("productAttributes", productAttributes);
            request.setAttribute("activeTab", "attributes");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in filterProductAttribute: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to filter attributes: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void viewProductAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int attrId = Integer.parseInt(request.getParameter("attributeId"));

            ProductAttributeDAO dao = new ProductAttributeDAO();
            ProductAttribute pa = dao.getByProductIdAndAttributeId(proId, attrId);

            request.setAttribute("viewedProductAttribute", pa);
            request.setAttribute("activeTab", "productAttributes");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in viewProductAttribute: " + e.getMessage());
            request.setAttribute("ERROR", "Không thể xem chi tiết thuộc tính sản phẩm.");
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
