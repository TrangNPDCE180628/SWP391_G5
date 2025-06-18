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
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.7.1/dist/chart.min.js"></script>
    <style>
        /* Existing Styles (unchanged) */
        .orders-card {
            border: none;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
        }
        .orders-card .card-header {
            background: linear-gradient(90deg, #007bff, #0056b3);
            color: white;
            border-radius: 10px 10px 0 0;
            padding: 1rem 1.5rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .orders-table th {
            font-weight: 600;
            color: #333;
            background-color: #f8f9fa;
            border-bottom: 2px solid #dee2e6;
        }
        .orders-table td {
            vertical-align: middle;
            padding: 1rem;
        }
        .orders-table tr {
            transition: background-color 0.2s ease;
        }
        .orders-table tr:hover {
            background-color: #f1f3f5;
        }
        .status-badge {
            font-size: 0.85rem;
            padding: 0.4rem 0.8rem;
            border-radius: 12px;
        }
        .status-pending { background-color: #ffc107; color: #333; }
        .status-done { background-color: #28a745; color: white; }
        .status-cancel { background-color: #dc3545; color: white; }
        .action-buttons .btn, .action-buttons .form-select {
            margin-right: 0.5rem;
            font-size: 0.9rem;
        }
        .action-buttons .btn i { margin-right: 0.3rem; }
        .filter-form select {
            border-radius: 8px;
            padding: 0.5rem;
            font-size: 0.95rem;
            border: 1px solid #ced4da;
        }
        .filter-form select:focus {
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
            border-color: #007bff;
        }
        .sidebar {
            background: #2c3e50;
            min-height: 100vh;
        }
        .sidebar .nav-link {
            color: #dcdcdc;
            padding: 0.75rem 1rem;
            border-radius: 5px;
            margin-bottom: 0.5rem;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #34495e;
            color: white;
        }
        .main-content {
            padding: 2rem;
            background-color: #f4f6f9;
        }

        /* New Styles for Revenue Tab */
        .revenue-card {
            border: none;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
            margin-bottom: 2rem;
        }
        .revenue-card .card-header {
            background: linear-gradient(90deg, #007bff, #0056b3);
            color: white;
            padding: 1.5rem;
            border-radius: 10px 10px 0 0;
            font-size: 1.5rem;
            font-weight: 600;
        }
        .filter-section {
            padding: 1rem;
            background-color: #f8f9fa;
            border-bottom: 1px solid #dee2e6;
        }
        .filter-btn {
            background-color: #fff;
            border: 1px solid #ced4da;
            border-radius: 8px;
            padding: 0.5rem 1rem;
            cursor: pointer;
            transition: border-color 0.3s ease;
        }
        .filter-btn:hover {
            border-color: #007bff;
        }
        .filter-btn.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .metric-card {
            background-color: #fff;
            padding: 1.5rem;
            border-radius: 8px;
            text-align: center;
            margin-right: 1rem;
        }
        .metric-card h4 {
            color: #6c757d;
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }
        .metric-card .value {
            font-size: 1.5rem;
            font-weight: 600;
            color: #333;
        }
        .metric-card .change {
            font-size: 0.9rem;
            color: #28a745;
        }
        .chart-container {
            position: relative;
            height: 400px;
            margin-top: 2rem;
        }
        .legend-item {
            display: flex;
            align-items: center;
            margin-bottom: 0.5rem;
            font-size: 0.9rem;
        }
        .legend-item span {
            width: 12px;
            height: 12px;
            margin-right: 0.5rem;
            display: inline-block;
        }
        .color-enrollments { background-color: #6f42c1; }
        .color-intro-offers { background-color: #28a745; }
        .color-drop-ins { background-color: #007bff; }
        .color-plans { background-color: #ff6384; }
        .color-retail { background-color: #ff9f40; }
        .color-fees { background-color: #dc3545; }
        .color-courses { background-color: #6610f2; }
        .color-appointments { background-color: #fd7e14; }

        /* Responsive Adjustments */
        @media (max-width: 768px) {
            .orders-table th, .orders-table td {
                font-size: 0.85rem;
                padding: 0.75rem;
            }
            .action-buttons .btn, .action-buttons .form-select {
                margin-bottom: 0.5rem;
            }
            .orders-card .card-header {
                flex-direction: column;
                align-items: flex-start;
            }
            .orders-card .card-header h2 {
                margin-bottom: 1rem;
            }
            .filter-section {
                flex-direction: column;
                gap: 1rem;
            }
            .metric-card {
                margin-bottom: 1rem;
                margin-right: 0;
            }
            .chart-container {
                height: 300px;
            }
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0 sidebar">
                <div class="d-flex flex-column p-3">
                    <h4 class="mb-4 text-white">Admin Dashboard</h4>
                    <ul class="nav nav-pills flex-column mb-auto">
                        <li class="nav-item">
                            <a href="#productTypes" class="nav-link ${activeTab == null || activeTab != 'orders' && activeTab != 'revenue' ? 'active' : ''}" data-bs-toggle="tab">
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
                            <a href="#orders" class="nav-link ${activeTab == 'orders' ? 'active' : ''}" data-bs-toggle="tab">
                                <i class="fas fa-shopping-cart me-2"></i>Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="#revenue" class="nav-link ${activeTab == 'revenue' ? 'active' : ''}" data-bs-toggle="tab">
                                <i class="fas fa-chart-bar me-2"></i>Revenue
                            </a>
                        </li>
                    </ul>
                    <hr class="text-white">
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
                    <!-- Product Types Tab (Unchanged) -->
                    <div class="tab-pane fade ${activeTab == null || activeTab != 'orders' && activeTab != 'revenue' ? 'show active' : ''}" id="productTypes">
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

                    <!-- Products Tab (Unchanged) -->
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

                    <!-- Users Tab (Unchanged) -->
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

                    <!-- Staff Tab (Unchanged) -->
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

                    <!-- Orders Tab (Unchanged) -->
                    <div class="tab-pane fade ${activeTab == 'orders' ? 'show active' : ''}" id="orders">
                        <div class="card orders-card">
                            <div class="card-header">
                                <h2 class="mb-0">Orders Management</h2>
                                <form action="AdminController" method="get" class="filter-form d-flex align-items-center">
                                    <input type="hidden" name="action" value="filterOrders">
                                    <label for="statusFilter" class="me-2 text-white">Filter by Status:</label>
                                    <select id="statusFilter" name="status" class="form-select me-2" style="width: auto;" onchange="this.form.submit()">
                                        <option value="All" ${selectedStatus == 'All' || empty selectedStatus ? 'selected' : ''}>All</option>
                                        <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="Done" ${selectedStatus == 'Done' ? 'selected' : ''}>Done</option>
                                        <option value="Cancel" ${selectedStatus == 'Cancel' ? 'selected' : ''}>Cancel</option>
                                    </select>
                                </form>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table orders-table mb-0">
                                        <thead>
                                            <tr>
                                                <th>Order ID</th>
                                                <th>Customer ID</th>
                                                <th>Order Date</th>
                                                <th>Status</th>
                                                <th>Total Amount</th>
                                                <th>Final Amount</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${orders}" var="order">
                                                <tr>
                                                    <td>${order.id}</td>
                                                    <td>${order.cusId}</td>
                                                    <td>${order.orderDate}</td>
                                                    <td>
                                                        <span class="status-badge status-${order.status.toLowerCase()}">
                                                            ${order.status}
                                                        </span>
                                                    </td>
                                                    <td>$${order.totalAmount}</td>
                                                    <td>$${order.finalAmount}</td>
                                                    <td class="action-buttons">
                                                        <a href="AdminController?action=viewOrderDetails&id=${order.id}" class="btn btn-sm btn-info" data-bs-toggle="tooltip" title="View Order Details">
                                                            <i class="fas fa-eye"></i> View
                                                        </a>
                                                        <select class="form-select form-select-sm" onchange="updateOrderStatus(${order.id}, this.value, '${selectedStatus}', this)">
                                                            <option value="" disabled selected>Change Status</option>
                                                            <option value="Pending">Pending</option>
                                                            <option value="Done">Done</option>
                                                            <option value="Cancel">Cancel</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Revenue Tab (New) -->
                    <div class="tab-pane fade ${activeTab == 'revenue' ? 'show active' : ''}" id="revenue">
                        <div class="revenue-card">
                            <div class="card-header">Manage Revenue</div>
                            <div class="card-body">
                                <!-- Filter Section -->
                                <div class="filter-section d-flex justify-content-between align-items-center">
                                    <div class="d-flex gap-2">
                                        <div class="dropdown">
                                            <button class="filter-btn dropdown-toggle" type="button" id="timeFrameDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                                Time Frame <span class="ms-2">${param.timeFrame != null ? param.timeFrame : 'Last Month (Jan 1 - ...)'}</span>
                                            </button>
                                            <ul class="dropdown-menu" aria-labelledby="timeFrameDropdown">
                                                <li><a class="dropdown-item" href="?action=viewRevenue&timeFrame=Last Month">Last Month</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&timeFrame=Last Week">Last Week</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&timeFrame=This Month">This Month</a></li>
                                            </ul>
                                        </div>
                                        <div class="dropdown">
                                            <button class="filter-btn dropdown-toggle" type="button" id="locationDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                                Location <span class="ms-2">${param.location != null ? param.location : 'Coast'}</span>
                                            </button>
                                            <ul class="dropdown-menu" aria-labelledby="locationDropdown">
                                                <li><a class="dropdown-item" href="?action=viewRevenue&location=Coast">Coast</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&location=Inland">Inland</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&location=All">All</a></li>
                                            </ul>
                                        </div>
                                        <div class="dropdown">
                                            <button class="filter-btn dropdown-toggle" type="button" id="paymentMethodDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                                Payment Method <span class="ms-2">${param.paymentMethod != null ? param.paymentMethod : 'All'}</span>
                                            </button>
                                            <ul class="dropdown-menu" aria-labelledby="paymentMethodDropdown">
                                                <li><a class="dropdown-item" href="?action=viewRevenue&paymentMethod=All">All</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&paymentMethod=Credit Card">Credit Card</a></li>
                                                <li><a class="dropdown-item" href="?action=viewRevenue&paymentMethod=PayPal">PayPal</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="dropdown">
                                        <button class="filter-btn dropdown-toggle" type="button" id="revenueTypeDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                            ${param.revenueType != null ? param.revenueType : 'Appointments'}
                                        </button>
                                        <ul class="dropdown-menu" aria-labelledby="revenueTypeDropdown">
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Appointments">Appointments</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Courses">Courses</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Drop-Ins">Drop-Ins</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Enrollments">Enrollments</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Fees">Fees</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Intro Offers">Intro Offers</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Plans">Plans</a></li>
                                            <li><a class="dropdown-item" href="?action=viewRevenue&revenueType=Retail">Retail</a></li>
                                        </ul>
                                    </div>
                                </div>

                                <!-- Metrics Section -->
                                <div class="d-flex justify-content-between mt-4">
                                    <div class="metric-card flex-fill">
                                        <h4>Total</h4>
                                        <div class="value">${totalRevenue != null ? String.format('%,.2f', totalRevenue) : '0.00'}</div>
                                        <div class="change">${totalRevenueChange != null ? totalRevenueChange : '0.00'}%</div>
                                    </div>
                                    <div class="metric-card flex-fill">
                                        <h4>Total Today</h4>
                                        <div class="value">${totalToday != null ? String.format('%,.2f', totalToday) : '0.00'}</div>
                                        <div class="change">${totalTodayChange != null ? totalTodayChange : '0.00'}%</div>
                                    </div>
                                    <div class="metric-card flex-fill">
                                        <h4>Average per day</h4>
                                        <div class="value">${averagePerDay != null ? String.format('%,.2f', averagePerDay) : '0.00'}</div>
                                        <div class="change">0.00%</div>
                                    </div>
                                </div>

                                <!-- Chart Section -->
                                <div class="chart-container">
                                    <canvas id="revenueChart"></canvas>
                                </div>
                                <div class="mt-3">
                                    <c:forEach var="type" items="${revenueTypes}">
                                        <div class="legend-item">
                                            <span class="color-${type}"></span>
                                            <span>${type}</span>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- View Revenue List Button -->
                                <a href="AdminController?action=viewRevenueList" class="btn btn-primary mt-4">View Revenue List</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript for Order Status Update, Tab Activation, and Revenue Chart -->
    <script>
        function updateOrderStatus(orderId, status, currentStatus, selectElement) {
            if (confirm('Are you sure you want to update the status to ' + status + '?')) {
                selectElement.disabled = true;
                window.location.href = 'AdminController?action=updateOrderStatus&id=' + orderId + 
                                      '&status=' + status + '¤tStatus=' + encodeURIComponent(currentStatus);
            } else {
                selectElement.value = '';
            }
        }

        // Ensure active tab on page load
        document.addEventListener('DOMContentLoaded', function() {
            const activeTab = '${activeTab}';
            if (activeTab) {
                const tab = document.querySelector('a[href="#' + activeTab + '"]');
                const pane = document.querySelector('#' + activeTab);
                if (tab && pane) {
                    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
                    document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('show', 'active'));
                    tab.classList.add('active');
                    pane.classList.add('show', 'active');
                }
            }
            // Initialize tooltips
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });

        // Revenue Chart
        const ctx = document.getElementById('revenueChart').getContext('2d');
        const revenueChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ${revenueData != null ? revenueData.stream().map(d -> "'" + d.date + "'").collect(Collectors.joining(",")) : "[]"},
                datasets: [
                    { label: 'Enrollments', data: ${revenueData != null ? revenueData.stream().map(d -> d.enrollments).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#6f42c1' },
                    { label: 'Intro Offers', data: ${revenueData != null ? revenueData.stream().map(d -> d.introOffers).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#28a745' },
                    { label: 'Drop-Ins', data: ${revenueData != null ? revenueData.stream().map(d -> d.dropIns).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#007bff' },
                    { label: 'Plans', data: ${revenueData != null ? revenueData.stream().map(d -> d.plans).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#ff6384' },
                    { label: 'Retail', data: ${revenueData != null ? revenueData.stream().map(d -> d.retail).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#ff9f40' },
                    { label: 'Fees', data: ${revenueData != null ? revenueData.stream().map(d -> d.fees).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#dc3545' },
                    { label: 'Courses', data: ${revenueData != null ? revenueData.stream().map(d -> d.courses).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#6610f2' },
                    { label: 'Appointments', data: ${revenueData != null ? revenueData.stream().map(d -> d.appointments).collect(Collectors.joining(",")) : "[]"}, backgroundColor: '#fd7e14' }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: { stacked: true },
                    y: { stacked: true, beginAtZero: true, title: { display: true, text: 'Revenue ($)' } }
                },
                plugins: {
                    legend: { display: false }
                }
            }
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ScriptAdminDashboard.js"></script>
</body>
</html>