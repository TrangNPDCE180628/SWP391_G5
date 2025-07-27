<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Order Management</h2>

<!-- Filter by Status -->
<form method="get" action="AdminController" class="mb-3 d-flex align-items-center gap-2">
    <input type="hidden" name="action" value="filterOrders"/>
    <input type="hidden" name="tab" value="orders"/>

    <label for="statusFilter" class="form-label mb-0">Status:</label>
    <select name="sortByOrderStatus" id="statusFilter" class="form-select w-auto">
        <option value="All" ${filterStatus == 'All' ? 'selected' : ''}>All</option>
        <option value="pending" ${filterStatus == 'pending' ? 'selected' : ''}>Pending</option>
        <option value="shipped" ${filterStatus == 'shipped' ? 'selected' : ''}>Shipped</option>
        <option value="processing" ${filterStatus == 'processing' ? 'selected' : ''}>Processing</option>
        <option value="completed" ${filterStatus == 'completed' ? 'selected' : ''}>Completed</option>
        <option value="cancel" ${filterStatus == 'cancelled' ? 'selected' : ''}>Cancelled</option>
    </select>

    <button type="submit" class="btn btn-primary">Apply Filter</button>
    <a href="AdminController?tab=orders" class="btn btn-secondary">Reset</a>
</form>

<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Order ID</th>
                <th>Customer Name</th>
                <th>Order Date</th>
                <th>Final Amount</th>
                <th>Status</th>
                <th>Payment</th>
                <th>Shipping Address</th>
                <th>Receiver Name</th>
                <th>Receiver Phone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${orders}" var="row">
                <tr>
                    <td>${row.orderId}</td>
                    <td>${row.cusFullName}</td>
                    <td><fmt:formatDate value="${row.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatNumber value="${row.finalAmount}" type="number" groupingUsed="true" /> đ
                    </td>
                    <td>${row.orderStatus}</td>
                    <td>${row.paymentMethod}</td>
                    <td>${row.shippingAddress}</td>
                    <td>${row.receiverName}</td>
                    <td>${row.receiverPhone}</td>
                    <td>
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-sm btn-info" onclick="loadOrderDetails(${row.orderId})">
                                <i class="fas fa-eye"></i> View
                            </button>

                            <button type="button" class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editModal" onclick="openEditModal('${row.orderId}', '${row.orderStatus}')">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <form method="post" action="AdminController" onsubmit="return confirm('Are you sure you want to delete this order?');">
                                <input type="hidden" name="action" value="deleteOrder"/>
                                <input type="hidden" name="orderId" value="${row.orderId}"/>
                                <input type="hidden" name="tab" value="orders"/>
                                <button type="submit" class="btn btn-sm btn-danger">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>


<!-- Edit Order Status Modal -->
<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="AdminController">
                <div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">Update Order Status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" name="action" value="updateOrderStatus"/>
                    <input type="hidden" name="orderId" id="modalOrderId"/>
                    <input type="hidden" name="tab" value="orders"/>
                    <input type="hidden" name="currentFilterStatus" value="${filterStatus}"/>

                    <label for="modalStatusSelect" class="form-label">Order Status:</label>
                    <select name="status" id="modalStatusSelect" class="form-select">
                        <option value="pending">Pending</option>
                        <option value="processing">Shipping</option>
                        <option value="completed">Completed</option>
                        <option value="cancel">Cancelled</option>
                    </select>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Order Detail Modal -->
<div class="modal fade" id="orderDetailModal" tabindex="-1" aria-labelledby="orderDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="orderDetailModalLabel">
                    <i class="fas fa-receipt me-2"></i>Order Details
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-0" id="orderDetailContent">
                <!-- Order detail will be loaded here via AJAX -->
                <div class="text-center p-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-3 text-muted">Loading order details...</p>
                </div>
            </div>
            <div class="modal-footer bg-light">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="fas fa-times me-2"></i>Close
                </button>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/ScriptAdminDashboard.js"></script>

<!-- Script mở modal -->
<script>
    function openEditModal(orderId, currentStatus) {
        // Set order ID in hidden input
        document.getElementById('modalOrderId').value = orderId;
        
        // Set current status as selected
        const statusSelect = document.getElementById('modalStatusSelect');
        statusSelect.value = currentStatus.toLowerCase();
    }

    function printOrderDetails() {
        const modalContent = document.getElementById('orderDetailContent');
        if (modalContent) {
            const printWindow = window.open('', '_blank');
            printWindow.document.write(`
                <html>
                    <head>
                        <title>Order Details</title>
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                        <style>
                            @media print {
                                .no-print { display: none !important; }
                                body { margin: 0; }
                                .order-details-container { box-shadow: none !important; }
                            }
                            body { font-family: Arial, sans-serif; }
                        </style>
                    </head>
                    <body>
                        ${modalContent.innerHTML}
                        <script>
                            // Remove action buttons for print
                            const actionButtons = document.querySelectorAll('.btn, .no-print');
                            actionButtons.forEach(btn => btn.style.display = 'none');
                            
                            // Auto print when loaded
                            window.onload = function() {
                                window.print();
                                window.onafterprint = function() {
                                    window.close();
                                }
                            }
                        <\/script>
                    </body>
                </html>
            `);
            printWindow.document.close();
        }
    }
</script>