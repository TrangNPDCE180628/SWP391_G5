```html
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
        .status-badge-pending { background-color: #ffc107; color: black; }
        .status-badge-done { background-color: #28a745; color: white; }
        .status-badge-cancel { background-color: #dc3545; color: white; }
        .card-header { background-color: #f8f9fa; }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Order Details</h1>
        <a href="AdminController?action=filterOrdersByStatus&status=${selectedStatus}" class="btn btn-secondary mb-3">
            <i class="fas fa-arrow-left"></i> Back to Orders
        </a>

        <!-- Order Information -->
        <div class="card mb-4">
            <div class="card-header">
                <h4>Order #${order.id}</h4>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Customer ID:</strong> ${order.cusId}</p>
                        <p><strong>Customer Name:</strong> ${customer.cusFullName}</p>
                        <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                        <p><strong>Status:</strong> 
                            <span class="badge status-badge-${order.status}">${order.status}</span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                        <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                        <p><strong>Total Amount:</strong> $${order.totalAmount}</p>
                        <p><strong>Discount:</strong> $${order.discountAmount}</p>
                        <p><strong>Final Amount:</strong> $${order.finalAmount}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Items -->
        <div class="card">
            <div class="card-header">
                <h4>Order Items</h4>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Item ID</th>
                                <th>Product ID</th>
                                <th>Quantity</th>
                                <th>Unit Price</th>
                                <th>Total Price</th>
                                <th>Voucher ID</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderDetails}" var="item">
                                <tr>
                                    <td>${item.id}</td>
                                    <td>${item.proId}</td>
                                    <td>${item.quantity}</td>
                                    <td>$${item.unitPrice}</td>
                                    <td>$${item.totalPrice}</td>
                                    <td>${item.voucherId != null ? item.voucherId : 'N/A'}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```