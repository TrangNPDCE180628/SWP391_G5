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

            List<Customer> users = cusDAO.getAllCustomers();
            List<Staff> staffs = staffDAO.getAll();
            List<Voucher> vouchers = voucherDAO.getAll();

            request.setAttribute("users", users);
            request.setAttribute("staffs", staffs);
            request.setAttribute("vouchers", vouchers);

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
