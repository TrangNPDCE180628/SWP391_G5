package Controllers;

import DAOs.AdminDAO;
import DAOs.CustomerDAO;
import DAOs.FeedbackDAO;
import DAOs.FeedbackReplyViewDAO;
import DAOs.OrderDAO;
import DAOs.AttributeDAO;
import DAOs.OrderDetailDAO;
import DAOs.ProductAttributeDAO;
import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.ReplyFeedbackDAO;
import DAOs.StaffDAO;
import DAOs.StockDAO;
import DAOs.VoucherDAO;
import DAOs.ViewProductAttributeDAO;

import Models.Admin;
import Models.Customer;
import Models.Feedback;
import Models.FeedbackReplyView;
import Models.Order;
import Models.Attribute;
import Models.OrderDetail;
import Models.Product;
import Models.ProductAttribute;
import Models.ProductTypes;
import Models.ReplyFeedback;
import Models.Staff;
import Models.Stock;
import Models.Voucher;
import Models.User;
import Models.ViewProductAttribute;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;
import Ultis.DBContext;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.sql.Connection;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class AdminController extends BaseController {

    private static final String UPLOAD_DIR = "images/products";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // Check authentication manually first
        HttpSession session = request.getSession(false);
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute("LOGIN_USER");
        }
        
        // If not authenticated, store current URL and redirect to login
        if (loginUser == null) {
            String originalURL = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                originalURL += "?" + queryString;
            }
            
            // Create session to store redirect URL
            session = request.getSession(true);
            session.setAttribute("REDIRECT_URL", originalURL);
            
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check if user has admin/staff role
        String role = loginUser.getRole();
        if (!"Admin".equals(role) && !"Staff".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/HomeController?error=access_denied");
            return;
        }
        
        String action = request.getParameter("action");

        try {
            if (action == null) {
                loadAdminPage(request, response);
                return;
            }

            switch (action) {
                case "editProfile":
                    editProfile(request, response);
                    break;
                case "addStaff":
                    addStaff(request, response);
                    break;
                case "editStaff":
                    editStaff(request, response);
                    break;
                case "deleteStaff":
                    deleteStaff(request, response);
                    break;
                case "replyFeedback":
                    replyFeedback(request, response);
                    break;
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
                case "filterProductAttribute":
                    filterProductAttribute(request, response);
                    return;
                case "updateOrderStatus":
                    updateOrderStatus(request, response);
                    return;
                case "filterOrders":
                    filterOrdersByStatus(request, response);
                    return;
                case "deleteOrder":
                    deleteOrder(request, response);
                    return;
                case "goToOrderDetailPage":
                    goToOrderDetailPage(request, response);
                    return;
                case "viewStockList":
                    viewStockList(request, response);
                    return;
                case "addStockQuantity":
                    addStockQuantity(request, response);
                    return;
                case "deleteStock":
                    deleteStock(request, response);
                    return;
                case "createStock":
                    createStock(request, response);
                    return;
                case "updateStockQuantity":
                    updateStockQuantity(request, response);
                    return;
                case "RevenueByMonth":
                    handleRevenueByMonth(request, response);
                    return;
                case "addProductType":
                    addProductType(request, response);
                    break;
                case "updateProductType":
                    updateProductType(request, response);
                    break;
                case "deleteProductType":
                    deleteProductType(request, response);
                    break;
                default:
                    loadAdminPage(request, response);
            }
        } catch (Exception e) {
            log("Error at AdminController: " + e.toString());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "An error occurred: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                log("Response was already committed. Cannot forward to error.jsp");
            }
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
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            FeedbackReplyViewDAO viewfeedbackDAO = new FeedbackReplyViewDAO();
            AttributeDAO attributeDAO = new AttributeDAO();
            ProductAttributeDAO paDAO = new ProductAttributeDAO();
            OrderDAO ordDao = new OrderDAO();
            StockDAO stockDAO = new StockDAO();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<Feedback> feedbacks = feedbackDAO.getAllFeedbacks();
            List<FeedbackReplyView> viewFeedbacks = viewfeedbackDAO.getAllFeedbackReplies();
            List<Attribute> attributes = attributeDAO.getAll();
            List<ProductAttribute> productAttributes = paDAO.getAll();
            List<Order> orders = ordDao.getAll();
            List<Stock> stocks = stockDAO.getAllStocks();
            BigDecimal totalRevenue = ordDao.getTotalRevenue();
            List<ProductTypes> productTypes = productTypeDAO.getAllProductTypes();

            // === NEW: Load từ VIEW ===
            Connection conn = DBContext.getConnection();
            ViewProductAttributeDAO viewProductAttributeDAO = new ViewProductAttributeDAO(conn);
            List<ViewProductAttribute> viewProductAttributes = viewProductAttributeDAO.getAll();
            request.setAttribute("viewProductAttributes", viewProductAttributes);

            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("vouchers", vouchers);
            request.setAttribute("feedbacks", feedbacks);
            request.setAttribute("viewFeedbacks", viewFeedbacks);
            request.setAttribute("attributes", attributes);
            request.setAttribute("productAttributes", productAttributes);
            request.setAttribute("orders", orders);
            request.setAttribute("stocks", stocks);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("productTypes", productTypes);

            // 1. Lấy danh sách tất cả đơn hàng
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            // 2. Tạo map: orderId -> list of OrderDetail
            Map<Integer, List<OrderDetail>> orderDetailsMap = new HashMap<>();
            for (Order o : orders) {
                List<OrderDetail> details = detailDAO.getByOrderId(o.getOrderId());
                orderDetailsMap.put(o.getOrderId(), details);
            }
            request.setAttribute("orderDetailsMap", orderDetailsMap);

            // Doanh thu theo tháng trong năm hiện tại
            int currentYear = LocalDate.now().getYear();
            Map<String, BigDecimal> monthlyRevenue = ordDao.getMonthlyRevenueInYear(currentYear);
            request.setAttribute("monthlyRevenueMap", monthlyRevenue);
            request.setAttribute("revenueYear", currentYear);

            System.out.println(monthlyRevenue);

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

            response.sendRedirect("AdminController?tab=profile");
        } catch (Exception e) {
            log("Error in editProfile(): " + e.getMessage());
            e.printStackTrace();
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Lỗi cập nhật hồ sơ: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                log("Response already committed while editing profile");
            }
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

                // Đường dẫn lưu ảnh (tùy cấu trúc thư mục project)
                String uploadPath = getServletContext().getRealPath("/") + "images" + File.separator + "staff";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // tạo thư mục nếu chưa có
                }

                // Ghi file ảnh vào thư mục
                filePart.write(uploadPath + File.separator + fileName);
            } else {
                fileName = "default.jpg"; // hoặc để rỗng nếu bạn có ảnh mặc định
            }

            System.out.println(id + username + password + phone + gmail + gender);

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
            System.out.println(staff);

            StaffDAO staffDAO = new StaffDAO();
            staffDAO.create(staff);
            response.sendRedirect("AdminController");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void editStaff(HttpServletRequest request, HttpServletResponse response)
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
                String uploadPath = getServletContext().getRealPath("") + File.separator + "images/staff";
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
            if (!response.isCommitted()) {
                request.setAttribute("error", "Error while deleting staff: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                log("Response already committed while deleting staff");
            }
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
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Failed to reply feedback.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                log("Response already committed while replying feedback");
            }
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
            String proId = request.getParameter("filterProductId");
            String attributeName = request.getParameter("filterAttributeName");
            String value = request.getParameter("filterAttributeValue");
            String sortField = request.getParameter("sortField");
            String sortOrder = request.getParameter("sortOrder");

            Connection conn = DBContext.getConnection();
            ViewProductAttributeDAO viewDAO = new ViewProductAttributeDAO(conn);
            List<ViewProductAttribute> filteredViewList = viewDAO.filterAndSort(proId, attributeName, value, sortField, sortOrder);

            request.setAttribute("viewProductAttributes", filteredViewList);
            request.setAttribute("activeTab", "attributes");

            // Đặt lại param để giữ giá trị đã chọn
            request.setAttribute("sortField", sortField);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("filterProductId", proId);
            request.setAttribute("filterAttributeName", attributeName);
            request.setAttribute("filterAttributeValue", value);

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in filterProductAttribute: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to filter and sort attributes: " + e.getMessage());
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

    private void viewStockList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            StockDAO stockDAO = new StockDAO();
            List<Stock> stocks = stockDAO.getAllStocks();
            request.setAttribute("stocks", stocks);
            request.setAttribute("activeTab", "inventory");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in viewStockList: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to load stock list: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void addStockQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            StockDAO stockDAO = new StockDAO();
            stockDAO.addStockQuantity(proId, quantity);
            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in addStockQuantity: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to add stock quantity: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void deleteStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            StockDAO stockDAO = new StockDAO();
            stockDAO.deleteStock(proId);
            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in deleteStock: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to delete stock: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    /**
     * Creates a new stock record for a product.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void createStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            StockDAO stockDAO = new StockDAO();
            stockDAO.createStock(proId, quantity);
            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in createStock: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to create stock: " + e.getMessage());
                request.getRequestDispatcher("inventory.jsp").forward(request, response);
            }
        }
    }

    private void updateStockQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int newQuantity = Integer.parseInt(request.getParameter("newQuantity"));
            StockDAO stockDAO = new StockDAO();
            stockDAO.updateStockQuantity(proId, newQuantity);
            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in updateStockQuantity: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to update stock quantity: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    /**
     * Điều hướng sang trang orderdetail.jsp và đổ danh sách chi tiết đơn hàng
     * theo orderId
     *
     * @param request HttpServletRequest chứa orderId
     * @param response HttpServletResponse để forward sang JSP
     * @throws ServletException nếu forward bị lỗi
     * @throws IOException nếu có lỗi khi đọc ghi dữ liệu
     */
    private void goToOrderDetailPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();

            Order order = orderDAO.getById(orderId);
            List<OrderDetail> orderDetailList = orderDetailDAO.getByOrderId(orderId);
            Map<String, Product> productMap = new HashMap<>();
            for (OrderDetail detail : orderDetailList) {
                Product product = productDAO.getById(detail.getProId());
                productMap.put(detail.getProId(), product);
            }

            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetailList);
            request.setAttribute("productMap", productMap);

            request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Failed to load order detail page", e);
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            OrderDAO dao = new OrderDAO();
            dao.updateStatus(orderId, newStatus);

            // Redirect về trang lọc đơn hàng với trạng thái hiện tại (giữ nguyên hoặc trả về All)
            String filterStatus = request.getParameter("currentFilterStatus");
            if (filterStatus == null || filterStatus.isEmpty()) {
                filterStatus = "All";
            }

            response.sendRedirect("AdminController?tab=orders");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Cannot update order status.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void filterOrdersByStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status"); // All, pending, shipped, etc.

            OrderDAO dao = new OrderDAO();
            List<Order> filteredOrders;

            if (status == null || status.isEmpty() || "All".equalsIgnoreCase(status)) {
                filteredOrders = dao.getAll();  // Không lọc
            } else {
                filteredOrders = dao.getByStatus(status);  // Lọc theo status
            }

            request.setAttribute("orders", filteredOrders);
            request.setAttribute("filterStatus", status); // Để giữ lại khi hiển thị lại dropdown
            request.setAttribute("activeTab", "orders");

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Unable to filter orders.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            OrderDAO dao = new OrderDAO();
            dao.delete(orderId);

            response.sendRedirect("AdminController?tab=orders");
        } catch (Exception e) {
            log("Error deleting order: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Cannot delete order.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void handleRevenueByMonth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String monthParam = request.getParameter("month"); // format: yyyy-MM

            BigDecimal revenue = BigDecimal.ZERO;

            if (monthParam != null && !monthParam.isEmpty()) {
                String[] parts = monthParam.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);

                OrderDAO orderDAO = new OrderDAO();
                revenue = orderDAO.getRevenueByMonth(year, month);

                request.setAttribute("activeTab", "revenue");
                request.setAttribute("selectedMonth", monthParam);
                request.setAttribute("monthlyRevenue", revenue);

                System.out.println("Controllers.AdminController.handleRevenueByMonth()");
                System.out.println(revenue);
                System.out.println(year);
                System.out.println(month);
            }

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {

            request.setAttribute("error", "Error loading revenue data: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    private void addProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proTypeName = request.getParameter("proTypeName").trim();
            ProductTypeDAO dao = new ProductTypeDAO();

            // BR01 & BR02: Validate name
            if (proTypeName.isEmpty() || proTypeName.length() < 3 || proTypeName.length() > 100) {
                request.getSession().setAttribute("error", "Product type name must be between 3 and 100 characters.");
                response.sendRedirect("AdminController?tab=productTypes");
                return;
            }

            // Standardize name (e.g., capitalize first letter)
            proTypeName = proTypeName.substring(0, 1).toUpperCase() + proTypeName.substring(1).toLowerCase();

            // BR01: Check for unique name
            if (dao.isProductTypeNameExists(proTypeName)) {
                request.getSession().setAttribute("error", "Product type name already exists.");
                response.sendRedirect("AdminController?tab=productTypes");
                return;
            }

            ProductTypes productType = new ProductTypes();
            productType.setName(proTypeName);
            dao.addProductType(productType);
            response.sendRedirect("AdminController?tab=productTypes");
        } catch (Exception e) {
            throw new ServletException("Error adding product type: " + e.getMessage());
        }
    }

    private void updateProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("proTypeId"));
            String proTypeName = request.getParameter("proTypeName").trim();
            ProductTypeDAO dao = new ProductTypeDAO();

            // BR02: Validate name length
            if (proTypeName.isEmpty() || proTypeName.length() < 3 || proTypeName.length() > 100) {
                request.getSession().setAttribute("error", "Product type name must be between 3 and 100 characters.");
                response.sendRedirect("AdminController?tab=productTypes");
                return;
            }

            // Standardize name
            proTypeName = proTypeName.substring(0, 1).toUpperCase() + proTypeName.substring(1).toLowerCase();

            // BR01: Check for unique name (excluding current ID)
            if (dao.isProductTypeNameExistsForOtherId(proTypeName, id)) {
                request.getSession().setAttribute("error", "Product type name already exists.");
                response.sendRedirect("AdminController?tab=productTypes");
                return;
            }

            // BR04: Check if there are associated products
            if (dao.hasAssociatedProducts(id)) {
                ProductTypes existingType = dao.getProductTypeById(id);
                String existingName = existingType.getName().toLowerCase();
                String newName = proTypeName.toLowerCase();
                // Allow minor spelling changes (e.g., Levenshtein distance <= 2)
                if (getLevenshteinDistance(existingName, newName) > 2) {
                    request.getSession().setAttribute("error", "Cannot update product type name significantly as it has associated products.");
                    response.sendRedirect("AdminController?tab=productTypes");
                    return;
                }
            }

            ProductTypes productType = new ProductTypes(id, proTypeName);
            dao.updateProductType(productType);
            response.sendRedirect("AdminController?tab=productTypes");
        } catch (Exception e) {
            throw new ServletException("Error updating product type: " + e.getMessage());
        }
    }

    private void deleteProductType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductTypeDAO dao = new ProductTypeDAO();

            // BR06: Prevent deletion if linked to products or attributes
            if (dao.hasAssociatedProducts(id) || dao.hasAssociatedAttributes(id)) {
                request.getSession().setAttribute("error", "Cannot delete product type as it has associated products or attributes.");
                response.sendRedirect("AdminController?tab=productTypes");
                return;
            }

            dao.deleteProductType(id);
            response.sendRedirect("AdminController?tab=productTypes");
        } catch (Exception e) {
            throw new ServletException("Error deleting product type: " + e.getMessage());
        }
    }
    
    // Levenshtein distance for minor spelling change detection
    private int getLevenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[len1][len2];
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
