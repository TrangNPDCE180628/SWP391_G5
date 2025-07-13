<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="Models.Order" %>
<%@ page import="Models.OrderDetail" %>
<%@ page import="java.util.List" %>
<%
    String message = (String) session.getAttribute("message");
    Order order = (Order) session.getAttribute("order");
    List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("orderDetails");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Thanh toán thành công</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="css/payment.css" rel="stylesheet" />

    </head>
    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
            <div class="container py-2">
                <a class="navbar-brand" href="HomeController">
                    <i class="fas fa-microchip me-2"></i>Tech Store
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">

                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="CartController?action=view" title="View Cart">
                                <i class="fas fa-shopping-cart fa-lg"></i>
                                <c:if test="${sessionScope.cartSize > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                        ${sessionScope.cartSize}
                                    </span>
                                </c:if>
                            </a>
                        </li>
                        <c:choose>
                            <c:when test="${not empty sessionScope.LOGIN_USER}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                       data-bs-toggle="dropdown">
                                        <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="#">Profile</a></li>
                                        <li><a class="dropdown-item" href="OrderController?action=view">My Orders</a></li>
                                            <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="MainController?action=Logout">Logout</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
                                <li class="nav-item"><a class="nav-link" href="register.jsp">Register</a></li>
                                </c:otherwise>
                            </c:choose>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container py-5 text-center">

            <div class="alert alert-success">
                <h2>Thanh toán thành công!</h2>
                <p><%= message != null ? message : "Cảm ơn bạn đã mua hàng!"%></p>
                <p>Mã đơn hàng: <%= order != null ? order.getOrderId() : "?"%></p>
                <a href="${pageContext.request.contextPath}/CartController?action=view" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Quay lại giỏ hàng
                </a>
            </div>

            <% if (details != null && !details.isEmpty()) { %>
            <h4 class="mt-4">Chi tiết đơn hàng</h4>
            <table class="table table-bordered mt-3">
                <thead>
                    <tr>
                        <th>Mã sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        double total = 0;
                        for (OrderDetail d : details) {
                            double lineTotal = d.getUnitPrice() * d.getQuantity();
                            total += lineTotal;
                    %>
                    <tr>
                        <td><%= d.getProId()%></td>
                        <td><%= d.getQuantity()%></td>
                        <td><%= String.format("%,.0f₫", d.getUnitPrice())%></td>
                        <td><%= String.format("%,.0f₫", lineTotal)%></td>
                    </tr>
                    <% }%>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="3" class="text-end">Tổng:</th>
                        <th><%= String.format("%,.0f₫", total)%></th>
                    </tr>
                    <tr>
                        <th colspan="3" class="text-end">Giảm giá:</th>
                        <th><%= order != null ? String.format("%,.0f₫", order.getDiscountAmount()) : "0₫"%></th>
                    </tr>
                    <tr>
                        <th colspan="3" class="text-end">Thành tiền:</th>
                        <th><%= order != null ? String.format("%,.0f₫", order.getFinalAmount()) : "?"%></th>
                    </tr>
                </tfoot>
            </table>
            <% } %>
        </div>

        <%
            session.removeAttribute("message");
            session.removeAttribute("order");
            session.removeAttribute("orderDetails");
        %>

        <!-- Footer -->
        <footer class="bg-dark text-light py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h5><i class="fas fa-microchip me-2"></i>Tech Store</h5>
                        <p>Your ultimate destination for quality tech products.</p>
                    </div>
                    <div class="col-md-4">
                        <h5>Quick Links</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>About Us</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>Contact</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>FAQs</a></li>
                        </ul>
                    </div>
                    <div class="col-md-4">
                        <h5>Connect With Us</h5>
                        <div class="social-links">
                            <a href="#" class="text-light me-3"><i class="fab fa-facebook fa-lg"></i></a>
                            <a href="#" class="text-light me-3"><i class="fab fa-instagram fa-lg"></i></a>
                            <a href="#" class="text-light me-3"><i class="fab fa-twitter fa-lg"></i></a>
                        </div>
                    </div>
                </div>
                <hr class="mt-4">
                <div class="text-center">
                    <small>© 2025 Tech Store. All rights reserved.</small>
                </div>
            </div>
        </footer>
    </body>
</html>
