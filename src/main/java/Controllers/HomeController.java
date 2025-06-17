package Controllers;

import DAOs.ProductDAO;
import DAOs.ProductTypeDAO;
import DAOs.ProductSpecDAO;
import Models.Product;
import Models.ProductTypes;
import Models.ProductSpecification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "HomeController", urlPatterns = {"/home", "/HomeController", ""})
public class HomeController extends HttpServlet {

    private static final int PRODUCTS_PER_PAGE = 8;
    private static final String DEFAULT_IMAGE = "images/products/default.jpg";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Initialize DAOs
            ProductDAO productDAO = new ProductDAO();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            ProductSpecDAO productSpecDAO = new ProductSpecDAO();

            // Get parameters
            String pageStr = request.getParameter("page");
            String typeIdStr = request.getParameter("typeId");
            String searchTerm = request.getParameter("searchTerm");
            String sortBy = request.getParameter("sortBy");

            // Set default values
            int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
            int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : 0;

            // Get all product types for filter
            List<ProductTypes> productTypes = productTypeDAO.getAll();
            request.setAttribute("productTypes", productTypes);

            // Get products based on filters
            List<Product> products;
            int totalProducts;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                products = productDAO.searchByName(searchTerm, page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countSearchResults(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
            } else if (typeId > 0) {
                products = productDAO.getByTypeId(typeId, page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countByTypeId(typeId);
                request.setAttribute("selectedTypeId", typeId);
            } else {
                products = productDAO.getAll(page, PRODUCTS_PER_PAGE);
                totalProducts = productDAO.countAll();
            }

            // Gán thông số kỹ thuật vào mỗi sản phẩm
            for (Product p : products) {
                try {
                    ProductSpecification spec;
                    spec = productSpecDAO.getByProductId(p.getProId());
                    p.setSpecification(spec);
                } catch (Exception ex) {
                    log("Unable to load spec for product ID " + p.getProId() + ": " + ex.getMessage());
                }
            }

            // Pagination
            int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);

            // Set request attributes
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            // Get cart size from session
            HttpSession session = request.getSession();
            Integer cartSize = (Integer) session.getAttribute("cartSize");
            if (cartSize == null) {
                session.setAttribute("cartSize", 0);
            }

            // Forward to JSP
            request.getRequestDispatcher("home.jsp").forward(request, response);

        } catch (Exception e) {
            log("Error at HomeController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the products. Please try again later.");
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
