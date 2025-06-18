<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .order-card {
            border: 1px solid #dee2e6;
            border-radius: 0.25rem;
            margin-bottom: 1rem;
            padding: 1rem;
        }
        .order-header {
            border-bottom: 1px solid #dee2e6;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
        .order-items {
            margin-bottom: 1rem;
        }
        .order-item {
            padding: 0.5rem 0;
            border-bottom: 1px solid #f8f9fa;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .status-badge {
            font-size: 0.875rem;
            padding: 0.25rem 0.5rem;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <h1 class="mb-4">My Orders</h1>
        
        <c:if test="${empty orders}">
            <div class="alert alert-info">
                You haven't placed any orders yet.
            </div>
        </c:if>

        <c:forEach items="${orders}" var="order">
            <div class="order-card">
                <div class="order-header d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="mb-0">Order #${order.id}</h5>
                        <small class="text-muted">${order.orderDate}</small>
                    </div>
                    <div>
                        <span class="badge ${order.status == 'pending' ? 'bg-warning' : 
                                            order.status == 'completed' ? 'bg-success' : 'bg-danger'} status-badge">
                            ${order.status}
                        </span>
                    </div>
                </div>

                <div class="order-items">
                    <c:forEach items="${orderDetailsMap[order.id]}" var="detail">
                        <div class="order-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-0">${detail.productName}</h6>
                                <small class="text-muted">Quantity: ${detail.quantity}</small>
                            </div>
                            <div class="text-end">
                                <div>$${detail.unitPrice} each</div>
                                <div><strong>$${detail.totalPrice}</strong></div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <c:if test="${order.status == 'pending'}">
                            <a class="btn btn-sm btn-success" href="${pageContext.request.contextPath}/PaymentController?orderId=${order.id}">
                                <i class="fas fa-credit-card"></i> Payment / Checkout
                            </a>
                        </c:if>
                    </div>
                    <div class="text-end">
                        <h5 class="mb-0">Total: $${order.totalPrice}</h5>
                    </div>
                </div>
            </div>
        </c:forEach>

        <div class="mt-4">
            <a href="HomeController" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to Home
            </a>
        </div>
    </div>

    <!-- Order Details Modal -->
    <div class="modal fade" id="orderDetailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Order Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <h6>Order Information</h6>
                            <p><strong>Order ID:</strong> <span id="detailOrderId"></span></p>
                            <p><strong>Order Date:</strong> <span id="detailOrderDate"></span></p>
                            <p><strong>Status:</strong> <span id="detailStatus"></span></p>
                        </div>
                        <div class="col-md-6">
                            <h6>Payment Information</h6>
                            <p><strong>Total Price:</strong> $<span id="detailTotalPrice"></span></p>
                        </div>
                    </div>
                    <h6>Order Items</h6>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Unit Price</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody id="orderItemsTable">
                                <!-- Order items will be populated here -->
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function viewOrderDetails(orderId) {
            // Fetch order details via AJAX
            fetch(`OrderController?action=getOrderDetails&id=${orderId}`)
                .then(response => response.json())
                .then(data => {
                    // Populate order information
                    document.getElementById('detailOrderId').textContent = data.order.id;
                    document.getElementById('detailOrderDate').textContent = data.order.orderDate;
                    document.getElementById('detailStatus').textContent = data.order.status;
                    document.getElementById('detailTotalPrice').textContent = data.order.totalPrice;

                    // Populate order items
                    const itemsTable = document.getElementById('orderItemsTable');
                    itemsTable.innerHTML = '';
                    data.orderItems.forEach(item => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${item.productName}</td>
                            <td>${item.quantity}</td>
                            <td>$${item.unitPrice}</td>
                            <td>$${item.totalPrice}</td>
                        `;
                        itemsTable.appendChild(row);
                    });

                    // Show modal
                    new bootstrap.Modal(document.getElementById('orderDetailsModal')).show();
                })
                .catch(error => {
                    console.error('Error fetching order details:', error);
                    alert('Failed to load order details. Please try again.');
                });
        }
    </script>
</body>
</html> 