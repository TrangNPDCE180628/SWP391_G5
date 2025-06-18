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

                       




                        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

                        </body>
                        </html>
