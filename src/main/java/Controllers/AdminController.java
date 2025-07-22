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
public class AdminController extends HttpServlet {

    private static final String UPLOAD_DIR = "images/products";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
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
                case "excelcreate":
                    generateInventoryExcel(request, response);
                    return;
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

            // 1. Load common data
            CustomerDAO cusDAO = new CustomerDAO();
            StaffDAO staffDAO = new StaffDAO();
            VoucherDAO voucherDAO = new VoucherDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            FeedbackReplyViewDAO viewfeedbackDAO = new FeedbackReplyViewDAO();
            AttributeDAO attributeDAO = new AttributeDAO();
            ProductAttributeDAO paDAO = new ProductAttributeDAO();
            OrderDAO ordDao = new OrderDAO();

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();
            List<Feedback> feedbacks = feedbackDAO.getAllFeedbacks();
            List<FeedbackReplyView> viewFeedbacks = viewfeedbackDAO.getAllFeedbackReplies();
            List<Attribute> attributes = attributeDAO.getAll();
            List<ProductAttribute> productAttributes = paDAO.getAll();
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

            StockDAO dao = new StockDAO();
            List<Map<String, Object>> productStockList = dao.getAllProductStockData();
            request.setAttribute("productStockList", productStockList);
            StockDAO stockDAO = new StockDAO();
            List<String> noStockProIds = stockDAO.getProductsWithoutStock();
            request.setAttribute("noStockProIds", noStockProIds);

            // 4. Load danh sách đơn hàng nếu không có filter trạng thái
            if (request.getParameter("sortByOrderStatus") == null) {
                List<Map<String, Object>> orderData = ordDao.getOrderDetailsAll();
                request.setAttribute("orders", orderData);
            }

            // 5. Load doanh thu theo tháng của năm hiện tại
            int currentYear = LocalDate.now().getYear();
            Map<String, BigDecimal> monthlyRevenue = ordDao.getMonthlyRevenueInYear(currentYear);
            request.setAttribute("monthlyRevenueMap", monthlyRevenue);
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

    // ----------------- ADD VOUCHER -----------------
    private void addVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            // Validate start < end
            if (!startDate.before(endDate)) {
                request.getSession().setAttribute("error", "Start date must be before end date.");
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            // Nếu hết hạn thì inactive
            if (endDate.toLocalDate().isBefore(LocalDate.now())) {
                isActive = false;
            }

            Voucher voucher = new Voucher(0, codeName, description, discountType,
                    discountValue, minOrderAmount, startDate, endDate, isActive, quantity);

            new VoucherDAO().create(voucher);
            response.sendRedirect("AdminController?tab=vouchers");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ----------------- UPDATE VOUCHER -----------------
    private void updateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("voucherId"));
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            if (quantity < 0) {
                request.getSession().setAttribute("error", "Quantity cannot be negative.");
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            // Validate start < end
            if (!startDate.before(endDate)) {
                request.getSession().setAttribute("error", "Start date must be before end date.");
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            // Nếu đã hết hạn thì inactive
            if (endDate.toLocalDate().isBefore(LocalDate.now())) {
                isActive = false;
            }

            Voucher voucher = new Voucher(id, codeName, description, discountType,
                    discountValue, minOrderAmount, startDate, endDate, isActive, quantity);

            new VoucherDAO().update(voucher);
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
                if (!response.isCommitted()) {
                    response.getWriter().write("Voucher not found");
                }
                return;
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(voucher));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (!response.isCommitted()) {
                response.getWriter().write("Error fetching voucher: " + e.getMessage());
            }
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

    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            int orderId = Integer.parseInt(orderIdStr);

            OrderDAO ordDao = new OrderDAO();
            List<Map<String, Object>> allData = ordDao.getOrderDetailsAll();

            // Lọc ra những dòng thuộc orderId này
            List<Map<String, Object>> orderDetails = new ArrayList<>();
            Map<String, Object> orderInfo = null;

            for (Map<String, Object> row : allData) {
                int currentOrderId = (int) row.get("orderId");

                if (currentOrderId == orderId) {
                    orderDetails.add(row);

                    // Lấy thông tin chung của order (1 lần)
                    if (orderInfo == null) {
                        orderInfo = new HashMap<>();
                        orderInfo.put("orderId", currentOrderId);
                        orderInfo.put("cusFullName", row.get("cusFullName"));
                        orderInfo.put("orderDate", row.get("orderDate"));
                        orderInfo.put("codeName", row.get("codeName") != null ? row.get("codeName") : "No voucher");
                        orderInfo.put("finalAmount", row.get("finalAmount"));
                        orderInfo.put("orderStatus", row.get("orderStatus"));
                        orderInfo.put("paymentMethod", row.get("paymentMethod"));
                        orderInfo.put("shippingAddress", row.get("shippingAddress"));
                        orderInfo.put("receiverName", row.get("receiverName"));
                        orderInfo.put("receiverPhone", row.get("receiverPhone"));
                    }
                }
            }

            if (orderInfo == null) {
                // Không tìm thấy đơn hàng
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            request.setAttribute("orderInfo", orderInfo);
            request.setAttribute("orderDetails", orderDetails);
            request.getRequestDispatcher("orderDetails.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading order details");
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
