/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.vnpay.common;

import DAOs.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import Models.*;
import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "VnpayReturn", urlPatterns = {"/vnpayReturn"})
public class VnpayReturn extends HttpServlet {

    OrderDAO orderDao = new OrderDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fields.put(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()),
                            URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            String signValue = Config.hashAllFields(fields);

            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");
                String orderIdStr = request.getParameter("vnp_TxnRef");
                int orderId = Integer.parseInt(orderIdStr);

                // Lấy order từ DB để có đầy đủ thông tin
                Order order = orderDao.getById(orderId);
                if (order == null) {
                    request.setAttribute("transResult", false);
                    request.setAttribute("message", "Order not found!");
                    request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                    return;
                }

                boolean transSuccess = false;
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    order.setOrderStatus("Completed");

                    // 1. Giảm số lượng voucher (nếu có)
                    if (order.getVoucherId() != null) {
                        VoucherDAO vDao = new VoucherDAO();
                        Voucher usedV = vDao.getById(order.getVoucherId());
                        if (usedV != null && usedV.getQuantity() > 0) {
                            int newQty = usedV.getQuantity() - 1;
                            vDao.updateQuantity(usedV.getVoucherId(), newQty);
                            if (newQty == 0) {
                                usedV.setQuantity(0);
                                usedV.setVoucherActive(false);
                                vDao.update(usedV);
                            }
                        }
                    }

                    // 2. Lấy chi tiết đơn hàng và xóa sản phẩm khỏi giỏ
                    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                    List<OrderDetail> details = orderDetailDAO.getByOrderId(order.getOrderId());
                    ProductDAO productDAO = new ProductDAO();
                    Map<String, String> productNames = new HashMap<>();
                    for (OrderDetail d : details) {
                        Product p = productDAO.getById(d.getProId());
                        if (p != null) {
                            productNames.put(d.getProId(), p.getProName());
                        }
                    }

                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, ViewCartCustomer> cart = (Map<String, ViewCartCustomer>) session.getAttribute("cart");
                        CartDAO cartDAO = new CartDAO();
                        if (cart != null) {
                            for (OrderDetail d : details) {
                                cart.remove(d.getProId());
                                cartDAO.removeItem(order.getCusId(), d.getProId());
                            }
                            session.setAttribute("cart", cart);
                            int cartSize = cart.values().stream().mapToInt(ViewCartCustomer::getQuantity).sum();
                            session.setAttribute("cartSize", cartSize);
                        }
                        session.setAttribute("order", order);
                        session.setAttribute("orderDetails", details);
                        session.setAttribute("productNames", productNames);
                        session.setAttribute("message", "Order payment successful #" + order.getOrderId());
                    }

                    // 3. Trừ tồn kho
                    StockDAO stockDAO = new StockDAO();
                    for (OrderDetail d : details) {
                        stockDAO.decreaseStockAfterOrder(d.getProId(), d.getQuantity());
                    }

                    transSuccess = true;
                } else {
                    order.setOrderStatus("Failed");
                }

                orderDao.updateStatus(order);
                request.setAttribute("transResult", transSuccess);
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
            } else {
                //RETURN PAGE ERROR
                request.setAttribute("transResult", false);
                request.setAttribute("message", "Invalid signature. Transaction not processed.");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
            }
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
