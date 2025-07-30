<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    Reusable Breadcrumb Component
    
    Parameters to pass when including this file:
    - breadcrumbType: "home", "product-detail", "category", "cart", "profile", etc.
    - product: Product object (for product-detail pages)
    - productTypes: List<ProductTypes> (for dynamic category names)
    - categoryName: Category name (optional override)
    - currentPageName: Current page name (optional override)
--%>

<div class="container mt-3">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-light p-3 rounded shadow-sm">
            
            <!-- Home Link (Always present) -->
            <li class="breadcrumb-item">
                <a href="HomeController" class="text-decoration-none">
                    <i class="fas fa-home me-1"></i>Home
                </a>
            </li>
            
            <!-- Dynamic Breadcrumb based on breadcrumbType -->
            <c:choose>
                
                <%-- Product Detail Page Breadcrumb --%>
                <c:when test="${breadcrumbType == 'product-detail' && not empty product}">
                    <li class="breadcrumb-item">
                        <a href="HomeController?typeId=${product.proTypeId}" class="text-decoration-none">
                            <i class="fas fa-layer-group me-1"></i>
                            <c:choose>
                                <c:when test="${not empty categoryName}">
                                    ${categoryName}
                                </c:when>
                                <c:otherwise>
                                    <%-- Dynamic category name from productTypes list --%>
                                    <c:set var="categoryFound" value="false" />
                                    <c:forEach items="${productTypes}" var="type">
                                        <c:if test="${type.id == product.proTypeId && !categoryFound}">
                                            ${type.name}
                                            <c:set var="categoryFound" value="true" />
                                        </c:if>
                                    </c:forEach>
                                    <%-- Fallback if no category found --%>
                                    <c:if test="${!categoryFound}">
                                        Electronics
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                    <li class="breadcrumb-item active text-truncate" aria-current="page" style="max-width: 300px;">
                        <i class="fas fa-mobile-alt me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">
                                ${currentPageName}
                            </c:when>
                            <c:when test="${not empty product.proName}">
                                <span title="${product.proName}">
                                    <c:choose>
                                        <c:when test="${product.proName.length() > 30}">
                                            ${product.proName.substring(0, 27)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${product.proName}
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </c:when>
                            <c:otherwise>Product Details</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Category Page Breadcrumb --%>
                <c:when test="${breadcrumbType == 'category'}">
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-layer-group me-1"></i>
                        <c:choose>
                            <c:when test="${not empty categoryName}">${categoryName}</c:when>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>Categories</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Cart Page Breadcrumb --%>
                <c:when test="${breadcrumbType == 'cart'}">
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-shopping-cart me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>Shopping Cart</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Profile Page Breadcrumb --%>
                <c:when test="${breadcrumbType == 'profile'}">
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-user me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>My Profile</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Order History Breadcrumb --%>
                <c:when test="${breadcrumbType == 'orders'}">
                    <li class="breadcrumb-item">
                        <a href="ProfileCustomerController" class="text-decoration-none">
                            <i class="fas fa-user me-1"></i>Profile
                        </a>
                    </li>
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-receipt me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>Order History</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Order Details Breadcrumb --%>
                <c:when test="${breadcrumbType == 'order-detail'}">
                    <li class="breadcrumb-item">
                        <a href="ProfileCustomerController" class="text-decoration-none">
                            <i class="fas fa-user me-1"></i>Profile
                        </a>
                    </li>
                    <li class="breadcrumb-item">
                        <a href="OrderHistoryController" class="text-decoration-none">
                            <i class="fas fa-receipt me-1"></i>Orders
                        </a>
                    </li>
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-file-invoice me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>Order Details</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Login/Register Breadcrumb --%>
                <c:when test="${breadcrumbType == 'auth'}">
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-sign-in-alt me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:otherwise>Account</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Search Results Breadcrumb --%>
                <c:when test="${breadcrumbType == 'search'}">
                    <li class="breadcrumb-item active" aria-current="page">
                        <i class="fas fa-search me-1"></i>
                        <c:choose>
                            <c:when test="${not empty currentPageName}">${currentPageName}</c:when>
                            <c:when test="${not empty param.searchTerm}">Search: "${param.searchTerm}"</c:when>
                            <c:otherwise>Search Results</c:otherwise>
                        </c:choose>
                    </li>
                </c:when>
                
                <%-- Default/Generic Breadcrumb --%>
                <c:otherwise>
                    <c:if test="${not empty currentPageName}">
                        <li class="breadcrumb-item active" aria-current="page">
                            <i class="fas fa-file me-1"></i>${currentPageName}
                        </li>
                    </c:if>
                </c:otherwise>
                
            </c:choose>
        </ol>
    </nav>
</div>

<style>
    /* Breadcrumb Styling */
    .breadcrumb {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border: 1px solid #dee2e6;
        margin-bottom: 0;
    }
    .breadcrumb-item + .breadcrumb-item::before {
        content: "›";
        color: #6c757d;
        font-size: 1.2rem;
        font-weight: bold;
    }
    .breadcrumb-item a {
        color: #495057;
        transition: color 0.3s ease, transform 0.2s ease;
    }
    .breadcrumb-item a:hover {
        color: #007bff;
        text-decoration: underline !important;
        transform: translateY(-1px);
    }
    .breadcrumb-item.active {
        color: #6c757d;
        font-weight: 500;
    }
    
    /* Responsive truncation */
    @media (max-width: 576px) {
        .breadcrumb-item.active {
            max-width: 200px !important;
        }
        .breadcrumb-item:not(.active):not(:first-child) {
            display: none;
        }
        .breadcrumb-item:last-child {
            display: list-item !important;
        }
        .breadcrumb-item:first-child + .breadcrumb-item:last-child::before {
            content: "› ... ›";
        }
    }
</style>
