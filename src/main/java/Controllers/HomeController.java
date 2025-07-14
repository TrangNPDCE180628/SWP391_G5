package Controllers;

import DAOs.FeedbackDAO;
import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.ReplyFeedbackDAO;
import Models.Feedback;
import Models.Product;
import Models.ProductTypes;
import Models.ReplyFeedback;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeController", urlPatterns = {"/home", "/HomeController", ""})
public class HomeController extends HttpServlet {

    private static final int PRODUCTS_PER_PAGE = 8;
    private static final String DEFAULT_IMAGE = "images/products/default.jpg";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            ProductDAO productDAO = new ProductDAO();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            List<ProductTypes> productTypes = productTypeDAO.getAllProductTypes();
            request.setAttribute("productTypes", productTypes);

            String pageStr = request.getParameter("page");
            String typeIdStr = request.getParameter("typeId");
            String searchTerm = request.getParameter("searchTerm");

            int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
            int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : 0;

            List<Product> products;
            int totalProducts;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                products = productDAO.searchByName(searchTerm, page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countSearchResults(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
            } else if (typeId > 0) {
                products = productDAO.getByCategoryId(typeId, page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countByCategoryId(typeId);
                request.setAttribute("selectedTypeId", typeId);
            } else {
                products = productDAO.getAll(page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countAll();
            }

            int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
                        
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            HttpSession session = request.getSession();
            Integer cartSize = (Integer) session.getAttribute("cartSize");
            if (cartSize == null) {
                session.setAttribute("cartSize", 0);
            }

            request.getRequestDispatcher("home.jsp").forward(request, response);

        } catch (Exception e) {
            log("Error at HomeController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the product list. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
