package Controllers;

import DAOs.ProductDAO;
import DAOs.ProductAttributeDAO;
import Models.Product;
import Models.ProductAttribute;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductById(productId);

            if (product != null) {
                // [THÊM MỚI]: Lấy danh sách thuộc tính sản phẩm
                ProductAttributeDAO attributeDAO = new ProductAttributeDAO();
                List<ProductAttribute> attributes = attributeDAO.getByProductId(productId);

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
}