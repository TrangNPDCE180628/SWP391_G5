<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    .admin-order-details {
        padding: 20px;
    }
    .order-header {
        background: linear-gradient(135deg, #007bff, #0056b3);
        color: white;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    .info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 15px;
        margin-bottom: 20px;
    }
    .info-card {
        background-color: #f8f9fa;
        padding: 15px;
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
    .status-badge {
        display: inline-block;
        padding: 4px 12px;
        border-radius: 15px;
        font-size: 0.85rem;
        font-weight: 600;
        text-transform: uppercase;
    }
    .status-shipped {
        background-color: #d1ecf1;
        color: #0c5460;
    }
    .status-pending {
        background-color: #fff3cd;
        color: #856404;
    }
    .status-completed {
        background-color: #d4edda;
        color: #155724;
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
        margin-top: 20px;
    }
    .product-item {
        display: flex;
        align-items: center;
        padding: 15px;
        border: 1px solid #dee2e6;
        border-radius: 8px;
        margin-bottom: 10px;
        background-color: white;
    }
    .product-image {
        width: 60px;
        height: 60px;
        object-fit: cover;
        border-radius: 6px;
        margin-right: 15px;
        border: 1px solid #dee2e6;
    }
    .product-info {
        flex: 1;
    }
    .product-name {
        font-weight: 600;
        color: #333;
        margin-bottom: 5px;
    }
    .product-id {
        color: #6c757d;
        font-size: 0.9rem;
    }
    .quantity-price {
        text-align: right;
        min-width: 120px;
    }
    .total-section {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        margin-top: 20px;
    }
    .total-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
    }
    .total-row:last-child {
        border-top: 2px solid #dee2e6;
        padding-top: 10px;
        font-weight: 600;
        font-size: 1.1rem;
        color: #e74c3c;
    }
</style>

<div class="admin-order-details">
    <!-- Order Header -->
    <div class="order-header">
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h4><i class="fas fa-receipt me-2"></i>Order #${order.orderId}</h4>
                <p class="mb-0 opacity-75">
                    <i class="fas fa-calendar me-2"></i>
                    Order Date: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                </p>
            </div>
            <div>
                <span class="status-badge status-${order.orderStatus.toLowerCase().replace(' ', '-')}">
                    ${order.orderStatus}
                </span>
            </div>
        </div>
    </div>

    <!-- Order Information Grid -->
    <div class="info-grid">
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
        </div>

        <!-- Payment Info -->
        <div class="info-card">
            <h6><i class="fas fa-credit-card me-2"></i>Payment Information</h6>
            <p><strong>Method:</strong> ${order.paymentMethod}</p>
            <p><strong>Total Amount:</strong> 
                <span class="text-danger fw-bold">
                    <fmt:formatNumber value="${finalAmount}" pattern="#,##0" />đ
                </span>
            </p>
        </div>
    </div>

    <!-- Products Section -->
    <div class="product-list">
        <h5 class="mb-3">
            <i class="fas fa-shopping-bag me-2"></i>
            Ordered Products (${orderDetailsWithProducts.size()} products)
        </h5>

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
                    <div class="text-muted">
                        <fmt:formatNumber value="${item.orderDetail.unitPrice}" pattern="#,##0" />đ / product
                    </div>
                </div>

                <!-- Quantity & Price -->
                <div class="quantity-price">
                    <div class="fw-bold">Qty: ${item.orderDetail.quantity}</div>
                    <div class="text-danger fw-bold">
                        <fmt:formatNumber value="${item.subtotal}" pattern="#,##0" />đ
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Voucher Section -->
    <c:if test="${voucher != null}">
        <div class="alert alert-info">
            <div class="d-flex align-items-center">
                <i class="fas fa-ticket-alt me-3 fs-4"></i>
                <div class="flex-grow-1">
                    <strong>Applied Voucher:</strong> ${voucher.codeName}
                    <br><small class="text-muted">${voucher.description}</small>
                </div>
                <div class="text-end">
                    <div class="fw-bold text-success">
                        -<fmt:formatNumber value="${discountAmount}" pattern="#,##0" />đ
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Total Section -->
    <div class="total-section">
        <div class="total-row">
            <span>Subtotal:</span>
            <span><fmt:formatNumber value="${subtotalAmount}" pattern="#,##0" />đ</span>
        </div>

        <c:if test="${discountAmount > 0}">
            <div class="total-row">
                <span>Discount:</span>
                <span class="text-success">-<fmt:formatNumber value="${discountAmount}" pattern="#,##0" />đ</span>
            </div>
        </c:if>

        <div class="total-row">
            <span>Shipping Fee:</span>
            <span>Free</span>
        </div>

        <div class="total-row">
            <span>Total:</span>
            <span><fmt:formatNumber value="${finalAmount}" pattern="#,##0" />đ</span>
        </div>
    </div>
</div>
