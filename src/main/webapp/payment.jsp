<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Order Payment</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
        <link href="css/payment.css" rel="stylesheet" />
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
                                            <a class="nav-link" href="OrderController?action=view">
                                                My Orders
                                            </a>
                                        </li>
                                        <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                            <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                            </c:if>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="MainController?action=Logout">Logout</a></li>
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

        <div class="container py-4">

            <div class="row justify-content-center">
                <div class="col-md-8">
                    <!-- Back to home -->
                    <a href="${pageContext.request.contextPath}/HomeController" class="back-link mb-3 d-inline-flex align-items-center">
                        <i class="fas fa-arrow-left me-2"></i>Home
                    </a>

                    <!-- Progress Steps -->
                    <div class="progress-wrapper mb-4">
                        <div class="progress-steps">
                            <div class="step-line"></div>

                            <div class="step-item completed">
                                <div class="step-circle">1</div>
                                <div class="step-label">Cart</div>
                            </div>

                            <div class="step-item completed">
                                <div class="step-circle">2</div>
                                <div class="step-label">Waiting for payment</div>
                            </div>

                            <div class="step-item active">
                                <div class="step-circle">3</div>
                                <div class="step-label">Order Successful</div>
                            </div>
                        </div>
                    </div>


                    <!-- Display error messages -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Display success messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fas fa-check-circle me-2"></i>${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Check if order exists -->
                    <c:if test="${empty order}">
                        <div class="alert alert-warning text-center">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            Order information not found. Please check your cart or contact support.
                            <a href="HomeController" class="btn btn-primary ms-2">Back to home page</a>
                            <a href="CartController?action=view" class="btn btn-secondary ms-2">View cart</a>
                        </div>
                    </c:if>

                    <c:if test="${not empty order}">

                        <!-- Order Information -->
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <h5 class="mb-0">
                                    <i class="fas fa-receipt me-2"></i>Order information
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>Order code:</strong> #${order.orderId}</p>
                                        <p><strong>Date booked:</strong> 
                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </p>
                                        <p><strong>Status:</strong>
                                            <c:choose>
                                                <c:when test="${order.orderStatus == 'pending'}">
                                                    <span class="badge bg-warning text-dark">Waiting for payment</span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'completed'}">
                                                    <span class="badge bg-success">Completed</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${order.orderStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Total:</strong>
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol=""/> ₫
                                        </p>
                                        <p><strong>Discount:</strong>
                                            <fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol=""/> ₫
                                        </p>
                                        <p><strong>Total amount:</strong>
                                            <span class="final-amount">
                                                <fmt:formatNumber value="${order.totalAmount - order.discountAmount}" type="currency" currencySymbol=""/> ₫
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Payment Form -->
                        <div class="card">
                            <div class="card-header bg-success text-white">
                                <h5 class="mb-0">
                                    <i class="fas fa-money-bill-wave me-2"></i>Payment information
                                </h5>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/PaymentController" method="post" onsubmit="return validateForm()">
                                    <input type="hidden" name="action" value="confirm"/>
                                    <input type="hidden" name="orderId" value="${order.orderId}"/>

                                    <!-- Payment Method Selection -->
                                    <div class="mb-4">
                                        <label class="form-label fw-bold">Select payment method:</label>

                                        <div class="payment-method" onclick="selectPaymentMethod('vnpay')">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="vnpay">
                                            <label class="form-check-label ms-2" for="vnpay">
                                                <i class="fas fa-university me-2"></i> VNPay
                                            </label>
                                        </div>

                                        <div class="payment-method" onclick="selectPaymentMethod('upon-receipt')">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="paypal" value="upon-receipt">
                                            <label class="form-check-label ms-2" for="paypal">
                                                <i class="fab fa-paypal me-2"></i>Cash on Delivery
                                            </label>
                                        </div>
                                    </div>

                                    <!-- Receiver Name -->
                                    <div class="mb-4">
                                        <label for="receiverName" class="form-label fw-bold">Receiver's Name <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="receiverName" name="receiverName"
                                               required placeholder="Enter receiver's full name" value="${order.receiverName != null ? order.receiverName : ''}">
                                    </div>

                                    <!-- Receiver Phone -->
                                    <div class="mb-4">
                                        <label for="receiverPhone" class="form-label fw-bold">
                                            Receiver's Phone <span class="text-danger">*</span>
                                        </label>
                                        <input
                                            type="tel"
                                            class="form-control"
                                            id="receiverPhone"
                                            name="receiverPhone"
                                            required
                                            placeholder="Enter phone number"
                                            value="${order.receiverPhone != null ? order.receiverPhone : ''}"
                                            pattern="0\d{9,14}"
                                            maxlength="15"
                                            inputmode="numeric"
                                            title="Phone number must start with 0 and contain 10–15 digits"
                                            >
                                        <div class="form-text">
                                            Phone number must start with 0 and contain 10–15 digits.
                                        </div>
                                    </div>


                                    <!-- Shipping Address -->
                                    <div class="mb-4">
                                        <label for="shippingAddress" class="form-label fw-bold">Shipping address <span class="text-danger">*</span></label>
                                        <textarea class="form-control" id="shippingAddress" name="shippingAddress" 
                                                  rows="3" required placeholder="Enter detailed delivery address (house number, street, ward/commune, district, province/city)">${order.shippingAddress}</textarea>
                                        <div class="form-text">Please enter detailed shipping address</div>
                                    </div>

                                    <!-- Order Summary -->
                                    <div class="card bg-light mb-4">
                                        <div class="card-body">
                                            <h6 class="card-title">Order Summary</h6>
                                            <div class="d-flex justify-content-between mb-2">
                                                <span>Total cost of goods:</span>
                                                <span><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol=""/> ₫</span>
                                            </div>
                                            <div class="d-flex justify-content-between mb-2">
                                                <span>Discount:</span>
                                                <span class="text-success">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol=""/> ₫</span>
                                            </div>
                                            <hr>
                                            <div class="d-flex justify-content-between">
                                                <strong>Total:</strong>
                                                <strong class="text-danger">
                                                    <fmt:formatNumber value="${order.totalAmount - order.discountAmount}" type="currency" currencySymbol=""/> ₫
                                                </strong>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Action Buttons -->
                                    <div class="d-flex justify-content-end">
                                        <input type="hidden" name="bankCode" value="" />
                                        <input type="hidden" name="language" value="vn" />
                                        <input type="hidden" name="totalBill" value="${order.totalAmount - order.discountAmount}" />

                                        <button type="submit" class="btn btn-success btn-lg">
                                            <i class="fas fa-check me-2"></i>Payment Confirmation
                                        </button>
                                    </div>

                                </form>
                                <div class="d-flex justify-content-start">
                                    <form action="PaymentController" method="post">
                                        <input type="hidden" name="action" value="cancel" />
                                        <input type="hidden" name="orderId" value="${order.orderId}" />
                                        <button type="submit" class="btn btn-danger">Cancel Order</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
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
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="/js/payment.js"></script>
    </body>
</html>