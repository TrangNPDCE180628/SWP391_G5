package Controllers;

import DAOs.FeedbackDAO;
import DAOs.FeedbackReplyViewDAO;
import DAOs.OrderDAO;
import DAOs.ProductDAO;
import DAOs.ProductAttributeDAO;
import DAOs.ReplyFeedbackDAO;
import Models.Feedback;
import Models.FeedbackReplyView;
import Models.Product;
import Models.ProductAttribute;
import Models.ReplyFeedback;
import Models.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product-detail"})
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productId = request.getParameter("id");
            if (productId == null || productId.trim().isEmpty()) {
                // [THÊM MỚI]: Xử lý trường hợp id không hợp lệ
                request.setAttribute("error", "Invalid product ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
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
            }

            if (loginUser != null) {
                String cusId = loginUser.getId();

                OrderDAO orderDAO = new OrderDAO();
                boolean hasBought = orderDAO.checkOrderStatus(cusId, productId);

                FeedbackDAO feedbackDAO = new FeedbackDAO();
                boolean hasFeedback = feedbackDAO.hasWrittenFeedback(cusId, productId);

                request.setAttribute("hasBought", hasBought);
                request.setAttribute("hasFeedback", hasFeedback);
            }

            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getById(productId);

            if (product != null) {
                // [THÊM MỚI]: Lấy danh sách thuộc tính sản phẩm
                ProductAttributeDAO attributeDAO = new ProductAttributeDAO();
                List<ProductAttribute> attributes = attributeDAO.getByProductId(productId);

                FeedbackReplyViewDAO feedbackReplyViewDAO = new FeedbackReplyViewDAO();
                List<FeedbackReplyView> FeedbackReplyViews = feedbackReplyViewDAO.getFeedbackRepliesByProduct(productId);

                request.setAttribute("viewfeedbacks", FeedbackReplyViews);
                request.setAttribute("product", product);
                request.setAttribute("attributes", attributes);
                request.getRequestDispatcher("product-detail.jsp").forward(request, response);
            } else {
                // [CẬP NHẬT]: Chuyển hướng đến trang lỗi thay vì product-list
                request.setAttribute("error", "Product not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // [CẬP NHẬT]: Chuyển hướng đến trang lỗi với thông báo
            request.setAttribute("error", "Error loading product details: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            handleCreateFeedback(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    private void handleCreateFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cusId = request.getParameter("cusId");
            String proId = request.getParameter("proId");
            String content = request.getParameter("content");
            int rate = Integer.parseInt(request.getParameter("rate"));

            Feedback feedback = new Feedback();
            feedback.setCusId(cusId);
            feedback.setProId(proId);
            feedback.setContent(content);
            feedback.setRate(rate);

            FeedbackDAO dao = new FeedbackDAO();
            dao.insertFeedback(feedback);

            response.sendRedirect("product-detail?id=" + proId);
        } catch (Exception e) {
            request.setAttribute("error", "Failed to submit feedback: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

}
