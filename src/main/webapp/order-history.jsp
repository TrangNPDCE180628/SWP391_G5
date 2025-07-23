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
                                            <a class="nav-link" href="OrderController?action=view">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
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
                        <button class="nav-link active" id="all-tab" data-bs-toggle="tab" data-bs-target="#all" 
                                type="button" role="tab" aria-controls="all" aria-selected="true">
                            Tất cả
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="pending-payment-tab" data-bs-toggle="tab" data-bs-target="#pending-payment" 
                                type="button" role="tab" aria-controls="pending-payment" aria-selected="false">
                            Chờ thanh toán
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="shipping-tab" data-bs-toggle="tab" data-bs-target="#shipping" 
                                type="button" role="tab" aria-controls="shipping" aria-selected="false">
                            Vận chuyển
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="delivery-tab" data-bs-toggle="tab" data-bs-target="#delivery" 
                                type="button" role="tab" aria-controls="delivery" aria-selected="false">
                            Chờ giao hàng
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="completed-tab" data-bs-toggle="tab" data-bs-target="#completed" 
                                type="button" role="tab" aria-controls="completed" aria-selected="false">
                            Hoàn thành
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="cancelled-tab" data-bs-toggle="tab" data-bs-target="#cancelled" 
                                type="button" role="tab" aria-controls="cancelled" aria-selected="false">
                            Đã hủy
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="refund-tab" data-bs-toggle="tab" data-bs-target="#refund" 
                                type="button" role="tab" aria-controls="refund" aria-selected="false">
                            Trả hàng/Hoàn tiền
                        </button>
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
                    <!-- All Orders Tab -->
                    <div class="tab-pane fade show active" id="all" role="tabpanel" aria-labelledby="all-tab">
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <c:forEach items="${orders}" var="order">
                                    <div class="order-card">
                                        <!-- Shop Header -->
                                        <div class="d-flex justify-content-between align-items-center p-3 border-bottom">
                                            <div class="d-flex align-items-center">
                                                <span class="badge bg-danger me-2">Yêu thích+</span>
                                                <strong>MyKidsLand HCM - Cửng B...</strong>
                                                <button class="btn btn-sm btn-outline-primary ms-2">
                                                    <i class="fas fa-comment"></i> Chat
                                                </button>
                                                <a href="#" class="btn btn-sm btn-link ms-2">
                                                    <i class="fas fa-store"></i> Xem Shop
                                                </a>
                                            </div>
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-truck text-success me-2"></i>
                                                <span class="text-success">Giao hàng thành công</span>
                                                <span class="status-badge status-completed ms-2">HOÀN THÀNH</span>
                                            </div>
                                        </div>

                                        <!-- Order Items -->
                                        <c:forEach items="${order.orderDetails}" var="detail">
                                            <div class="order-item">
                                                <div class="product-info">
                                                    <img src="${pageContext.request.contextPath}/images/${detail.product.image}" 
                                                         alt="${detail.product.name}" class="product-image">
                                                    <div class="product-details">
                                                        <h6>${detail.product.name}</h6>
                                                        <p>Phân loại hàng: ${detail.product.category}</p>
                                                    </div>
                                                </div>
                                                <div class="quantity">x${detail.quantity}</div>
                                                <div class="price">
                                                    <span class="text-muted text-decoration-line-through me-2">
                                                        <fmt:formatNumber value="${detail.product.originalPrice}" pattern="#,###"/>đ
                                                    </span>
                                                    <fmt:formatNumber value="${detail.price}" pattern="#,###"/>đ
                                                </div>
                                            </div>
                                        </c:forEach>

                                        <!-- Order Total -->
                                        <div class="order-total">
                                            <div class="mb-2">
                                                <small class="text-muted">Đánh giá sản phẩm trước 15-08-2025</small><br>
                                                <small class="text-muted">Đánh giá ngay và nhận 200 Xu</small>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div class="action-buttons">
                                                    <button class="btn btn-action btn-review">Đánh Giá</button>
                                                    <button class="btn btn-action btn-request dropdown-toggle" 
                                                            data-bs-toggle="dropdown">
                                                        Yêu Cầu Trả Hàng/Hoàn Tiền
                                                    </button>
                                                    <button class="btn btn-action btn-request">Thêm</button>
                                                </div>
                                                <div class="total-amount">
                                                    Thành tiền: <fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>đ
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <i class="fas fa-shopping-bag"></i>
                                    <h5>Chưa có đơn hàng nào</h5>
                                    <p>Bạn chưa có đơn hàng nào. Hãy mua sắm ngay!</p>
                                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">
                                        <i class="fas fa-shopping-cart me-2"></i>Mua sắm ngay
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Pending Payment Tab -->
                    <div class="tab-pane fade" id="pending-payment" role="tabpanel" aria-labelledby="pending-payment-tab">
                        <div class="empty-state">
                            <i class="fas fa-credit-card"></i>
                            <h5>Không có đơn hàng nào đang chờ thanh toán</h5>
                            <p>Tất cả đơn hàng của bạn đã được thanh toán hoặc đã bị hủy.</p>
                        </div>
                    </div>

                    <!-- Shipping Tab -->
                    <div class="tab-pane fade" id="shipping" role="tabpanel" aria-labelledby="shipping-tab">
                        <div class="empty-state">
                            <i class="fas fa-truck"></i>
                            <h5>Không có đơn hàng nào đang vận chuyển</h5>
                            <p>Hiện tại không có đơn hàng nào đang trong quá trình vận chuyển.</p>
                        </div>
                    </div>

                    <!-- Delivery Tab -->
                    <div class="tab-pane fade" id="delivery" role="tabpanel" aria-labelledby="delivery-tab">
                        <div class="empty-state">
                            <i class="fas fa-box"></i>
                            <h5>Không có đơn hàng nào đang chờ giao</h5>
                            <p>Không có đơn hàng nào đang chờ giao hàng.</p>
                        </div>
                    </div>

                    <!-- Completed Tab -->
                    <div class="tab-pane fade" id="completed" role="tabpanel" aria-labelledby="completed-tab">
                        <!-- Sample completed order (this would normally be populated from server) -->
                        <div class="order-card">
                            <div class="d-flex justify-content-between align-items-center p-3 border-bottom">
                                <div class="d-flex align-items-center">
                                    <span class="badge bg-danger me-2">Yêu thích+</span>
                                    <strong>MyKidsLand HCM - Cửng B...</strong>
                                    <button class="btn btn-sm btn-outline-primary ms-2">
                                        <i class="fas fa-comment"></i> Chat
                                    </button>
                                    <a href="#" class="btn btn-sm btn-link ms-2">
                                        <i class="fas fa-store"></i> Xem Shop
                                    </a>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-check-circle text-success me-2"></i>
                                    <span class="text-success">Giao hàng thành công</span>
                                    <span class="status-badge status-completed ms-2">HOÀN THÀNH</span>
                                </div>
                            </div>

                            <div class="order-item">
                                <div class="product-info">
                                    <img src="${pageContext.request.contextPath}/images/product-sample.jpg" 
                                         alt="Sample Product" class="product-image">
                                    <div class="product-details">
                                        <h6>[DATE 2026] Kẹo Gôm Làm Bánh KRACIE Popin Cookin Đồ Chơi Sáng Tạo Ăn Được Nhật Bản Cho Bé Sushi - Bento - Hamburger</h6>
                                        <p>Phân loại hàng: Bento Gấu Trúc</p>
                                    </div>
                                </div>
                                <div class="quantity">x1</div>
                                <div class="price">
                                    <span class="text-muted text-decoration-line-through me-2">₫120.000</span>
                                    ₫105.000
                                </div>
                            </div>

                            <div class="order-total">
                                <div class="mb-2">
                                    <small class="text-muted">Đánh giá sản phẩm trước 15-08-2025</small><br>
                                    <small class="text-muted">Đánh giá ngay và nhận 200 Xu</small>
                                </div>
                                <div class="d-flex justify-content-between align-items-center">
                                    <div class="action-buttons">
                                        <button class="btn btn-action btn-review">Đánh Giá</button>
                                        <button class="btn btn-action btn-request dropdown-toggle" data-bs-toggle="dropdown">
                                            Yêu Cầu Trả Hàng/Hoàn Tiền
                                        </button>
                                        <button class="btn btn-action btn-request">Thêm</button>
                                    </div>
                                    <div class="total-amount">
                                        Thành tiền: ₫185.000
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Cancelled Tab -->
                    <div class="tab-pane fade" id="cancelled" role="tabpanel" aria-labelledby="cancelled-tab">
                        <div class="empty-state">
                            <i class="fas fa-times-circle"></i>
                            <h5>Không có đơn hàng nào đã bị hủy</h5>
                            <p>Bạn chưa hủy đơn hàng nào.</p>
                        </div>
                    </div>

                    <!-- Refund Tab -->
                    <div class="tab-pane fade" id="refund" role="tabpanel" aria-labelledby="refund-tab">
                        <div class="empty-state">
                            <i class="fas fa-undo"></i>
                            <h5>Không có yêu cầu trả hàng/hoàn tiền nào</h5>
                            <p>Bạn chưa có yêu cầu trả hàng hoặc hoàn tiền nào.</p>
                        </div>
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
                    const productNames = card.querySelectorAll('.product-details h6');
                    let found = false;

                    productNames.forEach(name => {
                        if (name.textContent.toLowerCase().includes(searchTerm)) {
                            found = true;
                        }
                    });

                    card.style.display = found || searchTerm === '' ? 'block' : 'none';
                });
            });

            // Tab switching with URL hash
            document.querySelectorAll('#orderTabs button').forEach(tab => {
                tab.addEventListener('shown.bs.tab', function (e) {
                    const tabId = e.target.getAttribute('data-bs-target').substring(1);
                    history.pushState(null, null, '#' + tabId);
                });
            });

            // Load tab from URL hash on page load
            window.addEventListener('load', function () {
                const hash = window.location.hash.substring(1);
                if (hash) {
                    const tabButton = document.querySelector(`#${hash}-tab`);
                    if (tabButton) {
                        new bootstrap.Tab(tabButton).show();
                    }
                }
            });

            // Dropdown functionality for action buttons
            document.querySelectorAll('.btn-request.dropdown-toggle').forEach(btn => {
                btn.addEventListener('click', function (e) {
                    e.preventDefault();
                    // Add dropdown menu functionality here
                    console.log('Dropdown clicked');
                });
            });
        </script>
    </body>
</html>
