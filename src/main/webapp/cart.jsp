<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Shopping Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="HomeController">Shopping Mall</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="HomeController">Continue Shopping</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2>Your Shopping Cart</h2>
        
        <!-- Show error message if exists -->
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Show success message if exists -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("message"); %>
        </c:if>
        
        <c:if test="${empty sessionScope.cart}">
            <div class="alert alert-info">
                <i class="bi bi-info-circle"></i> Your cart is empty.
                <a href="HomeController" class="btn btn-primary ms-3">Continue Shopping</a>
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.cart}">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="total" value="0" />
                        <c:forEach var="item" items="${sessionScope.cart}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <span>${item.value.productName}</span>
                                    </div>
                                </td>
                                <td>$${item.value.price}</td>
                                <td style="width: 200px;">
                                    <div class="d-flex align-items-center">
                                        <form action="CartController" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="productId" value="${item.key}">
                                            <input type="hidden" name="change" value="-1">
                                            <button type="submit" class="btn btn-sm btn-outline-secondary">
                                                <i class="bi bi-dash"></i>
                                            </button>
                                        </form>
                                        
                                        <span class="mx-3">${item.value.quantity}</span>
                                        
                                        <form action="CartController" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="productId" value="${item.key}">
                                            <input type="hidden" name="change" value="1">
                                            <button type="submit" class="btn btn-sm btn-outline-secondary">
                                                <i class="bi bi-plus"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                                <td>$${item.value.price * item.value.quantity}</td>
                                <td>
                                    <form action="CartController" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${item.key}">
                                        <button type="submit" class="btn btn-sm btn-danger">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                            <c:set var="total" value="${total + (item.value.price * item.value.quantity)}" />
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-end"><strong>Total:</strong></td>
                            <td><strong>$${total}</strong></td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            
            <div class="d-flex justify-content-between mt-4">
                <a href="HomeController" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Continue Shopping
                </a>
                <a href="OrderController?action=checkout" class="btn btn-primary">
                    Proceed to Checkout <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 