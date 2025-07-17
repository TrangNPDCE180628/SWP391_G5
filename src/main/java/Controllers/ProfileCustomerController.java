package Controllers;

import DAOs.CustomerDAO;
import Models.Customer;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
@MultipartConfig
@WebServlet(name = "ProfileCustomerController", urlPatterns = {"/ProfileCustomerController"})
public class ProfileCustomerController extends HttpServlet {

    private static final String PROFILE_PAGE = "profileCustomer.jsp";
    private static final String LOGIN_PAGE = "login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        // Load lại thông tin từ DB (đảm bảo dữ liệu mới nhất)
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        System.out.println("LOGIN_USER: " + session.getAttribute("LOGIN_USER"));
        System.out.println("Login User ID: " + loginUser.getId());

        try {
            CustomerDAO dao = new CustomerDAO();
            Customer freshCustomer = dao.getCustomerById(loginUser.getId());
            request.setAttribute("customer", freshCustomer);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi khi tải thông tin khách hàng");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }
        Part imagePart = request.getPart("imageFile");
        String imagePath;
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/images/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            imagePart.write(uploadPath + "/" + fileName);
            imagePath = "images/uploads/" + fileName;
        } else {
            imagePath = request.getParameter("existingImage");
        }

        try {
            // Lấy dữ liệu từ form
            String cusId = request.getParameter("cusId");
            String fullName = request.getParameter("cusFullName");
            String gender = request.getParameter("cusGender");
            String image = imagePath;
            String email = request.getParameter("cusGmail");
            String phone = request.getParameter("cusPhone");
            String oldPassword = request.getParameter("oldPassword"); // từ modal
            String newPassword = request.getParameter("cusPassword");
            // Cập nhật vào DB
            CustomerDAO dao = new CustomerDAO();
            Customer customer = dao.getCustomerById(cusId);
            customer.setCusFullName(fullName);
            customer.setCusGender(gender);
            customer.setCusImage(image);
            customer.setCusGmail(email);
            customer.setCusPhone(phone);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (oldPassword == null || !oldPassword.equals(customer.getCusPassword())) {
                    // Sai mật khẩu cũ
                    request.setAttribute("error", "Mật khẩu cũ không đúng. Vui lòng thử lại.");
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                    return;
                } else {
                    customer.setCusPassword(newPassword);
                }
            }


            dao.updateCustomer(customer);

            // Cập nhật session nếu cần
            session.setAttribute("LOGIN_USER", customer);

            // Forward về lại trang profile với thông báo thành công
            request.setAttribute("customer", customer);
            request.setAttribute("message", "Cập nhật thành công!");
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật.");
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        }
    }
}
