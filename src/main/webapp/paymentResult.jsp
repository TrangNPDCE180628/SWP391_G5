<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Payment Result</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" 
              integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" 
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="css/payment.css" rel="stylesheet" />
        <style>
            .progress-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                flex-wrap: nowrap;
            }

            .step {
                flex: 1;
                text-align: center;
                position: relative;
            }

            .circle {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                background-color: #dee2e6;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                margin: 0 auto;
                z-index: 1;
            }

            .circle.active {
                background-color: #00C78C;
                color: white;
            }

            .line {
                position: absolute;
                top: 18px;
                left: 50%;
                width: 100%;
                height: 2px;
                background-color: #00C78C;
                z-index: 0;
            }

            .step:not(:last-child)::after {
                content: '';
                position: absolute;
                top: 18px;
                left: 50%;
                width: 100%;
                height: 2px;
                background-color: #00C78C;
                z-index: -1;
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
                                            <a class="nav-link" href="OrderHistoryController">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin' or sessionScope.LOGIN_USER.role eq 'Staff'}">
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

        <!-- MAIN CONTENT -->
        <div class="container">

            <!-- Back button -->
            <div class="pt-3">
                <a href="HomeController" class="text-dark text-decoration-none">
                    <i class="fas fa-arrow-left fa-lg"></i>
                </a>
            </div>
            <!-- Progress Bar -->
            <div class="container mt-4 mb-5">
                <div class="progress-container">
                    <div class="step">
                        <div class="circle bg-success text-white">1</div>
                        <small class="d-block mt-2">Cart</small>
                    </div>
                    <div class="step">
                        <div class="circle bg-success text-white">2</div>
                        <small class="d-block mt-2">Payment Confirmation</small>
                    </div>
                    <div class="step">
                        <!-- Thành công: màu xanh -->
                        <c:if test="${transResult}">
                            <div class="circle active">3</div>
                            <small class="d-block mt-2">Order Successful</small>
                        </c:if>
                        <!-- Thất bại: màu đỏ -->
                        <c:if test="${transResult == false}">
                            <div class="circle" style="background-color: #dc3545; color: white;">3</div>
                            <small class="d-block mt-2" style="color: #dc3545;">Failed</small>
                        </c:if>
                        <!-- Đang xử lý: màu cam -->
                        <c:if test="${transResult == null}">
                            <div class="circle" style="background-color: #fd7e14; color: white;">3</div>
                            <small class="d-block mt-2" style="color: #fd7e14;">Processing</small>
                        </c:if>
                    </div>
                </div>
            </div>
            <!-- Success Message -->
            <div class="d-flex justify-content-center align-items-center" style="min-height: 50vh;">

                <!-- Giao dịch thành công -->
                <c:if test="${transResult}">
                    <div class="text-center">
                        <div class="mb-4">
                            <div style="width: 64px; height: 64px; margin: 0 auto; background-color: #00C78C; border-radius: 50%;">
                                <i class="fas fa-check text-white" style="font-size: 32px; line-height: 64px;"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold mb-3">Thank you for your order!</h4>
                        <p class="text-muted mb-4">You will receive updates in the notification section of your inbox.</p>
                        <a href="OrderHistoryController" class="btn btn-light border px-4 py-2 fw-medium">View My Orders</a>
                    </div>
                </c:if>

                <!-- Giao dịch thất bại -->
                <c:if test="${transResult == false}">
                    <div class="text-center">
                        <div class="mb-4">
                            <div style="width: 64px; height: 64px; margin: 0 auto; background-color: #dc3545; border-radius: 50%;">
                                <i class="fas fa-times text-white" style="font-size: 32px; line-height: 64px;"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold mb-3 text-danger">Transaction Failed!</h4>
                        <p class="text-muted mb-4">Your payment could not be processed. Please try again or contact support.</p>
                        <a href="OrderHistoryController" class="btn btn-light border px-4 py-2 fw-medium">View My Orders</a>
                    </div>
                </c:if>

                <!-- Đang xử lý giao dịch -->
                <c:if test="${transResult == null}">
                    <div class="text-center">
                        <div class="mb-4">
                            <div style="width: 64px; height: 64px; margin: 0 auto; background-color: #fd7e14; border-radius: 50%;">
                                <i class="fas fa-exclamation text-white" style="font-size: 32px; line-height: 64px;"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold mb-3 text-warning">Transaction Processing</h4>
                        <p class="text-muted mb-4">Your payment is being processed. Please wait for confirmation.</p>
                        <a href="OrderHistoryController" class="btn btn-light border px-4 py-2 fw-medium">View My Orders</a>
                    </div>
                </c:if>
            </div>
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
    </body>
</html>
