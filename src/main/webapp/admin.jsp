<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background-color: #343a40;
            color: white;
        }
        .sidebar .nav-link {
            color: rgba(255,255,255,.75);
        }
        .sidebar .nav-link:hover {
            color: white;
        }
        .sidebar .nav-link.active {
            color: white;
            background-color: rgba(255,255,255,.1);
        }
        .main-content {
            padding: 20px;
        }
        .table-responsive {
            margin-top: 20px;
        }
        .action-buttons .btn {
            margin-right: 5px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0 sidebar">
                <div class="d-flex flex-column p-3">
                    <h4 class="mb-4">Admin Dashboard</h4>
                    <ul class="nav nav-pills flex-column mb-auto">
                        <li class="nav-item">
                            <a href="#productTypes" class="nav-link active" data-bs-toggle="tab">
                                <i class="fas fa-tags me-2"></i>Product Types
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="#products" class="nav-link" data-bs-toggle="tab">
                                <i class="fas fa-shoe-prints me-2"></i>Products
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="#users" class="nav-link" data-bs-toggle="tab">
                                <i class="fas fa-users me-2"></i>Users
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="#orders" class="nav-link" data-bs-toggle="tab">
                                <i class="fas fa-shopping-cart me-2"></i>Orders
                            </a>
                        </li>
                    </ul>
                    <hr>
                    <div class="dropdown">
                        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-2"></i>
                            <strong>${sessionScope.LOGIN_USER.fullname}</strong>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                        <li><a class="dropdown-item" href="login.jsp">Logout</a></li>

                        </ul>
                    </div>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 main-content">
                <div class="tab-content">
                    <!-- Product Types Tab -->
                    <div class="tab-pane fade show active" id="productTypes">
                        <h2>Product Types Management</h2>
                        <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addProductTypeModal">
                            <i class="fas fa-plus"></i> Add New Product Type
                        </button>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${productTypes}" var="type">
                                        <tr data-type-id="${type.id}">
                                            <td>${type.id}</td>
                                            <td data-type-name>${type.name}</td>
                                            <td class="action-buttons">
                                                <button class="btn btn-sm btn-warning" onclick="editProductType(`${type.id}`, `${type.name}`)">
                                                    <i class="fas fa-edit"></i> Edit
                                                </button>
                                                <button class="btn btn-sm btn-danger" onclick="deleteProductType('${type.id}')">
                                                    <i class="fas fa-trash"></i> Delete
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Products Tab -->
                    <div class="tab-pane fade" id="products">
                        <h2>Products Management</h2>
                        <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addProductModal">
                            <i class="fas fa-plus"></i> Add New Product
                        </button>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Type</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${products}" var="product">
                                        <tr data-product-id="${product.proId}" 
                                            data-product-name="${product.proName}"
                                            data-product-description="${product.proDescription}"
                                            data-product-price="${product.proPrice}"
                                            data-product-quantity="${product.proQuantity}"
                                            data-product-type-id="${product.proTypeId}">
                                            <td>${product.proId}</td>
                                            <td>${product.proName}</td>
                                            <td>${product.proDescription}</td>
                                            <td>$${product.proPrice}</td>
                                            <td>${product.proQuantity}</td>
                                            <td>
                                                <c:forEach items="${productTypes}" var="type">
                                                    <c:if test="${type.id == product.proTypeId}">
                                                        ${type.name}
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td class="action-buttons">
                                                <button class="btn btn-sm btn-warning" onclick="editProduct('${product.proId}')">
                                                    <i class="fas fa-edit"></i> Edit
                                                </button>
                                                <button class="btn btn-sm btn-danger" onclick="deleteProduct('${product.proId}')">
                                                    <i class="fas fa-trash"></i> Delete
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Users Tab -->
                    <div class="tab-pane fade" id="users">
                        <h2>Users Management</h2>
                        <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">
                            <i class="fas fa-plus"></i> Add New User
                        </button>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Full Name</th>
                                        <th>Role</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${users}" var="user">
                                        <tr data-user-id="${user.id}"
                                            data-username="${user.username}"
                                            data-fullname="${user.fullname}"
                                            data-role="${user.role}">
                                            <td>${user.id}</td>
                                            <td>${user.username}</td>
                                            <td>${user.fullname}</td>
                                            <td>${user.role}</td>
                                            <td class="action-buttons">
                                                <button class="btn btn-sm btn-warning" onclick="editUser('${user.id}')">
                                                    <i class="fas fa-edit"></i> Edit
                                                </button>
                                                <button class="btn btn-sm btn-danger" onclick="deleteUser('${user.id}')">
                                                    <i class="fas fa-trash"></i> Delete
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Orders Tab -->
                    <div class="tab-pane fade" id="orders">
                        <h2>Orders Management</h2>
                        <div class="mb-3">
                            <label for="orderStatusFilter" class="form-label">Filter by Status:</label>
                            <select class="form-select" id="orderStatusFilter" onchange="filterOrders()">
                                <option value="all">All Orders</option>
                                <option value="pending">Pending</option>
                                <option value="completed">Completed</option>
                                <option value="cancelled">Cancelled</option>
                            </select>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Order Date</th>
                                        <th>Total Price</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${orders}" var="order">
                                        <tr data-order-id="${order.id}" 
                                            data-user-id="${order.userId}"
                                            data-status="${order.status}">
                                            <td>${order.id}</td>
                                            <td>
                                                <c:forEach items="${users}" var="user">
                                                    <c:if test="${user.id == order.userId}">
                                                        ${user.fullname}
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>${order.orderDate}</td>
                                            <td>$${order.totalPrice}</td>
                                            <td>
                                                <span class="badge ${order.status == 'pending' ? 'bg-warning' : 
                                                                    order.status == 'completed' ? 'bg-success' : 'bg-danger'}">
                                                    ${order.status}
                                                </span>
                                            </td>
                                            <td class="action-buttons">
                                                <a class="btn btn-sm btn-info" href="${pageContext.request.contextPath}/AdminController?action=viewOrderDetails&id=${order.id}">
                                                    <i class="fas fa-eye"></i> View Details
                                                </a>
                                                <c:if test="${order.status == 'pending'}">
                                                    <button class="btn btn-sm btn-success" onclick="updateOrderStatus('${order.id}', 'completed')">
                                                        <i class="fas fa-check"></i> Complete
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="updateOrderStatus('${order.id}', 'cancelled')">
                                                        <i class="fas fa-times"></i> Cancel
                                                    </button>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Product Type Modal -->
    <div class="modal fade" id="addProductTypeModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Product Type</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="addProductType">
                        <div class="mb-3">
                            <label for="typeName" class="form-label">Type Name</label>
                            <input type="text" class="form-control" id="typeName" name="typeName" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Add Product Modal -->
    <div class="modal fade" id="addProductModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post" enctype="multipart/form-data">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="addProduct">
                        <div class="mb-3">
                            <label for="productName" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="productName" name="productName" required>
                        </div>
                        <div class="mb-3">
                            <label for="productDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="productDescription" name="productDescription" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="productPrice" class="form-label">Price</label>
                            <input type="number" step="0.01" class="form-control" id="productPrice" name="productPrice" required>
                        </div>
                        <div class="mb-3">
                            <label for="productQuantity" class="form-label">Quantity</label>
                            <input type="number" class="form-control" id="productQuantity" name="productQuantity" required>
                        </div>
                        <div class="mb-3">
                            <label for="productType" class="form-label">Product Type</label>
                            <select class="form-select" id="productType" name="productType" required>
                                <c:forEach items="${productTypes}" var="type">
                                    <option value="${type.id}">${type.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="productImage" class="form-label">Product Image</label>
                            <input type="file" class="form-control" id="productImage" name="productImage" accept="image/*">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade" id="addUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="addUser">
                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="fullname" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullname" name="fullname" required>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role</label>
                            <select class="form-select" id="role" name="role" required>
                                <option value="admin">Admin</option>
                                <option value="customer">Customer</option>
                                <option value="staff">Staff</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Product Type Modal -->
    <div class="modal fade" id="editProductTypeModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Product Type</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateProductType">
                        <input type="hidden" name="id" id="editTypeId">
                        <div class="mb-3">
                            <label for="editTypeName" class="form-label">Type Name</label>
                            <input type="text" class="form-control" id="editTypeName" name="typeName" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Product Modal -->
    <div class="modal fade" id="editProductModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post" enctype="multipart/form-data">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateProduct">
                        <input type="hidden" name="id" id="editProductId">
                        <div class="mb-3">
                            <label for="editProductName" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="editProductName" name="productName">
                        </div>
                        <div class="mb-3">
                            <label for="editProductDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="editProductDescription" name="productDescription"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="editProductPrice" class="form-label">Price</label>
                            <input type="number" step="0.01" class="form-control" id="editProductPrice" name="productPrice">
                        </div>
                        <div class="mb-3">
                            <label for="editProductQuantity" class="form-label">Quantity</label>
                            <input type="number" class="form-control" id="editProductQuantity" name="productQuantity">
                        </div>
                        <div class="mb-3">
                            <label for="editProductType" class="form-label">Product Type</label>
                            <select class="form-select" id="editProductType" name="productType">
                                <c:forEach items="${productTypes}" var="type">
                                    <option value="${type.id}">${type.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="editProductImage" class="form-label">Product Image</label>
                            <input type="file" class="form-control" id="editProductImage" name="productImage" accept="image/*">
                            <small class="text-muted">Leave empty to keep current image</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit User Modal -->
    <div class="modal fade" id="editUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateUser">
                        <input type="hidden" name="id" id="editUserId">

                        <div class="mb-3">
                            <label for="editFullname" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="editFullname" name="fullname">
                        </div>
                        <div class="mb-3">
                            <label for="editRole" class="form-label">Role</label>
                            <select class="form-select" id="editRole" name="role">
                                <option value="admin">Admin</option>
                                <option value="customer">Customer</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </div>
                </form>
            </div>
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
                            <p><strong>Customer:</strong> <span id="detailCustomer"></span></p>
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
        // Product functions
        function editProduct(productId) {
            const row = document.querySelector(`tr[data-product-id="${productId}"]`);
            if (row) {
                // Set values directly in function call
                const name = row.getAttribute('data-product-name');
                const description = row.getAttribute('data-product-description');
                const price = row.getAttribute('data-product-price');
                const quantity = row.getAttribute('data-product-quantity');
                const typeId = row.getAttribute('data-product-type-id');
                
                document.getElementById('editProductId').value = productId;
                document.getElementById('editProductName').value = name;
                document.getElementById('editProductDescription').value = description;
                document.getElementById('editProductPrice').value = price;
                document.getElementById('editProductQuantity').value = quantity;
                document.getElementById('editProductType').value = typeId;

                console.log('Product data:', {
                    id: productId,
                    name: name,
                    description: description,
                    price: price,
                    quantity: quantity,
                    typeId: typeId
                });
            }
            new bootstrap.Modal(document.getElementById('editProductModal')).show();
        }

        function deleteProduct(id) {
            if (confirm('Are you sure you want to delete this product?')) {
                window.location.href = '${pageContext.request.contextPath}/AdminController?action=deleteProduct&id=' + id;
            }
        }

        // User functions
        function editUser(userId) {
            const row = document.querySelector(`tr[data-user-id="${userId}"]`);
            if (row) {
                // Set values directly in function call
                const username = row.getAttribute('data-username');
                const fullname = row.getAttribute('data-fullname');
                const role = row.getAttribute('data-role');

                document.getElementById('editUserId').value = userId;
                document.getElementById('displayUsername').value = username;
                document.getElementById('editFullname').value = fullname;
                document.getElementById('editRole').value = role.toLowerCase();

                console.log('User data:', {
                    id: userId,
                    username: username,
                    fullname: fullname,
                    role: role
                });
            }
            new bootstrap.Modal(document.getElementById('editUserModal')).show();
        }

        function deleteUser(id) {
            if (confirm('Are you sure you want to delete this user?')) {
                window.location.href = '${pageContext.request.contextPath}/AdminController?action=deleteUser&id=' + id;
            }
        }

        // Product Type functions
        function editProductType(id, name) {
            document.getElementById('editTypeId').value = id;
            document.getElementById('editTypeName').value = name;
            new bootstrap.Modal(document.getElementById('editProductTypeModal')).show();
        }

        function deleteProductType(id) {
            if (confirm('Are you sure you want to delete this product type?')) {
                window.location.href = '${pageContext.request.contextPath}/AdminController?action=deleteProductType&id=' + id;
            }
        }

        // Order functions
        function filterOrders() {
            const status = document.getElementById('orderStatusFilter').value;
            const rows = document.querySelectorAll('tr[data-order-id]');
            
            rows.forEach(row => {
                if (status === 'all' || row.getAttribute('data-status') === status) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }

        function viewOrderDetails(orderId) {
            // Fetch order details via AJAX
            fetch(`${pageContext.request.contextPath}/AdminController?action=getOrderDetails&id=${orderId}`)
                .then(response => response.json())
                .then(data => {
                    // Populate order information
                    document.getElementById('detailOrderId').textContent = data.order.id;
                    document.getElementById('detailCustomer').textContent = data.customerName;
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

        function updateOrderStatus(orderId, newStatus) {
            console.log('updateOrderStatus called with:', orderId, newStatus);

            // Make sure orderId is not empty
            if (!orderId || orderId === 'null' || orderId === 'undefined') {
                alert('Error: Order ID is missing or invalid!');
                return;
            }

            // Convert to integer if it's a string
            const id = parseInt(orderId, 10);

            if (isNaN(id)) {
                alert('Error: Order ID must be a number!');
                return;
            }

            if (confirm(`Are you sure you want to ${newStatus} this order?`)) {
                const formData = new FormData();
                formData.append('action', 'updateOrderStatus');
                formData.append('id', id);
                formData.append('status', newStatus);

                fetch('${pageContext.request.contextPath}/AdminController', {
                    method: 'POST',
                    body: new URLSearchParams(formData)
                })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        return response.text().then(text => {
                            throw new Error(text);
                        });
                    }
                })
                .catch(error => {
                    alert('Failed to update order status: ' + error.message);
                    console.error('Error updating order status:', error);
                });
            }
        }
    </script>
</body>
</html>
