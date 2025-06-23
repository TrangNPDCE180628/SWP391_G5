package Controllers;

import DAOs.CustomerDAO;

import Models.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {

    private static final String ERROR = "register.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Lấy dữ liệu từ form
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String gmail = request.getParameter("gmail");
            String phone = request.getParameter("phone");

            // Validate input
            if (username == null || username.trim().isEmpty()
                    || password == null || password.trim().isEmpty()
                    || fullname == null || fullname.trim().isEmpty()
                    || gender == null || gender.trim().isEmpty()
                    || gmail == null || gmail.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()) {

                request.setAttribute("ERROR", "All fields are required");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            CustomerDAO dao = new CustomerDAO();

            // Check if username already exists
            if (dao.checkUsernameExists(username)) {
                request.setAttribute("ERROR", "Username already exists!");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            // Tạo đối tượng customer
            Customer user = new Customer();
            user.setCusId(dao.generateNextCusId());
            user.setUsername(username);
            user.setCusPassword(password);
            user.setCusFullName(fullname);
            user.setCusGender(gender);
            user.setCusGmail(gmail);
            user.setCusPhone(phone);
            user.setCusImage("default.png"); // Nếu không có upload ảnh, có thể set null hoặc ảnh mặc định

            dao.insertCustomer(user);

            // Set success
            HttpSession session = request.getSession();
            session.setAttribute("SUCCESS", "Registration successful! Please login.");

            // Redirect
            request.getRequestDispatcher(SUCCESS).forward(request, response);

        } catch (Exception e) {
            log("Error at RegisterController: " + e.toString());
            request.setAttribute("ERROR", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(ERROR); // Redirect GET requests to register page
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Register Controller";
    }
}
