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
import Ultis.MD5Util;

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
import Ultis.ExcelUtils;
import Ultis.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;

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
                case "deleteReply":
                    deleteReply(request, response);
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
                case "viewOrderDetails":
                    viewOrderDetails(request, response);
                    return;
                case "viewStockList":
                    viewStockList(request, response);
                    return;
                case "ImportStockQuantity":
                    importStockQuantity(request, response);
                    return;
                case "deleteStock":
                    deleteStock(request, response);
                    return;
                case "createStock":
                    createStock(request, response);
                    return;
                case "listStocks":
                    listStocks(request, response);
                    return;
                case "ExportStockQuantity":
                    exportStockQuantity(request, response);
                    return;
                case "RevenueByMonth":
                    handleRevenueByMonth(request, response);
                    return;
                case "RevenueByYear":
                    handleRevenueByYear(request, response);
                    return;
                case "add":
                    addProduct(request, response);
                    return;
                case "searchPrd":
                    searchProduct(request, response);
                    return;
                case "editProduct":
                    editProduct(request, response);
                    return;
                case "deleteProduct":
                    deleteProduct(request, response);
                    return;
                case "viewProductDetail":
                    viewProductDetail(request, response);
                    return;
                case "getCustomerInfo":
                    getCustomerInfo(request, response);
                    return;
                case "deleteFeedback":
                    deleteFeedback(request, response);
                    return;
                case "getAttributesByType":
                    getAttributesByType(request, response);
                    return;
                case "filterAttribute":
                    filterAttribute(request, response);
                    return;
                default:
                    loadAdminPage(request, response);
                    return;
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

            // Check for revenue data in session (from redirect)
            HttpSession revenueSession = request.getSession();
            if (revenueSession.getAttribute("yearlyRevenueMap") != null) {
                request.setAttribute("yearlyRevenueMap", revenueSession.getAttribute("yearlyRevenueMap"));
                request.setAttribute("yearlyGrowthRateMap", revenueSession.getAttribute("yearlyGrowthRateMap"));
                
                // Move specific year data if available
                if (revenueSession.getAttribute("selectedYear") != null) {
                    request.setAttribute("selectedYear", revenueSession.getAttribute("selectedYear"));
                    request.setAttribute("yearlyRevenue", revenueSession.getAttribute("yearlyRevenue"));
                    request.setAttribute("monthlyRevenueMap", revenueSession.getAttribute("monthlyRevenueMap"));
                    request.setAttribute("monthlyGrowthRateMap", revenueSession.getAttribute("monthlyGrowthRateMap"));
                    request.setAttribute("productRevenueList", revenueSession.getAttribute("productRevenueList"));
                    request.setAttribute("revenueYear", revenueSession.getAttribute("revenueYear"));
                }
                
                // Clear session after moving to request
                revenueSession.removeAttribute("yearlyRevenueMap");
                revenueSession.removeAttribute("yearlyGrowthRateMap");
                revenueSession.removeAttribute("selectedYear");
                revenueSession.removeAttribute("yearlyRevenue");
                revenueSession.removeAttribute("monthlyRevenueMap");
                revenueSession.removeAttribute("monthlyGrowthRateMap");
                revenueSession.removeAttribute("productRevenueList");
                revenueSession.removeAttribute("revenueYear");
            }

            // 1. Load common data
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            FeedbackReplyViewDAO viewfeedbackDAO = new FeedbackReplyViewDAO();
            AttributeDAO attributeDAO = new AttributeDAO();
            ProductAttributeDAO paDAO = new ProductAttributeDAO();
            OrderDAO ordDao = new OrderDAO();
            StockDAO stockDAO = new StockDAO();

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<Feedback> feedbacks = feedbackDAO.getAllFeedbacks();
            List<FeedbackReplyView> viewFeedbacks = viewfeedbackDAO.getAllFeedbackReplies();
            List<Attribute> attributes = attributeDAO.getAll();
            List<ProductAttribute> productAttributes = paDAO.getAll();
            
           
            // Add: Load all products for product tab
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();
            
            ProductTypeDAO typeDAO = new ProductTypeDAO();
            List<ProductTypes> types = typeDAO.getAllProductTypes();
            
            // Create a map for type ID to type name lookup
            Map<Integer, String> typeMap = new HashMap<>();
            for (ProductTypes type : types) {
                typeMap.put(type.getId(), type.getName());
            }
            
            BigDecimal totalRevenue = ordDao.getTotalRevenue();

            // 2. Load ViewProductAttributes
            Connection conn = DBContext.getConnection();
            ViewProductAttributeDAO viewProductAttributeDAO = new ViewProductAttributeDAO(conn);
            List<ViewProductAttribute> viewProductAttributes = viewProductAttributeDAO.getAll();
            request.setAttribute("viewProductAttributes", viewProductAttributes);

            // 3. Set data to request
            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("vouchers", vouchers);
            request.setAttribute("feedbacks", feedbacks);
            request.setAttribute("viewFeedbacks", viewFeedbacks);
            request.setAttribute("attributes", attributes);
            request.setAttribute("productAttributes", productAttributes);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("types", types);
            request.setAttribute("typeMap", typeMap); // Add typeMap for lookup
            request.setAttribute("productTypes", types); // For backward compatibility
             request.setAttribute("prds", products);

            StockDAO dao = new StockDAO();
            List<Map<String, Object>> productStockList = dao.getAllProductStockData();
            request.setAttribute("productStockList", productStockList);
            
            List<String> noStockProIds = stockDAO.getProductsWithoutStock();
            request.setAttribute("noStockProIds", noStockProIds);

            // 4. Load danh sách đơn hàng nếu không có filter trạng thái
            if (request.getParameter("sortByOrderStatus") == null) {
                List<Map<String, Object>> orderData = ordDao.getOrderDetailsAll();
                request.setAttribute("orders", orderData);
            }

            // 5. Load doanh thu theo tháng của năm hiện tại và dữ liệu yearly
            int currentYear = LocalDate.now().getYear();
            Map<String, BigDecimal> monthlyRevenue = ordDao.getMonthlyRevenueInYear(currentYear);
            Map<String, Double> monthlyGrowthRate = ordDao.getMonthlyRevenueGrowthRate(currentYear);
            List<Map<String, Object>> productRevenue = ordDao.getProductRevenue(currentYear);
            
            // Load yearly data
            Map<String, BigDecimal> yearlyRevenue = ordDao.getYearlyRevenue();
            Map<String, Double> yearlyGrowthRate = ordDao.getYearlyGrowthRate();
            
            request.setAttribute("monthlyRevenueMap", monthlyRevenue);
            request.setAttribute("monthlyGrowthRateMap", monthlyGrowthRate);
            request.setAttribute("productRevenueList", productRevenue);
            request.setAttribute("yearlyRevenueMap", yearlyRevenue);
            request.setAttribute("yearlyGrowthRateMap", yearlyGrowthRate);
            request.setAttribute("revenueYear", currentYear);

            // 6. Load profile info từ LOGIN_USER
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

            // 7. Load successMessage từ session nếu có
            HttpSession session = request.getSession();
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage"); // clear after showing
            }

            // 8. Forward về admin.jsp
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
            staff.setStaffPassword(MD5Util.hashPassword(password)); // Hash password before storing
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
            Staff updatedStaff = new Staff(id, username, fullname, MD5Util.hashPassword(password),
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
            response.sendRedirect("AdminController?tab=feedbacks");
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

    private void deleteStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            StockDAO stockDAO = new StockDAO();
            // Có thể kiểm tra số lượng hiện tại tại đây nếu muốn
            int current = stockDAO.getStockByProductId(proId).getStockQuantity();

            ProductDAO prodao = new ProductDAO();
            String proname = prodao.getById(proId).getProName();
            LogUtil.logToFile("logs/inventory_log.txt", "[Deleted] " + current + " into stock of [ProductID] " + proId + " [ProductName] " + proname + " [Beginning Inventory] " + current + " [Ending Inventory] " + "0");

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

            int finalstock = stockDAO.getStockByProductId(proId).getStockQuantity();
            ProductDAO prodao = new ProductDAO();
            String proname = prodao.getById(proId).getProName();
            LogUtil.logToFile("logs/inventory_log.txt", "[Created] " + quantity + " into stock of [ProductID] " + proId + " [ProductName] " + proname + " [Beginning Inventory] " + "0" + " [Ending Inventory] " + finalstock);

            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in createStock: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to create stock: " + e.getMessage());
                request.getRequestDispatcher("inventory.jsp").forward(request, response);
            }
        }
    }

    private void exportStockQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }

            StockDAO stockDAO = new StockDAO();
            // Có thể kiểm tra số lượng hiện tại tại đây nếu muốn
            int current = stockDAO.getStockByProductId(proId).getStockQuantity();
            stockDAO.exportStockQuantity(proId, quantity);
            int finalstock = stockDAO.getStockByProductId(proId).getStockQuantity();
            ProductDAO prodao = new ProductDAO();
            String proname = prodao.getById(proId).getProName();
            LogUtil.logToFile("logs/inventory_log.txt", "[Exported] " + quantity + " into stock of [ProductID] " + proId + " [ProductName] " + proname + " [Beginning Inventory] " + current + " [Ending Inventory] " + finalstock);

            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in exportStockQuantity: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to export stock: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void listStocks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String sortBy = request.getParameter("sortBy");
            String order = request.getParameter("order");

            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "lastUpdated";
            }
            if (order == null || order.isEmpty()) {
                order = "DESC";
            }

            StockDAO stockDAO = new StockDAO();
            List<Stock> stocks = stockDAO.getStocksSorted(sortBy, order);

            request.setAttribute("stocks", stocks);
            request.getRequestDispatcher("inventory.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in listStocks: " + e.getMessage());
            request.setAttribute("ERROR", "Unable to list stocks: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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

    /**
     * Filters orders by their status (e.g., All, Pending, Done, Cancel) and
     * forwards the result to admin.jsp for display.
     */
    private void filterOrdersByStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("sortByOrderStatus");
            OrderDAO dao = new OrderDAO();
            if (status.equals("All")) {
                List<Map<String, Object>> filteredOrders = dao.getOrderDetailsAll();
                request.setAttribute("orders", filteredOrders);
            } else {
                List<Map<String, Object>> filteredOrders = dao.getOrderDetailsByStatus(status);
                request.setAttribute("orders", filteredOrders);
                System.out.println(filteredOrders);
            }

            // Set attributes for displaying in JSP
            request.setAttribute("filterStatus", status); // Preserve selected filter in dropdown
            request.setAttribute("activeTab", "orders");

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in filterOrdersByStatus: " + e.getMessage());

        }
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            OrderDAO dao = new OrderDAO();
            dao.delete(orderId);

            // Thêm thông báo vào session
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Order #" + orderId + " deleted successfully.");

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

    private void handleRevenueByYear(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String yearParam = request.getParameter("year");
            OrderDAO orderDAO = new OrderDAO();

            // Declare variables outside if block
            BigDecimal yearlyRevenue = null;
            Map<String, BigDecimal> monthlyRevenue = null;
            Map<String, Double> monthlyGrowthRate = null;
            List<Map<String, Object>> productRevenue = null;
            Integer selectedYear = null;

            if (yearParam != null && !yearParam.isEmpty()) {
                selectedYear = Integer.parseInt(yearParam);
                
                // Get specific year revenue
                yearlyRevenue = orderDAO.getRevenueByYear(selectedYear);
                
                // Get monthly data for selected year
                monthlyRevenue = orderDAO.getMonthlyRevenueInYear(selectedYear);
                monthlyGrowthRate = orderDAO.getMonthlyRevenueGrowthRate(selectedYear);
                productRevenue = orderDAO.getProductRevenue(selectedYear);
                
                request.setAttribute("selectedYear", yearParam);
                request.setAttribute("yearlyRevenue", yearlyRevenue);
                request.setAttribute("monthlyRevenueMap", monthlyRevenue);
                request.setAttribute("monthlyGrowthRateMap", monthlyGrowthRate);
                request.setAttribute("productRevenueList", productRevenue);
                request.setAttribute("revenueYear", selectedYear);
            }

            // Get yearly revenue for all years (for year selector)
            Map<String, BigDecimal> allYearlyRevenue = orderDAO.getYearlyRevenue();
            Map<String, Double> yearlyGrowthRate = orderDAO.getYearlyGrowthRate();
            
            // Store data in session to persist through redirect
            HttpSession session = request.getSession();
            session.setAttribute("yearlyRevenueMap", allYearlyRevenue);
            session.setAttribute("yearlyGrowthRateMap", yearlyGrowthRate);
            
            // Also store specific year data if available
            if (yearParam != null && !yearParam.isEmpty()) {
                session.setAttribute("selectedYear", yearParam);
                session.setAttribute("yearlyRevenue", yearlyRevenue);
                session.setAttribute("monthlyRevenueMap", monthlyRevenue);
                session.setAttribute("monthlyGrowthRateMap", monthlyGrowthRate);
                session.setAttribute("productRevenueList", productRevenue);
                session.setAttribute("revenueYear", selectedYear);
            }
            
            // Redirect to admin page with revenue tab active
            response.sendRedirect("AdminController?tab=revenue");
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

    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            int orderId = Integer.parseInt(orderIdStr);

            // Initialize DAOs
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            
            // Get order information
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                response.getWriter().write("<div class='alert alert-danger'>Không tìm thấy đơn hàng</div>");
                return;
            }
            
            // Get order details with product information
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailByOrderId(orderId);
            List<OrderDetailsController.OrderDetailWithProduct> orderDetailsWithProducts = new ArrayList<>();
            
            for (OrderDetail detail : orderDetails) {
                try {
                    Product product = productDAO.getById(detail.getProId());
                    OrderDetailsController.OrderDetailWithProduct detailWithProduct = new OrderDetailsController.OrderDetailWithProduct();
                    detailWithProduct.setOrderDetail(detail);
                    detailWithProduct.setProduct(product);
                    
                    // Calculate subtotal for this item
                    double subtotal = detail.getQuantity() * detail.getUnitPrice();
                    detailWithProduct.setSubtotal(subtotal);
                    
                    orderDetailsWithProducts.add(detailWithProduct);
                } catch (Exception e) {
                    // Continue with other products even if one fails
                }
            }
            
            // Get voucher information if exists
            Voucher voucher = null;
            if (order.getVoucherId() != null) {
                try {
                    voucher = voucherDAO.getById(order.getVoucherId());
                } catch (Exception e) {
                    // Voucher not found, continue without it
                }
            }
            
            // Calculate totals
            double subtotalAmount = orderDetailsWithProducts.stream()
                    .mapToDouble(OrderDetailsController.OrderDetailWithProduct::getSubtotal)
                    .sum();
            
            double discountAmount = order.getDiscountAmount() != null ? 
                    order.getDiscountAmount().doubleValue() : 0.0;
            
            double finalAmount = subtotalAmount - discountAmount;
            
            // Set attributes for JSP
            request.setAttribute("order", order);
            request.setAttribute("orderDetailsWithProducts", orderDetailsWithProducts);
            request.setAttribute("voucher", voucher);
            request.setAttribute("subtotalAmount", subtotalAmount);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("finalAmount", finalAmount);
            
            // Forward to the admin order details JSP
            request.getRequestDispatcher("admin-order-details.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<div class='alert alert-danger'>Lỗi khi tải chi tiết đơn hàng: " + e.getMessage() + "</div>");
        }
    }

    private void importStockQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }

            StockDAO stockDAO = new StockDAO();
            int current = stockDAO.getStockByProductId(proId).getStockQuantity();
            stockDAO.importStockQuantity(proId, quantity);
            int finalstock = stockDAO.getStockByProductId(proId).getStockQuantity();
            ProductDAO prodao = new ProductDAO();
            String proname = prodao.getById(proId).getProName();
            LogUtil.logToFile("logs/inventory_log.txt", "[Imported] " + quantity + " into stock of [ProductID] " + proId + " [ProductName] " + proname + " [Beginning Inventory] " + current + " [Ending Inventory] " + finalstock);

            response.sendRedirect("AdminController?tab=inventory");
        } catch (Exception e) {
            log("Error in importStockQuantity: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Unable to import stock: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    private void generateInventoryExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startDate = new java.sql.Date(dateOnlyFormat.parse(startDateStr).getTime());
            java.util.Date endDate = new java.sql.Date(dateOnlyFormat.parse(endDateStr).getTime());
            
            System.out.println(startDate + " " + endDate);
            // Validate: startDate must not be after endDate
            if (startDate.after(endDate)) {
                request.setAttribute("ERROR", "Start date must not be after end date.");
                request.setAttribute("activeTab", "inventory");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            }

            // Gọi ExcelUtils với ServletContext và log file path
            ServletContext context = request.getServletContext();
            
            String logFilePath = context.getRealPath("/logs/inventory_log.txt");
            System.out.println(logFilePath);
            File excelFile = ExcelUtils.generateInventoryReport(context, startDate, endDate, logFilePath);

            // Thiết lập response để tải file về
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelFile.getName());

            try ( FileInputStream fis = new FileInputStream(excelFile);  OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Xoá file tạm sau khi tải
            excelFile.delete();
        } catch (Exception e) {
            log("Error in generateInventoryExcel: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Failed to generate inventory report: " + e.getMessage());
                request.getRequestDispatcher("inventory.jsp").forward(request, response);
            }
        }
    }

    private void searchProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String searchName = request.getParameter("searchValue");
            ProductDAO productDAO = new ProductDAO();
            List<Product> products;
            if (searchName != null && !searchName.trim().isEmpty()) {
                products = productDAO.searchByName(searchName, 1, 100); // lấy tối đa 100 sản phẩm
            } else {
                products = productDAO.getAllProducts();
            }
            request.setAttribute("prds", products);
            request.setAttribute("searchName", searchName);
            // Load lại các dữ liệu khác cần thiết cho admin.jsp
