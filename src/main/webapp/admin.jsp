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
                                                data-status="${product.status}"
                                                >
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
                                            <th>Discount Type</th>
                                            <th>Discount Value</th>
                                            <th>Start Date</th>
                                            <th>End Date</th>
                                            <th>Active</th>
                                            <th>Admin ID</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${discounts}" var="discount">
                                            <tr data-discount-id="${discount.discountId}"
                                                data-pro-id="${discount.proId}"
                                                data-discount-type="${discount.discountType}"
                                                data-discount-value="${discount.discountValue}"
                                                data-start-date="<fmt:formatDate value='${discount.startDate}' pattern='yyyy-MM-dd' type='date'/>"
                                                data-end-date="<fmt:formatDate value='${discount.endDate}' pattern='yyyy-MM-dd' type='date'/>"
                                                data-active="${discount.active}"
                                                data-admin-id="${discount.adminId}">
                                                <td>${discount.discountId}</td>
                                                <td>${discount.proId}</td>
                                                <td>${discount.discountType}</td>
                                                <td>${discount.discountValue}</td>
                                                <td><fmt:formatDate value="${discount.startDate}" pattern="yyyy-MM-dd" type="date"/></td>
                                                <td><fmt:formatDate value="${discount.endDate}" pattern="yyyy-MM-dd" type="date"/></td>
                                                <td>${discount.active ? 'Yes' : 'No'}</td>
                                                <td>${discount.adminId}</td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-info" onclick="viewDiscount('${discount.discountId}')">
                                                        <i class="fas fa-eye"></i> View
                                                    </button>
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
                        <!-- Product Specifications Tab -->
                        <div class="tab-pane fade" id="productSpecs">
                            <h2>Product Specification Management</h2>
                            <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addSpecModal">
                                <i class="fas fa-plus"></i> Add New Specification
                            </button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Spec ID</th>
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
                                            <tr 
                                                data-spec-id="${spec.specId}"
                                                data-product-id="${spec.productId}"
                                                data-cpu="${spec.cpu}"
                                                data-ram="${spec.ram}"
                                                data-storage="${spec.storage}"
                                                data-screen="${spec.screen}"
                                                data-os="${spec.os}"
                                                data-battery="${spec.battery}"
                                                data-camera="${spec.camera}"
                                                data-graphic="${spec.graphic}">
                                                <td>${spec.specId}</td>
                                                <td>${spec.productId}</td>
                                                <td>${spec.cpu}</td>
                                                <td>${spec.ram}</td>
                                                <td>${spec.storage}</td>
                                                <td>${spec.screen}</td>
                                                <td>${spec.os}</td>
                                                <td>${spec.battery}</td>
                                                <td>${spec.camera}</td>
                                                <td>${spec.graphic}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-warning" onclick="editSpec(this)">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <form action="AdminController" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="deleteProductSpec">
                                                        <input type="hidden" name="specId" value="${spec.specId}">
                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </button>
                                                    </form>
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
        <!-- Edit Profile Modal -->
        <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <form action="AdminController" method="post" enctype="multipart/form-data" class="modal-content">
                    <!-- BẮT BUỘC: Cho servlet biết action đang gọi -->
                    <input type="hidden" name="action" value="editProfile">

                    <!-- Hidden fields -->
                    <input type="hidden" name="userId" id="editUserId">
                    <input type="hidden" name="userRole" id="editUserRole">
                    <input type="hidden" name="currentImage" id="currentProfileImagePath">

                    <div class="modal-body">
                        <div class="row">
                            <!-- Avatar -->
                            <div class="col-md-4 text-center">
                                <img id="previewProfileImage" src="" alt="Preview" class="rounded-circle mb-3" style="width: 150px; height: 150px; object-fit: cover;">
                                <input type="file" class="form-control" name="image" id="editProfileImage" accept="image/*">
                            </div>

                            <!-- Thông tin chung -->
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="editProfileFullName" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" name="fullName" id="editProfileFullName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="editProfileEmail" class="form-label">Email</label>
                                    <input type="email" class="form-control" name="email" id="editProfileEmail" required>
                                </div>

                                <!-- Staff-only fields -->
                                <div id="staffFields" style="display: none;">
                                    <div class="mb-3">
                                        <label for="editProfileGender" class="form-label">Gender</label>
                                        <select class="form-select" name="gender" id="editProfileGender">
                                            <option value="Male">Male</option>
                                            <option value="Female">Female</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                    <div class="mb-3">
                                        <label for="editProfilePhone" class="form-label">Phone</label>
                                        <input type="text" class="form-control" name="phone" id="editProfilePhone">
                                    </div>
                                    <div class="mb-3">
                                        <label for="editProfilePosition" class="form-label">Position</label>
                                        <input type="text" class="form-control" name="position" id="editProfilePosition">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-save me-1"></i> Save Changes
                        </button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i> Cancel
                        </button>
                    </div>
                </form>

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
                                <label for="codeName" class="form-label">Voucher Code</label>
                                <input type="text" class="form-control" id="codeName" name="codeName" required>
                            </div>

                            <div class="mb-3">
                                <label for="voucherDescription" class="form-label">Description</label>
                                <textarea class="form-control" id="voucherDescription" name="voucherDescription"></textarea>
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
                                <label for="minOrderAmount" class="form-label">Min Order Amount</label>
                                <input type="number" step="0.01" class="form-control" id="minOrderAmount" name="minOrderAmount" value="0">
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
                                <label for="voucherActive" class="form-label">Status</label>
                                <select class="form-select" id="voucherActive" name="voucherActive" required>
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
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
        <!-- Edit Voucher Modal -->
        <div class="modal fade" id="editVoucherModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit Voucher</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" id="editVoucherId" name="voucherId">
                            <input type="hidden" name="action" value="updateVoucher">

                            <div class="mb-3">
                                <label for="editCodeName" class="form-label">Voucher Code</label>
                                <input type="text" class="form-control" id="editCodeName" name="codeName" required>
                            </div>

                            <div class="mb-3">
                                <label for="editDescription" class="form-label">Description</label>
                                <textarea class="form-control" id="editDescription" name="voucherDescription" rows="3"></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="editDiscountType" class="form-label">Discount Type</label>
                                <select class="form-select" id="editDiscountType" name="discountType">
                                    <option value="percentage">Percentage (%)</option>
                                    <option value="fixed">Fixed Amount ($)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="editDiscountValue" class="form-label">Discount Value</label>
                                <input type="number" class="form-control" id="editDiscountValue" name="discountValue" step="0.01" required>
                            </div>

                            <div class="mb-3">
                                <label for="editMinOrderAmount" class="form-label">Minimum Order Amount</label>
                                <input type="number" class="form-control" id="editMinOrderAmount" name="minOrderAmount" step="0.01" required>
                            </div>

                            <div class="mb-3">
                                <label for="editStartDate" class="form-label">Start Date</label>
                                <input type="date" class="form-control" id="editStartDate" name="startDate" required>
                            </div>

                            <div class="mb-3">
                                <label for="editEndDate" class="form-label">End Date</label>
                                <input type="date" class="form-control" id="editEndDate" name="endDate" required>
                            </div>

                            <div class="mb-3">
                                <label for="editVoucherActive" class="form-label">Status</label>
                                <select class="form-select" id="editVoucherActive" name="voucherActive" required>
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
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

        <!-- Modals -->
        <div class="modal fade" id="addDiscountModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Discount</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="AdminController" method="post">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="addDiscount">
                            <div class="mb-3">
                                <label for="proId" class="form-label">Product ID</label>
                                <input type="text" class="form-control" id="proId" name="proId" required>
                            </div>
                            <div class="mb-3">
                                <label for="discountType" class="form-label">Discount Type</label>
                                <select class="form-select" id="discountType" name="discountType" required>
                                    <option value="percentage">Percentage</option>
                                    <option value="fixed">Fixed</option>
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
                                <select class="form-select" id="active" name="active" required>
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="adminId" class="form-label">Admin ID</label>
                                <input type="text" class="form-control" id="adminId" name="adminId" required>
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

        <div class="modal fade" id="editDiscountModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Discount</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="AdminController" method="post">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="updateDiscount">
                            <input type="hidden" name="id" id="editDiscountId">
                            <div class="mb-3">
                                <label for="editProId" class="form-label">Product ID</label>
                                <input type="text" class="form-control" id="editProId" name="proId">
                            </div>
                            <div class="mb-3">
                                <label for="editDiscountType" class="form-label">Discount Type</label>
                                <select class="form-select" id="editDiscountType" name="discountType">
                                    <option value="percentage">Percentage</option>
                                    <option value="fixed">Fixed</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editDiscountValue" class="form-label">Discount Value</label>
                                <input type="number" step="0.01" class="form-control" id="editDiscountValue" name="discountValue">
                            </div>
                            <div class="mb-3">
                                <label for="editStartDate" class="form-label">Start Date</label>
                                <input type="date" class="form-control" id="editStartDate" name="startDate">
                            </div>
                            <div class="mb-3">
                                <label for="editEndDate" class="form-label">End Date</label>
                                <input type="date" class="form-control" id="editEndDate" name="endDate">
                            </div>
                            <div class="mb-3">
                                <label for="editActive" class="form-label">Active</label>
                                <select class="form-select" id="editActive" name="active">
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editAdminId" class="form-label">Admin ID</label>
                                <input type="text" class="form-control" id="editAdminId" name="adminId">
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

        <div class="modal fade" id="viewDiscountModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">View Discount</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Discount ID</label>
                            <p id="viewDiscountId" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Product ID</label>
                            <p id="viewProId" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Discount Type</label>
                            <p id="viewDiscountType" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Discount Value</label>
                            <p id="viewDiscountValue" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Start Date</label>
                            <p id="viewStartDate" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">End Date</label>
                            <p id="viewEndDate" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Active</label>
                            <p id="viewActive" class="form-control-plaintext"></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Admin ID</label>
                            <p id="viewAdminId" class="form-control-plaintext"></p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Edit Specification Modal -->
        <div class="modal fade" id="addSpecModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <form id="specForm" method="post" action="AdminController" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add / Edit Specification</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body row g-3 px-3">
                        <input type="hidden" name="action" id="specFormAction" value="addProductSpec">
                        <input type="hidden" name="specId" id="specId">

                        <div class="col-md-6">
                            <label for="productId" class="form-label">Product ID</label>
                            <input type="text" class="form-control" name="productId" id="productId" required>
                        </div>
                        <div class="col-md-6">
                            <label for="cpu" class="form-label">CPU</label>
                            <input type="text" class="form-control" name="cpu" id="cpu">
                        </div>
                        <div class="col-md-6">
                            <label for="ram" class="form-label">RAM</label>
                            <input type="text" class="form-control" name="ram" id="ram">
                        </div>
                        <div class="col-md-6">
                            <label for="storage" class="form-label">Storage</label>
                            <input type="text" class="form-control" name="storage" id="storage">
                        </div>
                        <div class="col-md-6">
                            <label for="screen" class="form-label">Screen</label>
                            <input type="text" class="form-control" name="screen" id="screen">
                        </div>
                        <div class="col-md-6">
                            <label for="os" class="form-label">OS</label>
                            <input type="text" class="form-control" name="os" id="os">
                        </div>
                        <div class="col-md-6">
                            <label for="battery" class="form-label">Battery</label>
                            <input type="text" class="form-control" name="battery" id="battery">
                        </div>
                        <div class="col-md-6">
                            <label for="camera" class="form-label">Camera</label>
                            <input type="text" class="form-control" name="camera" id="camera">
                        </div>
                        <div class="col-md-6">
                            <label for="graphic" class="form-label">Graphic</label>
                            <input type="text" class="form-control" name="graphic" id="graphic">
                        </div>
                    </div>
                    <div class="modal-footer px-3">
                        <button type="submit" class="btn btn-primary">Save</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>
            </div>
        </div>

        
        <script>const contextPath = '${pageContext.request.contextPath}';</script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>