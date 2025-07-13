<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Order Management</h2>

<!-- Filter by Status -->
<form method="get" action="AdminController" class="mb-3 d-flex align-items-center gap-2">
    <input type="hidden" name="action" value="filterOrders"/>
    <input type="hidden" name="tab" value="orders"/>

    <label for="statusFilter" class="form-label mb-0">Status:</label>
    <select name="status" id="statusFilter" class="form-select w-auto">
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
                <th>Customer ID</th>
                <th>Order Date</th>
                <th>Total Amount</th>
                <th>Discount</th>
                <th>Final Amount</th>
                <th>Voucher</th>
                <th>Status</th>
                <th>Payment</th>
                <th>Shipping Address</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${orders}" var="o">
            <tr>
                <td>${o.orderId}</td>
                <td>${o.cusId}</td>
                <td><fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>$${o.totalAmount}</td>
            <td>$${o.discountAmount}</td>
            <td><strong>$${o.finalAmount}</strong></td>
            <td>
            <c:choose>
                <c:when test="${not empty o.voucherId}">
                    ${o.voucherId}
                </c:when>
                <c:otherwise>
                    <span class="text-muted">N/A</span>
                </c:otherwise>
            </c:choose>
            </td>
            <td>${o.orderStatus}</td>
            <td>${o.paymentMethod}</td>
            <td>${o.shippingAddress}</td>
            <td class="d-flex flex-column gap-1">
                <!-- View (chuyển hướng sang orderDetails.jsp) -->
                <a href="AdminController?action=goToOrderDetailPage&orderId=${o.orderId}" class="btn btn-sm btn-info">
                    <i class="fas fa-eye"></i> View
                </a>

                <!-- Edit -->
                <button class="btn btn-sm btn-warning"
                        data-bs-toggle="modal"
                        data-bs-target="#editModal"
                        onclick="openEditModal('${o.orderId}', '${o.orderStatus}')">
                    <i class="fas fa-edit"></i> Edit
                </button>

                <!-- Delete -->
                <form method="post" action="AdminController">
                    <input type="hidden" name="action" value="deleteOrder"/>
                    <input type="hidden" name="orderId" value="${o.orderId}"/>
                    <button class="btn btn-sm btn-danger w-100"
                            onclick="return confirm('Are you sure you want to delete this order?');">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </form>
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