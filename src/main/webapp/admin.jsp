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
                                <a href="#vouchers" class="nav-link" data-bs-toggle="tab">
                                    <i class="fa-solid fa-ticket me-2"></i>Voucher
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

        
        <script>const contextPath = '${pageContext.request.contextPath}';</script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>