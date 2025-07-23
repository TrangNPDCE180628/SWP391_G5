/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener('DOMContentLoaded', function () {
    const creditCardFields = document.getElementById('creditCardFields');
    const cardNumberInput = document.getElementById('cardNumber');
    const expiryDateInput = document.getElementById('expiryDate');
    const cvvInput = document.getElementById('cvv');
    const paymentMethods = document.querySelectorAll('.payment-method');

    // Format card number input
//    cardNumberInput.addEventListener('input', function (e) {
//        let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
//        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
//        if (formattedValue.length <= 19) {
//            e.target.value = formattedValue;
//        }
//    });

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
    const receiverName = document.getElementById('receiverName').value.trim();
    const receiverPhone = document.getElementById('receiverPhone').value.trim();

    if (receiverName.length < 2) {
        showToast('Please enter the receiver\'s name (at least 2 characters)');
        document.getElementById('receiverName').focus();
        return false;
    }

    if (!/^0\d{9,14}$/.test(receiverPhone)) {
        showToast('Please enter a valid phone number (10–15 digits, starts with 0)');
        document.getElementById('receiverPhone').focus();
        return false;
    }

    // Validate credit card fields
    if (paymentMethod === 'creditcard') {
        const cleanCardNumber = cardNumber.replace(/\s+/g, '');
        if (cleanCardNumber.length < 8 || cleanCardNumber.length > 16) {
            showToast('Credit card number must be 8-16 digits');
            document.getElementById('cardNumber').focus();
            return false;
        }
        if (!/^\d+$/.test(cleanCardNumber)) {
            showToast('Credit card numbers must contain only digits.');
            document.getElementById('cardNumber').focus();
            return false;
        }

        if (!/^(0[1-9]|1[0-2])\/[0-9]{2}$/.test(expiryDate)) {
            showToast('Expiration date is invalid. Please enter in MM/YY format');
            document.getElementById('expiryDate').focus();
            return false;
        }

        if (!/^\d{3,4}$/.test(cvv)) {
            showToast('CVV code must be 3 or 4 digits');
            document.getElementById('cvv').focus();
            return false;
        }
    }

    // Validate shipping address
    if (shippingAddress.trim().length < 10) {
        showToast('Please enter detailed delivery address (at least 10 characters)');
        document.getElementById('shippingAddress').focus();
        return false;
    }

    return true;
}
let isPaymentConfirmed = false;

// Đánh dấu khi người dùng bấm nút xác nhận thanh toán
document.querySelector('form[onsubmit="return validateForm()"]')
        .addEventListener('submit', function () {
            isPaymentConfirmed = true;
        });

// Gửi yêu cầu cancel nếu thoát trang mà không xác nhận thanh toán
window.addEventListener('beforeunload', function (event) {
    if (!isPaymentConfirmed) {
        const orderId = document.querySelector('input[name="orderId"]').value;
        const data = new FormData();
        data.append("action", "cancel");
        data.append("orderId", orderId);

        // Dùng sendBeacon để đảm bảo gửi được trước khi trang đóng
        navigator.sendBeacon(
                `${window.location.origin}${pageContext.request.contextPath}/PaymentController`,
                new URLSearchParams(data)
                );
    }
});
