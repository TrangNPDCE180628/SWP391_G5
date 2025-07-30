<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart Test - Tech Store</title>
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

    <!-- Test Breadcrumb for Cart -->
    <c:set var="breadcrumbType" value="cart" />
    <%@ include file="includes/breadcrumb.jsp" %>

    <!-- Content -->
    <div class="container my-4">
        <h2><i class="fas fa-shopping-cart me-2"></i>Cart Test Page</h2>
        <p>This page tests if the breadcrumb is working for cart pages.</p>
        
        <div class="alert alert-info">
            <strong>Debug Info:</strong><br>
            breadcrumbType: <c:out value="${breadcrumbType}" /><br>
            Request URI: <%= request.getRequestURI() %><br>
            Context Path: <%= request.getContextPath() %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
