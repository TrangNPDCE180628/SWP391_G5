<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart - Tech Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation (simplified) -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="HomeController">
                <i class="fas fa-microchip me-2"></i>Tech Store
            </a>
        </div>
    </nav>

    <!-- Breadcrumb for Cart Page -->
    <c:set var="breadcrumbType" value="cart" />
    <%@ include file="includes/breadcrumb.jsp" %>

    <!-- Cart Content -->
    <div class="container my-4">
        <h2><i class="fas fa-shopping-cart me-2"></i>Shopping Cart</h2>
        <p>This is a demo cart page showing the breadcrumb component.</p>
        
        <!-- Demo different breadcrumb types -->
        <div class="row mt-5">
            <div class="col-12">
                <h4>Different Breadcrumb Examples:</h4>
                
                <div class="mb-4">
                    <h6>1. Cart Breadcrumb (Current):</h6>
                    <c:set var="breadcrumbType" value="cart" />
                    <%@ include file="includes/breadcrumb.jsp" %>
                </div>
                
                <div class="mb-4">
                    <h6>2. Profile Breadcrumb:</h6>
                    <c:set var="breadcrumbType" value="profile" />
                    <%@ include file="includes/breadcrumb.jsp" %>
                </div>
                
                <div class="mb-4">
                    <h6>3. Orders Breadcrumb:</h6>
                    <c:set var="breadcrumbType" value="orders" />
                    <%@ include file="includes/breadcrumb.jsp" %>
                </div>
                
                <div class="mb-4">
                    <h6>4. Category Breadcrumb:</h6>
                    <c:set var="breadcrumbType" value="category" />
                    <c:set var="categoryName" value="Smartphones" />
                    <%@ include file="includes/breadcrumb.jsp" %>
                </div>
                
                <div class="mb-4">
                    <h6>5. Login Breadcrumb:</h6>
                    <c:set var="breadcrumbType" value="auth" />
                    <c:set var="currentPageName" value="Login" />
                    <%@ include file="includes/breadcrumb.jsp" %>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
