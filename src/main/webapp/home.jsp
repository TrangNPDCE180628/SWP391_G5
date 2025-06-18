<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shoe Paradise - Your Ultimate Footwear Destination</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)), url('images/hero-bg.jpg');
                background-size: cover;
                background-position: center;
                color: white;
                padding: 100px 0;
                margin-bottom: 40px;
            }
            .product-card {
                background: white;
                border-radius: 10px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                transition: transform 0.3s;
                margin-bottom: 20px;
                height: 100%;
            }
            .product-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 4px 10px rgba(0,0,0,0.2);
            }
            .product-image {
                height: 200px;
                object-fit: contain;
                border-radius: 10px 10px 0 0;
                width: 100%;
                padding: 10px;
                background-color: #f8f9fa;
            }
            .product-info {
                padding: 15px;
                display: flex;
                flex-direction: column;
                height: calc(100% - 200px);
            }
            .product-title {
                font-weight: 600;
                margin-bottom: 10px;
                height: 48px;
                overflow: hidden;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                color: #333;
            }
            .product-description {
                color: #6c757d;
                font-size: 0.9rem;
                margin-bottom: 15px;
                flex-grow: 1;
                overflow: hidden;
                display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;
            }
            .product-price {
                color: #dc3545;
                font-weight: 600;
                font-size: 1.2rem;
            }
            .btn-add-cart {
                background-color: #198754;
                color: white;
                border: none;
                width: 100%;
                margin-top: auto;
                padding: 10px;
                border-radius: 5px;
                transition: background-color 0.3s;
            }
            .btn-add-cart:hover {
                background-color: #146c43;
                color: white;
            }
            .category-filter {
                margin-bottom: 30px;
                background-color: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .category-filter .btn-group {
                flex-wrap: wrap;
                gap: 5px;
            }
            .pagination {
                margin-top: 2rem;
            }
            .stock-badge {
                position: absolute;
                top: 10px;
                right: 10px;
                padding: 5px 10px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 500;
            }
            .stock-badge.low-stock {
                background-color: #ffc107;
                color: #000;
            }
            .stock-badge.out-of-stock {
                background-color: #dc3545;
                color: #fff;
            }
            .category-title {
                color: #333;
                margin-bottom: 1rem;
                font-weight: 600;
            }
            .hero-title {
                font-size: 3rem;
                font-weight: 700;
                margin-bottom: 1rem;
                text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
            }
            .hero-subtitle {
                font-size: 1.5rem;
                margin-bottom: 2rem;
                text-shadow: 1px 1px 2px rgba(0,0,0,0.5);
            }
        </style>
    </head>
    <body>
        <%-- <!-- Navigation Bar --> 
         <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
             <div class="container">
                 <a class="navbar-brand" href="HomeController">
                     <i class="fas fa-shoe-prints me-2"></i>Store
                 </a>
                 <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                     <span class="navbar-toggler-icon"></span>
                 </button>
                 <div class="collapse navbar-collapse" id="navbarNav">
                     <form class="d-flex search-form mx-auto" action="HomeController" method="GET">
                         <input class="form-control me-2" type="search" name="searchTerm"
                                placeholder="Search for shoes..." value="${searchTerm}">
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
                                         <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullname}
                                     </a>
                                     <ul class="dropdown-menu dropdown-menu-end">
                                         <li><a class="dropdown-item" href="">Profile</a></li>
                                         <li><a class="dropdown-item" href="cart.jsp">ViewCart</a></li>
                                         <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                             <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                         </c:if>
                                         <li><hr class="dropdown-divider"></li>

                                    <li><a class="dropdown-item" href="login.jsp">Logout</a></li>


                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="login.jsp">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="register.jsp">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section text-center">
        <div class="container">
            <h1 class="hero-title">Welcome to Store</h1>
            <p class="hero-subtitle">Discover Your Perfect Pair</p>
            <a href="#categories" class="btn btn-light btn-lg">
                Shop Now <i class="fas fa-arrow-right ms-2"></i>
            </a>
        </div>
    </section>

    <!-- Main Content -->
    <div class="container">
        <!-- Messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Category Filter -->
        <div class="category-filter" id="categories">
            <div class="row">
                <div class="col-md-12">
                    <h4 class="category-title">Shop by Category</h4>
                    <div class="btn-group" role="group">
                        <a href="HomeController"
                           class="btn ${selectedTypeId == 0 ? 'btn-primary' : 'btn-outline-primary'}">
                            All Categories
                        </a>
                        <c:forEach items="${productTypes}" var="type">
                            <a href="HomeController?typeId=${type.id}"
                               class="btn ${selectedTypeId == type.id ? 'btn-primary' : 'btn-outline-primary'}">
                                ${type.name}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <!-- Products Grid -->
        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
            <c:forEach items="${products}" var="product">
                <div class="col">
                    <div class="product-card position-relative">
                        <c:if test="${product.proQuantity <= 5 && product.proQuantity > 0}">
                            <span class="stock-badge low-stock">
                                <i class="fas fa-exclamation-triangle"></i>
                                Only ${product.proQuantity} left
                            </span>
                        </c:if>
                        <c:if test="${product.proQuantity == 0}">
                            <span class="stock-badge out-of-stock">
                                <i class="fas fa-times-circle"></i>
                                Out of Stock
                            </span>
                        </c:if>
                        <img src="images/products/${product.proImage}" class="product-image" alt="${product.proName}">
                        <div class="product-info">
                            <h5 class="product-title">${product.proName}</h5>
                            <p class="product-description">${product.proDescription}</p>
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <span class="product-price">
                                    <fmt:formatNumber value="${product.proPrice}" type="currency" currencySymbol="$"/>
                                </span>
                                <span class="text-muted small">
                                    <i class="fas fa-box"></i> ${product.proQuantity} in stock
                                </span>
                            </div>
                            <form action="CartController" method="POST">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.proId}">
                                <button type="submit" class="btn btn-add-cart"
                                        ${product.proQuantity == 0 ? 'disabled' : ''}>
                                    <i class="fas fa-shopping-cart me-2"></i>
                                    ${product.proQuantity == 0 ? 'Out of Stock' : 'Add to Cart'}
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation" class="mt-4 mb-5">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="HomeController?page=${currentPage - 1}
                           ${not empty typeId ? '&typeId='.concat(typeId) : ''}
                           ${not empty searchTerm ? '&searchTerm='.concat(searchTerm) : ''}">
                            <i class="fas fa-chevron-left"></i> Previous
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="HomeController?page=${i}
                               ${not empty typeId ? '&typeId='.concat(typeId) : ''}
                               ${not empty searchTerm ? '&searchTerm='.concat(searchTerm) : ''}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="HomeController?page=${currentPage + 1}
                           ${not empty typeId ? '&typeId='.concat(typeId) : ''}
                           ${not empty searchTerm ? '&searchTerm='.concat(searchTerm) : ''}">
                            Next <i class="fas fa-chevron-right"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div> 
        --%>

        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
            <div class="container">
                <a class="navbar-brand" href="HomeController">
                    <i class="fas fa-shoe-prints me-2"></i>Store
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <form class="d-flex search-form mx-auto" action="HomeController" method="GET">
                        <input class="form-control me-2" type="search" name="searchTerm"
                               placeholder="Search for shoes..." value="${searchTerm}">
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
                                        <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullname}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="#">Profile</a></li>
                                        <li><a class="dropdown-item" href="cart.jsp">ViewCart</a></li>
                                            <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="login.jsp">Logout</a></li>
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

        <!-- Hero Section -->
        <section class="hero-section text-center">
            <div class="container">
                <h1 class="hero-title">Welcome to Store</h1>
                <p class="hero-subtitle">Discover Your Perfect Pair</p>
                <a href="#categories" class="btn btn-light btn-lg">Shop Now <i class="fas fa-arrow-right ms-2"></i></a>
            </div>
        </section>

        <!-- Main Content -->
        <div class="container">
            <!-- Messages -->
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("message"); %>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("error");%>
            </c:if>

            <!-- Category Filter -->
            <div class="category-filter" id="categories">
                <div class="row">
                    <div class="col-md-12">
                        <h4 class="category-title">Shop by Category</h4>
                        <div class="btn-group" role="group">
                            <a href="HomeController"
                               class="btn ${selectedTypeId == 0 ? 'btn-primary' : 'btn-outline-primary'}">
                                All Categories
                            </a>
                            <c:forEach items="${productTypes}" var="type">
                                <a href="HomeController?typeId=${type.cateId}"
                                   class="btn ${selectedTypeId == type.cateId ? 'btn-primary' : 'btn-outline-primary'}">
                                    ${type.cateName}
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Products Grid -->
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                <c:forEach items="${products}" var="product">
                    <div class="col">
                        <div class="product-card position-relative">
                            <c:if test="${product.proStockQuantity <= 5 && product.proStockQuantity > 0}">
                                <span class="stock-badge low-stock">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    Only ${product.proQuantity} left
                                </span>
                            </c:if>
                            <c:if test="${product.proQuantity == 0}">
                                <span class="stock-badge out-of-stock">
                                    <i class="fas fa-times-circle"></i>
                                    Out of Stock
                                </span>
                            </c:if>
                            <img src="images/products/${product.proImageMain}" class="product-image" alt="${product.proName}">
                            <div class="product-info">
                                <h5 class="product-title">${product.proName}</h5>
                                <p class="product-description">${product.proDescription}</p>
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="product-price">
                                        <fmt:formatNumber value="${product.proPrice}" type="currency" currencySymbol="$"/>
                                    </span>
                                    <span class="text-muted small">
                                        <i class="fas fa-box"></i> ${product.proStockQuantity} in stock
                                    </span>
                                </div>
                                <form action="CartController" method="POST">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="productId" value="${product.proId}">
                                    <button type="submit" class="btn btn-add-cart" ${product.proStockQuantity == 0 ? 'disabled' : ''}>
                                        <i class="fas fa-shopping-cart me-2"></i>
                                        ${product.proStockQuantity == 0 ? 'Out of Stock' : 'Add to Cart'}
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Page navigation" class="mt-4 mb-5">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="HomeController?page=${currentPage - 1}${not empty selectedTypeId ? '&typeId=' + selectedTypeId : ''}${not empty searchTerm ? '&searchTerm=' + searchTerm : ''}">
                                <i class="fas fa-chevron-left"></i> Previous
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link"
                                   href="HomeController?page=${i}${not empty selectedTypeId ? '&typeId=' + selectedTypeId : ''}${not empty searchTerm ? '&searchTerm=' + searchTerm : ''}">
                                    ${i}
                                </a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="HomeController?page=${currentPage + 1}${not empty selectedTypeId ? '&typeId=' + selectedTypeId : ''}${not empty searchTerm ? '&searchTerm=' + searchTerm : ''}">
                                Next <i class="fas fa-chevron-right"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>

        </div>

        <!-- Footer -->
        <footer class="bg-dark text-light py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h5><i class="fas fa-shoe-prints me-2"></i>Shoe Paradise</h5>
                        <p>Your ultimate destination for quality footwear.</p>
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
                    <small>&copy; 2024 Shoe Paradise. All rights reserved.</small>
                </div>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const shopNowButton = document.querySelector('.btn-light');
                shopNowButton.addEventListener('click', function (e) {
                    e.preventDefault();
                    const productsSection = document.getElementById('categories');
                    productsSection.scrollIntoView({behavior: 'smooth'});
                });
            });
        </script>
    </body>
</html>