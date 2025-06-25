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
        .sidebar {
            background-color: #343a40;
            color: white;
            min-height: 100vh;
        }
        .sidebar .nav-link {
            color: white;
        }
        .sidebar .nav-link:hover {
            background-color: #495057;
        }
        .main-content {
            padding: 20px;
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
                    <c:if test="${LOGIN_USER.role == 'Admin'}">
                        <li class="nav-item">
                            <a href="#staff" class="nav-link ${activeTab == 'staff' ? 'active' : ''}" data-bs-toggle="tab">
                                <i class="fas fa-users me-2"></i>Staff Manage
                            </a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a href="#profile" class="nav-link ${activeTab == 'profile' ? 'active' : ''}" data-bs-toggle="tab">
                            <i class="fas fa-user me-2"></i>Profile
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#productTypes" class="nav-link ${activeTab == 'productTypes' ? 'active' : ''}" data-bs-toggle="tab">
                            <i class="fas fa-tags me-2"></i>Product Types
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#vouchers" class="nav-link ${activeTab == 'vouchers' ? 'active' : ''}" data-bs-toggle="tab">
                            <i class="fas fa-ticket-alt me-2"></i>Voucher
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#feedbacks" class="nav-link ${activeTab == 'feedbacks' ? 'active' : ''}" data-bs-toggle="tab">
                            <i class="fas fa-comment-dots me-2"></i>Feedback Manage
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#orders" class="nav-link ${activeTab == 'orders' ? 'active' : ''}" data-bs-toggle="tab">
                            <i class="fas fa-shopping-cart me-2"></i>Order Management
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
                            <a class="dropdown-item" href="LogoutController">Logout</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 main-content">
            <c:if test="${not empty ERROR}">
                <div class="alert alert-danger">${ERROR}</div>
            </c:if>
            <div class="tab-content">
                <!-- Orders Tab -->
                <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'orders'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="orders">
                    <h2>Order Management</h2>
                    <div class="mb-3">
                        <label for="statusFilter" class="form-label">Filter by Status:</label>
                        <select class="form-select w-auto d-inline-block" id="statusFilter" onchange="filterOrders()">
                            <option value="">All</option>
                            <option value="Done" ${param.statusFilter == 'Done' ? 'selected' : ''}>Done</option>
                            <option value="Pending" ${param.statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                            <option value="Cancel" ${param.statusFilter == 'Cancel' ? 'selected' : ''}>Cancel</option>
                        </select>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Customer</th>
                                    <th>Order Date</th>
                                    <th>Status</th>
                                    <th>Total Amount</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="ordersTableBody">
                            <c:set var="orders" value="${orderList}" />    
                                <c:forEach items="${orders}" var="order">
                                    <tr data-order-id="${order.id}">
                                        <td>${order.id}</td>
                                        <td>${order.customerName}</td>
                                        <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>
                                            <select class="form-select status-select" onchange="updateOrderStatus(${order.id}, this.value)">
                                                <option value="Done" ${order.status == 'Done' ? 'selected' : ''}>Done</option>
                                                <option value="Pending" ${order.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                                <option value="Cancel" ${order.status == 'Cancel' ? 'selected' : ''}>Cancel</option>
                                            </select>
                                        </td>
                                        <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/></td>
                                        <td class="action-buttons">
                                            <a href="AdminController?action=viewOrderDetails&orderId=${order.id}" class="btn btn-sm btn-info">
                                                <i class="fas fa-eye"></i> View
                                            </a>
                                            <button class="btn btn-sm btn-danger" onclick="deleteOrder(${order.id})">
                                                <i class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty orders}">
                                    <tr>
                                        <td colspan="6" class="text-center">No orders found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Staff Tab (Admin Only) -->
                <c:if test="${LOGIN_USER.role == 'Admin'}">
                    <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'staff'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="staff">
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
                                            id="staff-row-${staff.staffId}"
                                            data-staff-id="${staff.staffId}"
                                            data-staff-name="${staff.staffName}"
                                            data-staff-fullname="${staff.staffFullName}"
                                            data-staff-password="${staff.staffPassword}"
                                            data-staff-gender="${staff.staffGender}"
                                            data-staff-gmail="${staff.staffGmail}"
                                            data-staff-phone="${staff.staffPhone}"
                                            data-staff-position="${staff.staffPosition}"
                                            data-staff-image="${staff.staffImage}">
                                            <td>${staff.staffId}</td>
                                            <td>${staff.staffName}</td>
                                            <td>${staff.staffFullName}</td>
                                            <td>
                                                <img src="${pageContext.request.contextPath}/images/staff/${staff.staffImage}"
                                                     alt="Staff Image" width="60" height="60" style="object-fit: cover;">
                                            </td>
                                            <td class="action-buttons">
                                                <button type="button" class="btn btn-sm btn-primary" onclick="editStaff('${staff.staffId}')">
                                                    <i class="fas fa-edit"></i> Edit
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty staffs}">
                                        <tr>
                                            <td colspan="5" class="text-center">No staff found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <!-- Profile Tab -->
                <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'profile'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="profile">
                    <h2>My Profile</h2>
                    <button class="btn btn-primary mb-3" onclick="editProfile('${LOGIN_USER.role}', '${LOGIN_USER.id}')">
                        <i class="fas fa-edit"></i> Edit Profile
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
                                        <tr>
                                            <th>Full Name</th>
                                            <td>${profile.adminFullName}</td>
                                        </tr>
                                        <tr>
                                            <th>Email</th>
                                            <td>${profile.adminGmail}</td>
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
                                                <img src="${pageContext.request.contextPath}/images/staff/${profile.staffImage}"
                                                     alt="Avatar" class="rounded-circle"
                                                     style="width: 120px; height: 120px; object-fit: cover;">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Full Name</th>
                                            <td>${profile.staffFullName}</td>
                                        </tr>
                                        <tr>
                                            <th>Email</th>
                                            <td>${profile.staffGmail}</td>
                                        </tr>
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
                                    </c:when>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Product Types Tab -->
                <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'productTypes'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="productTypes">
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
                            <tbody id="productTypesTableBody">
                                <c:forEach items="${productTypes}" var="type">
                                    <tr data-type-id="${type.id}" data-type-name="${type.name}">
                                        <td>${type.id}</td>
                                        <td>${type.name}</td>
                                        <td class="action-buttons">
                                            <button class="btn btn-sm btn-primary" onclick="editProductType(${type.id}, '${type.name}')">
                                                <i class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="deleteProductType(${type.id})">
                                                <i class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty productTypes}">
                                    <tr>
                                        <td colspan="3" class="text-center">No product types found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Vouchers Tab -->
                <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'vouchers'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="vouchers">
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
                                        data-voucher-start-date="<fmt:formatDate value='${voucher.startDate}' pattern='yyyy-MM-dd'/>"
                                        data-voucher-end-date="<fmt:formatDate value='${voucher.endDate}' pattern='yyyy-MM-dd'/>"
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
                                                    <fmt:formatNumber value="${voucher.discountValue}" type="currency" currencySymbol="$"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><fmt:formatDate value="${voucher.startDate}" pattern="yyyy-MM-dd"/></td>
                                        <td><fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd"/></td>
                                        <td>
                                            <span class="badge ${voucher.voucherActive ? 'bg-success' : 'bg-secondary'}">
                                                ${voucher.voucherActive ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td class="action-buttons">
                                            <button class="btn btn-sm btn-primary" onclick="editVoucher(${voucher.voucherId})">
                                                <i class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="deleteVoucher(${voucher.voucherId})">
                                                <i class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty vouchers}">
                                    <tr>
                                        <td colspan="8" class="text-center">No vouchers found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Feedback Tab -->
                <div class="tab-pane fade <c:choose><c:when test="${activeTab == 'feedbacks'}">show active</c:when><c:otherwise></c:otherwise></c:choose>" id="feedbacks">
                    <h2>Feedback Management</h2>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Feedback ID</th>
                                    <th>Customer</th>
                                    <th>Product</th>
                                    <th>Content</th>
                                    <th>Rate</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${viewFeedbacks}" var="fb">
                                    <tr
                                        data-feedback-id="${fb.feedbackId}"
                                        data-cus-id="${fb.cusId}"
                                        data-pro-id="${fb.proId}"
                                        data-content="${fb.feedbackContent}"
                                        data-rate="${fb.rate}"
                                        data-reply-id="${fb.replyFeedbackId}"
                                        data-reply-content="${fb.contentReply}"
                                        data-staff-id="${fb.staffId}"
                                        data-reply-time="${fb.createdAt}">
                                        <td>${fb.feedbackId}</td>
                                        <td>${fb.cusFullName}</td>
                                        <td>${fb.proName}</td>
                                        <td>${fb.feedbackContent}</td>
                                        <td>${fb.rate}★</td>
                                        <td>
                                            <span class="badge ${not empty fb.replyFeedbackId ? 'bg-success' : 'bg-warning text-dark'}">
                                                ${not empty fb.replyFeedbackId ? 'Replied' : 'Pending'}
                                            </span>
                                        </td>
                                        <td class="action-buttons">
                                            <c:choose>
                                                <c:when test="${empty fb.replyFeedbackId}">
                                                    <button class="btn btn-sm btn-info" onclick="replyFeedback(${fb.feedbackId})">
                                                        <i class="fas fa-reply"></i> Reply
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn btn-sm btn-secondary" onclick="viewReply(${fb.feedbackId})">
                                                        <i class="fas fa-eye"></i> View Reply
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="btn btn-sm btn-danger" onclick="deleteFeedback(${fb.feedbackId})">
                                                <i class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty viewFeedbacks}">
                                    <tr>
                                        <td colspan="7" class="text-center">No feedback found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Profile Modal -->
    <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <form action="AdminController" method="post" enctype="multipart/form-data" class="modal-content">
                <input type="hidden" name="action" value="editProfile">
                <input type="hidden" name="tab" value="profile">
                <input type="hidden" name="userId" id="editUserId">
                <input type="hidden" name="userRole" id="editUserRole">
                <input type="hidden" name="currentImage" id="currentProfileImagePath">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProfileModalLabel">Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <img id="previewProfileImage" src="" alt="Preview" class="rounded-circle mb-3" style="width: 150px; height: 150px; object-fit: cover;">
                            <input type="file" class="form-control" name="image" id="editProfileImage" accept="image/*" onchange="previewImage(this)">
                        </div>
                        <div class="col-md-8">
                            <div class="mb-3">
                                <label for="editProfileFullName" class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="fullName" id="editProfileFullName" required>
                            </div>
                            <div class="mb-3">
                                <label for="editProfileEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" id="editProfileEmail" required>
                            </div>
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

    <!-- Add Product Type Modal -->
    <div class="modal fade" id="addProductTypeModal" tabindex="-1" aria-labelledby="addProductTypeModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addProductTypeModalLabel">Add New Product Type</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <form id="addProductTypeForm" action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="addProductType">
                        <input type="hidden" name="tab" value="productTypes">
                        <div class="mb-3">
                            <label for="typeName" class="form-label">Product Type Name</label>
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

    <!-- Edit Product Type Modal -->
    <div class="modal fade" id="editProductTypeModal" tabindex="-1" aria-labelledby="editProductTypeModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProductTypeModalLabel">Edit Product Type</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <form id="editProductTypeForm" action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateProductType">
                        <input type="hidden" name="tab" value="productTypes">
                        <input type="hidden" name="typeId" id="editTypeId">
                        <div class="mb-3">
                            <label for="editTypeName" class="form-label">Product Type Name</label>
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

    <!-- Add Voucher Modal -->
    <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-labelledby="addVoucherModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addVoucherModalLabel">Add New Voucher</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <form action="AdminController" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="addVoucher">
                        <input type="hidden" name="tab" value="vouchers">
                        <div class="mb-3">
                            <label for="codeName" class="form-label">Voucher Code</label>
                            <input type="text" class="form-control" id="codeName" name="codeName" required>
                        </div>
                        <div class="mb-3">
                            <label for="voucherDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="voucherDescription" name="voucherDescription" rows="3"></textarea>
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
                            <input type="number" step="0.01" class="form-control" id="minOrderAmount" name="minOrderAmount" value="0" required>
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
    <div class="modal fade" id="editVoucherModal" tabindex="-1" aria-labelledby="editVoucherModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="AdminController" method="post">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editVoucherModalLabel">Edit Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateVoucher">
                        <input type="hidden" name="tab" value="vouchers">
                        <input type="hidden" name="voucherId" id="editVoucherId">
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
                            <select class="form-select" id="editDiscountType" name="discountType" required>
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

    <!-- Add Staff Modal -->
    <div class="modal fade" id="addStaffModal" tabindex="-1" aria-labelledby="addStaffModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addStaffModalLabel">Add New Staff</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <form action="AdminController" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="addStaff">
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-4 text-center">
                                <img id="previewAddStaffImage" src="${pageContext.request.contextPath}/images/staff/default.jpg"
                                     alt="Preview" class="rounded-circle mb-3" style="width: 150px; height: 150px; object-fit: cover;">
                                <input type="file" class="form-control" name="image" id="addStaffImage" accept="image/*" onchange="previewImage(this)">
                            </div>
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="staffId" class="form-label">Staff ID</label>
                                    <input type="text" class="form-control" id="staffId" name="id" required>
                                </div>
                                <div class="mb-3">
                                    <label for="staffUsername" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="staffUsername" name="username" required>
                                </div>
                                <div class="mb-3">
                                    <label for="staffPassword" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="staffPassword" name="password" required>
                                </div>
                                <div class="mb-3">
                                    <label for="staffFullName" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="staffFullName" name="fullname" required>
                                </div>
                                <div class="mb-3">
                                    <label for="staffGender" class="form-label">Gender</label>
                                    <select class="form-select" id="staffGender" name="gender" required>
                                        <option value="Male">Male</option>
                                        <option value="Female">Female</option>
                                        <option value="Other">Other</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="staffEmail" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="staffEmail" name="gmail" required>
                                </div>
                                <div class="mb-3">
                                    <label for="staffPhone" class="form-label">Phone</label>
                                    <input type="text" class="form-control" id="staffPhone" name="phone">
                                </div>
                                <div class="mb-3">
                                    <label for="staffPosition" class="form-label">Position</label>
                                    <input type="text" class="form-control" id="staffPosition" name="position" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add Staff</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Staff Modal -->
    <div class="modal fade" id="editStaffModal" tabindex="-1" aria-labelledby="editStaffModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editStaffModalLabel">Edit Staff</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <form action="AdminController" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="editStaff">
                    <input type="hidden" name="staffId" id="editStaffId">
                    <input type="hidden" name="currentImage" id="currentStaffImage">
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-4 text-center">
                                <img id="previewEditStaffImage" src="" alt="Preview" class="rounded-circle mb-3"
                                     style="width: 150px; height: 150px; object-fit: cover;">
                                <input type="file" class="form-control" name="staffImage" id="editStaffImage" accept="image/*"
                                       onchange="previewImage(this)">
                            </div>
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="editStaffName" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="editStaffName" name="staffName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffFullName" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="editStaffFullName" name="staffFullName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffPassword" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="editStaffPassword" name="staffPassword">
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffGender" class="form-label">Gender</label>
                                    <select class="form-select" id="editStaffGender" name="staffGender" required>
                                        <option value="Male">Male</option>
                                        <option value="Female">Female</option>
                                        <option value="Other">Other</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffGmail" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="editStaffGmail" name="staffGmail" required>
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffPhone" class="form-label">Phone</label>
                                    <input type="text" class="form-control" id="editStaffPhone" name="staffPhone">
                                </div>
                                <div class="mb-3">
                                    <label for="editStaffPosition" class="form-label">Position</label>
                                    <input type="text" class="form-control" id="editStaffPosition" name="staffPosition" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update Staff</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Reply Feedback Modal -->
    <div class="modal fade" id="replyFeedbackModal" tabindex="-1" aria-labelledby="replyFeedbackModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="AdminController" method="post">
                    <div class="modal-header">
                        <h5 class="modal-title" id="replyFeedbackModalLabel">Reply to Feedback</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="replyFeedback">
                        <input type="hidden" name="tab" value="feedbacks">
                        <input type="hidden" name="feedbackId" id="replyFeedbackId">
                        <input type="hidden" name="cusId" id="replyCusId">
                        <div class="mb-3">
                            <label for="staffSelect" class="form-label">Select Staff</label>
                            <select class="form-select" id="staffSelect" name="staffId" required>
                                <c:forEach items="${staffs}" var="staff">
                                    <option value="${staff.staffId}">${staff.staffFullName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="contentReply" class="form-label">Reply Content</label>
                            <textarea class="form-control" id="contentReply" name="contentReply" rows="4" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Submit Reply</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- View Reply Modal -->
    <div class="modal fade" id="viewReplyModal" tabindex="-1" aria-labelledby="viewReplyModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="viewReplyModalLabel">Reply Content</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                </div>
                <div class="modal-body">
                    <p><strong>Reply By Staff ID:</strong> <span id="modalReplyStaffId"></span></p>
                    <p><strong>Reply At:</strong> <span id="modalReplyTime"></span></p>
                    <hr>
                    <p id="modalReplyContent"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/ScriptAdminDashboard.js"></script>
    <script>
        function previewImage(input) {
            const preview = input.parentElement.querySelector('img');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        }

        function updateOrderStatus(orderId, status) {
            if (confirm('Are you sure you want to update the status of order #' + orderId + ' to ' + status + '?')) {
                fetch('AdminController?action=updateOrderStatus&orderId=' + orderId + '&status=' + encodeURIComponent(status), {
                    method: 'POST'
                })
                .then(response => {
                    if (response.ok) {
                        alert('Order status updated successfully!');
                        location.reload(); // Refresh to update the table
                    } else {
                        alert('Failed to update order status.');
                    }
                })
                .catch(error => {
                    console.error('Error updating order status:', error);
                    alert('An error occurred while updating the order status.');
                });
            }
        }

        function filterOrders() {
            const statusFilter = document.getElementById('statusFilter').value;
            window.location.href = 'AdminController?action=loadOrders&tab=orders&statusFilter=' + encodeURIComponent(statusFilter);
        }

        function deleteOrder(orderId) {
            if (confirm('Are you sure you want to delete order #' + orderId + '?')) {
                fetch('AdminController?action=deleteOrder&orderId=' + orderId, {
                    method: 'POST'
                })
                .then(response => {
                    if (response.ok) {
                        alert('Order deleted successfully!');
                        document.querySelector(`tr[data-order-id="${orderId}"]`).remove();
                    } else {
                        alert('Failed to delete order.');
                    }
                })
                .catch(error => {
                    console.error('Error deleting order:', error);
                    alert('An error occurred while deleting the order.');
                });
            }
        }
    </script>
</body>
</html>