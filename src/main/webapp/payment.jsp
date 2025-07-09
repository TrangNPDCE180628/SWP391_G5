<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán đơn hàng</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card {
                border: none;
                border-radius: 10px;
                box-shadow: 0 0 20px rgba(0,0,0,0.1);
            }
            .payment-method {
                transition: all 0.3s ease;
                border: 2px solid #e9ecef;
                border-radius: 8px;
                padding: 15px;
                margin: 5px 0;
                cursor: pointer;
            }
            .payment-method:hover {
                border-color: #007bff;
                background-color: #f8f9ff;
            }
            .payment-method.active {
                border-color: #007bff;
                background-color: #f8f9ff;
            }
            .payment-method input[type="radio"]:checked + label {
                color: #007bff;
                font-weight: bold;
            }
            .final-amount {
                font-size: 1.2em;
                font-weight: bold;
                color: #dc3545;
            }
        </style>
    </head>
    <body>
        <div class="container py-4">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <h2 class="mb-4 text-center">
                        <i class="fas fa-credit-card me-2"></i>Thanh toán đơn hàng
                    </h2>

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
                            Không tìm thấy thông tin đơn hàng. Vui lòng kiểm tra giỏ hàng hoặc liên hệ hỗ trợ.
                            <a href="HomeController" class="btn btn-primary ms-2">Quay về trang chủ</a>
                            <a href="CartController?action=view" class="btn btn-secondary ms-2">Xem giỏ hàng</a>
                        </div>
                    </c:if>

                    <c:if test="${not empty order}">
                        <!-- Order Information -->
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <h5 class="mb-0">
                                    <i class="fas fa-receipt me-2"></i>Thông tin đơn hàng
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>Mã đơn hàng:</strong> #${order.orderId}</p>
                                        <p><strong>Ngày đặt:</strong> 
                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </p>
                                        <p><strong>Trạng thái:</strong>
                                            <c:choose>
                                                <c:when test="${order.orderStatus == 'pending'}">
                                                    <span class="badge bg-warning text-dark">Chờ thanh toán</span>
                                                </c:when>
                                                <c:when test="${order.orderStatus == 'paid'}">
                                                    <span class="badge bg-success">Đã thanh toán</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${order.orderStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Tổng tiền hàng:</strong>
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol=""/> ₫
                                        </p>
                                        <p><strong>Giảm giá:</strong>
                                            <fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol=""/> ₫
                                        </p>
                                        <p><strong>Thành tiền:</strong>
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
                                    <i class="fas fa-money-bill-wave me-2"></i>Thông tin thanh toán
                                </h5>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/PaymentController" method="post" onsubmit="return validateForm()">
                                    <input type="hidden" name="action" value="confirm"/>
                                    
                                    <!-- Payment Method Selection -->
                                    <div class="mb-4">
                                        <label class="form-label fw-bold">Chọn phương thức thanh toán:</label>

                                        <div class="payment-method" onclick="selectPaymentMethod('creditcard')">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="creditcard" value="creditcard" checked>
                                            <label class="form-check-label ms-2" for="creditcard">
                                                <i class="fas fa-credit-card me-2"></i>Thẻ tín dụng/Thẻ ghi nợ
                                            </label>
                                        </div>

                                        <div class="payment-method" onclick="selectPaymentMethod('paypal')">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="paypal" value="paypal">
                                            <label class="form-check-label ms-2" for="paypal">
                                                <i class="fab fa-paypal me-2"></i>PayPal
                                            </label>
                                        </div>

                                        <div class="payment-method" onclick="selectPaymentMethod('momo')">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="momo" value="momo">
                                            <label class="form-check-label ms-2" for="momo">
                                                <i class="fas fa-mobile-alt me-2"></i>Ví MoMo
                                            </label>
                                        </div>
                                    </div>

                                    <!-- Credit Card Information -->
                                    <div id="creditCardFields" class="mb-4">
                                        <label for="cardNumber" class="form-label fw-bold">Số thẻ tín dụng <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="cardNumber" name="cardNumber" 
                                               maxlength="19" placeholder="XXXX XXXX XXXX XXXX" 
                                               pattern="[0-9\s]{8,19}" title="Vui lòng nhập 8-16 chữ số">
                                        <div class="form-text">Nhập từ 8-16 chữ số</div>

                                        <div class="row mt-3">
                                            <div class="col-md-6">
                                                <label for="expiryDate" class="form-label fw-bold">Ngày hết hạn <span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" id="expiryDate" name="expiryDate" 
                                                       placeholder="MM/YY" maxlength="5" pattern="(0[1-9]|1[0-2])\/[0-9]{2}">
                                                <div class="form-text">Định dạng: MM/YY</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="cvv" class="form-label fw-bold">Mã CVV <span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" id="cvv" name="cvv" 
                                                       maxlength="4" placeholder="XXX" pattern="[0-9]{3,4}">
                                                <div class="form-text">3 hoặc 4 chữ số ở mặt sau thẻ</div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Shipping Address -->
                                    <div class="mb-4">
                                        <label for="shippingAddress" class="form-label fw-bold">Địa chỉ giao hàng <span class="text-danger">*</span></label>
                                        <textarea class="form-control" id="shippingAddress" name="shippingAddress" 
                                                  rows="3" required placeholder="Nhập địa chỉ giao hàng chi tiết (số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố)">${order.shippingAddress}</textarea>
                                        <div class="form-text">Vui lòng nhập địa chỉ giao hàng chi tiết</div>
                                    </div>

                                    <!-- Order Summary -->
                                    <div class="card bg-light mb-4">
                                        <div class="card-body">
                                            <h6 class="card-title">Tóm tắt đơn hàng</h6>
                                            <div class="d-flex justify-content-between mb-2">
                                                <span>Tổng tiền hàng:</span>
                                                <span><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol=""/> ₫</span>
                                            </div>
                                            <div class="d-flex justify-content-between mb-2">
                                                <span>Giảm giá:</span>
                                                <span class="text-success">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol=""/> ₫</span>
                                            </div>
                                            <hr>
                                            <div class="d-flex justify-content-between">
                                                <strong>Tổng cộng:</strong>
                                                <strong class="text-danger">
                                                    <fmt:formatNumber value="${order.totalAmount - order.discountAmount}" type="currency" currencySymbol=""/> ₫
                                                </strong>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Action Buttons -->
                                    <div class="d-flex justify-content-between">
                                        <a href="${pageContext.request.contextPath}/CartController?action=view" class="btn btn-secondary">
                                            <i class="fas fa-arrow-left me-2"></i>Quay lại giỏ hàng
                                        </a>
                                        <button type="submit" class="btn btn-success btn-lg">
                                            <i class="fas fa-check me-2"></i>Xác nhận thanh toán
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                            document.addEventListener('DOMContentLoaded', function () {
                                                const creditCardFields = document.getElementById('creditCardFields');
                                                const cardNumberInput = document.getElementById('cardNumber');
                                                const expiryDateInput = document.getElementById('expiryDate');
                                                const cvvInput = document.getElementById('cvv');
                                                const paymentMethods = document.querySelectorAll('.payment-method');

                                                // Format card number input
                                                cardNumberInput.addEventListener('input', function (e) {
                                                    let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
                                                    let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
                                                    if (formattedValue.length <= 19) {
                                                        e.target.value = formattedValue;
                                                    }
                                                });

                                                // Format expiry date input
                                                expiryDateInput.addEventListener('input', function (e) {
                                                    let value = e.target.value.replace(/[^0-9]/g, '');
                                                    if (value.length > 2) {
                                                        e.target.value = value.slice(0, 2) + '/' + value.slice(2, 4);
                                                    } else {
                                                        e.target.value = value;
                                                    }
                                                });

                                                // Restrict CVV to numbers only
                                                cvvInput.addEventListener('input', function (e) {
                                                    e.target.value = e.target.value.replace(/[^0-9]/g, '').slice(0, 4);
                                                });

                                                // Toggle credit card fields based on payment method
                                                function toggleCardFields() {
                                                    const selectedMethod = document.querySelector('input[name=paymentMethod]:checked').value;
                                                    const isCreditCard = selectedMethod === 'creditcard';

                                                    creditCardFields.style.display = isCreditCard ? 'block' : 'none';
                                                    cardNumberInput.required = isCreditCard;
                                                    expiryDateInput.required = isCreditCard;
                                                    cvvInput.required = isCreditCard;

                                                    paymentMethods.forEach(method => {
                                                        method.classList.remove('active');
                                                    });

                                                    const activeMethod = document.querySelector('input[name=paymentMethod]:checked').closest('.payment-method');
                                                    if (activeMethod) {
                                                        activeMethod.classList.add('active');
                                                    }
                                                }

                                                document.querySelectorAll('input[name=paymentMethod]').forEach(radio => {
                                                    radio.addEventListener('change', toggleCardFields);
                                                });

                                                toggleCardFields();
                                            });

                                            function selectPaymentMethod(method) {
                                                document.getElementById(method).checked = true;
                                                document.getElementById(method).dispatchEvent(new Event('change'));
                                            }

                                            function validateForm() {
                                                const paymentMethod = document.querySelector('input[name=paymentMethod]:checked').value;
                                                const cardNumber = document.getElementById('cardNumber').value;
                                                const expiryDate = document.getElementById('expiryDate').value;
                                                const cvv = document.getElementById('cvv').value;
                                                const shippingAddress = document.getElementById('shippingAddress').value;

                                                // Validate credit card fields
                                                if (paymentMethod === 'creditcard') {
                                                    const cleanCardNumber = cardNumber.replace(/\s+/g, '');
                                                    if (cleanCardNumber.length < 8 || cleanCardNumber.length > 16) {
                                                        alert('Số thẻ tín dụng phải có từ 8-16 chữ số');
                                                        document.getElementById('cardNumber').focus();
                                                        return false;
                                                    }
                                                    if (!/^\d+$/.test(cleanCardNumber)) {
                                                        alert('Số thẻ tín dụng chỉ được chứa chữ số');
                                                        document.getElementById('cardNumber').focus();
                                                        return false;
                                                    }

                                                    if (!/^(0[1-9]|1[0-2])\/[0-9]{2}$/.test(expiryDate)) {
                                                        alert('Ngày hết hạn không hợp lệ. Vui lòng nhập theo định dạng MM/YY');
                                                        document.getElementById('expiryDate').focus();
                                                        return false;
                                                    }

                                                    if (!/^\d{3,4}$/.test(cvv)) {
                                                        alert('Mã CVV phải có 3 hoặc 4 chữ số');
                                                        document.getElementById('cvv').focus();
                                                        return false;
                                                    }
                                                }

                                                // Validate shipping address
                                                if (shippingAddress.trim().length < 10) {
                                                    alert('Vui lòng nhập địa chỉ giao hàng chi tiết (ít nhất 10 ký tự)');
                                                    document.getElementById('shippingAddress').focus();
                                                    return false;
                                                }

                                                return true;
                                            }
        </script>
    </body>
</html>