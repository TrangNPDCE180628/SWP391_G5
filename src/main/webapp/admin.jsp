<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
        <!-- Add Chart.js for charts -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
        <style>
            .action-buttons .btn {
                margin-right: 5px;
            }
            .action-buttons .btn-info {
                background-color: #17a2b8;
                border-color: #17a2b8;
            }
            .action-buttons .btn-warning {
                background-color: #ffc107;
                border-color: #ffc107;
            }
            .action-buttons .btn-danger {
                background-color: #dc3545;
                border-color: #dc3545;
            }
            .status-badge-pending { background-color: #ffc107; color: black; }
            .status-badge-done { background-color: #28a745; color: white; }
            .status-badge-cancel { background-color: #dc3545; color: white; }
            /* Revenue Dashboard Styles */
            .kpi-card {
                background: #fff;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                text-align: center;
                margin-bottom: 20px;
            }
            .kpi-card h5 {
                font-size: 1.1rem;
                color: #6c757d;
                margin-bottom: 10px;
            }
            .kpi-card .value {
                font-size: 1.8rem;
                font-weight: bold;
                color: #343a40;
            }
            .chart-container {
                background: #fff;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            .recent-transactions table {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
                                <a href="#profile" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-user me-2"></i>Profile
                                </a>
                            </li>
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
                                    <i class="fas fa-users me-2"></i>Customer
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#staff" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-users me-2"></i>Staff Manage
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#orders" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-shopping-cart me-2"></i>Orders
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#vouchers" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>Voucher
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#discounts" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-percent me-2"></i>Discounts
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#productSpecs" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-microchip me-2"></i>Product Specs
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#revenue" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-chart-line me-2"></i>Revenue
                                </a>
                            </li>
                        </ul>
                        <hr>
                        <div class="dropdown">
                            <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown">
                                <i class="fas fa-user-circle me-2"></i>
                                <strong>${sessionScope.LOGIN_USER.fullName}</strong>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                                <li>
                                    <a class="dropdown-item" href="login.jsp">Logout</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
                    <div class="tab-content">
                        <!-- Profile Tab -->
                        <div class="tab-pane fade" id="profile">
                            <h2>My Profile</h2>
                            <button class="btn btn-primary mb-3"
                                    onclick="editProfile('${LOGIN_USER.role}', '${LOGIN_USER.id}')">
                                <i class="fas fa-edit me-1"></i> Edit Profile
                            </button>
                            <div id="profileTabContent">
                                <table class="table table-striped">
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${LOGIN_USER.role == 'Admin'}">
                                                <tr
                                                    data-user-role="Admin"
                                                    data-user-id="${LOGIN_USER.id}"
                                                    data-user-fullname="${profile.adminFullName}"
                                                    data-user-email="${profile.adminGmail}"
                                                    data-user-image="${profile.adminImage}">
                                                    <td colspan="2" class="text-center">
                                                        <img src="${pageContext.request.contextPath}/images/${profile.adminImage}"
                                                             alt="Avatar" class="rounded-circle"
                                                             style="width: 120px; height: 120px; object-fit: cover;">
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:when test="${LOGIN_USER.role == 'Staff'}">
                                                <tr
                                                    data-user-role="Staff"
                                                    data-user-id="${LOGIN_USER.id}"
                                                    data-user-fullname="${profile.staffFullName}"
                                                    data-user-email="${profile.staffGmail}"
                                                    data-user-image="${profile.staffImage}"
                                                    data-user-gender="${profile.staffGender}"
                                                    data-user-phone="${profile.staffPhone}"
                                                    data-user-position="${profile.staffPosition}">
                                                    <td colspan="2" class="text-center">
                                                        <img src="${pageContext.request.contextPath}/images/${profile.staffImage}"
                                                             alt="Avatar" class="rounded-circle"
                                                             style="width: 120px; height: 120px; object-fit: cover;">
                                                    </td>
                                                </tr>
                                            </c:when>
                                        </c:choose>
                                        <tr>
                                            <th>Full Name</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${LOGIN_USER.role == 'Admin'}">
                                                        ${profile.adminFullName}
                                                    </c:when>
                                                    <c:when test="${LOGIN_USER.role == 'Staff'}">
                                                        ${profile.staffFullName}
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Email</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${LOGIN_USER.role == 'Admin'}">
                                                        ${profile.adminGmail}
                                                    </c:when>
                                                    <c:when test="${LOGIN_USER.role == 'Staff'}">
                                                        ${profile.staffGmail}
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <c:if test="${LOGIN_USER.role == 'Staff'}">
                                            <tr>
                                                <th>Gender</th>
                                                <td>${profile.staffGender}</td>
                                            </tr>
                                            <tr>
                                                <th>Phone</th>
                                                <td>${profile.staffPhone}</td>
                                            </tr>
                                            <tr>
                                                <th>Position</th>
                                                <td>${profile.staffPosition}</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>

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
                                        <c:forEach items="${productTypes}" var="cate">
                                            <tr data-type-id="${cate.cateId}">
                                                <td>${cate.cateId}</td>
                                                <td data-type-name>${cate.cateName}</td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-warning" onclick="editProductType(`${cate.cateId}`, `${cate.cateName}`)">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteProductType('${cate.cateId}')">
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
                                            <th>Price</th>
                                            <th>Category</th>
                                            <th>Stock</th>
                                            <th>Image</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${products}" var="product">
                                            <tr 
                                                data-pro-id="${product.proId}"
                                                data-pro-name="${product.proName}"
                                                data-pro-description="${product.proDescription}"
                                                data-pro-price="${product.proPrice}"
                                                data-pro-stock-quantity="${product.proStockQuantity}"
                                                data-pro-warranty-months="${product.proWarrantyMonths}"
                                                data-pro-model="${product.proModel}"
                                                data-pro-color="${product.proColor}"
                                                data-pro-weight="${product.proWeight}"
                                                data-pro-dimensions="${product.proDimensions}"
                                                data-pro-origin="${product.proOrigin}"
                                                data-pro-material="${product.proMaterial}"
                                                data-pro-connectivity="${product.proConnectivity}"
                                                data-pro-image-main="${product.proImageMain}"
                                                data-cate-id="${product.cateId}"
                                                data-brand-id="${product.brandId}"
                                                data-status="${product.status}">
                                                <td>${product.proId}</td>
                                                <td>${product.proName}</td>
                                                <td>$${product.proPrice}</td>
                                                <td>
                                                    <c:forEach items="${productTypes}" var="cate">
                                                        <c:if test="${cate.cateId == product.cateId}">
                                                            ${cate.cateName}
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td>${product.proStockQuantity}</td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/products/${product.proImageMain}" 
                                                         alt="Image" width="120" height="150">
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.status == 1}">Available</c:when>
                                                        <c:when test="${product.status == 2}">Out of Stock</c:when>
                                                        <c:otherwise>Unavailable</c:otherwise>
                                                    </c:choose>
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
                            <h2>Customer Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">
                                <i class="fas fa-plus"></i> Add New Customer
                            </button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Username</th>
                                            <th>Full Name</th>
                                            <th>Image</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${users}" var="cus">
                                            <tr 
                                                data-cusid="${cus.cusId}"
                                                data-username="${cus.username}"
                                                data-password="${cus.cusPassword}"
                                                data-fullname="${cus.cusFullName}"
                                                data-gender="${cus.cusGender}"
                                                data-gmail="${cus.cusGmail}"
                                                data-phone="${cus.cusPhone}"
                                                data-image="${cus.cusImage}">
                                                <td>${cus.cusId}</td>
                                                <td>${cus.username}</td>
                                                <td>${cus.cusFullName}</td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/customers/${cus.cusImage}"
                                                         alt="Customer Image" width="80" height="100">
                                                </td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-info" onclick="viewDetail('${cus.cusId}')">
                                                        <i class="fas fa-eye"></i> View
                                                    </button>
                                                    <button class="btn btn-sm btn-warning" onclick="editUser('${cus.cusId}')">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteUser('${cus.cusId}')">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Staff Tab -->
                        <div class="tab-pane fade" id="staff">
                            <h2>Staff Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addStaffModal">
                                <i class="fas fa-plus"></i> Add New Staff
                            </button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Username</th>
                                            <th>Full Name</th>
                                            <th>Image</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${staffs}" var="staff">
                                            <tr
                                                data-staff-id="${staff.staffId}"
                                                data-staff-name="${staff.staffName}"
                                                data-fullname="${staff.staffFullName}"
                                                data-password="${staff.staffPassword}"
                                                data-gender="${staff.staffGender}"
                                                data-gmail="${staff.staffGmail}"
                                                data-phone="${staff.staffPhone}"
                                                data-position="${staff.staffPosition}"
                                                data-image="${staff.staffImage}">
                                                <td>${staff.staffId}</td>
                                                <td>${staff.staffName}</td>
                                                <td>${staff.staffFullName}</td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/staff/${staff.staffImage}" 
                                                         alt="Staff Image" width="80" height="100">
                                                </td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-info" onclick="viewStaffDetail('${staff.staffId}')">
                                                        <i class="fas fa-eye"></i> View
                                                    </button>
                                                    <button class="btn btn-sm btn-warning" onclick="editStaff('${staff.staffId}')">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteStaff('${staff.staffId}')">
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
                            <div class="mb-3 d-flex align-items-center">
                                <form action="AdminController" method="GET" class="d-flex">
                                    <input type="hidden" name="action" value="filterOrdersByStatus">
                                    <select name="status" class="form-select me-2" style="width: 200px;" onchange="this.form.submit()">
                                        <option value="All" ${selectedStatus == 'All' ? 'selected' : ''}>All</option>
                                        <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="Done" ${selectedStatus == 'Done' ? 'selected' : ''}>Done</option>
                                        <option value="Cancel" ${selectedStatus == 'Cancel' ? 'selected' : ''}>Cancel</option>
                                    </select>
                                    <button type="submit" class="btn btn-primary">Filter</button>
                                </form>
                            </div>
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
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${orders}" var="order">
                                            <tr>
                                                <td>${order.id}</td>
                                                <td>${order.cusId}</td>
                                                <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>$${order.totalAmount}</td>
                                                <td>$${order.discountAmount}</td>
                                                <td>$${order.finalAmount}</td>
                                                <td>
                                                    <span class="badge status-badge-${order.status}">
                                                        ${order.status}
                                                    </span>
                                                </td>
                                                <td class="action-buttons">
                                                    <a href="AdminController?action=viewOrderDetails&id=${order.id}" class="btn btn-sm btn-info">
                                                        <i class="fas fa-eye"></i> View
                                                    </a>
                                                    <div class="dropdown d-inline-block">
                                                        <button class="btn btn-sm btn-warning dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            <i class="fas fa-edit"></i> Update Status
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="dropdown-item" href="AdminController?action=updateOrderStatus&id=${order.id}&status=Pending¤tFilter=${selectedStatus}">Pending</a></li>
                                                            <li><a class="dropdown-item" href="AdminController?action=updateOrderStatus&id=${order.id}&status=Done¤tFilter=${selectedStatus}">Done</a></li>
                                                            <li><a class="dropdown-item" href="AdminController?action=updateOrderStatus&id=${order.id}&status=Cancel¤tFilter=${selectedStatus}">Cancel</a></li>
                                                        </ul>
                                                    </div>
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
                                            <tr 
                                                data-voucher-id="${voucher.voucherId}"
                                                data-voucher-code="${voucher.codeName}"
                                                data-voucher-description="${voucher.voucherDescription}"
                                                data-voucher-discount-type="${voucher.discountType}"
                                                data-voucher-discount-value="${voucher.discountValue}"
                                                data-voucher-min-order="${voucher.minOrderAmount}"
                                                data-voucher-start-date="<fmt:formatDate value='${voucher.startDate}' pattern='yyyy-MM-dd' />"
                                                data-voucher-end-date="<fmt:formatDate value='${voucher.endDate}' pattern='yyyy-MM-dd' />"
                                                data-voucher-status="${voucher.voucherActive}">
                                                <td>${voucher.voucherId}</td>
                                                <td>${voucher.codeName}</td>
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
                                                <td><fmt:formatDate value="${voucher.startDate}" pattern="yyyy-MM-dd" /></td>
                                                <td><fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd" /></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${voucher.voucherActive}">
                                                            Active
                                                        </c:when>
                                                        <c:otherwise>
                                                            Inactive
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
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

                        <!-- Discounts Tab -->
                        <div class="tab-pane fade" id="discounts">
                            <h2>Discounts Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addDiscountModal">
                                <i class="fas fa-plus"></i> Add New Discount
                            </button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Product ID</th>
                                            <th>Type</th>
                                            <th>Value</th>
                                            <th>Start Date</th>
                                            <th>End Date</th>
                                            <th>Active</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${discounts}" var="discount">
                                            <tr>
                                                <td>${discount.discountId}</td>
                                                <td>${discount.proId}</td>
                                                <td>${discount.discountType}</td>
                                                <td>${discount.discountValue}</td>
                                                <td><fmt:formatDate value="${discount.startDate}" pattern="yyyy-MM-dd" /></td>
                                                <td><fmt:formatDate value="${discount.endDate}" pattern="yyyy-MM-dd" /></td>
                                                <td>${discount.active ? 'Yes' : 'No'}</td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-warning" onclick="editDiscount('${discount.discountId}')">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteDiscount('${discount.discountId}')">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Product Specs Tab -->
                        <div class="tab-pane fade" id="productSpecs">
                            <h2>Product Specifications Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addProductSpecModal">
                                <i class="fas fa-plus"></i> Add New Specification
                            </button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Product ID</th>
                                            <th>CPU</th>
                                            <th>RAM</th>
                                            <th>Storage</th>
                                            <th>Screen</th>
                                            <th>OS</th>
                                            <th>Battery</th>
                                            <th>Camera</th>
                                            <th>Graphic</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${productSpecs}" var="spec">
                                            <tr>
                                                <td>${spec.id}</td>
                                                <td>${spec.proId}</td>
                                                <td>${spec.cpu.cpuName}</td>
                                                <td>${spec.ram}</td>
                                                <td>${spec.storage}</td>
                                                <td>${spec.screen}</td>
                                                <td>${spec.os}</td>
                                                <td>${spec.battery}</td>
                                                <td>${spec.camera}</td>
                                                <td>${spec.graphic}</td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-warning" onclick="editProductSpec('${spec.specId}')">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteProductSpec('${spec.specId}')">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Revenue Tab -->
                        <div class="tab-pane fade" id="revenue">
                            <h2>Revenue Dashboard</h2>
                            <!-- Header / Time Filters -->
                            <div class="mb-3 d-flex align-items-center">
                                <form action="AdminController" method="GET" class="d-flex">
                                    <input type="hidden" name="action" value="filterRevenue">
                                    <select name="timeRange" class="form-select me-2" style="width: 150px;">
                                        <option value="day">Day</option>
                                        <option value="week">Week</option>
                                        <option value="month" selected>Month</option>
                                        <option value="quarter">Quarter</option>
                                        <option value="year">Year</option>
                                        <option value="custom">Custom</option>
                                    </select>
                                    <input type="date" name="startDate" class="form-control me-2" style="width: 150px;">
                                    <input type="date" name="endDate" class="form-control me-2" style="width: 150px;">
                                    <select name="category" class="form-select me-2" style="width: 200px;">
                                        <option value="all">All Categories</option>
                                        <c:forEach items="${productTypes}" var="cate">
                                            <option value="${cate.cateId}">${cate.cateName}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-primary">Filter</button>
                                </form>
                            </div>

                            <!-- Key Metrics / KPIs -->
                            <div class="row">
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>Total Revenue</h5>
                                        <div class="value">$${revenueMetrics.totalRevenue}</div>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>Total Orders</h5>
                                        <div class="value">${revenueMetrics.totalOrders}</div>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>Products Sold</h5>
                                        <div class="value">${revenueMetrics.totalProductsSold}</div>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>New Customers</h5>
                                        <div class="value">${revenueMetrics.newCustomers}</div>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>Return Rate</h5>
                                        <div class="value">${revenueMetrics.returnRate}%</div>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-2">
                                    <div class="kpi-card">
                                        <h5>Net Profit</h5>
                                        <div class="value">$${revenueMetrics.netProfit}</div>
                                    </div>
                                </div>
                            </div>

                            <!-- Revenue Over Time Chart -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="chart-container">
                                        <h5>Revenue Over Time</h5>
                                        <canvas id="revenueOverTimeChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Revenue by Category/Product -->
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5>Revenue by Category</h5>
                                        <canvas id="revenueByCategoryChart"></canvas>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5>Top 5 Products</h5>
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Product</th>
                                                    <th>Revenue</th>
                                                    <th>Units Sold</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${topProducts}" var="product">
                                                    <tr>
                                                        <td>${product.proName}</td>
                                                        <td>$${product.revenue}</td>
                                                        <td>${product.unitsSold}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <!-- Revenue by Customer -->
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5>Customer Type</h5>
                                        <canvas id="customerTypeChart"></canvas>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="chart-container">
                                        <h5>Top 5 Customers</h5>
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Customer</th>
                                                    <th>Total Spent</th>
                                                    <th>Orders</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${topCustomers}" var="customer">
                                                    <tr>
                                                        <td>${customer.cusFullName}</td>
                                                        <td>$${customer.totalSpent}</td>
                                                        <td>${customer.orderCount}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <!-- Order Status -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="chart-container">
                                        <h5>Order Status</h5>
                                        <canvas id="orderStatusChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Recent Transactions -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="recent-transactions">
                                        <h5>Recent Transactions</h5>
                                        <div class="table-responsive">
                                            <table class="table table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>Order ID</th>
                                                        <th>Customer</th>
                                                        <th>Date</th>
                                                        <th>Amount</th>
                                                        <th>Status</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${recentOrders}" var="order">
                                                        <tr>
                                                            <td>${order.id}</td>
                                                            <td>${order.cusId}</td>
                                                            <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                            <td>$${order.finalAmount}</td>
                                                            <td>
                                                                <span class="badge status-badge-${order.status}">
                                                                    ${order.status}
                                                                </span>
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
                </div>
            </div>
        </div>

        <!-- Modals -->
        <!-- Add Product Type Modal -->
        <div class="modal fade" id="addProductTypeModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Product Type</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addProductTypeForm">
                            <input type="hidden" name="action" value="addProductType">
                            <div class="mb-3">
                                <label for="typeName" class="form-label">Name</label>
                                <input type="text" class="form-control" id="typeName" name="typeName" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Product Type</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Product Modal -->
        <div class="modal fade" id="addProductModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Product</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" enctype="multipart/form-data" id="addProductForm">
                            <input type="hidden" name="action" value="addProduct">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="productId" class="form-label">Product ID</label>
                                    <input type="text" class="form-control" id="productId" name="productId" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="categoryId" class="form-label">Category</label>
                                    <select class="form-select" id="categoryId" name="categoryId" required>
                                        <c:forEach items="${productTypes}" var="cate">
                                            <option value="${cate.cateId}">${cate.cateName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="brandId" class="form-label">Brand</label>
                                    <input type="number" class="form-control" id="brandId" name="brandId" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productName" class="form-label">Name</label>
                                    <input type="text" class="form-control" id="productName" name="productName" required>
                                </div>
                                <div class="col-md-12 mb-3">
                                    <label for="productDescription" class="form-label">Description</label>
                                    <textarea class="form-control" id="productDescription" name="productDescription" rows="4"></textarea>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productPrice" class="form-label">Price</label>
                                    <input type="number" step="0.01" class="form-control" id="productPrice" name="productPrice" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productQuantity" class="form-label">Quantity</label>
                                    <input type="number" class="form-control" id="productQuantity" name="productQuantity" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="warrantyMonths" class="form-label">Warranty (Months)</label>
                                    <input type="number" class="form-control" id="warrantyMonths" name="warrantyMonths">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productModel" class="form-label">Model</label>
                                    <input type="text" class="form-control" id="productModel" name="productModel">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productColor" class="form-label">Color</label>
                                    <input type="text" class="form-control" id="productColor" name="productColor">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productWeight" class="form-label">Weight</label>
                                    <input type="number" step="0.01" class="form-control" id="productWeight" name="productWeight">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productDimensions" class="form-label">Dimensions</label>
                                    <input type="text" class="form-control" id="productDimensions" name="productDimensions">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productOrigin" class="form-label">Origin</label>
                                    <input type="text" class="form-control" id="productOrigin" name="productOrigin">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productMaterial" class="form-label">Material</label>
                                    <input type="text" class="form-control" id="productMaterial" name="productMaterial">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productConnectivity" class="form-label">Connectivity</label>
                                    <input type="text" class="form-control" id="productConnectivity" name="productConnectivity">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productStatus" class="form-label">Status</label>
                                    <select class="form-select" id="productStatus" name="productStatus">
                                        <option value="1">Available</option>
                                        <option value="2">Out of Stock</option>
                                        <option value="0">Unavailable</option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="productImage" class="form-label">Image</label>
                                    <input type="file" class="form-control" id="productImage" name="image">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Product</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add User Modal -->
        <div class="modal fade" id="addUserModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addUserForm">
                            <input type="hidden" name="action" value="addUser">
                            <input type="hidden" name="role" value="customer">
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
                                <label for="gender" class="form-label">Gender</label>
                                <select class="form-select" id="gender" name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="gmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="gmail" name="gmail">
                            </div>
                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone</label>
                                <input type="text" class="form-control" id="phone" name="phone">
                            </div>
                            <div class="mb-3">
                                <label for="image" class="form-label">Image</label>
                                <input type="text" class="form-control" id="image" name="image">
                            </div>
                            <button type="submit" class="btn btn-primary">Add Customer</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Staff Modal -->
        <div class="modal fade" id="addStaffModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Staff</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addStaffForm">
                            <input type="hidden" name="action" value="addUser">
                            <input type="hidden" name="role" value="staff">
                            <div class="mb-3">
                                <label for="staffUsername" class="form-label">Username</label>
                                <input type="text" class="form-control" id="staffUsername" name="username" required>
                            </div>
                            <div class="mb-3">
                                <label for="staffPassword" class="form-label">Password</label>
                                <input type="password" class="form-control" id="staffPassword" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="staffFullname" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="staffFullname" name="fullname" required>
                            </div>
                            <div class="mb-3">
                                <label for="staffGender" class="form-label">Gender</label>
                                <select class="form-select" id="staffGender" name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="staffGmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="staffGmail" name="gmail">
                            </div>
                            <div class="mb-3">
                                <label for="staffPhone" class="form-label">Phone</label>
                                <input type="text" class="form-control" id="staffPhone" name="phone">
                            </div>
                            <div class="mb-3">
                                <label for="staffImage" class="form-label">Image</label>
                                <input type="text" class="form-control" id="staffImage" name="image">
                            </div>
                            <button type="submit" class="btn btn-primary">Add Staff</button>
                        </form>
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
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addVoucherForm">
                            <input type="hidden" name="action" value="addVoucher">
                            <div class="mb-3">
                                <label for="codeName" class="form-label">Code</label>
                                <input type="text" class="form-control" id="codeName" name="codeName" required>
                            </div>
                            <div class="mb-3">
                                <label for="voucherDescription" class="form-label">Description</label>
                                <textarea class="form-control" id="voucherDescription" name="voucherDescription"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="discountType" class="form-label">Discount Type</label>
                                <select class="form-select" id="discountType" name="discountType">
                                    <option value="percentage">Percentage</option>
                                    <option value="fixed">Fixed Amount</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="discountValue" class="form-label">Discount Value</label>
                                <input type="number" step="0.01" class="form-control" id="discountValue" name="discountValue" required>
                            </div>
                            <div class="mb-3">
                                <label for="minOrderAmount" class="form-label">Minimum Order Amount</label>
                                <input type="number" step="0.01" class="form-control" id="minOrderAmount" name="minOrderAmount" required>
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
                                <label for="voucherActive" class="form-label">Active</label>
                                <select class="form-select" id="voucherActive" name="voucherActive">
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Voucher</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Discount Modal -->
        <div class="modal fade" id="addDiscountModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Discount</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addDiscountForm">
                            <input type="hidden" name="action" value="addDiscount">
                            <div class="mb-3">
                                <label for="proId" class="form-label">Product ID</label>
                                <input type="text" class="form-control" id="proId" name="proId" required>
                            </div>
                            <div class="mb-3">
                                <label for="discountType" class="form-label">Discount Type</label>
                                <select class="form-select" id="discountType" name="discountType">
                                    <option value="percentage">Percentage</option>
                                    <option value="fixed">Fixed Amount</option>
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
                                <label for="active" class="form-label">Active</label>
                                <select class="form-select" id="active" name="active">
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="adminId" class="form-label">Admin ID</label>
                                <input type="text" class="form-control" id="adminId" name="adminId" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Discount</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Product Specification Modal -->
        <div class="modal fade" id="addProductSpecModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Product Specification</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AdminController" method="post" id="addProductSpecForm">
                            <input type="hidden" name="action" value="addProductSpec">
                            <div class="mb-3">
                                <label for="productId" class="form-label">Product ID</label>
                                <input type="text" class="form-control" id="productId" name="productId" required>
                            </div>
                            <div class="mb-3">
                                <label for="cpu" class="form-label">CPU</label>
                                <input type="text" class="form-control" id="cpu" name="cpu">
                            </div>
                            <div class="mb-3">
                                <label for="ram" class="form-label">RAM</label>
                                <input type="text" class="form-control" id="ram" name="ram">
                            </div>
                            <div class="mb-3">
                                <label for="storage" class="form-label">Storage</label>
                                <input type="text" class="form-control" id="storage" name="storage">
                            </div>
                            <div class="mb-3">
                                <label for="screen" class="form-label">Screen</label>
                                <input type="text" class="form-control" id="screen" name="screen">
                            </div>
                            <div class="mb-3">
                                <label for="os" class="form-label">OS</label>
                                <input type="text" class="form-control" id="os" name="os">
                            </div>
                            <div class="mb-3">
                                <label for="battery" class="form-label">Battery</label>
                                <input type="text" class="form-control" id="battery" name="battery">
                            </div>
                            <div class="mb-3">
                                <label for="camera" class="form-label">Camera</label>
                                <input type="text" class="form-control" id="camera" name="camera">
                            </div>
                            <div class="mb-3">
                                <label for="graphic" class="form-label">Graphic</label>
                                <input type="text" class="form-control" id="graphic" name="graphic">
                            </div>
                            <button type="submit" class="btn btn-primary">Add Specification</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Activate the Orders tab if specified
            <c:if test="${activeTab == 'orders'}">
                document.querySelector('a[href="#orders"]').click();
            </c:if>
            // Activate the Revenue tab if specified
            <c:if test="${activeTab == 'revenue'}">
                document.querySelector('a[href="#revenue"]').click();
            </c:if>

            // Initialize Charts with Error Handling
            // Revenue Over Time Chart
            let revenueOverTimeLabels = [];
            let revenueOverTimeData = [];
            let revenueOverTimePreviousData = [];

            try {
                revenueOverTimeLabels = JSON.parse('<c:out value="${revenueOverTime.labels}" escapeXml="true"/>');
                revenueOverTimeData = JSON.parse('<c:out value="${revenueOverTime.data}" escapeXml="true"/>');
                revenueOverTimePreviousData = JSON.parse('<c:out value="${revenueOverTime.previousData}" escapeXml="true"/>');
            } catch (e) {
                console.error('Error parsing revenue over time data:', e);
                revenueOverTimeLabels = ['No Data'];
                revenueOverTimeData = [0];
                revenueOverTimePreviousData = [0];
            }

            const revenueOverTimeCtx = document.getElementById('revenueOverTimeChart').getContext('2d');
            new Chart(revenueOverTimeCtx, {
                type: 'line',
                data: {
                    labels: revenueOverTimeLabels,
                    datasets: [{
                        label: 'Revenue',
                        data: revenueOverTimeData,
                        borderColor: '#28a745',
                        fill: false
                    }, {
                        label: 'Previous Period',
                        data: revenueOverTimePreviousData,
                        borderColor: '#ffc107',
                        fill: false,
                        borderDash: [5, 5]
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: { display: true, text: 'Revenue ($)' }
                        }
                    }
                }
            });

            // Revenue by Category Chart
            let revenueByCategoryLabels = [];
            let revenueByCategoryData = [];

            try {
                revenueByCategoryLabels = JSON.parse('<c:out value="${revenueByCategory.labels}" escapeXml="true"/>');
                revenueByCategoryData = JSON.parse('<c:out value="${revenueByCategory.data}" escapeXml="true"/>');
            } catch (e) {
                console.error('Error parsing revenue by category data:', e);
                revenueByCategoryLabels = ['No Data'];
                revenueByCategoryData = [0];
            }

            const revenueByCategoryCtx = document.getElementById('revenueByCategoryChart').getContext('2d');
            new Chart(revenueByCategoryCtx, {
                type: 'pie',
                data: {
                    labels: revenueByCategoryLabels,
                    datasets: [{
                        data: revenueByCategoryData,
                        backgroundColor: ['#28a745', '#ffc107', '#dc3545', '#17a2b8', '#6c757d']
                    }]
                },
                options: {
                    responsive: true
                }
            });

            // Customer Type Chart
            let customerTypeData = [];

            try {
                customerTypeData = JSON.parse('<c:out value="${customerType.data}" escapeXml="true"/>');
            } catch (e) {
                console.error('Error parsing customer type data:', e);
                customerTypeData = [0, 0];
            }

            const customerTypeCtx = document.getElementById('customerTypeChart').getContext('2d');
            new Chart(customerTypeCtx, {
                type: 'doughnut',
                data: {
                    labels: ['New Customers', 'Returning Customers'],
                    datasets: [{
                        data: customerTypeData,
                        backgroundColor: ['#28a745', '#17a2b8']
                    }]
                },
                options: {
                    responsive: true
                }
            });

            // Order Status Chart
            let orderStatusData = [];

            try {
                orderStatusData = JSON.parse('<c:out value="${orderStatus.data}" escapeXml="true"/>');
            } catch (e) {
                console.error('Error parsing order status data:', e);
                orderStatusData = [0, 0, 0];
            }

            const orderStatusCtx = document.getElementById('orderStatusChart').getContext('2d');
            new Chart(orderStatusCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Pending', 'Done', 'Cancel'],
                    datasets: [{
                        data: orderStatusData,
                        backgroundColor: ['#ffc107', '#28a745', '#dc3545']
                    }]
                },
                options: {
                    responsive: true
                }
            });
        </script>
    </body>
</html>