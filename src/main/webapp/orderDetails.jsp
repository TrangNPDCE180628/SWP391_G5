<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <title>Order Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>

        <div class="container mt-4">
            <h2>Order Detail - Order ID: ${order.orderId}</h2>

            <p><strong>Customer ID:</strong> ${order.cusId}</p>
            <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>

            <c:if test="${empty orderDetails}">
                <p class="text-danger">No order details found for this order.</p>
            </c:if>

            <c:if test="${not empty orderDetails}">
                <table class="table table-bordered mt-3">
                    <thead class="thead-dark">
                        <tr>
                            <th>Order Detail ID</th>
                            <th>Product ID</th>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Total Price</th>
                            <th>Voucher ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${orderDetails}">
                            <tr>
                                <td>${detail.orderDetailId}</td>
                                <td>${detail.proId}</td>
                                <td>${productMap[detail.proId].proName}</td>
                                <td>${detail.quantity}</td>
                                <td><fmt:formatNumber value="${detail.unitPrice}" type="currency"/></td>
                                <td><fmt:formatNumber value="${detail.totalPrice}" type="currency"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${detail.voucherId != null}">
                                            ${detail.voucherId}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <a href="AdminController?action=loadAdminPage&tab=orders" class="btn btn-secondary mt-3">Back to Orders</a>
        </div>

    </body>
</html>