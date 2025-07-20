package Controllers;

import DAOs.VoucherDAO;
import Models.Voucher;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "VoucherController", urlPatterns = {"/VoucherController"})
public class VoucherController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("AdminController?tab=vouchers");
            return;
        }

        try {
            switch (action) {
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
                    response.getWriter().println("Unknown action: " + action);
                    break;
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "An error occurred: " + e.getMessage());
            response.sendRedirect("AdminController?tab=vouchers");
        }
    }

    private void addVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal maxDiscountValue = new BigDecimal(request.getParameter("maxDiscountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            if (quantity < 0) {
                request.getSession().setAttribute("error", "Quantity cannot be negative.");
                request.getSession().setAttribute("openAddModal", true);
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            if (!startDate.before(endDate)) {
                request.getSession().setAttribute("error", "Start date must be before end date.");
                request.getSession().setAttribute("openAddModal", true);
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            if (endDate.toLocalDate().isBefore(LocalDate.now())) {
                isActive = false;
            }

            VoucherDAO dao = new VoucherDAO();

            if (dao.isCodeNameExists(codeName)) {
                request.getSession().setAttribute("error", "Voucher code already exists.");
                request.getSession().setAttribute("openAddModal", true);
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            Voucher voucher = new Voucher(0, codeName, description, discountType,
                    discountValue, maxDiscountValue, minOrderAmount, startDate, endDate, isActive, quantity);

            dao.create(voucher);

            request.getSession().setAttribute("success", "Voucher added successfully.");
            response.sendRedirect("AdminController?tab=vouchers");

        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to add voucher: " + e.getMessage());
            request.getSession().setAttribute("openAddModal", true);
            response.sendRedirect("AdminController?tab=vouchers");
        }
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("voucherId"));
            String codeName = request.getParameter("codeName");
            String description = request.getParameter("voucherDescription");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String discountType = request.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(request.getParameter("discountValue"));
            BigDecimal maxDiscountValue = new BigDecimal(request.getParameter("maxDiscountValue"));
            BigDecimal minOrderAmount = new BigDecimal(request.getParameter("minOrderAmount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("voucherActive"));

            VoucherDAO dao = new VoucherDAO();

            // Validate input
            if (quantity < 0) {
                request.getSession().setAttribute("editError", "Quantity cannot be negative.");
                request.getSession().setAttribute("openEditModalId", id);
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            if (!startDate.before(endDate)) {
                request.getSession().setAttribute("editError", "Start date must be before end date.");
                request.getSession().setAttribute("openEditModalId", id);
                response.sendRedirect("AdminController?tab=vouchers");
                return;
            }

            if (endDate.toLocalDate().isBefore(LocalDate.now())) {
                isActive = false;
            }

            // Check trùng mã (codeName) cho voucher khác
            // Lấy voucher cũ để so sánh
            Voucher oldVoucher = dao.getById(id);

            // Nếu đổi sang codeName khác => mới check trùng
            if (dao.isCodeNameExistsForOtherId(codeName, id)) {
                Voucher voucher = new Voucher(id, codeName, description, discountType,
                        discountValue, maxDiscountValue, minOrderAmount, startDate, endDate, isActive, quantity);
                request.getSession().setAttribute("editError", "Voucher code already exists.");
                request.getSession().setAttribute("editVoucherData", voucher);
                request.getSession().setAttribute("openEditModalId", id);
                response.sendRedirect("AdminController?tab=vouchers");

                return;
            }

            // Cập nhật
            Voucher voucher = new Voucher(id, codeName, description, discountType,
                    discountValue, maxDiscountValue, minOrderAmount, startDate, endDate, isActive, quantity);

            dao.update(voucher);

            request.getSession().setAttribute("success", "Voucher updated successfully.");
            response.sendRedirect("AdminController?tab=vouchers");

        } catch (Exception e) {
            request.getSession().setAttribute("editError", "Failed to update voucher: " + e.getMessage());
            request.getSession().setAttribute("openEditModalId", request.getParameter("voucherId"));
            response.sendRedirect("AdminController?tab=vouchers");
        }
    }

    private void deleteVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            new VoucherDAO().delete(id);
            response.sendRedirect("AdminController?tab=vouchers");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to delete voucher: " + e.getMessage());
            response.sendRedirect("AdminController?tab=vouchers");
        }
    }

    private void getVoucherDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Voucher voucher = new VoucherDAO().getById(id);

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
