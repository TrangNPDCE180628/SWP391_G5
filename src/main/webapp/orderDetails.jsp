<%@ page contentType="text/html;charset=UTF-8" language="Java"  %>              
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">



<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="text-primary">Order Details - Order ID: ${order.orderId}</h2>
</div>

<!-- Order Summary Section -->
<div class="card mb-4 shadow-sm">
    <div class="card-header bg-light fw-bold">Customer Information & Order Summary</div>
    <div class="card-body">
        <div class="row mb-2">
            <div class="col-md-4"><strong>Customer Name:</strong> ${order.cusFullName}</div>
            <div class="col-md-4"><strong>Order Date:</strong> ${order.orderDate}</div>
            <div class="col-md-4"><strong>Order Status:</strong> ${order.orderStatus}</div>
        </div>
        <div class="row mb-2">
            <div class="col-md-4"><strong>Payment Method:</strong> ${order.paymentMethod}</div>
            <div class="col-md-4"><strong>Total Amount:</strong> <fmt:formatNumber value="${order.finalAmount}" type="number" groupingUsed="true" /> Đ
            </div>
            <div class="col-md-4"><strong>Voucher Name:</strong> ${order.codeName}</div>
        </div>
        <div class="row mb-2">
            <div class="col-12"><strong>Shipping Address:</strong> ${order.shippingAddress}</div>
            <div class="col-12"><strong>Receiver Name:</strong> ${order.receiverName}</div>
            <div class="col-12"><strong>Receiver Phone:</strong> ${order.receiverPhone}</div>
        </div>
    </div>
</div>

<!-- Product Detail Table -->
<div class="card shadow-sm">
    <div class="card-header bg-light fw-bold">
        Product Details
        <c:if test="${fn:toLowerCase(fn:trim(order.orderStatus)) == 'shipped'}">
            <small class="text-info ms-3">
                <i class="fas fa-star"></i> Click "Review" to rate and review each product
            </small>
        </c:if>
        <!-- Debug: Show current order status -->
        <small class="text-muted ms-2">[Status: "${order.orderStatus}" | Length: ${fn:length(order.orderStatus)} | Lower: "${fn:toLowerCase(fn:trim(order.orderStatus))}" | Match: ${fn:toLowerCase(fn:trim(order.orderStatus)) == 'shipped'}]</small>
    </div>
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
                        <c:if test="${fn:toLowerCase(fn:trim(order.orderStatus)) == 'shipped'}">
                            <th>Action</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${orderDetailsWithProducts}" varStatus="loop">
                        <tr>
                            <td class="text-center">${loop.index + 1}</td>
                            <td>${row.orderDetail.proId}</td>
                            <td>${row.product.proName}</td>
                            <td class="text-center">${row.orderDetail.quantity}</td>
                            <td class="text-end"><fmt:formatNumber value="${row.orderDetail.unitPrice}" type="number" groupingUsed="true" /> Đ
                            </td>
                            <td class="text-end"><fmt:formatNumber value="${row.subtotal}" type="number" groupingUsed="true" /> Đ
                            </td>
                            <td class="text-center">${row.orderDetail.codeName}</td>
                            <c:if test="${fn:toLowerCase(fn:trim(order.orderStatus)) == 'shipped'}">
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${row.orderDetail.proId}" 
                                       class="btn btn-sm btn-warning text-white" 
                                       title="Đánh giá sản phẩm này"
                                       style="text-decoration: none;">
                                        <i class="fas fa-star me-1"></i>Đánh giá
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>


