<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order Details #${order.orderId} - Tech Store</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .navbar {
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
            }
            .navbar-brand {
                font-weight: 600;
                color: #333;
            }
            .order-details-container {
                background-color: white;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-top: 20px;
            }
            .order-header {
                background: linear-gradient(135deg, #007bff, #0056b3);
                color: white;
                padding: 25px;
                border-radius: 8px 8px 0 0;
            }
            .order-header h2 {
                margin: 0;
                font-weight: 600;
            }
            .order-info-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                padding: 25px;
            }
            .info-card {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                border-left: 4px solid #007bff;
            }
            .info-card h6 {
                color: #007bff;
                font-weight: 600;
                margin-bottom: 10px;
                text-transform: uppercase;
                font-size: 0.85rem;
            }
            .info-card p {
                margin: 5px 0;
                color: #333;
            }
            .status-badge {
                display: inline-block;
                padding: 6px 15px;
                border-radius: 20px;
                font-size: 0.85rem;
                font-weight: 600;
                text-transform: uppercase;
            }
            .status-completed {
                background-color: #d4edda;
                color: #155724;
            }
            .status-shipping {
                background-color: #d1ecf1;
                color: #0c5460;
            }
            .status-pending {
                background-color: #fff3cd;
                color: #856404;
            }
            .status-cancelled {
                background-color: #f8d7da;
                color: #721c24;
            }
            .status-processing {
                background-color: #cce7ff;
                color: #004085;
            }
            .product-list {
                padding: 0;
            }
            .product-item {
                display: flex;
                align-items: center;
                padding: 20px 25px;
                border-bottom: 1px solid #dee2e6;
                transition: background-color 0.2s ease;
            }
            .product-item:hover {
                background-color: #f8f9fa;
            }
            .product-item:last-child {
                border-bottom: none;
            }
            .product-image {
                width: 80px;
                height: 80px;
                object-fit: cover;
                border-radius: 8px;
                margin-right: 20px;
                border: 1px solid #dee2e6;
            }
            .product-info {
                flex: 1;
            }
            .product-name {
                font-weight: 600;
                color: #333;
                margin-bottom: 5px;
                font-size: 1.1rem;
            }
            .product-id {
                color: #6c757d;
                font-size: 0.9rem;
                margin-bottom: 5px;
            }
            .product-price {
                color: #007bff;
                font-weight: 600;
            }
            .quantity-info {
                text-align: center;
                min-width: 100px;
                margin: 0 20px;
            }
            .quantity-label {
                color: #6c757d;
                font-size: 0.85rem;
                margin-bottom: 5px;
            }
            .quantity-value {
                font-size: 1.2rem;
                font-weight: 600;
                color: #333;
            }
            .price-info {
                text-align: right;
                min-width: 120px;
            }
            .unit-price {
                color: #6c757d;
                font-size: 0.9rem;
                margin-bottom: 5px;
            }
            .subtotal {
                font-size: 1.1rem;
                font-weight: 600;
                color: #e74c3c;
            }
            .voucher-section {
                background: linear-gradient(135deg, #28a745, #20c997);
                color: white;
                margin: 0 25px;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .voucher-icon {
                font-size: 1.5rem;
                margin-right: 15px;
            }
            .voucher-info h6 {
                margin: 0;
                font-weight: 600;
            }
            .voucher-desc {
                opacity: 0.9;
                margin: 5px 0 0 0;
            }
            .total-section {
                background-color: #f8f9fa;
                padding: 25px;
                border-top: 1px solid #dee2e6;
            }
            .total-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 10px;
            }
            .total-row:last-child {
                margin-bottom: 0;
                padding-top: 15px;
                border-top: 2px solid #dee2e6;
                font-size: 1.2rem;
                font-weight: 600;
            }
            .total-label {
                color: #333;
            }
            .total-value {
                font-weight: 600;
            }
            .final-total {
                color: #e74c3c;
                font-size: 1.3rem;
            }
            .discount-value {
                color: #28a745;
            }
            .back-btn {
                margin: 20px 0;
            }
            .section-title {
                background-color: #f8f9fa;
                padding: 15px 25px;
                margin: 0;
                font-weight: 600;
                border-bottom: 1px solid #dee2e6;
                color: #333;
            }
            .payment-method-badge {
                background-color: #e9ecef;
                color: #495057;
                padding: 4px 12px;
                border-radius: 15px;
                font-size: 0.85rem;
                font-weight: 500;
            }
            .review-action {
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .review-action .btn {
                white-space: nowrap;
            }
            @media (max-width: 768px) {
                .order-info-grid {
                    grid-template-columns: 1fr;
                }
                .product-item {
                    flex-direction: column;
                    text-align: center;
                }
                .product-image {
                    margin: 0 0 15px 0;
                }
                .quantity-info,
                .price-info {
                    margin: 10px 0;
                }
                .review-action {
                    margin-top: 15px;
                    width: 100%;
                }
                .review-action .btn {
                    width: 200px;
                }
            }
        </style>
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
                            <a class="nav-link position-relative" href="CartController?action=view" title="View Cart">
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
                                            <a class="nav-link" href="OrderHistoryController">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin' or sessionScope.LOGIN_USER.role eq 'Staff'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutController">
                                                <i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><a class="dropdown-item" href="login.jsp">Login</a></li>
                                <li><a class="dropdown-item" href="register.jsp">Register</a></li>
                                </c:otherwise>
                            </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container py-4">
            <!-- Back Button -->
            <div class="back-btn">
                <a href="${pageContext.request.contextPath}/order-history" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Back
                </a>
            </div>

            <div class="order-details-container">
                <!-- Order Header -->
                <div class="order-header">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2><i class="fas fa-receipt me-3"></i>Order Details #${order.orderId}</h2>
                            <p class="mb-0 opacity-75">
                                <i class="fas fa-calendar me-2"></i>
                                Order Date: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                            </p>
                        </div>
                        <div class="text-end">
                            <span class="status-badge status-${order.orderStatus.toLowerCase().replace(' ', '-')}">
                                ${order.orderStatus}
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Order Information Grid -->
                <div class="order-info-grid">
                    <!-- Customer Info -->
                    <div class="info-card">
                        <h6><i class="fas fa-user me-2"></i>Customer Information</h6>
                        <p><strong>Customer ID:</strong> ${order.cusId}</p>
                        <c:if test="${not empty order.receiverName}">
                            <p><strong>Receiver:</strong> ${order.receiverName}</p>
                        </c:if>
                        <c:if test="${not empty order.receiverPhone}">
                            <p><strong>Phone:</strong> ${order.receiverPhone}</p>
                        </c:if>
                    </div>

                    <!-- Shipping Info -->
                    <div class="info-card">
                        <h6><i class="fas fa-shipping-fast me-2"></i>Shipping Information</h6>
                        <p><strong>Address:</strong> ${order.shippingAddress}</p>
                        <p><strong>Status:</strong> 
                            <span class="status-badge status-${order.orderStatus.toLowerCase().replace(' ', '-')}">
                                ${order.orderStatus}
                            </span>
                        </p>
                    </div>

                    <!-- Payment Info -->
                    <div class="info-card">
                        <h6><i class="fas fa-credit-card me-2"></i>Payment Information</h6>
                        <p><strong>Method:</strong> 
                            <span class="payment-method-badge">${order.paymentMethod}</span>
                        </p>
                        <p><strong>Total Amount:</strong> 
                            <span class="text-danger fw-bold">
                                <fmt:formatNumber value="${finalAmount}" pattern="#,##0" />₫
                            </span>
                        </p>
                    </div>
                </div>

                <!-- Products Section -->
                <h5 class="section-title">
                    <i class="fas fa-shopping-bag me-2"></i>
                    Ordered Products (${orderDetailsWithProducts.size()} products)
                    <c:if test="${order.orderStatus.toLowerCase() == 'shipped'}">
                        <small class="text-info ms-3" style="font-size: 0.8rem;">
                            <i class="fas fa-star"></i> You can review each product
                        </small>
                    </c:if>
                </h5>

                <div class="product-list">
                    <c:forEach var="item" items="${orderDetailsWithProducts}">
                        <div class="product-item">
                            <!-- Product Image -->
                            <img src="${pageContext.request.contextPath}/images/products/${item.product.proImageMain}" 
                                 alt="${item.product.proName}" 
                                 class="product-image"
                                 onerror="this.src='${pageContext.request.contextPath}/images/no-image.png'">

                            <!-- Product Info -->
                            <div class="product-info">
                                <div class="product-name">${item.product.proName}</div>
                                <div class="product-id">Product ID: ${item.product.proId}</div>
                                <div class="product-price">
                                    <fmt:formatNumber value="${item.orderDetail.unitPrice}" pattern="#,##0" />₫ / product
                                </div>
                            </div>

                            <!-- Quantity -->
                            <div class="quantity-info">
                                <div class="quantity-label">Quantity</div>
                                <div class="quantity-value">${item.orderDetail.quantity}</div>
                            </div>

                            <!-- Price -->
                            <div class="price-info">
                                <div class="unit-price">
                                    <fmt:formatNumber value="${item.orderDetail.unitPrice}" pattern="#,##0" />₫ x ${item.orderDetail.quantity}
                                </div>
                                <div class="subtotal">
                                    <fmt:formatNumber value="${item.subtotal}" pattern="#,##0" />₫
                                </div>
                            </div>

                            <!-- Review Button for shipped orders -->
                            <c:if test="${order.orderStatus.toLowerCase() == 'shipped'}">
                                <div class="review-action ms-3">
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${item.product.proId}" 
                                       class="btn btn-warning btn-sm text-white" 
                                       title="Review this product">
                                        <i class="fas fa-star me-1"></i>Review
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>

                <!-- Voucher Section -->
                <c:if test="${voucher != null}">
                    <div class="voucher-section">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-ticket-alt voucher-icon"></i>
                            <div class="voucher-info flex-grow-1">
                                <h6>Applied Voucher: ${voucher.codeName}</h6>
                                <p class="voucher-desc">${voucher.description}</p>
                            </div>
                            <div class="text-end">
                                <div class="fw-bold">
                                    -<fmt:formatNumber value="${discountAmount}" pattern="#,##0" />₫
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Total Section -->
                <div class="total-section">
                    <div class="row">
                        <div class="col-md-6 offset-md-6">
                            <div class="total-row">
                                <span class="total-label">Subtotal:</span>
                                <span class="total-value">
                                    <fmt:formatNumber value="${subtotalAmount}" pattern="#,##0" />₫
                                </span>
                            </div>

                            <c:if test="${discountAmount > 0}">
                                <div class="total-row">
                                    <span class="total-label">Discount:</span>
                                    <span class="total-value discount-value">
                                        -<fmt:formatNumber value="${discountAmount}" pattern="#,##0" />₫
                                    </span>
                                </div>
                            </c:if>

                            <div class="total-row">
                                <span class="total-label">Shipping Fee:</span>
                                <span class="total-value">30.000₫</span>
                            <div class="total-row">
                                <span class="total-label">Total:</span>
                                <span class="total-value final-total">
                                    <fmt:formatNumber value="${finalAmount}" pattern="#,##0" />₫
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/order-history" class="btn btn-primary btn-lg me-3">
                    <i class="fas fa-list me-2"></i>View All Orders
                </a>

                <c:choose>
                    <c:when test="${order.orderStatus == 'pending' || order.orderStatus == 'Waiting for Payment'}">
                        <button class="btn btn-danger btn-lg me-3" data-order-id="${order.orderId}" onclick="cancelOrder(this.getAttribute('data-order-id'))">
                            <i class="fas fa-times me-2"></i>Cancel Order
                        </button>
                        <c:if test="${order.paymentMethod != 'COD'}">
                            <button class="btn btn-success btn-lg" data-order-id="${order.orderId}" onclick="payNow(this.getAttribute('data-order-id'))">
                                <i class="fas fa-credit-card me-2"></i>Pay Now
                            </button>
                        </c:if>
                    </c:when>
                </c:choose>
            </div>
        </div>

        <!-- Footer -->
        <footer class="bg-dark text-light py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h5><i class="fas fa-microchip me-2"></i>Tech Store</h5>
                        <p>Your ultimate destination for quality technology products.</p>
                    </div>
                    <div class="col-md-4">
                        <h5>Quick Links</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>About Us</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>Contact</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>FAQ</a></li>
                        </ul>
                    </div>
                    <div class="col-md-4">
                        <h5>Connect with Us</h5>
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                            // Order action functions (reuse from order-history.jsp)
                            function showReviewModal(orderId) {
                                alert('Product review feature is under development.\nYou will be able to review each product in order #' + orderId);
                            }
                            

                            function cancelOrder(orderId) {
                                if (confirm('Are you sure you want to cancel order #' + orderId + '?\nThis action cannot be undone.')) {
                                    // TODO: Call cancel API
                                    window.location.href = '${pageContext.request.contextPath}/PaymentController?action=cancel&orderId=' + orderId;
                                }
                            }

                            function payNow(orderId) {
                                // TODO: Redirect to payment page
                                window.location.href = '${pageContext.request.contextPath}/PaymentController?orderId=' + orderId;
                            }

                            function trackOrder(orderId) {
                                alert('Order tracking feature for order #' + orderId + ' is under development.\nYou will be able to track detailed delivery status soon.');
                            }
        </script>

    </body>
</html>
