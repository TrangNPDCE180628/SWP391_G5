<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 900px;
        }
        .card {
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .badge {
            font-size: 0.9em;
        }
    </style>
</head>
<body>
<div class="container py-4">
    <h2 class="mb-4"><i class="fas fa-shopping-cart me-2"></i>Order Details</h2>
    
    <c:if test="${not empty ERROR}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${ERROR}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-hidden="true"></button>
        </div>
    </c:if>

    <c:if test="${not empty order}">
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Order #${order.id}</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Customer:</strong> ${customer != null ? customer.cusFullName : 'Unknown'}</p>
                        <p><strong>Email:</strong> ${customer != null ? customer.cusGmail : 'N/A'}</p>
                        <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                        <p><strong>Status:</strong> 
                            <span class="badge ${order.status == 'Pending' ? 'bg-warning text-dark' : order.status == 'Done' ? 'bg-success' : 'bg-danger'}">
                                ${order.status}
                            </span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Total Amount:</strong> <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/></p>
                        <p><strong>Shipping Address:</strong> ${order.shippingAddress != null ? order.shippingAddress : 'N/A'}</p>
                        <p><strong>Payment Method:</strong> ${order.paymentMethod != null ? order.paymentMethod : 'N/A'}</p>
                        <c:if test="${order.voucherId != null}">
                            <p><strong>Voucher ID:</strong> ${order.voucherId}</p>
                            <p><strong>Discount Amount:</strong> <fmt:formatNumber value="${order.discountAmount != null ? order.discountAmount : 0}" type="currency" currencySymbol="$"/></p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <h5 class="mb-3">Order Items</h5>
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="table-primary">
                    <tr>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orderDetails}" var="item">
                        <tr>
                            <td>${item.productName != null ? item.productName : 'Unknown'}</td>
                            <td>${item.quantity}</td>
                            <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="$"/></td>
                            <td><fmt:formatNumber value="${item.quantity * item.unitPrice}" type="currency" currencySymbol="$"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orderDetails}">
                        <tr>
                            <td colspan="4" class="text-center">No items found for this order.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/AdminController?action=loadOrders&tab=orders" class="btn btn-secondary mt-3">
        <i class="fas fa-arrow-left me-1"></i> Back to Orders
    </a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>