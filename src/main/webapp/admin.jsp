<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="css/admindashboard.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
            .container {
                padding-left: 0 !important;
                padding-right: 0 !important;
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
                                    <a href="#staff" class="nav-link" data-bs-toggle="tab">
                                        <i class="fas fa-users me-2"></i>Staff Manage
                                    </a>
                                </li>
                            </c:if>
                            <li class="nav-item">
                                <a href="#profile" class="nav-link" data-bs-toggle="tab">
                                    <i class="fas fa-user me-2"></i>Profile
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#products" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-box me-2"></i>Product
                                </a>
                            </li>
                            <c:if test="${LOGIN_USER.role == 'Admin'}">
                                <li class="nav-item">
                                    <a href="#productTypes" class="nav-link" data-bs-toggle="tab">
                                        <i class="fas fa-list-alt me-2"></i>Product Types
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${LOGIN_USER.role == 'Admin'}">
                                <li class="nav-item">
                                    <a href="#vouchers" class="nav-link" data-bs-toggle="tab">
                                        <i class="fa-solid fa-ticket me-2"></i>Voucher
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${LOGIN_USER.role == 'Admin'}">
                                <li class="nav-item">
                                    <a href="#attributes" class="nav-link" data-bs-toggle="tab">
                                        <i class="fas fa-list me-2"></i>Attributes
                                    </a>
                                </li>
                            </c:if>
                            <li class="nav-item">
                                <a href="#feedbacks" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>FeedBack Manage
                                </a>
                            </li>

                            <li class="nav-item">
                                <a href="#orders" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>Order Manage
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#inventory" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>Manage Product Quantity
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="#revenue" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>Manage Revenue
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
                                    <a class="dropdown-item" href="/LogoutController">Logout</a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="/HomeController">Go Home Page</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
                    <div class="tab-content">

                        <!-- Staff Tab only Admin -->

                        <c:if test="${LOGIN_USER.role == 'Admin'}">
                            <div class="tab-pane fade" id="staff">
                                <h2>Staff Management</h2>
                                <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addStaffModal">
                                    <i class="fas fa-plus"></i> Add New Staff
                                </button>
                                <input type="text" id="searchStaffInput" class="form-control mb-3" placeholder="Search staff by ID or Name">

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
                                                             alt="Staff Image" width="80" height="100">
                                                    </td>
                                                    <td class="action-buttons">
                                                        <button type="button" class="btn btn-sm btn-warning" onclick="editStaff('${staff.staffId}')">
                                                            <i class="fas fa-edit"></i> Edit
                                                        </button>
                                                        <button type="button" class="btn btn-sm btn-danger" onclick="deleteStaff('${staff.staffId}')">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>

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
                        <!-- Product Tab -->
                        <%--<jsp:include page="productManager.jsp" />--%>
                        <div class="tab-pane fade" id="products">
                            <h2>Product Management</h2>
                            <!-- Search form -->
                            <form class="row g-3 mb-3" method="get" action="AdminController">
                                <input type="hidden" name="tab" value="products" />
                                <input type="hidden" name="action" value="searchPrd" />
                                <div class="col-auto">
                                    <input type="text" class="form-control" name="searchValue" placeholder="Search by product name" value="${param.searchName}" />
                                </div>
                                <div class="col-auto">
                                    <button type="submit" class="btn btn-primary mb-3">
                                        <i class="fas fa-search"></i> Search
                                    </button>
                                </div>
                            </form>
                            <c:set var="prods" value="${requestScope.prds}" />
                            <c:if test="${empty prods}">
                                <h3>Item not found</h3>
                            </c:if>
                            <c:if test="${not empty sessionScope.error}">
                                <div class="alert alert-danger">${sessionScope.error}</div>
                                <c:remove var="error" scope="session"/>
                            </c:if>
                            <c:if test="${not empty sessionScope.success}">
                                <div class="alert alert-danger">${sessionScope.success}</div>
                                <c:remove var="error" scope="session"/>
                            </c:if>
                            <button class="btn btn-success mt-4" data-bs-toggle="modal" data-bs-target="#addProductModal">➕ Add Product</button>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Product Type</th>
                                            <th>Description</th>
                                            <th>Price</th>
                                            <th>Image</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${prods}" var="product">
                                            <tr>
                                                <td>${product.proId}</td>
                                                <td>${product.proName}</td>
                                                <td>${typeMap[product.proTypeId]}</td>
                                                <td>${product.proDescription}</td>
                                                <td>${product.proPrice}Đ</td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/products/${product.proImageMain}" alt="Product Image" width="80" height="80">
                                                </td>
                                                <td class="action-buttons">
                                                    <button type="button" class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#viewProductModal" data-proid="${product.proId}">
                                                        <i class="fas fa-eye"></i> View Detail
                                                    </button>
                                                    <!-- Edit button -->
                                                    <button type="button" class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editProductModal"
                                                            data-proid="${product.proId}"
                                                            data-proname="${product.proName}"
                                                            data-protypeid="${product.proTypeId}"
                                                            data-prodescription="${product.proDescription}"
                                                            data-proprice="${product.proPrice}"
                                                            data-proimagemain="${product.proImageMain}"
                                                            data-proAttName="${product.productAttributes.attributeName}"
                                                            data-proAttValue="${product.productAttributes.value}"
                                                            data-proAttUnit="${product.productAttributes.unit}">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </button>
                                                    <!-- Delete button -->
                                                    <form action="AdminController" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="deleteProduct" />
                                                        <input type="hidden" name="proId" value="${product.proId}" />
                                                        <input type="hidden" name="tab" value="products" />
                                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this product?');">
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

                        <!-- Product Types Tab -->
                        <div class="tab-pane fade" id="productTypes">
                            <h2>Product Type Management</h2>
                            <c:if test="${not empty sessionScope.error}">
                                <div class="alert alert-danger">${sessionScope.error}</div>
                                <c:remove var="error" scope="session"/>
                            </c:if>
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
                                            <tr
                                                data-product-type-id="${type.id}"
                                                data-product-type-name="${type.name}">
                                                <td>${type.id}</td>
                                                <td>${type.name}</td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-warning" onclick="editProductType('${type.id}', '${type.name}')"
                                                            data-bs-toggle="modal" data-bs-target="#editProductTypeModal">
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


                        <!-- Vouchers Tab -->
                        <div class="tab-pane fade" id="vouchers">
                            <jsp:include page="voucher-manager.jsp" />
                        </div>
                        <!-- Feedback Tab -->
                        <div class="tab-pane fade" id="feedbacks">
                            <h2>Feedback Management</h2>

                            <!-- Search and Filter Controls -->
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-search"></i></span>
                                        <input type="text" class="form-control" id="feedbackSearchInput" 
                                               placeholder="Search by customer name, product name, or feedback content...">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select" id="feedbackStatusFilter">
                                        <option value="all">All Status</option>
                                        <option value="pending">Pending</option>
                                        <option value="replied">Replied</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select" id="feedbackSortOrder">
                                        <option value="newest">Newest First</option>
                                        <option value="oldest">Oldest First</option>
                                        <option value="highest-rating">Highest Rating</option>
                                        <option value="lowest-rating">Lowest Rating</option>
                                    </select>
                                </div>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-striped" id="feedbackTable">
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
                                                data-cus-name="${fb.cusFullName}"
                                                data-pro-id="${fb.proId}"
                                                data-pro-name="${fb.proName}"
                                                data-content="${fb.feedbackContent}"
                                                data-rate="${fb.rate}"
                                                data-reply-id="${fb.replyFeedbackId}"
                                                data-reply-content="${fb.contentReply}"
                                                data-staff-id="${fb.staffId}"
                                                data-reply-time="${fb.createdAt}"
                                                data-status="${not empty fb.replyFeedbackId ? 'replied' : 'pending'}">
                                                <td>${fb.feedbackId}</td>
                                                <td>${fb.cusFullName}</td>
                                                <td>${fb.proName}</td>
                                                <td>${fb.feedbackContent}</td>
                                                <td>${fb.rate}★</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty fb.replyFeedbackId}">
                                                            <span class="badge bg-success">Replied</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-warning text-dark">Pending</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="action-buttons">
                                                    <button class="btn btn-sm btn-primary" onclick="viewCustomerInfo('${fb.cusId}')">
                                                        <i class="fas fa-user"></i> View Customer
                                                    </button>
                                                    <c:choose>
                                                        <c:when test="${empty fb.replyFeedbackId}">
                                                            <button class="btn btn-sm btn-info" onclick="replyFeedback('${fb.feedbackId}')">
                                                                <i class="fas fa-reply"></i> Reply
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button class="btn btn-sm btn-secondary" onclick="viewReply('${fb.feedbackId}')">
                                                                <i class="fas fa-eye"></i> View Reply
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteFeedback('${fb.feedbackId}')">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- No results message -->
                            <div id="noFeedbackResults" class="alert alert-info text-center" style="display: none;">
                                <i class="fas fa-search"></i> No feedback found matching your criteria.
                            </div>
                        </div>
                        <!-- Tab Attributes -->
                        <div class="tab-pane fade" id="attributes">
                            <h2>Attributes Management</h2>

                            <!-- Nav tabs for sub-sections -->
                            <ul class="nav nav-tabs mb-3" id="attributeSubTabs" role="tablist">
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link active" id="manage-attributes-tab" data-bs-toggle="tab" data-bs-target="#manage-attributes" type="button" role="tab">
                                        <i class="fas fa-tags"></i> Manage Attributes
                                    </button>
                                </li>
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link" id="product-attributes-tab" data-bs-toggle="tab" data-bs-target="#product-attributes" type="button" role="tab">
                                        <i class="fas fa-link"></i> Product Attributes
                                    </button>
                                </li>
                            </ul>

                            <div class="tab-content" id="attributeSubTabContent">
                                <!-- Manage Attributes Tab -->
                                <div class="tab-pane fade show active" id="manage-attributes" role="tabpanel">
                                    <h4>Attribute by Product Type</h4>

                                    <!-- Filter and Add button -->
                                    <div class="row mb-3">
                                        <div class="col-md-8">
                                            <form method="get" action="AdminController" class="d-flex gap-2">
                                                <input type="hidden" name="action" value="filterAttribute">
                                                <input type="hidden" name="tab" value="attributes">

                                                <select class="form-select" name="filterTypeId" style="max-width: 200px;">
                                                    <option value="">All Types</option>
                                                    <c:forEach var="type" items="${types}">
                                                        <option value="${type.id}" ${param.filterTypeId == type.id ? 'selected' : ''}>${type.name}</option>
                                                    </c:forEach>
                                                </select>

                                                <input type="text" class="form-control" name="filterAttributeName" placeholder="Attribute Name" value="${param.filterAttributeName}" style="max-width: 200px;">

                                                <button type="submit" class="btn btn-outline-primary">
                                                    <i class="fas fa-filter"></i> Filter
                                                </button>
                                                <a href="AdminController?tab=attributes" class="btn btn-outline-secondary">Reset</a>
                                            </form>
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addAttributeModal">
                                                <i class="fas fa-plus"></i> Add Attribute
                                            </button>
                                        </div>
                                    </div>

                                    <!-- Attributes Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Attribute Name</th>
                                                    <th>Unit</th>
                                                    <th>Product Type</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="attr" items="${attributes}">
                                                    <tr>
                                                        <td>${attr.attributeId}</td>
                                                        <td>${attr.attributeName}</td>
                                                        <td>${attr.unit}</td>
                                                        <td>
                                                            <c:forEach var="type" items="${types}">
                                                                <c:if test="${type.id == attr.proTypeId}">
                                                                    <span class="badge bg-info">${type.name}</span>
                                                                </c:if>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <button class="btn btn-sm btn-warning" onclick="editAttribute('${attr.attributeId}', '${fn:escapeXml(attr.attributeName)}', '${fn:escapeXml(attr.unit)}', '${attr.proTypeId}')" data-bs-toggle="modal" data-bs-target="#editAttributeModal">
                                                                <i class="fas fa-edit"></i> Edit
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" onclick="deleteAttribute('${attr.attributeId}')">
                                                                <i class="fas fa-trash"></i> Delete
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- Product Attributes Tab -->
                                <div class="tab-pane fade" id="product-attributes" role="tabpanel">
                                    <h4>Product Attribute Assignment</h4>

                                    <!-- Filter + Add button -->
                                    <div class="row mb-3">
                                        <div class="col-md-8">
                                            <form method="get" action="AdminController" class="d-flex gap-2">
                                                <input type="hidden" name="action" value="filterProductAttribute">
                                                <input type="hidden" name="tab" value="attributes">

                                                <input type="text" class="form-control" name="filterProductId" placeholder="Product ID" value="${param.filterProductId}" style="max-width: 150px;">

                                                <input type="text" class="form-control" name="filterAttributeName" placeholder="Attribute Name" value="${param.filterAttributeName}" style="max-width: 150px;">

                                                <input type="text" class="form-control" name="filterAttributeValue" placeholder="Value" value="${param.filterAttributeValue}" style="max-width: 150px;">

                                                <button type="submit" class="btn btn-outline-primary">
                                                    <i class="fas fa-filter"></i> Filter
                                                </button>
                                                <a href="AdminController?tab=attributes" class="btn btn-outline-secondary">Reset</a>
                                            </form>
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addProductAttributeModal">
                                                <i class="fas fa-link"></i> Assign Attribute to Product
                                            </button>
                                        </div>
                                    </div>

                                    <!-- Product Attributes Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Product ID</th>
                                                    <th>Product Name</th>
                                                    <th>Product Type</th>
                                                    <th>Attribute</th>
                                                    <th>Value</th>
                                                    <th>Unit</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="pa" items="${viewProductAttributes}">
                                                    <tr data-product-id="${pa.productId}" data-attribute-id="${pa.attributeId}" data-attribute-value="${pa.value}">
                                                        <td>${pa.productId}</td>
                                                        <td>${pa.productName}</td>
                                                        <td><span class="badge bg-secondary">${pa.productType}</span></td>
                                                        <td>${pa.attributeName}</td>
                                                        <td><strong>${pa.value}</strong></td>
                                                        <td>${pa.unit}</td>
                                                        <td>
                                                            <button class="btn btn-sm btn-info" onclick="viewProductAttribute('${pa.productId}', '${pa.attributeId}')">
                                                                <i class="fas fa-eye"></i> View
                                                            </button>
                                                            <button class="btn btn-sm btn-warning" onclick="editProductAttribute('${pa.productId}', '${pa.attributeId}', '${pa.value}')">
                                                                <i class="fas fa-edit"></i> Edit
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" onclick="deleteProductAttribute('${pa.productId}', '${pa.attributeId}')">
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




                        <!-- Orders Tab -->
                        <div class="tab-pane fade" id="orders">
                            <jsp:include page="orderManage.jsp" />
                        </div>
                        <!-- inventory Tab -->
                        <div class="tab-pane fade" id="inventory">
                            <jsp:include page="Stock.jsp" />
                        </div>
                        <!-- Revenue Tab -->
                        <div class="tab-pane fade" id="revenue">
                            <jsp:include page="Revenue.jsp" />
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
                    <input type="hidden" name="tab" value="profile">
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

        <!-- View Reply Modal -->
        <div class="modal fade" id="replyModal" tabindex="-1" aria-labelledby="replyModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="replyModalLabel">Reply Content</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Reply By Staff ID:</strong> <span id="modalReplyStaffId"></span></p>
                        <p><strong>Reply At:</strong> <span id="modalReplyTime"></span></p>
                        <hr>
                        <p id="modalReplyContent"></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" id="deleteReplyBtn" onclick="deleteReply()">
                            <i class="fas fa-trash"></i> Delete Reply
                        </button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Reply Feedback Modal -->
        <div class="modal fade" id="replyFeedbackModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Reply to Feedback</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <!-- Hidden Fields -->
                            <input type="hidden" name="action" value="replyFeedback">
                            <input type="hidden" name="feedbackId" id="replyFeedbackId">
                            <input type="hidden" name="cusId" id="replyCusId">

                            <!-- Staff Selection - Only show for Admin -->
                            <c:choose>
                                <c:when test="${LOGIN_USER.role == 'Admin'}">
                                    <div class="mb-3">
                                        <label for="staffSelect" class="form-label">Select Staff to Reply</label>
                                        <select class="form-select" id="staffSelect" name="staffId" required>
                                            <option value="">-- Choose Staff --</option>
                                            <c:forEach var="staff" items="${staffs}">
                                                <option value="${staff.staffId}">${staff.staffFullName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:when>
                                <c:when test="${LOGIN_USER.role == 'Staff'}">
                                    <!-- Hidden field for Staff ID and display current staff info -->
                                    <input type="hidden" name="staffId" value="${LOGIN_USER.id}">
                                    <div class="mb-3">
                                        <label class="form-label">Replying as:</label>
                                        <div class="alert alert-info">
                                            <i class="fas fa-user"></i> ${profile.staffFullName} (Staff)
                                        </div>
                                    </div>
                                </c:when>
                            </c:choose>

                            <!-- Reply Content -->
                            <div class="mb-3">
                                <label for="replyContent" class="form-label">Reply Content</label>
                                <textarea class="form-control" id="replyContent" name="contentReply" rows="4" required></textarea>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Send Reply</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- View Customer Info Modal -->
        <div class="modal fade" id="viewCustomerModal" tabindex="-1" aria-labelledby="viewCustomerModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewCustomerModalLabel">Customer Information</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-primary">Customer Details</h6>
                                <table class="table table-borderless">
                                    <tbody>
                                        <tr>
                                            <th width="35%">Customer ID:</th>
                                            <td id="modalCusId">-</td>
                                        </tr>
                                        <tr>
                                            <th>Full Name:</th>
                                            <td id="modalCusFullName">-</td>
                                        </tr>
                                        <tr>
                                            <th>Email:</th>
                                            <td id="modalCusEmail">-</td>
                                        </tr>
                                        <tr>
                                            <th>Phone:</th>
                                            <td id="modalCusPhone">-</td>
                                        </tr>
                                        <tr>
                                            <th>Gender:</th>
                                            <td id="modalCusGender">-</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-primary">Order History</h6>
                                <div id="customerOrderHistory" style="max-height: 300px; overflow-y: auto;">
                                    <div class="text-center">
                                        <div class="spinner-border text-primary" role="status">
                                            <span class="visually-hidden">Loading...</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Product Type Modal -->
        <div class="modal fade" id="addProductTypeModal" tabindex="-1" aria-labelledby="addProductTypeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form action="AdminController" method="post" class="modal-content">
                    <input type="hidden" name="action" value="addProductType">
                    <input type="hidden" name="tab" value="productTypes">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addProductTypeModalLabel">Add New Product Type</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="proTypeName" class="form-label">Product Type Name</label>
                            <input type="text" class="form-control" name="proTypeName" id="proTypeName" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Add Product Type</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Product Type Modal -->
        <div class="modal fade" id="editProductTypeModal" tabindex="-1" aria-labelledby="editProductTypeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form action="AdminController" method="post" class="modal-content">
                    <input type="hidden" name="action" value="updateProductType">
                    <input type="hidden" name="tab" value="productTypes">
                    <input type="hidden" name="proTypeId" id="editProTypeId">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editProductTypeModalLabel">Edit Product Type</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="editProTypeName" class="form-label">Product Type Name</label>
                            <input type="text" class="form-control" name="proTypeName" id="editProTypeName" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Update Product Type</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Add Staff Modal -->
        <div class="modal fade" id="addStaffModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Staff</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="AdminController" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="addStaff">


                            <div class="mb-3">
                                <label for="id" class="form-label">ID</label>
                                <input type="text" class="form-control" id="id" name="id" required>
                            </div>
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
                                <input type="text" class="form-control" id="fullname" name="fullname">
                            </div>

                            <div class="mb-3">
                                <label for="gender" class="form-label">Gender</label>
                                <select class="form-select" id="gender" name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="image" class="form-label">Image</label>
                                <input type="file" class="form-control" id="image" name="image">
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
                                <label for="role" class="form-label">Position</label>
                                <input type="text" class="form-control" id="position" name="position">
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
        <div class="modal fade" id="editStaffModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Staff</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="AdminController" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="editStaff">
                            <input type="hidden" id="edit-id-hidden" name="id">


                            <div class="mb-3">
                                <label for="edit-username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="edit-username" name="username" readonly>
                            </div>

                            <div class="mb-3">
                                <label for="edit-password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="edit-password" name="password" required>
                            </div>

                            <div class="mb-3">
                                <label for="edit-fullname" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="edit-fullname" name="fullname">
                            </div>

                            <div class="mb-3">
                                <label for="edit-gender" class="form-label">Gender</label>
                                <select class="form-select" id="edit-gender" name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="edit-image" class="form-label">Image</label>
                                <input type="file" class="form-control" id="edit-image" name="image">
                                <div class="mt-2">
                                    <img id="editStaffImagePreview" src="#" alt="Current Image"
                                         style="max-height: 120px; display: none; border: 1px solid #ccc; padding: 3px;">
                                </div>

                            </div>

                            <div class="mb-3">
                                <label for="edit-gmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="edit-gmail" name="gmail">
                            </div>

                            <div class="mb-3">
                                <label for="edit-phone" class="form-label">Phone</label>
                                <input type="text" class="form-control" id="edit-phone" name="phone">
                            </div>

                            <div class="mb-3">
                                <label for="edit-position" class="form-label">Position</label>
                                <input type="text" class="form-control" id="edit-position" name="position">
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



        <!-- Edit Product Attribute Modal -->
        <div class="modal fade" id="editProductAttributeModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit Product Attribute</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" name="action" value="updateProductAttribute">
                            <input type="hidden" name="tab" value="productAttributes">

                            <div class="mb-3">
                                <label for="editProId" class="form-label">Product ID</label>
                                <input type="text" class="form-control" id="editProId" name="proId" readonly>
                            </div>

                            <div class="mb-3">
                                <label for="editAttributeId" class="form-label">Attribute ID</label>
                                <input type="number" class="form-control" id="editAttributeId" name="attributeId" readonly>
                            </div>

                            <div class="mb-3">
                                <label for="editAttributeValue" class="form-label">Attribute Value</label>
                                <input type="text" class="form-control" id="editAttributeValue" name="value" required>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Update</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Add Attribute Modal -->
        <div class="modal fade" id="addAttributeModal" tabindex="-1" aria-labelledby="addAttributeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addAttributeModalLabel">Add New Attribute</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="action" value="addAttribute">
                            <input type="hidden" name="tab" value="attributes">

                            <div class="mb-3">
                                <label for="addAttributeName" class="form-label">Attribute Name</label>
                                <input type="text" class="form-control" id="addAttributeName" name="attributeName" required>
                            </div>

                            <div class="mb-3">
                                <label for="addAttributeUnit" class="form-label">Unit</label>
                                <input type="text" class="form-control" id="addAttributeUnit" name="unit" placeholder="e.g., cm, kg, inch">
                            </div>

                            <div class="mb-3">
                                <label for="addProTypeId" class="form-label">Product Type</label>
                                <select class="form-select" id="addProTypeId" name="proTypeId" required>
                                    <option value="">-- Select Product Type --</option>
                                    <c:forEach var="type" items="${types}">
                                        <option value="${type.id}">${type.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary">Add Attribute</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Edit Attribute Modal -->
        <div class="modal fade" id="editAttributeModal" tabindex="-1" aria-labelledby="editAttributeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editAttributeModalLabel">Edit Attribute</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="action" value="updateAttribute">
                            <input type="hidden" name="tab" value="attributes">
                            <input type="hidden" id="editAttributeId" name="attributeId">

                            <div class="mb-3">
                                <label for="editAttributeName" class="form-label">Attribute Name</label>
                                <input type="text" class="form-control" id="editAttributeName" name="attributeName" required>
                            </div>

                            <div class="mb-3">
                                <label for="editAttributeUnit" class="form-label">Unit</label>
                                <input type="text" class="form-control" id="editAttributeUnit" name="unit" placeholder="e.g., cm, kg, inch">
                            </div>

                            <div class="mb-3">
                                <label for="editProTypeId" class="form-label">Product Type</label>
                                <select class="form-select" id="editProTypeId" name="proTypeId" required>
                                    <option value="">-- Select Product Type --</option>
                                    <c:forEach var="type" items="${types}">
                                        <option value="${type.id}">${type.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary">Update Attribute</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal Add Product Attribute -->
        <div class="modal fade" id="addProductAttributeModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Assign Attribute to Product</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" name="action" value="addProductAttribute">
                            <input type="hidden" name="tab" value="attributes">

                            <!-- Product Selection -->
                            <div class="mb-3">
                                <label for="addPAProductId" class="form-label">Product</label>
                                <select class="form-select" id="addPAProductId" name="proId" required onchange="loadAttributesByProduct(this.value)">
                                    <option value="">-- Select Product --</option>
                                    <c:forEach var="product" items="${prds}">
                                        <option value="${product.proId}" data-type-id="${product.proTypeId}">
                                            ${product.proId} - ${product.proName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Product Type Display -->
                            <div class="mb-3">
                                <label class="form-label">Product Type</label>
                                <input type="text" class="form-control" id="addPAProductType" readonly placeholder="Select a product first">
                            </div>

                            <!-- Attribute Selection (will be populated based on product type) -->
                            <div class="mb-3">
                                <label for="addPAAttributeId" class="form-label">Attribute</label>
                                <select class="form-select" id="addPAAttributeId" name="attributeId" required disabled>
                                    <option value="">-- Select Product First --</option>
                                </select>
                            </div>

                            <!-- Attribute Value -->
                            <div class="mb-3">
                                <label for="addPAValue" class="form-label">Attribute Value</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="addPAValue" name="value" required>
                                    <span class="input-group-text" id="addPAUnit">Unit</span>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-success">Assign Attribute</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- View Product Attribute Modal -->
        <div class="modal fade" id="viewProductAttributeModal" tabindex="-1" aria-labelledby="viewProductAttributeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">View Product Attribute</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Product ID:</strong> <span id="viewProId"></span></p>
                        <p><strong>Product Name:</strong> <span id="viewProductName"></span></p>
                        <p><strong>Product Type:</strong> <span id="viewProductType"></span></p>
                        <p><strong>Attribute ID:</strong> <span id="viewAttributeId"></span></p>
                        <p><strong>Attribute Name:</strong> <span id="viewAttributeName"></span></p>
                        <p><strong>Value:</strong> <span id="viewAttributeValue"></span></p>
                        <p><strong>Unit:</strong> <span id="viewUnit"></span></p>
                    </div>

                </div>
            </div>
        </div>

        <!-- Edit Product Modal -->
        <div class="modal fade" id="editProductModal" tabindex="-1" aria-labelledby="editProductModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="AdminController" method="post" enctype="multipart/form-data">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editProductModalLabel">Edit Product</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="action" value="editProduct" />
                            <input type="hidden" name="tab" value="products" />
                            <input type="hidden" name="proId" id="prdId" />
                            <input type="hidden" name="oldImage" id="editOldImage" />
                            <div class="mb-3">
                                <label for="editProName" class="form-label">Name</label>
                                <input type="text" class="form-control" name="proName" id="editProName" required />
                            </div>
                            <div class="mb-3">
                                <label for="editProTypeId" class="form-label">Product Type</label>
                                <select class="form-select" name="proTypeId" id="editProTypeId" required>
                                    <option value="">-- Select Type --</option>
                                    <c:forEach var="type" items="${requestScope.types}">
                                        <option value="${type.id}">${type.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editProDescription" class="form-label">Description</label>
                                <textarea class="form-control" name="proDescription" id="editProDescription" required></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="editProPrice" class="form-label">Price</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" name="proPrice" id="editProPrice" required placeholder="0" />
                                    <span class="input-group-text">đ</span>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="editProImageMain" class="form-label">Image</label>
                                <input type="file" class="form-control" name="proImageMain" id="editProImageMain" accept="image/*" />
                                <img id="editProductImagePreview" src="#" alt="Current Image" style="max-height: 80px; margin-top: 5px; display: none;" />
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Save changes</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- View Product Modal -->
        <!-- Modal for View Product Detail -->
        <div class="modal fade" id="viewProductModal" tabindex="-1" aria-labelledby="viewProductModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewProductModalLabel">Product Detail</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Product details will be displayed here -->
                        <p><strong>ID:</strong> <span id="viewPrdId"></span></p>
                        <p><strong>Name:</strong> <span id="viewProName"></span></p>
                        <p><strong>Description:</strong> <span id="viewProDescription"></span></p>
                        <p><strong>Type ID:</strong> <span id="viewPrdTypeId"></span></p>
                        <p><strong>Price:</strong> <span id="viewProPrice"></span></p>
                        <p><strong>Image:</strong> <img id="viewProImage" src="#" alt="Product Image" style="max-width: 100px;"></p>
                        <p><strong>Product Attributes:</strong></p>
                        <ul id="viewProAttributes"></ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Add Product Modal -->
        <div class="modal fade" id="addProductModal" tabindex="-1">
            <div class="modal-dialog">
                <form class="modal-content" action="AdminController" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add"/>
                    <div class="modal-header">
                        <h5 class="modal-title">📝 Product Form</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-2">
                            <label>Product ID</label>
                            <input type="text" name="proId" class="form-control"/>
                        </div>
                        <div class="mb-2">
                            <label>Product Name</label>
                            <input type="text" name="proName" class="form-control" required/>
                        </div>
                        <div class="mb-2">
                            <label>Description</label>
                            <textarea name="proDescription" class="form-control" rows="3"></textarea>
                        </div>
                        <div class="mb-2">
                            <label for="addProPrice" class="form-label">Price</label>
                            <div class="input-group">
                                <input type="text" id="addProPrice" name="proPrice" class="form-control" required placeholder="0"/>
                                <span class="input-group-text">đ</span>
                            </div>
                        </div>
                        <div class="mb-2">
                            <label>Upload Image</label>
                            <input type="file" name="proImageMain" class="form-control" accept="image/*"/>
                        </div>

                        <div class="mb-2">
                            <label>Product Type</label>
                            <select name="proTypeId" class="form-select" required>
                                <option value="">-- Select Type --</option>
                                <c:forEach var="type" items="${requestScope.types}">
                                    <option value="${type.id}">${type.name}</option>
                                </c:forEach>
                            </select>
                        </div>


                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button class="btn btn-primary" type="submit">Save</button>
                    </div>
                </form>
            </div>
        </div>
        <script>
            // Define contextPath for use in JavaScript
            var contextPath = '${pageContext.request.contextPath}';

            // Price formatting functions
            function formatPrice(num) {
                if (!num)
                    return '';
                return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }

            function unformatPrice(str) {
                if (!str)
                    return '';
                return str.replace(/[,đ]/g, '').trim();
            }

            var editProductModal = document.getElementById('editProductModal');
            if (editProductModal) {
                editProductModal.addEventListener('show.bs.modal', function (event) {
                    var button = event.relatedTarget; // Nút Edit mà người dùng nhấn

                    // Debug: Log all data attributes
                    console.log('Button data attributes:');
                    console.log('proId:', button.getAttribute('data-proid'));
                    console.log('proName:', button.getAttribute('data-proname'));
                    console.log('proTypeId:', button.getAttribute('data-protypeid'));
                    console.log('proDescription:', button.getAttribute('data-prodescription'));
                    console.log('proPrice:', button.getAttribute('data-proprice'));
                    console.log('proImageMain:', button.getAttribute('data-proimagemain'));

                    document.getElementById('prdId').value = button.getAttribute('data-proid'); // Lấy proId và gán vào input ẩn

                    // Điền các giá trị khác vào modal
                    document.getElementById('editProName').value = button.getAttribute('data-proname') || '';
                    document.getElementById('editProTypeId').value = button.getAttribute('data-protypeid') || '';
                    document.getElementById('editProDescription').value = button.getAttribute('data-prodescription') || '';

                    // Format price for display
                    var rawPrice = button.getAttribute('data-proprice');
                    document.getElementById('editProPrice').value = formatPrice(rawPrice);

                    // Cập nhật hình ảnh nếu có
                    var img = button.getAttribute('data-proimagemain');
                    var preview = document.getElementById('editProductImagePreview');
                    document.getElementById('editOldImage').value = img; // Store current image name

                    if (img) {
                        preview.src = contextPath + '/images/products/' + img;
                        preview.style.display = 'block';
                    } else {
                        preview.style.display = 'none';
                    }
                });

                // Add price formatting on input
                var priceInput = document.getElementById('editProPrice');
                if (priceInput) {
                    priceInput.addEventListener('input', function (event) {
                        var value = event.target.value;
                        var unformatted = unformatPrice(value);

                        // Only allow numbers
                        if (!/^\d*$/.test(unformatted)) {
                            event.target.value = event.target.value.slice(0, -1);
                            return;
                        }

                        // Format the number
                        if (unformatted) {
                            event.target.value = formatPrice(unformatted);
                        }
                    });

                    // Remove formatting before form submission
                    priceInput.closest('form').addEventListener('submit', function () {
                        priceInput.value = unformatPrice(priceInput.value);
                    });
                }

                // Add image preview functionality for file input
                var editImageInput = document.getElementById('editProImageMain');
                if (editImageInput) {
                    editImageInput.addEventListener('change', function (event) {
                        var file = event.target.files[0];
                        var preview = document.getElementById('editProductImagePreview');

                        if (file) {
                            var reader = new FileReader();
                            reader.onload = function (e) {
                                preview.src = e.target.result;
                                preview.style.display = 'block';
                            };
                            reader.readAsDataURL(file);
                        }
                    });
                }
            }

            // Setup Add Product form
            var addProductModal = document.getElementById('addProductModal');
            if (addProductModal) {
                var addPriceInput = document.getElementById('addProPrice');
                if (addPriceInput) {
                    addPriceInput.addEventListener('input', function (event) {
                        var value = event.target.value;
                        var unformatted = unformatPrice(value);

                        // Only allow numbers
                        if (!/^\d*$/.test(unformatted)) {
                            event.target.value = event.target.value.slice(0, -1);
                            return;
                        }

                        // Format the number
                        if (unformatted) {
                            event.target.value = formatPrice(unformatted);
                        }
                    });

                    // Remove formatting before form submission
                    addPriceInput.closest('form').addEventListener('submit', function () {
                        addPriceInput.value = unformatPrice(addPriceInput.value);
                    });
                }
            }

            var viewProductModal = document.getElementById('viewProductModal');
            if (viewProductModal) {
                viewProductModal.addEventListener('show.bs.modal', function (event) {
                    const button = event.relatedTarget;
                    const proId = button.getAttribute('data-proid');

                    fetch('AdminController?action=viewProductDetail&proId=' + proId)
                            .then(response => response.json())
                            .then(product => {
                                console.log("Full product:", product);

                                // Điền thông tin sản phẩm vào modal
                                document.getElementById('viewPrdId').textContent = product.proId || '';
                                document.getElementById('viewProName').textContent = product.proName || '';
                                document.getElementById('viewProDescription').textContent = product.proDescription || '';
                                document.getElementById('viewPrdTypeId').textContent = product.proTypeId || '';
                                document.getElementById('viewProPrice').textContent = product.proPrice || '';

                                // Hình ảnh
                                const imgTag = document.getElementById('viewProImage');
                                imgTag.src = product.proImage
                                        ? '/images/products/' + product.proImage
                                        : '/images/default.png';
                                imgTag.alt = 'Product Image';

                                // Hiển thị thuộc tính
                                const attributesList = document.getElementById('viewProAttributes');
                                attributesList.innerHTML = ''; // Xoá cũ
                                console.log("Danh sách thuộc tính:", product.productAttributes);
                                if (Array.isArray(product.productAttributes) && product.productAttributes.length > 0) {
                                    product.productAttributes.forEach(attr => {
                                        console.log("Attr in loop:", attr);
                                        const name = attr.attributeName || 'Unnamed';
                                        const value = attr.value || 'N/A';
                                        const unit = attr.unit || '';

                                        // Tạo phần tử <li> mới
                                        const listItem = document.createElement('li');
                                        listItem.textContent = name + ': ' + value + ' ' + unit;
                                        attributesList.appendChild(listItem);
                                    });
                                } else {
                                    const listItem = document.createElement('li');
                                    listItem.textContent = 'No attributes available';
                                    attributesList.appendChild(listItem);
                                }
                            })
                            .catch(error => console.error('Error fetching product details:', error));
                });
            }

            // Attribute Management Functions
            function editAttribute(attributeId, attributeName, unit, proTypeId) {
                document.getElementById('editAttributeId').value = attributeId;
                document.getElementById('editAttributeName').value = attributeName;
                document.getElementById('editAttributeUnit').value = unit;
                document.getElementById('editProTypeId').value = proTypeId;
            }

            function deleteAttribute(attributeId) {
                if (confirm('Are you sure you want to delete this attribute?')) {
                    window.location.href = `${contextPath}/AdminController?action=deleteAttribute&attributeId=${attributeId}&tab=attributes`;
                }
            }

            // Product Attribute Management Functions
            function loadAttributesByProduct(productId) {
                if (!productId) {
                    resetProductAttributeForm();
                    return;
                }

                // Get product type from selected option  
                const productSelect = document.getElementById('addPAProductId');
                const selectedOption = productSelect.options[productSelect.selectedIndex];
                const typeId = selectedOption.getAttribute('data-type-id');

                // Show product type - find type name by iterating through options
                const typeOptions = document.querySelectorAll('#addProTypeId option');
                let typeName = 'Unknown Type';
                typeOptions.forEach(option => {
                    if (option.value === typeId) {
                        typeName = option.textContent;
                    }
                });
                document.getElementById('addPAProductType').value = typeName;

                // Load attributes for this product type via AJAX
                loadAttributesForType(typeId);
            }

            function resetProductAttributeForm() {
                document.getElementById('addPAProductType').value = '';
                document.getElementById('addPAAttributeId').innerHTML = '<option value="">-- Select Product First --</option>';
                document.getElementById('addPAAttributeId').disabled = true;
                document.getElementById('addPAUnit').textContent = 'Unit';
            }

            function loadAttributesForType(typeId) {
                const attributeSelect = document.getElementById('addPAAttributeId');
                attributeSelect.innerHTML = '<option value="">Loading...</option>';

                // Make AJAX call to get attributes for the type
                fetch(contextPath + '/AdminController?action=getAttributesByType&typeId=' + typeId)
                        .then(response => response.json())
                        .then(attributes => {
                            attributeSelect.innerHTML = '<option value="">-- Select Attribute --</option>';

                            attributes.forEach(attr => {
                                const option = document.createElement('option');
                                option.value = attr.attributeId;
                                option.textContent = attr.attributeName;
                                option.setAttribute('data-unit', attr.unit || '');
                                attributeSelect.appendChild(option);
                            });

                            attributeSelect.disabled = false;

                            // Add event listener for unit display
                            attributeSelect.addEventListener('change', function () {
                                const selectedAttr = this.options[this.selectedIndex];
                                const unit = selectedAttr.getAttribute('data-unit') || 'Unit';
                                document.getElementById('addPAUnit').textContent = unit;
                            });
                        })
                        .catch(error => {
                            console.error('Error loading attributes:', error);
                            attributeSelect.innerHTML = '<option value="">Error loading attributes</option>';
                        });
            }

            function setupPriceInputFormat(inputId) {
                const priceInput = document.getElementById(inputId);
                if (priceInput) {
                    priceInput.addEventListener('input', function () {
                        let value = this.value.replace(/\./g, '');
                        if (!isNaN(value) && value !== '') {
                            this.value = Number(value).toLocaleString('vi-VN');
                        } else {
                            this.value = '';
                        }
                    });
                }
            }

            // Auto-activate tabs based on URL parameter or session attribute
            document.addEventListener('DOMContentLoaded', function () {
                const urlParams = new URLSearchParams(window.location.search);
                const activeTab = urlParams.get('tab') || '${param.tab}' || 'products';

                if (activeTab) {
                    // Activate the tab link
                    const tabLink = document.querySelector(`a[href="#${activeTab}"]`);
                    if (tabLink) {
                        const tab = new bootstrap.Tab(tabLink);
                        tab.show();
                    }
                }
            });
        </script>


        <script>const contextPath = '${pageContext.request.contextPath}';</script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script src="${pageContext.request.contextPath}/js/ScriptAdminDashboard.js"></script>

    </body>
</html>

