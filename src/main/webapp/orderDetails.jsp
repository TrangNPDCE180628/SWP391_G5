<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Custom Styles for Order Details */
        .order-details-card {
            border: none;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
            margin-bottom: 2rem;
        }
        .order-details-card .card-header {
            background: linear-gradient(90deg, #007bff, #0056b3);
            color: white;
            padding: 1.5rem;
            border-radius: 10px 10px 0 0;
            font-size: 1.5rem;
            font-weight: 600;
        }
        .order-details-card .card-body {
            padding: 2rem;
        }
        .order-details-info p {
            margin-bottom: 0.75rem;
            font-size: 1.1rem;
            color: #333;
        }
        .order-details-info .status-badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 12px;
            font-size: 1rem;
            font-weight: 500;
        }
        .status-done {
            background-color: #28a745;
            color: white;
        }
        .status-pending {
            background-color: #ffc107;
            color: #333;
        }
        .status-cancel {
            background-color: #dc3545;
            color: white;
        }
        .order-items-card {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
        }
        .order-items-card .card-header {
            background-color: #f8f9fa;
            padding: 1rem 1.5rem;
            font-size: 1.3rem;
            font-weight: 600;
            border-bottom: 1px solid #dee2e6;
        }
        .order-items-table th {
            background-color: #e9ecef;
            font-weight: 600;
            color: #333;
            padding: 1rem;
            border-bottom: 2px solid #dee2e6;
        }
        .order-items-table td {
            padding: 1rem;
            vertical-align: middle;
            font-size: 1rem;
        }
        .order-items-table tr {
            transition: background-color 0.2s ease;
        }
        .order-items-table tr:hover {
            background-color: #f1f3f5;
        }
        .currency {
            font-weight: 500;
            color: #28a745;
        }
        .back-btn {
            background-color: #6c757d;
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            text-decoration: none;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }
        .back-btn:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
        }
        /* Responsive Adjustments */
        @media (max-width: 768px) {
            .order-details-card .card-header {
                font-size: 1.2rem;
                padding: 1rem;
            }
            .order-details-card .card-body {
                padding: 1rem;
            }
            .order-items-table th, .order-items-table td {
                font-size: 0.9rem;
                padding: 0.75rem;
            }
            .back-btn {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="order-details-card">
            <div class="card-header">Order Details</div>
            <div class="card-body">
                <div class="order-details-info">
                    <p><strong>Order #${order.id}</strong></p>
                    <p><strong>Customer:</strong> ${customer.cusFullName}</p>
                    <p><strong>Order Date:</strong> ${order.orderDate}</p>
                    <p><strong>Status:</strong> 
                        <span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span>
                    </p>
                    <p><strong>Total Amount:</strong> <span class="currency">$${String.format('%,.2f', order.totalAmount)}</span></p>
                    <p><strong>Discount Amount:</strong> <span class="currency">$${String.format('%,.2f', order.totalAmount - order.finalAmount)}</span></p>
                    <p><strong>Final Amount:</strong> <span class="currency">$${String.format('%,.2f', order.finalAmount)}</span></p>
                    <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                    <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                </div>
            </div>
        </div>

        <div class="order-items-card mt-4">
            <div class="card-header">Order Items</div>
            <div class="card-body p-0">
                <table class="table order-items-table">
                    <thead>
                        <tr>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Total</th>
                            <th>Voucher ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${orderDetails}" var="detail">
                            <tr>
                                <td>${detail.productName}</td>
                                <td>${detail.quantity}</td>
                                <td><span class="currency">$${String.format('%,.2f', detail.unitPrice)}</span></td>
                                <td><span class="currency">$${String.format('%,.2f', detail.total)}</span></td>
                                <td>${detail.voucherId}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <a href="AdminController?action=viewOrders" class="back-btn mt-4 d-inline-block">Back to Orders</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>