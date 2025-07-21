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
        <option value="completed" ${filterStatus == 'completed' ? 'selected' : ''}>Completed</option>
        <option value="cancelled" ${filterStatus == 'cancelled' ? 'selected' : ''}>Cancelled</option>
    </select>

    <button type="submit" class="btn btn-primary">Apply Filter</button>
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
                            <form method="get" action="AdminController">
                                <input type="hidden" name="action" value="viewOrderDetails"/>
                                <input type="hidden" name="orderId" value="${row.orderId}"/>
                                <button type="submit" class="btn btn-sm btn-info">
                                    <i class="fas fa-eye"></i> View
                                </button>
                            </form>
                            <button type="button" class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editModal" onclick="openEditModal('${row.orderId}', '${row.orderStatus}')">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <form method="post" action="AdminController" onsubmit="return confirm('Are you sure you want to delete this order?');">
                                <input type="hidden" name="action" value="deleteOrder"/>
                                <input type="hidden" name="orderId" value="${row.orderId}"/>
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
                    <input type="hidden" name="currentFilterStatus" value="${filterStatus}"/>

                    <label for="modalStatusSelect" class="form-label">Order Status:</label>
                    <select name="status" id="modalStatusSelect" class="form-select">
                        <option value="pending">Pending</option>
                        <option value="shipped">Shipped</option>
                        <option value="completed">Completed</option>
                        <option value="cancelled">Cancelled</option>
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

<!-- Script mở modal -->
<script>
    function openEditModal(orderId, currentStatus) {
        document.getElementById("modalOrderId").value = orderId;
        document.getElementById("modalStatusSelect").value = currentStatus;
    }
</script>