<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-4">
    <h2 class="mb-4">Order Details</h2>
    <c:if test="${not empty ERROR}">
        <div class="alert alert-danger">${ERROR}</div>
    </c:if>
    <div class="card mb-4">
        <div class="card-body">
            <h5 class="card-title">Order #${order.id}</h5>
            <p><strong>Customer:</strong> ${customer.fullname}</p>
            <p><strong>Order Date:</strong> ${order.orderDate}</p>
            <p><strong>Status:</strong> <span class="badge ${order.status == 'pending' ? 'bg-warning' : order.status == 'completed' ? 'bg-success' : 'bg-danger'}">${order.status}</span></p>
            <p><strong>Total Price:</strong> $${order.totalPrice}</p>
        </div>
    </div>
    <h5>Order Items</h5>
    <div class="table-responsive">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${orderDetails}" var="item">
                    <tr>
                        <td>${item.productId}</td>
                        <td>${item.quantity}</td>
                        <td>$${item.unitPrice}</td>
                        <td>$${item.totalPrice}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <a href="${pageContext.request.contextPath}/AdminController" class="btn btn-secondary mt-3">Back to Admin</a>
</div>
</body>
</html> 