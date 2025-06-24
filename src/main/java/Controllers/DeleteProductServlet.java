//package Controllers;
//
//import DAOs.ProductDAO;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet(name = "DeleteProductServlet", urlPatterns = {"/delete-product"})
//public class DeleteProductServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            int productId = Integer.parseInt(request.getParameter("id"));
//            ProductDAO productDAO = new ProductDAO();
//            productDAO.delete(productId);
//            response.sendRedirect("product-list");
//        } catch (Exception e) {
//            request.setAttribute("error", "Error: " + e.getMessage());
//            request.getRequestDispatcher("product-list").forward(request, response);
//        }
//    }
//} 