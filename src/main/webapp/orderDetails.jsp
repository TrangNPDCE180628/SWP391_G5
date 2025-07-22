<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">



<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="text-primary">Order Detail - Order ID: ${orderInfo.orderId}</h2>
</div>

<!-- Order Summary Section -->
<div class="card mb-4 shadow-sm">
    <div class="card-header bg-light fw-bold">Customer & Order Summary</div>
    <div class="card-body">
        <div class="row mb-2">
            <div class="col-md-4"><strong>Customer Name:</strong> ${orderInfo.cusFullName}</div>
            <div class="col-md-4"><strong>Order Date:</strong> ${orderInfo.orderDate}</div>
            <div class="col-md-4"><strong>Order Status:</strong> ${orderInfo.orderStatus}</div>
        </div>
        <div class="row mb-2">
            <div class="col-md-4"><strong>Payment Method:</strong> ${orderInfo.paymentMethod}</div>
            <div class="col-md-4"><strong>Final Amount:</strong> <fmt:formatNumber value="${orderInfo.finalAmount}" type="number" groupingUsed="true" /> Đ
            </div>
            <div class="col-md-4"><strong>Voucher Name:</strong> ${orderInfo.codeName}</div>
        </div>
        <div class="row mb-2">
            <div class="col-12"><strong>Shipping Address:</strong> ${orderInfo.shippingAddress}</div>
            <div class="col-12"><strong>Receiver Name:</strong> ${orderInfo.receiverName}</div>
            <div class="col-12"><strong>Receiver Phone:</strong> ${orderInfo.receiverPhone}</div>
        </div>
    </div>
</div>

<!-- Product Detail Table -->
<div class="card shadow-sm">
    <div class="card-header bg-light fw-bold">Product Details</div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-bordered table-hover mb-0">
                <thead class="table-light text-center align-middle">
                    <tr>
                        <th>#</th>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Total Price</th>
                        <th>Voucher Name</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${orderDetails}" varStatus="loop">
                        <tr>
                            <td class="text-center">${loop.index + 1}</td>
                            <td>${row.proId}</td>
                            <td>${row.proName}</td>
                            <td class="text-center">${row.quantity}</td>
                            <td class="text-end"><fmt:formatNumber value="${row.unitPrice}" type="number" groupingUsed="true" /> Đ
                            </td>
                            <td class="text-end"><fmt:formatNumber value="${row.totalPrice}" type="number" groupingUsed="true" /> Đ
                            </td>
                            <td class="text-center">${row.codeName}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>


