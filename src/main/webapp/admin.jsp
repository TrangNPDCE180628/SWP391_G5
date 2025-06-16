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
        <link href="css/admindashboard.css" rel="stylesheet" />
        <script src="${pageContext.request.contextPath}/js/ScriptAdminDashboard.js"></script>

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
                                <a href="#staff" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-users me-2"></i>Staff Manage
                                </a>
                            </li>
                        </ul>
                        <li class="nav-item">
                            <a href="#voucher" class="nav-link" data-bs-toggle="tab">
                                <i class="fa-solid fa-ticket me-2"></i>Voucher
                            </a>
                        </li>
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
                                            <th>Image</th>
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
                                                data-product-type-id="${product.proTypeId}"
                                                data-product-image="${product.proImage}">

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
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/products/${product.proImage}" alt="Image" width="120" height="150">
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
                                                data-role="${user.role}"
                                                data-password="${user.password}">
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
                                                <td style="display: none;">${user.password}</td> <!-- Ẩn password khỏi giao diện -->
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Staff Tab -->
                        <div class="tab-pane fade" id="staff">
                            <h2>Staff Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">
                                <i class="fas fa-plus"></i> Add New Staff
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
                                            <c:if test="${user.role eq 'staff'}">
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
                                            </c:if>
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

                            <!-- Vouchers Tab -->
                            <div class="tab-pane fade" id="vouchers">
                                <h2>Vouchers Management</h2>
                                <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addVoucherModal">
                                    <i class="fas fa-plus"></i> Add New Voucher
                                </button>
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Code</th>
                                                <th>Type</th>
                                                <th>Value</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${vouchers}" var="voucher">
                                                <tr data-voucher-id="${voucher.voucherId}"
                                                    data-voucher-code="${voucher.voucherCode}"
                                                    data-voucher-discount-type="${voucher.discountType}"
                                                    data-voucher-discount-value="${voucher.discountValue}"
                                                    data-voucher-start-date="${voucher.startDate}"
                                                    data-voucher-end-date="${voucher.endDate}"
                                                    data-voucher-status="${voucher.status}">

                                                    <td>${voucher.voucherId}</td>
                                                    <td>${voucher.voucherCode}</td>
                                                    <td>${voucher.discountType}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${voucher.discountType == 'percentage'}">
                                                                ${voucher.discountValue}%
                                                            </c:when>
                                                            <c:otherwise>
                                                                $${voucher.discountValue}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${voucher.startDate}</td>
                                                    <td>${voucher.endDate}</td>
                                                    <td>${voucher.status}</td>
                                                    <td class="action-buttons">
                                                        <button class="btn btn-sm btn-warning" onclick="editVoucher('${voucher.voucherId}')">
                                                            <i class="fas fa-edit"></i> Edit
                                                        </button>
                                                        <button class="btn btn-sm btn-danger" onclick="deleteVoucher('${voucher.voucherId}')">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </button>
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
                                <input type="hidden" name="currentImage" id="currentImagePath">

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
                                    <script>
                                        const contextPath = '<%= request.getContextPath()%>';
                                    </script>

                                    <label for="editProductImage" class="form-label">Product Image</label>
                                    <input type="file" class="form-control" id="editProductImage" name="productImage" accept="image/*">
                                    <small class="text-muted">Leave empty to keep current image</small>

                                    <!-- Hiển thị ảnh hiện tại -->
                                    <div class="mt-2 text-center">
                                        <img id="currentProductImage" src="" alt="Current Image" style="max-width: 200px; max-height: 200px;">
                                    </div>
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
                                    <label for="displayUsername" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="displayUsername" name="username" readonly>
                                </div>
                                <div class="mb-3">
                                    <label for="editFullname" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="editFullname" name="fullname">
                                </div>
                                <div class="mb-3">
                                    <label for="editPassword" class="form-label">Password</label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="editPassword" name="password">
                                        <button class="btn btn-outline-secondary" type="button" onclick="togglePasswordVisibility()">
                                            <i id="passwordToggleIcon" class="fas fa-eye-slash"></i>
                                        </button>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="editRole" class="form-label">Role</label>
                                    <select class="form-select" id="editRole" name="role">
                                        <option value="admin">Admin</option>
                                        <option value="customer">Customer</option>
                                        <option value="staff">Staff</option>
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
            
            <!-- Add Voucher Modal -->
            <div class="modal fade" id="addVoucherModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Add New Voucher</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form action="AdminController" method="post">
                            <div class="modal-body">
                                <input type="hidden" name="action" value="addVoucher">

                                <div class="mb-3">
                                    <label for="voucherCode" class="form-label">Voucher Code</label>
                                    <input type="text" class="form-control" id="voucherCode" name="voucherCode" required>
                                </div>

                                <div class="mb-3">
                                    <label for="discountType" class="form-label">Discount Type</label>
                                    <select class="form-select" id="discountType" name="discountType" required>
                                        <option value="percentage">Percentage (%)</option>
                                        <option value="fixed">Fixed Amount ($)</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="discountValue" class="form-label">Discount Value</label>
                                    <input type="number" step="0.01" class="form-control" id="discountValue" name="discountValue" required>
                                </div>

                                <div class="mb-3">
                                    <label for="startDate" class="form-label">Start Date</label>
                                    <input type="date" class="form-control" id="startDate" name="startDate" required>
                                </div>

                                <div class="mb-3">
                                    <label for="endDate" class="form-label">End Date</label>
                                    <input type="date" class="form-control" id="endDate" name="endDate" required>
                                </div>

                                <div class="mb-3">
                                    <label for="status" class="form-label">Status</label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="active">Active</option>
                                        <option value="inactive">Inactive</option>
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

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

        </body>
    </html>
