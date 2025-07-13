<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tech Store - Product Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
        /* [END ADDED] */
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
        <div class="container">
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
                    <li class="nav-item">
                        <a class="nav-link" href="OrderController?action=view">
                            <i class="fas fa-shopping-cart"></i> Orders
                        </a>
                    </li>
                    <c:choose>
                        <c:when test="${not empty sessionScope.LOGIN_USER}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="#">Profile</a></li>
                                    <li><a class="dropdown-item" href="cart.jsp">View Cart</a></li>
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
                            <!-- [ADDED]: Color selection -->
                            <div class="mb-4">
                                <span class="fw-bold me-2">Color:</span>
                                <span class="color-dot bg-dark selected" title="Space Gray"></span>
                                <span class="color-dot bg-light border" title="Silver"></span>
                            </div>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>