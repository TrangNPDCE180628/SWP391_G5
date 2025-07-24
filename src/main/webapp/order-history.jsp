<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử đơn hàng - Tech Store</title>
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
            .search-form {
                width: 300px;
            }
            .order-history-container {
                background-color: white;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-top: 20px;
            }
            .order-tabs {
                border-bottom: 1px solid #dee2e6;
                padding: 0;
                margin: 0;
                background-color: white;
                border-radius: 8px 8px 0 0;
            }
            .nav-tabs .nav-link {
                border: none;
                color: #6c757d;
                padding: 15px 20px;
                font-weight: 500;
                border-radius: 0;
                border-bottom: 3px solid transparent;
                transition: all 0.3s ease;
            }
            .nav-tabs .nav-link:hover {
                border-color: transparent;
                color: #e74c3c;
            }
            .nav-tabs .nav-link.active {
                color: #e74c3c;
                background-color: transparent;
                border-color: transparent;
                border-bottom: 3px solid #e74c3c;
            }
            .search-container {
                padding: 20px;
                border-bottom: 1px solid #dee2e6;
                background-color: #f8f9fa;
            }
            .order-item {
                border-bottom: 1px solid #dee2e6;
                padding: 20px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                transition: background-color 0.2s ease;
            }
            .order-item:hover {
                background-color: #f8f9fa;
            }
            .order-item:last-child {
                border-bottom: none;
            }
            .product-info {
                display: flex;
                align-items: center;
                flex: 1;
            }
            .product-image {
                width: 80px;
                height: 80px;
                object-fit: cover;
                border-radius: 8px;
                margin-right: 15px;
                border: 1px solid #dee2e6;
            }
            .product-details h6 {
                margin: 0 0 5px 0;
                font-weight: 600;
                color: #333;
            }
            .product-details p {
                margin: 0;
                color: #6c757d;
                font-size: 0.9rem;
            }
            .quantity {
                margin: 0 20px;
                font-weight: 500;
                color: #333;
            }
            .price {
                margin: 0 20px;
                font-weight: 600;
                color: #e74c3c;
                font-size: 1.1rem;
            }
            .order-total {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-top: 1px solid #dee2e6;
                text-align: right;
            }
            .total-amount {
                font-size: 1.2rem;
                font-weight: 600;
                color: #e74c3c;
            }
            .action-buttons {
                display: flex;
                gap: 10px;
            }
            .btn-action {
                padding: 8px 16px;
                border-radius: 4px;
                font-size: 0.9rem;
                font-weight: 500;
                border: 1px solid;
                transition: all 0.3s ease;
            }
            .btn-review {
                background-color: #e74c3c;
                color: white;
                border-color: #e74c3c;
            }
            .btn-review:hover {
                background-color: #c0392b;
                border-color: #c0392b;
                color: white;
            }
            .btn-request {
                background-color: transparent;
                color: #6c757d;
                border-color: #6c757d;
            }
            .btn-request:hover {
                background-color: #6c757d;
                color: white;
            }
            .empty-state {
                text-align: center;
                padding: 60px 20px;
                color: #6c757d;
            }
            .empty-state i {
                font-size: 4rem;
                margin-bottom: 20px;
                color: #dee2e6;
            }
            .status-badge {
                display: inline-block;
                padding: 4px 12px;
                border-radius: 12px;
                font-size: 0.8rem;
                font-weight: 500;
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
            .order-card {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                overflow: hidden;
            }
            .order-total-summary {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                border: 1px solid #dee2e6;
            }
            .nav-link {
                text-decoration: none;
                transition: all 0.3s ease;
            }
            .nav-link:hover {
                text-decoration: none;
            }
            .badge {
                font-size: 0.75rem;
            }
            .btn-action {
                padding: 6px 12px;
                margin-right: 8px;
                border-radius: 4px;
                font-size: 0.875rem;
                font-weight: 500;
                border: 1px solid;
                transition: all 0.3s ease;
            }
            .btn-action:hover {
                transform: translateY(-1px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }
            .btn-review {
                background: linear-gradient(135deg, #ffc107, #ff8c00);
                border-color: #ffc107;
                color: white;
            }
            .btn-review:hover {
                background: linear-gradient(135deg, #ff8c00, #ffc107);
                border-color: #ff8c00;
                color: white;
            }
            .btn-request {
                background: linear-gradient(135deg, #007bff, #0056b3);
                border-color: #007bff;
                color: white;
            }
            .btn-request:hover {
                background: linear-gradient(135deg, #0056b3, #007bff);
                border-color: #0056b3;
                color: white;
            }
            .btn-cancel {
                background: linear-gradient(135deg, #dc3545, #c82333);
                border-color: #dc3545;
                color: white;
            }
            .btn-cancel:hover {
                background: linear-gradient(135deg, #c82333, #dc3545);
                border-color: #c82333;
                color: white;
            }
            .action-buttons {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
            }
            @media (max-width: 768px) {
                .action-buttons {
                    flex-direction: column;
                    align-items: flex-start;
                }
                .btn-action {
                    margin-right: 0;
                    margin-bottom: 8px;
                    width: 100%;
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
                                            <a class="nav-link" href="OrderHistoryController">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin' or sessionScope.LOGIN_USER.role eq 'Staff'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutController">
                                                <i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
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

        <div class="container py-4">
            <div class="order-history-container">
                <!-- Header -->
                <div class="p-4 border-bottom">
                    <h2 class="mb-0">
                        <i class="fas fa-history me-2"></i>Lịch sử đơn hàng
                    </h2>
                </div>

                <!-- Tab Navigation -->
                <ul class="nav nav-tabs order-tabs" id="orderTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'all' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=all">
                            Tất cả
                            <c:if test="${allCount > 0}">
                                <span class="badge bg-secondary ms-1">${allCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'pending-payment' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=pending-payment">
                            Chờ thanh toán
                            <c:if test="${pendingCount > 0}">
                                <span class="badge bg-warning ms-1">${pendingCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'shipping' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=shipping">
                            Vận chuyển
                            <c:if test="${shippingCount > 0}">
                                <span class="badge bg-info ms-1">${shippingCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'delivery' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=delivery">
                            Chờ giao hàng
                            <c:if test="${deliveryCount > 0}">
                                <span class="badge bg-primary ms-1">${deliveryCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'completed' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=completed">
                            Hoàn thành
                            <c:if test="${completedCount > 0}">
                                <span class="badge bg-success ms-1">${completedCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link ${currentTab == 'cancelled' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/order-history?tab=cancelled">
                            Đã hủy
                            <c:if test="${cancelledCount > 0}">
                                <span class="badge bg-danger ms-1">${cancelledCount}</span>
                            </c:if>
                        </a>
                    </li>

                </ul>

                <!-- Search Bar -->
                <div class="search-container">
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-search"></i>
                        </span>
                        <input type="text" class="form-control" placeholder="Bạn có thể tìm kiếm theo tên Shop, ID đơn hàng hoặc Tên Sản phẩm">
                    </div>
                </div>

                <!-- Tab Content -->
                <div class="tab-content" id="orderTabsContent">
                    <!-- Orders Display -->
                    <div class="tab-pane fade show active">
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <c:forEach items="${orders}" var="order" varStatus="status">
                                    <div class="order-card">
                                        <!-- Order Header -->
                                        <div class="d-flex justify-content-between align-items-center p-3 border-bottom bg-light">
                                            <div class="d-flex align-items-center">
                                                <span class="badge bg-primary me-2">Đơn hàng #${order.orderId}</span>
                                                <small class="text-muted">
                                                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </small>
                                            </div>
                                            <div class="d-flex align-items-center">
                                                <c:choose>
                                                    <c:when test="${order.orderStatus == 'Completed'}">
                                                        <i class="fas fa-check-circle text-success me-2"></i>
                                                        <span class="text-success">Giao hàng thành công</span>
                                                        <span class="status-badge status-completed ms-2">HOÀN THÀNH</span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Shipping' || order.orderStatus == 'Processing'}">
                                                        <i class="fas fa-truck text-info me-2"></i>
                                                        <span class="text-info">Đang vận chuyển</span>
                                                        <span class="status-badge status-shipping ms-2">VẬN CHUYỂN</span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Pending' || order.orderStatus == 'Waiting for Payment'}">
                                                        <i class="fas fa-clock text-warning me-2"></i>
                                                        <span class="text-warning">Chờ thanh toán</span>
                                                        <span class="status-badge status-pending ms-2">CHỜ THANH TOÁN</span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Cancelled'}">
                                                        <i class="fas fa-times-circle text-danger me-2"></i>
                                                        <span class="text-danger">Đã hủy</span>
                                                        <span class="status-badge status-cancelled ms-2">ĐÃ HỦY</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fas fa-info-circle text-secondary me-2"></i>
                                                        <span class="text-secondary">${order.orderStatus}</span>
                                                        <span class="status-badge status-pending ms-2">${order.orderStatus}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <!-- Order Summary -->
                                        <div class="p-3">
                                            <div class="row">
                                                <div class="col-md-8">
                                                    <h6><i class="fas fa-map-marker-alt me-2"></i>Địa chỉ giao hàng:</h6>
                                                    <p class="text-muted mb-2">${order.shippingAddress}</p>

                                                    <c:if test="${not empty order.receiverName}">
                                                        <p class="mb-1"><strong>Người nhận:</strong> ${order.receiverName}</p>
                                                    </c:if>
                                                    <c:if test="${not empty order.receiverPhone}">
                                                        <p class="mb-1"><strong>SĐT:</strong> ${order.receiverPhone}</p>
                                                    </c:if>

                                                    <p class="mb-1"><strong>Phương thức thanh toán:</strong> 
                                                        <c:choose>
                                                            <c:when test="${order.paymentMethod == 'COD'}">
                                                                <span class="badge bg-warning">Thanh toán khi nhận hàng</span>
                                                            </c:when>
                                                            <c:when test="${order.paymentMethod == 'VNPay'}">
                                                                <span class="badge bg-primary">VNPay</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${order.paymentMethod}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                                <div class="col-md-4 text-end">
                                                    <div class="order-total-summary">
                                                        <p class="mb-1">Tổng tiền hàng: 
                                                            <fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>đ
                                                        </p>
                                                        <c:if test="${order.discountAmount != null && order.discountAmount > 0}">
                                                            <p class="mb-1 text-success">Giảm giá: 
                                                                -<fmt:formatNumber value="${order.discountAmount}" pattern="#,###"/>đ
                                                            </p>
                                                        </c:if>
                                                        <h5 class="mb-0 text-danger">Thành tiền: 
                                                            <fmt:formatNumber value="${order.finalAmount != null ? order.finalAmount : order.totalAmount}" pattern="#,###"/>đ
                                                        </h5>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Order Actions -->
                                        <div class="order-total">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div class="action-buttons">
                                                    <c:choose>
                                                        <c:when test="${order.orderStatus == 'Completed'}">
                                                            <button class="btn btn-action btn-review" data-order-id="${order.orderId}" onclick="showReviewModal(this.getAttribute('data-order-id'))">
                                                                <i class="fas fa-star me-1"></i>Đánh Giá
                                                            </button>

                                                        </c:when>
                                                        <c:when test="${order.orderStatus == 'Pending' || order.orderStatus == 'Waiting for Payment'}">
                                                            <button class="btn btn-action btn-review" data-order-id="${order.orderId}" onclick="cancelOrder(this.getAttribute('data-order-id'))">
                                                                <i class="fas fa-times me-1"></i>Hủy Đơn Hàng
                                                            </button>
                                                            <c:if test="${order.paymentMethod != 'COD'}">
                                                                <button class="btn btn-action btn-request" data-order-id="${order.orderId}" onclick="payNow(this.getAttribute('data-order-id'))">
                                                                    <i class="fas fa-credit-card me-1"></i>Thanh Toán Ngay
                                                                </button>
                                                            </c:if>
                                                        </c:when>
                                                        <c:when test="${order.orderStatus == 'Shipping' || order.orderStatus == 'Processing'}">
                                                            <button class="btn btn-action btn-request" data-order-id="${order.orderId}" onclick="trackOrder(this.getAttribute('data-order-id'))">
                                                                <i class="fas fa-truck me-1"></i>Theo Dõi Đơn Hàng
                                                            </button>
                                                        </c:when>
                                                    </c:choose>

                                                    <button class="btn btn-action btn-request" data-order-id="${order.orderId}" onclick="viewOrderDetails(this.getAttribute('data-order-id'))">
                                                        <i class="fas fa-eye me-1"></i>Xem Chi Tiết
                                                    </button>
                                                </div>

                                                <div class="text-end">
                                                    <a href="${pageContext.request.contextPath}/OrderDetailsController?orderId=${order.orderId}" 
                                                       class="btn btn-outline-primary btn-sm">
                                                        <i class="fas fa-info-circle me-1"></i>Chi tiết đơn hàng
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <c:choose>
                                        <c:when test="${currentTab == 'pending-payment'}">
                                            <i class="fas fa-credit-card"></i>
                                            <h5>Không có đơn hàng nào đang chờ thanh toán</h5>
                                            <p>Tất cả đơn hàng của bạn đã được thanh toán hoặc đã bị hủy.</p>
                                        </c:when>
                                        <c:when test="${currentTab == 'shipping'}">
                                            <i class="fas fa-truck"></i>
                                            <h5>Không có đơn hàng nào đang vận chuyển</h5>
                                            <p>Hiện tại không có đơn hàng nào đang trong quá trình vận chuyển.</p>
                                        </c:when>
                                        <c:when test="${currentTab == 'delivery'}">
                                            <i class="fas fa-box"></i>
                                            <h5>Không có đơn hàng nào đang chờ giao</h5>
                                            <p>Không có đơn hàng nào đang chờ giao hàng.</p>
                                        </c:when>
                                        <c:when test="${currentTab == 'completed'}">
                                            <i class="fas fa-check-circle"></i>
                                            <h5>Chưa có đơn hàng nào hoàn thành</h5>
                                            <p>Bạn chưa có đơn hàng nào được giao thành công.</p>
                                        </c:when>
                                        <c:when test="${currentTab == 'cancelled'}">
                                            <i class="fas fa-times-circle"></i>
                                            <h5>Không có đơn hàng nào đã bị hủy</h5>
                                            <p>Bạn chưa hủy đơn hàng nào.</p>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-shopping-bag"></i>
                                            <h5>Chưa có đơn hàng nào</h5>
                                            <p>Bạn chưa có đơn hàng nào. Hãy mua sắm ngay!</p>
                                            <a href="${pageContext.request.contextPath}/HomeController" class="btn btn-primary">
                                                <i class="fas fa-shopping-cart me-2"></i>Mua sắm ngay
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                        // Search functionality
                                                        document.querySelector('input[placeholder*="tìm kiếm"]').addEventListener('input', function (e) {
                                                            const searchTerm = e.target.value.toLowerCase();
                                                            const orderCards = document.querySelectorAll('.order-card');

                                                            orderCards.forEach(card => {
                                                                const orderText = card.textContent.toLowerCase();
                                                                card.style.display = orderText.includes(searchTerm) ? 'block' : 'none';
                                                            });
                                                        });

                                                        // Order action functions
                                                        function showReviewModal(orderId) {
                                                            const modal = new bootstrap.Modal(document.getElementById('reviewModal'));
                                                            const content = document.getElementById('reviewContent');

                                                            content.innerHTML = `
                    <div class="text-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Đang tải...</span>
                        </div>
                    </div>
                `;

                                                            modal.show();

                                                            // Simulate loading review data
                                                            setTimeout(() => {
                                                                content.innerHTML = `
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            Chức năng đánh giá đơn hàng #${orderId} đang được phát triển.
                            <br>Bạn sẽ có thể đánh giá sản phẩm và để lại nhận xét về chất lượng dịch vụ.
                        </div>
                        <div class="mt-3">
                            <h6>Đặc tính sẽ có trong phiên bản tiếp theo:</h6>
                            <ul class="list-unstyled">
                                <li><i class="fas fa-star text-warning me-2"></i>Đánh giá 1-5 sao cho từng sản phẩm</li>
                                <li><i class="fas fa-comment text-info me-2"></i>Viết nhận xét chi tiết</li>
                                <li><i class="fas fa-image text-success me-2"></i>Đăng hình ảnh sản phẩm thực tế</li>
                                <li><i class="fas fa-thumbs-up text-primary me-2"></i>Đánh giá chất lượng dịch vụ</li>
                            </ul>
                        </div>
                    `;
                                                            }, 1000);
                                                        }



                                                        function cancelOrder(orderId) {
                                                            if (confirm('Bạn có chắc chắn muốn hủy đơn hàng #' + orderId + '?\nHành động này không thể hoàn tác.')) {
                                                                // Show loading state
                                                                const button = event.target.closest('button');
                                                                const originalText = button.innerHTML;
                                                                button.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang hủy...';
                                                                button.disabled = true;

                                                                // TODO: Call cancel API
                                                                setTimeout(() => {
                                                                    window.location.href = '${pageContext.request.contextPath}/OrderController?action=cancel&orderId=' + orderId;
                                                                }, 1000);
                                                            }
                                                        }

                                                        function payNow(orderId) {
                                                            // Show loading state
                                                            const button = event.target.closest('button');
                                                            button.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Chuyển hướng...';
                                                            button.disabled = true;

                                                            // TODO: Redirect to payment page
                                                            setTimeout(() => {
                                                                window.location.href = '${pageContext.request.contextPath}/PaymentController?orderId=' + orderId;
                                                            }, 1000);
                                                        }

                                                        function trackOrder(orderId) {
                                                            const modal = new bootstrap.Modal(document.getElementById('orderDetailsModal'));
                                                            const modalTitle = document.getElementById('orderDetailsModalLabel');
                                                            const content = document.getElementById('orderDetailsContent');

                                                            modalTitle.innerHTML = '<i class="fas fa-truck text-primary me-2"></i>Theo Dõi Đơn Hàng #' + orderId;

                                                            content.innerHTML = `
                    <div class="text-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Đang tải...</span>
                        </div>
                    </div>
                `;

                                                            modal.show();

                                                            // Simulate loading tracking data
                                                            setTimeout(() => {
                                                                content.innerHTML = `
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            Chức năng theo dõi đơn hàng #${orderId} đang được phát triển.
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Trạng thái hiện tại:</h6>
                                <div class="progress mb-3">
                                    <div class="progress-bar bg-warning" style="width: 60%"></div>
                                </div>
                                <p class="text-muted">Đang vận chuyển (60% hoàn thành)</p>
                            </div>
                            <div class="col-md-6">
                                <h6>Thông tin vận chuyển:</h6>
                                <ul class="list-unstyled">
                                    <li><strong>Đơn vị vận chuyển:</strong> Giao Hàng Nhanh</li>
                                    <li><strong>Mã vận đơn:</strong> GHN${orderId}789</li>
                                    <li><strong>Dự kiến giao:</strong> 2-3 ngày</li>
                                </ul>
                            </div>
                        </div>
                    `;
                                                            }, 1000);
                                                        }

                                                        function viewOrderDetails(orderId) {
                                                            // Direct redirect to order details page
                                                            window.location.href = '${pageContext.request.contextPath}/OrderDetailsController?orderId=' + orderId;
                                                        }

                                                        // Tab functionality with URL update
                                                        document.querySelectorAll('.nav-link').forEach(link => {
                                                            link.addEventListener('click', function (e) {
                                                                // Let the default link behavior happen (no preventDefault)
                                                                // The URL will change automatically due to href
                                                            });
                                                        });

                                                        // Highlight current tab based on URL parameter
                                                        window.addEventListener('load', function () {
                                                            const urlParams = new URLSearchParams(window.location.search);
                                                            const currentTab = urlParams.get('tab') || 'all';

                                                            // Remove active class from all tabs
                                                            document.querySelectorAll('.nav-link').forEach(link => {
                                                                link.classList.remove('active');
                                                            });

                                                            // Add active class to current tab
                                                            const activeTab = document.querySelector(`a[href*="tab=${currentTab}"]`);
                                                            if (activeTab) {
                                                                activeTab.classList.add('active');
                                                            }
                                                        });
        </script>

        <!-- Review Modal -->
        <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="reviewModalLabel">
                            <i class="fas fa-star text-warning me-2"></i>
                            Đánh Giá Sản Phẩm
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="reviewContent">
                            <div class="text-center">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="visually-hidden">Đang tải...</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Details Modal -->
        <div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderDetailsModalLabel">
                            <i class="fas fa-receipt text-primary me-2"></i>
                            Chi Tiết Đơn Hàng
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="orderDetailsContent">
                            <div class="text-center">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="visually-hidden">Đang tải...</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
