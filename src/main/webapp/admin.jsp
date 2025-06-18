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
        .action-buttons .btn-info { background-color: #17a2b8; border-color: #17a2b8; }
        .action-buttons .btn-warning { background-color: #ffc107; border-color: #ffc107; }
        .action-buttons .btn-danger { background-color: #dc3545; border-color: #dc3545; }
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
                            <a href="#discounts" class="nav-link" data-bs-toggle="tab">
                                <i class="fas fa-percent me-2"></i>Discounts
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
                                    <c:forEach items="${productTypes}" var="cate">
                                        <tr data-type-id="${cate.cateId}">
                                            <td>${cate.cateId}</td>
                                            <td data-type-name>${cate.cateName}</td>
                                            <td class="action-buttons">
                                                <button class="btn btn-sm btn-warning" onclick="editProductType('${cate.cateId}', '${cate.cateName}')">
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
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>