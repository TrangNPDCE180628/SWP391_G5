package Controllers;

import DAOs.ProductDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import Models.Product;

@WebServlet(name = "UpdateProductServlet", urlPatterns = {"/update-product"})
public class UpdateProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getById(productId);
            
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("update-product.jsp").forward(request, response);
            } else {
                response.sendRedirect("product-list");
            }
        } catch (Exception e) {
            response.sendRedirect("product-list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String image = request.getParameter("image");
            int typeId = Integer.parseInt(request.getParameter("typeId"));

            Product product = new Product();
            product.setProductId(productId);
            product.setProductName(productName);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImage(image);
            product.setTypeId(typeId);
            product.setUpdatedAt(Date.valueOf(LocalDate.now()));
            product.setStatus(true);

            ProductDAO productDAO = new ProductDAO();
            productDAO.update(product);
            response.sendRedirect("product-list");
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("update-product.jsp").forward(request, response);
        }
    }
} 