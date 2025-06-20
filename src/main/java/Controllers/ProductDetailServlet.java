package Controllers;

import DAOs.ProductDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import Models.Product;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product-detail"})
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getById(productId);
            
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("product-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("product-list");
            }
        } catch (Exception e) {
            response.sendRedirect("product-list");
        }
    }
} 