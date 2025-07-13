package Controllers;

import DAOs.ProductDAO;
import Models.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductManagerController", urlPatterns = {"/ProductManagerController"})
public class ProductManagerController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String ADMIN_PAGE = "productManager.jsp";
    private final ProductDAO productDAO = new ProductDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = ERROR;

        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "list":
                    url = listProducts(request);
                    break;
                case "add":
                    url = addProduct(request);
                    break;
                case "edit":
                    url = editProduct(request);
                    break;
                case "delete":
                    url = deleteProduct(request);
                    break;
                case "search":
                    url = searchProducts(request);
                    break;
                default:
                    url = listProducts(request);
                    break;
            }
        } catch (Exception e) {
            log("Error at ProductManagerController: " + e.toString());
            request.setAttribute("ERROR", "System error occurred!");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private String listProducts(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Product> products = productDAO.getAll(page, recordsPerPage);
        int totalRecords = productDAO.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("products", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        return ADMIN_PAGE;
    }

    private String addProduct(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        try {
            String proId = request.getParameter("proId");
            if (proId == null || proId.trim().isEmpty()) {
                request.setAttribute("ERROR", "Product ID is required!");
                return listProducts(request);
            }

            if (productDAO.getById(proId) != null) {
                request.setAttribute("ERROR", "Product ID already exists!");
                return listProducts(request);
            }

            Product product = parseProductFromRequest(request);
            productDAO.insertProduct(product);
            request.setAttribute("SUCCESS", "Product added successfully!");

        } catch (NumberFormatException e) {
            request.setAttribute("ERROR", "Invalid number format!");
        } catch (Exception e) {
            request.setAttribute("ERROR", "Error adding product: " + e.getMessage());
        }

        return listProducts(request);
    }

    private String editProduct(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        try {
            String proId = request.getParameter("proId");
            if (proId == null || proId.trim().isEmpty()) {
                request.setAttribute("ERROR", "Product ID is required!");
                return listProducts(request);
            }

            Product product = parseProductFromRequest(request);
            productDAO.updateProduct(product);
            request.setAttribute("SUCCESS", "Product updated successfully!");

        } catch (NumberFormatException e) {
            request.setAttribute("ERROR", "Invalid number format!");
        } catch (Exception e) {
            request.setAttribute("ERROR", "Error updating product: " + e.getMessage());
        }

        return listProducts(request);
    }

    private String deleteProduct(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String proId = request.getParameter("proId");

        if (proId == null || proId.trim().isEmpty()) {
            request.setAttribute("ERROR", "Product ID is required!");
            return listProducts(request);
        }

        try {
            productDAO.deleteProduct(proId);
            request.setAttribute("SUCCESS", "Product deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("ERROR", "Error deleting product: " + e.getMessage());
        }

        return listProducts(request);
    }

    private String searchProducts(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String searchTerm = request.getParameter("searchTerm");
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listProducts(request);
        }

        List<Product> products = productDAO.searchByName(searchTerm, page, recordsPerPage);
        int totalRecords = productDAO.countSearchResults(searchTerm);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("products", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("searchTerm", searchTerm);

        return ADMIN_PAGE;
    }

    private Product parseProductFromRequest(HttpServletRequest request) throws NumberFormatException {
        String proId = request.getParameter("proId");
        String proName = request.getParameter("proName");
        String proDescription = request.getParameter("proDescription");
        BigDecimal proPrice = new BigDecimal(request.getParameter("proPrice"));
        String proImageMain = request.getParameter("proImageUrl");
        int proTypeId = Integer.parseInt(request.getParameter("proTypeId"));

        return new Product(proId, proTypeId, proName, proDescription, proPrice, proImageMain);
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

    @Override
    public String getServletInfo() {
        return "Product Manager Controller";
    }
}
