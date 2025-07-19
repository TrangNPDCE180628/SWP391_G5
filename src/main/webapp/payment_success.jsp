<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Payment successful</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="css/payment.css" rel="stylesheet" />
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
            <div class="container py-2">
                <a class="navbar-brand" href="HomeController">
                    <i class="fas fa-microchip me-2"></i>Tech Store
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <form class="d-flex search-form mx-auto" action="HomeController" method="GET">
                        <input class="form-control me-2" type="search" name="searchTerm"
                               placeholder="Search for products..." value="${searchTerm}">
                        <button class="btn btn-outline-success" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </form>
                    <ul class="navbar-nav ms-auto">
                        <!-- Cart icon (moved up) -->
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="CartController?action=view" title="Xem giỏ hàng">
                                <i class="fas fa-shopping-cart fa-lg"></i>
                                <c:if test="${sessionScope.cartSize > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                        ${sessionScope.cartSize}
                                    </span>
                                </c:if>
                            </a>
                        </li>

                        <!-- Account dropdown -->
                        <c:choose>
                            <c:when test="${not empty sessionScope.LOGIN_USER}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                       data-bs-toggle="dropdown">
                                        <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="ProfileCustomerController">Profile</a></li>

                                        <!-- Orders (moved below Cart) -->
                                        <li class="nav-item">
                                            <a class="nav-link" href="OrderController?action=view">
                                                My Orders
                                            </a>
                                        </li>
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
                <h2>Payment successful!</h2>
                <p>${sessionScope.message != null ? sessionScope.message : "Thank you for your purchase!"}</p>
                <p>Order code: ${sessionScope.order != null ? sessionScope.order.orderId : "?"}</p>
                <a href="${pageContext.request.contextPath}/CartController?action=view" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Back to cart
                </a>
            </div>

            <c:if test="${not empty sessionScope.orderDetails}">
                <h4 class="mt-4">Order details</h4>
                <table class="table table-bordered mt-3">
                    <thead>
                        <tr>
                            <th>Product name</th>
                            <th>Quantity</th>
                            <th>Unit price</th>
                            <th>Total amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="total" value="0" />
                        <c:forEach var="d" items="${sessionScope.orderDetails}">
                            <c:set var="lineTotal" value="${d.unitPrice * d.quantity}" />
                            <c:set var="total" value="${total + lineTotal}" />
                            <tr>
                                <td>${sessionScope.productNames[d.proId]}</td>
                                <td>${d.quantity}</td>
                                <td><fmt:formatNumber value="${d.unitPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫</td>
                                <td><fmt:formatNumber value="${lineTotal}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <th colspan="3" class="text-end">Total:</th>
                            <th><fmt:formatNumber value="${total}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫</th>
                        </tr>
                        <tr>
                            <th colspan="3" class="text-end">Discount:</th>
                            <th>
                                <fmt:formatNumber value="${sessionScope.order.discountAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </th>
                        </tr>
                        <tr>
                            <th colspan="3" class="text-end">Total amount:</th>
                            <th>
                                <fmt:formatNumber value="${sessionScope.order.finalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>₫
                            </th>
                        </tr>
                    </tfoot>
                </table>
            </c:if>
        </div>

        <c:remove var="message" scope="session"/>
        <c:remove var="order" scope="session"/>
        <c:remove var="orderDetails" scope="session"/>
        <c:remove var="productNames" scope="session"/>


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
