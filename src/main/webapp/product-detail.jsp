<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tech Store - Product Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
                -webkit-font-smoothing: antialiased;
            }
            .navbar {
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
            }
            .navbar-brand {
                font-weight: 600;
                color: #333;
            }
            /* [ADDED]: Enhanced CSS for product detail page */
            .product-detail-page {
                background-color: #fff;
                padding: 40px 0;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                margin: 20px 0;
            }
            .product-image-large {
                max-height: 400px; /* [UPDATED]: Increased for premium look */
                object-fit: contain;
                width: 100%;
                border-radius: 12px;
                background-color: #f8f9fa;
                padding: 15px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            }
            .product-status-tag {
                position: absolute;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                padding: 4px 12px;
                font-size: 0.85rem;
                font-weight: 600;
                color: white;
                border-radius: 8px;
                z-index: 2;
            }
            .product-status-tag.like-new {
                background-color: #28a745; /* Green for Like New */
            }
            .product-status-tag.used {
                background-color: #6c757d; /* Gray for Used */
            }
            .color-dot {
                display: inline-block;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                vertical-align: middle;
                margin: 0 5px;
                border: 1px solid #ddd;
            }
            .color-dot.selected {
                border: 2px solid #007bff;
                box-shadow: 0 0 5px rgba(0,123,255,0.5);
            }
            .custom-checklist {
                list-style: none;
                padding: 0;
            }
            .custom-checklist li {
                position: relative;
                padding-left: 28px;
                margin-bottom: 12px;
                font-size: 0.95rem;
                color: #333;
            }
            .custom-checklist li::before {
                content: '✔';
                position: absolute;
                left: 0;
                color: #28a745;
                font-weight: bold;
                font-size: 1.1rem;
            }
            .spec-box {
                border: 1px solid #ddd;
                border-radius: 10px;
                padding: 20px;
                background-color: #f8f9fa;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            }
            .spec-box ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            .spec-box li {
                margin-bottom: 10px;
                font-size: 0.95rem;
                color: #333;
            }
            .btn-buy-now {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 8px;
                font-weight: 500;
                transition: background-color 0.3s, transform 0.2s;
            }
            .btn-buy-now:hover {
                background-color: #c82333;
                transform: scale(1.02);
            }
            .btn-add-cart {
                background-color: #198754;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 8px;
                font-weight: 500;
                transition: background-color 0.3s, transform 0.2s;
            }
            .btn-add-cart:hover {
                background-color: #146c43;
                transform: scale(1.02);
            }
            .btn-installment {
                background-color: #6c757d;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 8px;
                font-weight: 500;
                transition: background-color 0.3s, transform 0.2s;
            }
            .btn-installment:hover {
                background-color: #5a6268;
                transform: scale(1.02);
            }
            .btn-back {
                border-radius: 8px;
                font-weight: 500;
                transition: transform 0.2s;
            }
            .btn-back:hover {
                transform: scale(1.02);
            }
            .shadow-sm {
                box-shadow: 0 2px 8px rgba(0,0,0,0.05) !important;
            }
            /* [END ADDED] */
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

        <!-- Product Detail Section -->
        <c:choose>
            <c:when test="${not empty product}">
                <section class="product-detail-page">
                    <div class="container">
                        <div class="row">
                            <!-- Product Image -->
                            <div class="col-md-6 text-center mb-4 position-relative">
                                <!-- [ADDED]: Status tag -->
                                <span class="product-status-tag like-new">Like New</span>
                                <img src="images/products/${product.proImageMain}" class="product-image-large" alt="${product.proName}">
                            </div>
                            <!-- Product Details -->
                            <div class="col-md-6">
                                <!-- [UPDATED]: Product name and price -->
                                <h2 class="fw-bold mb-3">${product.proName}</h2>
                                <p class="text-danger fs-3 fw-bold mb-4">
                                    <fmt:formatNumber value="${product.proPrice}" type="currency" currencySymbol="$" maxFractionDigits="${product.proPrice % 1 == 0 ? 0 : 2}"/>
                                </p>

                                <!-- Description -->
                                <div class="mb-4">
                                    <h5 class="fw-bold">Description</h5>
                                    <p>${product.proDescription}</p>
                                </div>
                                <!-- Product Attributes -->
                                <c:if test="${not empty attributes}">
                                    <div class="spec-box mb-4">
                                        <h5 class="fw-bold mb-3">Specifications</h5>
                                        <ul>
                                            <c:forEach items="${attributes}" var="attr">
                                                <li><strong>${attr.attributeName}:</strong> ${attr.value}</li>
                                                </c:forEach>
                                        </ul>
                                    </div>
                                </c:if>
                                <!-- [UPDATED]: Policies with enhanced content -->
                                <ul class="custom-checklist mb-4">
                                    <li>Official Apple product with warranty.</li>
                                    <li>Free 10-day trial and return.</li>
                                    <li>1-to-1 exchange within 30 days if defective.</li>
                                    <li>Nationwide delivery with COD available.</li>
                                    <li>Contact <a href="tel:0936996900">0936 996 900</a> for support.</li>
                                </ul>
                                <!-- [UPDATED]: Buttons with new styling -->
                                <div class="d-grid gap-2 mb-4">
                                    <button class="btn btn-buy-now" disabled>Buy Now</button>
                                    <form action="CartController" method="POST" class="d-inline">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="productId" value="${product.proId}">
                                        <button type="submit" class="btn btn-add-cart w-100">
                                            <i class="fas fa-shopping-cart me-2"></i>Add to Cart
                                        </button>
                                    </form>
                                    <button class="btn btn-installment" disabled>Installment Purchase</button>
                                </div>
                                <!-- Back to Home -->
                                <a href="HomeController" class="btn btn-outline-secondary btn-back">
                                    <i class="fas fa-arrow-left me-2"></i>Back to Home
                                </a>
                            </div>
                        </div>
                    </div>
                </section>
            </c:when>
            <c:otherwise>
                <div class="container text-center py-5">
                    <h3 class="text-danger">Product not found.</h3>
                    <a href="HomeController" class="btn btn-outline-primary mt-3">Back to Home</a>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- FEEDBACK SECTION UPGRADED -->
        <c:if test="${not empty viewfeedbacks}">
            <div class="mt-5">
                <h4 class="fw-bold mb-4">Customer Feedback</h4>

                <c:forEach var="fb" items="${viewfeedbacks}">
                    <div class="d-flex mb-4">
                        <!-- Avatar khách hàng -->
                        <div class="me-3">
                            <img src="images/customers/${fb.cusId}.jpg" onerror="this.onerror=null;this.src='images/customer-default.webp';" class="rounded-circle" width="50" height="50" alt="${fb.cusFullName}" style="object-fit: cover;">                           
                        </div>

                        <!-- Nội dung Feedback -->
                        <div class="flex-grow-1">
                            <div class="d-flex align-items-center justify-content-between">
                                <h6 class="mb-1 fw-bold d-flex align-items-center">
                                    ${fb.cusFullName}
                                    <span class="ms-3 text-success d-flex align-items-center" style="font-size: 0.9rem;">
                                        <i class="fas fa-check-circle me-1"></i> Confirmed purchase
                                    </span>
                                    <span class="ms-3 text-warning">
                                        <c:forEach begin="1" end="${fb.rate}">
                                            <i class="fas fa-star"></i>
                                        </c:forEach>
                                        <c:forEach begin="${fb.rate + 1}" end="5">
                                            <i class="far fa-star"></i>
                                        </c:forEach>
                                    </span>
                                </h6>
                            </div>

                            <!-- Bubble Feedback -->
                            <div class="mt-2 p-3 bg-white border rounded shadow-sm">
                                <p class="mb-0">${fb.feedbackContent}</p>
                            </div>

                            <!-- Reply nếu có -->
                            <c:if test="${not empty fb.replyFeedbackId}">
                                <div class="mt-3 ps-4 border-start border-3 border-danger">
                                    <div class="d-flex align-items-center mb-1 text-muted">
                                        <i class="fas fa-reply me-2 text-danger"></i>
                                        <strong>Replied by staff (${fb.staffId})</strong>
                                    </div>
                                    <div class="bg-light p-3 rounded shadow-sm">
                                        <p class="mb-1">${fb.contentReply}</p>
                                        <small class="text-muted fst-italic">
                                            <fmt:formatDate value="${fb.createdAt}" pattern="dd-MM-yyyy HH:mm"/>
                                        </small>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <!-- Write Feedback -->
        <c:if test="${sessionScope.LOGIN_USER != null && hasBought && !hasFeedback}">
            <div class="card my-4 shadow-sm border-0">
                <div class="card-body">
                    <h5 class="card-title mb-3">
                        <i class="fas fa-comment-dots text-primary me-2"></i> Write a Review
                    </h5>
                    <form action="product-detail" method="POST">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="cusId" value="${sessionScope.LOGIN_USER.id}">
                        <input type="hidden" name="proId" value="${product.proId}">

                        <!-- Star Rating Select -->
                        <div class="mb-3">
                            <label for="rate" class="form-label fw-bold">
                                <i class="fas fa-star text-warning me-1"></i> Rating
                            </label>
                            <select class="form-select shadow-sm" name="rate" id="rate" required>
                                <option value="5">★★★★★ - Excellent</option>
                                <option value="4">★★★★ - Good</option>
                                <option value="3">★★★ - Average</option>
                                <option value="2">★★ - Poor</option>
                                <option value="1">★ - Terrible</option>
                            </select>
                        </div>

                        <!-- Feedback Content -->
                        <div class="mb-3">
                            <label for="content" class="form-label fw-bold">
                                <i class="fas fa-pencil-alt me-1"></i> Your Feedback
                            </label>
                            <textarea class="form-control shadow-sm" id="content" name="content" rows="4" 
                                      placeholder="Share your thoughts..." required></textarea>
                        </div>

                        <!-- Submit Button -->
                        <div class="text-end">
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="fas fa-paper-plane me-1"></i> Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>

        </c:if>

        <c:if test="${sessionScope.LOGIN_USER != null && hasFeedback}">
            <p class="text-muted">You have already written feedback for this product.</p>
        </c:if>



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
    </body>
</html>