//            loadAdminPage(request, response);
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void viewProductDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductDetail(proId);
            // Return the product details as JSON
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(product));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy các tham số từ request
            String proId = request.getParameter("proId");
            String proName = request.getParameter("proName");
            String proDescription = request.getParameter("proDescription");
            String proTypeIdStr = request.getParameter("proTypeId");
            String proPriceStr = request.getParameter("proPrice");
            String oldImage = request.getParameter("oldImage");

            // Kiểm tra nếu các tham số quan trọng bị null hoặc rỗng
            if (proId == null || proId.trim().isEmpty()) {
                throw new ServletException("Product ID is required.");
            }

            // Lấy các giá trị cũ của sản phẩm từ cơ sở dữ liệu
            ProductDAO productDAO = new ProductDAO();
            Product existingProduct = productDAO.getById(proId);
            if (existingProduct == null) {
                throw new ServletException("Product not found.");
            }

            // Nếu không có giá trị mới, giữ nguyên giá trị cũ
            String proNameToUse = (proName != null && !proName.trim().isEmpty()) ? proName : existingProduct.getProName();
            String proDescriptionToUse = (proDescription != null && !proDescription.trim().isEmpty()) ? proDescription : existingProduct.getProDescription();
            int proTypeId = (proTypeIdStr != null && !proTypeIdStr.trim().isEmpty()) ? Integer.parseInt(proTypeIdStr) : existingProduct.getProTypeId();
            
            // Handle price conversion properly
            java.math.BigDecimal proPrice;
            if (proPriceStr != null && !proPriceStr.trim().isEmpty()) {
                // Remove any currency formatting, spaces, and Vietnamese dong symbol
                String cleanPriceStr = proPriceStr.replaceAll("[^0-9.]", "");
                if (!cleanPriceStr.isEmpty()) {
                    proPrice = new java.math.BigDecimal(cleanPriceStr);
                } else {
                    proPrice = existingProduct.getProPrice();
                }
            } else {
                proPrice = existingProduct.getProPrice();
            }

            // Handle image upload
            String imageFileName = (oldImage != null && !oldImage.trim().isEmpty()) ? oldImage : existingProduct.getProImageMain();
            Part imagePart = request.getPart("proImageMain");

            // Check if a new image has been uploaded
            if (imagePart != null && imagePart.getSize() > 0 && imagePart.getSubmittedFileName() != null && !imagePart.getSubmittedFileName().trim().isEmpty()) {
                imageFileName = java.nio.file.Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/") + "images" + java.io.File.separator + "products";
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                imagePart.write(uploadPath + java.io.File.separator + imageFileName);
            }

            // Update product
            Product product = new Product(proId, proTypeId, proNameToUse, proDescriptionToUse, proPrice, imageFileName);
            productDAO.updateProduct(product);

            // Redirect to admin page with products tab
            response.sendRedirect("AdminController?tab=products");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error updating product: " + e.getMessage());
            response.sendRedirect("AdminController?tab=products");
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String proId = request.getParameter("proId");
            ProductDAO productDAO = new ProductDAO();

            Product product = parseProductFromRequest(request);
            productDAO.insertProduct(product);
            List<Product> prds = productDAO.getAllProducts();
            request.setAttribute("SUCCESS", "Product added successfully!");
            request.setAttribute("prds", prds);
        } catch (NumberFormatException e) {
            request.setAttribute("ERROR", "Invalid number format!");
        } catch (ClassNotFoundException ex) {
            log("CreateAccount _ ClassNotFound: " + ex.getMessage());
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("CreateAccount _ SQL: " + msg);
            if (msg.contains("duplicate")) {
                request.setAttribute("ERROR", "Product ID already exists!");
            }
        } finally {
            HttpSession session = request.getSession();
            if (request.getAttribute("ERROR") != null) {
                session.setAttribute("error", (String) request.getAttribute("ERROR"));
            } else if (request.getAttribute("SUCCESS") != null) {
                session.setAttribute("success", (String) request.getAttribute("SUCCESS"));
            }
            response.sendRedirect("AdminController?tab=products");
        }
    }

    private Product parseProductFromRequest(HttpServletRequest request) throws NumberFormatException, IOException, ServletException {
        String proId = request.getParameter("proId");
        String proName = request.getParameter("proName");
        String proDescription = request.getParameter("proDescription");
        BigDecimal proPrice = new BigDecimal(request.getParameter("proPrice"));
        int proTypeId = Integer.parseInt(request.getParameter("proTypeId"));
        // HANDLE FILE UPLOAD
        Part filePart = request.getPart("proImageMain");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String uploadPath = getServletContext().getRealPath("/") + "images" + File.separator + "products";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        if (fileName != null && !fileName.isEmpty()) {
            filePart.write(uploadPath + File.separator + fileName);
        }

        String proImageMain = fileName;

        return new Product(proId, proTypeId, proName, proDescription, proPrice, proImageMain);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String proId = request.getParameter("proId");
            ProductDAO productDAO = new ProductDAO();
            productDAO.deleteProduct(proId);
            response.sendRedirect("AdminController?tab=products");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Delete feedback by feedback ID
     */
    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String feedbackIdStr = request.getParameter("feedbackId");
            int feedbackId = Integer.parseInt(feedbackIdStr);
            
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            
            // Delete the feedback (assuming database has CASCADE DELETE for replies)
            feedbackDAO.deleteFeedback(feedbackId);
            
            // Set success message
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Feedback deleted successfully.");
            
            response.sendRedirect("AdminController?tab=feedbacks");
            
        } catch (Exception e) {
            log("Error deleting feedback: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Cannot delete feedback: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    /**
     * Delete reply feedback by reply ID
     */
    private void deleteReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String replyIdStr = request.getParameter("replyId");
            
            if (replyIdStr == null || replyIdStr.trim().isEmpty()) {
                request.setAttribute("ERROR", "Reply ID is required.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            int replyId = Integer.parseInt(replyIdStr);
            
            ReplyFeedbackDAO replyDAO = new ReplyFeedbackDAO();
            
            // Delete the reply
            replyDAO.deleteReply(replyId);
            
            // Set success message
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Reply deleted successfully.");
            
            response.sendRedirect("AdminController?tab=feedbacks");
            
        } catch (Exception e) {
            log("Error deleting reply: " + e.getMessage());
            if (!response.isCommitted()) {
                request.setAttribute("ERROR", "Cannot delete reply: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }

    /**
     * Get customer information including personal details and order history
     */
    private void getCustomerInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String cusId = request.getParameter("cusId");
        
        try {
            CustomerDAO customerDAO = new CustomerDAO();
            OrderDAO orderDAO = new OrderDAO();
            
            // Get customer details
            Customer customer = customerDAO.getCustomerById(cusId);
            
            // Get customer's order history
            List<Order> orders = orderDAO.getOrdersByCustomerId(cusId);
            
            // Create response JSON
            Map<String, Object> responseData = new HashMap<>();
            
            if (customer != null) {
                responseData.put("success", true);
                responseData.put("customer", customer);
                responseData.put("orders", orders);
            } else {
                responseData.put("success", false);
                responseData.put("message", "Customer not found");
            }
            
            // Convert to JSON and send response
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseData);
            
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            // Handle error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error loading customer information: " + e.getMessage());
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(errorResponse);
            
            response.getWriter().write(jsonResponse);
        }
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
    
    /**
     * Load basic admin data needed for admin.jsp
     */
    private void loadBasicAdminData(HttpServletRequest request) throws Exception {
        // Load profile info from LOGIN_USER
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
        
        // Load basic data for navigation and common functionality
        CustomerDAO cusDAO = new CustomerDAO();
        StaffDAO staffDAO = new StaffDAO();
        ProductDAO productDAO = new ProductDAO();
        
        List<Customer> users = cusDAO.getAllCustomers();
        List<Staff> staffs = staffDAO.getAll();
        List<Product> products = productDAO.getAllProducts();
        
        request.setAttribute("users", users);
        request.setAttribute("staffs", staffs);
        request.setAttribute("prds", products);
    }

    /**
     * Filter attributes by type or name
     */
    private void filterAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String filterTypeId = request.getParameter("filterTypeId");
            String filterAttributeName = request.getParameter("filterAttributeName");

            AttributeDAO attributeDAO = new AttributeDAO();
            List<Attribute> attributes = attributeDAO.getAll();
            
            // Apply filters
            if (filterTypeId != null && !filterTypeId.trim().isEmpty()) {
                attributes = attributes.stream()
                    .filter(attr -> String.valueOf(attr.getProTypeId()).equals(filterTypeId))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            if (filterAttributeName != null && !filterAttributeName.trim().isEmpty()) {
                attributes = attributes.stream()
                    .filter(attr -> attr.getAttributeName().toLowerCase().contains(filterAttributeName.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }

            // Set filtered attributes BEFORE loading admin page
            request.setAttribute("attributes", attributes);
            request.setAttribute("activeTab", "attributes");
            
            // Preserve filter parameters
            request.setAttribute("filterTypeId", filterTypeId);
            request.setAttribute("filterAttributeName", filterAttributeName);
            
            // Load other data but preserve filtered attributes  
            loadAdminPageWithFilteredAttributes(request, response);
        } catch (Exception e) {
            log("Error in filterAttribute: " + e.getMessage());
            request.setAttribute("ERROR", "Error filtering attributes: " + e.getMessage());
            loadAdminPage(request, response);
        }
    }

    /**
     * Load admin page data while preserving already set attributes
     */
    private void loadAdminPageWithFilteredAttributes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String activeTab = request.getParameter("tab");
            request.setAttribute("activeTab", activeTab);

            // Check for revenue data in session (from redirect)
            HttpSession revenueSession = request.getSession();
            if (revenueSession.getAttribute("yearlyRevenueMap") != null) {
                request.setAttribute("yearlyRevenueMap", revenueSession.getAttribute("yearlyRevenueMap"));
                request.setAttribute("yearlyGrowthRateMap", revenueSession.getAttribute("yearlyGrowthRateMap"));
                
                // Move specific year data if available
                if (revenueSession.getAttribute("selectedYear") != null) {
                    request.setAttribute("selectedYear", revenueSession.getAttribute("selectedYear"));
                    request.setAttribute("yearlyRevenue", revenueSession.getAttribute("yearlyRevenue"));
                    request.setAttribute("monthlyRevenueMap", revenueSession.getAttribute("monthlyRevenueMap"));
                    request.setAttribute("monthlyGrowthRateMap", revenueSession.getAttribute("monthlyGrowthRateMap"));
                    request.setAttribute("productRevenueList", revenueSession.getAttribute("productRevenueList"));
                    request.setAttribute("revenueYear", revenueSession.getAttribute("revenueYear"));
                }
                
                // Clear session after moving to request
                revenueSession.removeAttribute("yearlyRevenueMap");
                revenueSession.removeAttribute("yearlyGrowthRateMap");
                revenueSession.removeAttribute("selectedYear");
                revenueSession.removeAttribute("yearlyRevenue");
                revenueSession.removeAttribute("monthlyRevenueMap");
                revenueSession.removeAttribute("monthlyGrowthRateMap");
                revenueSession.removeAttribute("productRevenueList");
                revenueSession.removeAttribute("revenueYear");
            }

            // 1. Load common data
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            FeedbackReplyViewDAO viewfeedbackDAO = new FeedbackReplyViewDAO();
            ProductAttributeDAO paDAO = new ProductAttributeDAO();
            OrderDAO ordDao = new OrderDAO();
            StockDAO stockDAO = new StockDAO();

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<Feedback> feedbacks = feedbackDAO.getAllFeedbacks();
            List<FeedbackReplyView> viewFeedbacks = viewfeedbackDAO.getAllFeedbackReplies();
            // DON'T load attributes - they are already filtered and set
            List<ProductAttribute> productAttributes = paDAO.getAll();
            
           
            // Add: Load all products for product tab
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();
            
            ProductTypeDAO typeDAO = new ProductTypeDAO();
            List<ProductTypes> types = typeDAO.getAllProductTypes();
            
            // Create a map for type ID to type name lookup
            Map<Integer, String> typeMap = new HashMap<>();
            for (ProductTypes type : types) {
                typeMap.put(type.getId(), type.getName());
            }
            
            BigDecimal totalRevenue = ordDao.getTotalRevenue();

            // 2. Load ViewProductAttributes
            Connection conn = DBContext.getConnection();
            ViewProductAttributeDAO viewProductAttributeDAO = new ViewProductAttributeDAO(conn);
            List<ViewProductAttribute> viewProductAttributes = viewProductAttributeDAO.getAll();
            request.setAttribute("viewProductAttributes", viewProductAttributes);

            // 3. Set data to request (DON'T override attributes)
            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("vouchers", vouchers);
            request.setAttribute("feedbacks", feedbacks);
            request.setAttribute("viewFeedbacks", viewFeedbacks);
            // request.setAttribute("attributes", attributes); // Skip this line
            request.setAttribute("productAttributes", productAttributes);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("types", types);
            request.setAttribute("typeMap", typeMap); // Add typeMap for lookup
            request.setAttribute("productTypes", types); // For backward compatibility
            request.setAttribute("prds", products);

            StockDAO dao = new StockDAO();
            List<Map<String, Object>> productStockList = dao.getAllProductStockData();
            request.setAttribute("productStockList", productStockList);
            
            List<String> noStockProIds = stockDAO.getProductsWithoutStock();
            request.setAttribute("noStockProIds", noStockProIds);

            // 4. Forward to admin page
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error loading admin page: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR", "System error occurred");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        }
    }

    /**
     * Get attributes by product type ID for AJAX request
     */
    private void getAttributesByType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String typeId = request.getParameter("typeId");
            if (typeId == null || typeId.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            AttributeDAO attributeDAO = new AttributeDAO();
            List<Attribute> attributes = attributeDAO.getAll();
            
            // Filter attributes by type ID
            List<Attribute> filteredAttributes = new ArrayList<>();
            for (Attribute attr : attributes) {
                if (String.valueOf(attr.getProTypeId()).equals(typeId)) {
                    filteredAttributes.add(attr);
                }
            }

            // Convert to JSON
            Gson gson = new Gson();
            String json = gson.toJson(filteredAttributes);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            log("Error in getAttributesByType: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
