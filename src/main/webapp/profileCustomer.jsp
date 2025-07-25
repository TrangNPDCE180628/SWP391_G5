<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Models.Customer" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Personal Information</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .navbar {
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
            }
            .navbar-brand {
                font-weight: 600;
                color: #333;
            }
            .search-form {
                width: 300px;
            }
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
    <body>

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
                            <a class="nav-link position-relative" href="CartController?action=view" title="View Cart">
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
                                        <li><a class="dropdown-item" href="OrderHistoryController">My Orders</a></li>
                                        <c:if test="${not empty sessionScope.LOGIN_USER.role and (sessionScope.LOGIN_USER.role eq 'Admin' or sessionScope.LOGIN_USER.role eq 'Staff')}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                        </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutController">
                                                <i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
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
            <h3 class="text-center mb-4">Personal Information</h3>

            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Profile Information Form (without file upload) -->
            <form id="profileForm" action="ProfileCustomerController" method="post">
                <input type="hidden" name="cusId" value="${customer.cusId}" />
                <input type="hidden" name="action" value="updateProfile" />
                <input type="hidden" name="existingImage" value="${customer.cusImage}" />

                <div class="text-center">
                    <img src="${customer.cusImage}" alt="Avatar" class="profile-img">
                </div>

                <div class="mb-3 mt-3">
                    <label>Username:</label>
                    <input type="text" class="form-control" value="${customer.username}" disabled>
                </div>

                <div class="mb-3">
                    <label>Full Name:</label>
                    <input type="text" class="form-control" name="cusFullName" value="${customer.cusFullName}" required>
                </div>

                <div class="mb-3">
                    <label>Gender:</label>
                    <select class="form-control" name="cusGender">
                        <option value="Male" ${customer.cusGender == 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${customer.cusGender == 'Female' ? 'selected' : ''}>Female</option>
                        <option value="Other" ${customer.cusGender == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label>Email:</label>
                    <input type="email" class="form-control" name="cusGmail" value="${customer.cusGmail}" required>
                </div>

                <div class="mb-3">
                    <label>Phone Number:</label>
                    <input type="text" class="form-control" name="cusPhone" value="${customer.cusPhone}" required>
                </div>

                <div class="mb-3">
                    <label>Password:</label>
                    <input type="password" class="form-control" name="cusPassword" value="${customer.cusPassword}" disabled>
                </div>
                <div class="text-center mb-3">
                    <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                        Change Password
                    </button>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary px-4">Update Profile</button>
                </div>
            </form>

            <!-- Separate File Upload Form -->
            <div class="text-center mt-4">
                <h5>Change Profile Picture</h5>
                <form id="imageForm" action="ProfileCustomerController" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="cusId" value="${customer.cusId}" />
                    <input type="hidden" name="action" value="updateImage" />
                    <input type="hidden" name="existingImage" value="${customer.cusImage}" />
                    
                    <div class="mt-2">
                        <label class="form-label">Upload New Photo:</label>
                        <input type="file" class="form-control" name="imageFile" accept="image/*">
                    </div>
                    <div class="mt-3">
                        <button type="submit" class="btn btn-outline-primary">Upload Image</button>
                    </div>
                </form>
            </div>

                <!-- Change Password Modal -->
                <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="changePasswordModalLabel">Change Password</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label>Old Password:</label>
                                    <input type="password" class="form-control" id="oldPassword" name="oldPassword">
                                </div>
                                <div class="mb-3">
                                    <label>New Password:</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword">
                                </div>
                                <div class="mb-3">
                                    <label>Confirm New Password:</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" class="btn btn-primary" onclick="changePassword()">Confirm</button>
                            </div>
                        </div>
                    </div>
                </div>


            </form>
        </div>

        <!-- Footer -->
        <footer class="bg-dark text-light py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h5><i class="fas fa-microchip me-2"></i>Tech Store</h5>
                        <p>Your ultimate destination for quality tech products.</p>
                    </div>
                    <div class="col-md-4">
                        <h5>Quick Links</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>About Us</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>Contact</a></li>
                            <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>FAQs</a></li>
                        </ul>
                    </div>
                    <div class="col-md-4">
                        <h5>Connect With Us</h5>
                        <div class="social-links">
                            <a href="#" class="text-light me-3"><i class="fab fa-facebook fa-lg"></i></a>
                            <a href="#" class="text-light me-3"><i class="fab fa-instagram fa-lg"></i></a>
                            <a href="#" class="text-light me-3"><i class="fab fa-twitter fa-lg"></i></a>
                        </div>
                    </div>
                </div>
                <hr class="mt-4">
                <div class="text-center">
                    <small>© 2025 Tech Store. All rights reserved.</small>
                </div>
            </div>
        </footer>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function submitForm() {
                document.getElementById("profileForm").submit();
            }
            
            function changePassword() {
                const oldPassword = document.getElementById("oldPassword").value;
                const newPassword = document.getElementById("newPassword").value;
                const confirmPassword = document.getElementById("confirmPassword").value;
                
                // Validation
                if (!oldPassword || !newPassword || !confirmPassword) {
                    alert("Please fill in all password fields.");
                    return;
                }
                
                if (newPassword !== confirmPassword) {
                    alert("New password and confirm password do not match.");
                    return;
                }
                
                if (newPassword.length < 6) {
                    alert("New password must be at least 6 characters long.");
                    return;
                }
                
                // Clear modal fields
                document.getElementById("oldPassword").value = "";
                document.getElementById("newPassword").value = "";
                document.getElementById("confirmPassword").value = "";
                
                // Create a new form for password change
                const passwordForm = document.createElement("form");
                passwordForm.method = "post";
                passwordForm.action = "ProfileCustomerController";
                passwordForm.style.display = "none";
                
                // Add action field
                const actionField = document.createElement("input");
                actionField.type = "hidden";
                actionField.name = "action";
                actionField.value = "changePassword";
                passwordForm.appendChild(actionField);
                
                // Add old password field
                const oldPasswordField = document.createElement("input");
                oldPasswordField.type = "hidden";
                oldPasswordField.name = "oldPasswordHidden";
                oldPasswordField.value = oldPassword;
                passwordForm.appendChild(oldPasswordField);
                
                // Add new password field
                const newPasswordField = document.createElement("input");
                newPasswordField.type = "hidden";
                newPasswordField.name = "newPasswordHidden";
                newPasswordField.value = newPassword;
                passwordForm.appendChild(newPasswordField);
                
                // Add form to body and submit
                document.body.appendChild(passwordForm);
                
                // Close modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('changePasswordModal'));
                if (modal) {
                    modal.hide();
                }
                
                // Submit the password form
                passwordForm.submit();
            }
        </script>

    </body>
</html>
