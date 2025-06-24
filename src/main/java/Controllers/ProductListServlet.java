//package Controllers;
//
//import DAOs.ProductDAO;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//import Models.Product;
//
//@WebServlet(name = "ProductListServlet", urlPatterns = {"/product-list"})
//public class ProductListServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            ProductDAO productDAO = new ProductDAO();
//            List<Product> products = productDAO.getAll();
//            request.setAttribute("products", products);
//            request.getRequestDispatcher("product-list.jsp").forward(request, response);
//        } catch (Exception e) {
//            request.setAttribute("error", "Error: " + e.getMessage());
//            request.getRequestDispatcher("product-list.jsp").forward(request, response);
//        }
//    }
//} 