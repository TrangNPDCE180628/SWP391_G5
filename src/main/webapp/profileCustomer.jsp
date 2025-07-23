<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Models.Customer" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Thông Tin Cá Nhân</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style>
            .profile-container {
                max-width: 700px;
                margin: 40px auto;
                padding: 20px;
                border-radius: 10px;
                background-color: #ffffff;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .profile-img {
                width: 150px;
                height: 150px;
                object-fit: cover;
                border-radius: 50%;
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body class="bg-light">

        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
            <div class="container py-2">
                <a class="navbar-brand" href="HomeController">
                    <i class="fas fa-microchip me-2"></i>Tech Store
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <form class="d-flex search-form mx-auto" action="HomeController" method="GET">
                        <input class="form-control me-2" type="search" name="searchTerm"
                               placeholder="Search for products..." value="${searchTerm}">
                        <button class="btn btn-outline-success" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </form>
                    <ul class="navbar-nav ms-auto">
                        <!-- Cart icon (moved up) -->
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="CartController?action=view" title="Xem giỏ hàng">
                                <i class="fas fa-shopping-cart fa-lg"></i>
                                <c:if test="${sessionScope.cartSize > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                        ${sessionScope.cartSize}
                                    </span>
                                </c:if>
                            </a>
                        </li>

                        <!-- Account dropdown -->
                        <c:choose>
                            <c:when test="${not empty sessionScope.LOGIN_USER}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                       data-bs-toggle="dropdown">
                                        <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="ProfileCustomerController">Profile</a></li>

                                        <!-- Orders (moved below Cart) -->
                                        <li class="nav-item">
                                            <a class="nav-link" href="OrderController?action=view">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutController">
                                                <i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
                                <li class="nav-item"><a class="nav-link" href="register.jsp">Register</a></li>
                                </c:otherwise>
                            </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Profile Form -->
        <div class="container profile-container">
            <h3 class="text-center mb-4">Thông Tin Cá Nhân</h3>

            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Multipart for file upload -->
            <form  id="profileForm" action="ProfileCustomerController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="cusId" value="${customer.cusId}" />

                <div class="text-center">
                    <img src="${customer.cusImage}" alt="Avatar" class="profile-img">
                </div>

                <div class="mb-3 mt-3">
                    <label>Tên đăng nhập:</label>
                    <input type="text" class="form-control" value="${customer.username}" disabled>
                </div>

                <div class="mb-3">
                    <label>Họ và tên:</label>
                    <input type="text" class="form-control" name="cusFullName" value="${customer.cusFullName}" required>
                </div>

                <div class="mb-3">
                    <label>Giới tính:</label>
                    <select class="form-control" name="cusGender">
                        <option value="Male" ${customer.cusGender == 'Male' ? 'selected' : ''}>Nam</option>
                        <option value="Female" ${customer.cusGender == 'Female' ? 'selected' : ''}>Nữ</option>
                        <option value="Other" ${customer.cusGender == 'Other' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label>Email:</label>
                    <input type="email" class="form-control" name="cusGmail" value="${customer.cusGmail}" required>
                </div>

                <div class="mb-3">
                    <label>Số điện thoại:</label>
                    <input type="text" class="form-control" name="cusPhone" value="${customer.cusPhone}" required>
                </div>

                <div class="mb-3">
                    <label>Mật khẩu:</label>
                    <input type="password" class="form-control" name="cusPassword" value="${customer.cusPassword}" disabled>
                </div>
                <div class="text-center mb-3">
                    <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                        Đổi mật khẩu
                    </button>
                </div>

                <div class="text-center">
                    <div class="mt-2">
                        <label class="form-label">Tải ảnh mới:</label>
                        <input type="file" class="form-control" name="imageFile">
                        <input type="hidden" name="existingImage" value="${customer.cusImage}">
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary px-4">Cập nhật</button>
                </div>

                <!-- Modal đổi mật khẩu -->
                <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="changePasswordModalLabel">Đổi mật khẩu</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label>Mật khẩu cũ:</label>
                                    <input type="password" class="form-control" name="oldPassword" form="profileForm">
                                </div>
                                <div class="mb-3">
                                    <label>Mật khẩu mới:</label>
                                    <input type="password" class="form-control" name="cusPassword" form="profileForm">
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                <button type="button" class="btn btn-primary" onclick="submitForm()">Xác nhận</button>
                            </div>
                        </div>
                    </div>
                </div>


            </form>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                    function submitForm() {
                                        document.getElementById("profileForm").submit();
                                    }
        </script>

    </body>
</html>
