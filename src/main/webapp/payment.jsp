<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-4">
    <h2 class="mb-4">Proceed to Payment</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <div class="card mb-4">
        <div class="card-body">
            <h5 class="card-title">Order #${order.id}</h5>
            <p><strong>Order Date:</strong> ${order.orderDate}</p>
            <p><strong>Status:</strong> <span class="badge bg-warning">${order.status}</span></p>
            <p><strong>Total Price:</strong> $${order.totalPrice}</p>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/PaymentController" method="post">
        <input type="hidden" name="orderId" value="${order.id}" />
        <div class="mb-3">
            <label class="form-label">Payment Method</label><br>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="paymentMethod" id="creditcard" value="creditcard" checked>
                <label class="form-check-label" for="creditcard">Credit Card</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="paymentMethod" id="paypal" value="paypal">
                <label class="form-check-label" for="paypal">PayPal</label>
            </div>
        </div>
        <div id="creditCardFields">
            <div class="mb-3">
                <label for="cardNumber" class="form-label">Credit Card Number</label>
                <input type="text" class="form-control" id="cardNumber" name="cardNumber" maxlength="16">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Pay Now</button>
        <a href="${pageContext.request.contextPath}/orders.jsp" class="btn btn-secondary ms-2">Cancel</a>
    </form>
</div>
<script>
    // Ẩn/hiện trường credit card tuỳ phương thức
    document.addEventListener('DOMContentLoaded', function() {
        const creditCardFields = document.getElementById('creditCardFields');
        const radios = document.getElementsByName('paymentMethod');
        radios.forEach(radio => {
            radio.addEventListener('change', function() {
                if (this.value === 'creditcard') {
                    creditCardFields.style.display = '';
                } else {
                    creditCardFields.style.display = 'none';
                }
            });
        });
        // Khởi tạo trạng thái
        if (document.querySelector('input[name=paymentMethod]:checked').value === 'paypal') {
            creditCardFields.style.display = 'none';
        }
    });
</script>
</body>
</html> 