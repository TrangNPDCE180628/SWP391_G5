/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener('DOMContentLoaded', function () {
    const paymentMethods = document.querySelectorAll('.payment-method');

    // Highlight selected payment method
    function toggleCardFields() {
        const selectedMethod = document.querySelector('input[name=paymentMethod]:checked').value;

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
    const shippingAddress = document.getElementById('shippingAddress').value.trim();
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

    if (shippingAddress.length < 10) {
        showToast('Please enter detailed delivery address (at least 10 characters)');
        document.getElementById('shippingAddress').focus();
        return false;
    }

    // Change form action dynamically
    const form = document.querySelector('form[onsubmit="return validateForm()"]');
    if (paymentMethod === "vnpay") {
        form.action = `${window.location.origin}${pageContext.request.contextPath}/ajaxServlet`; // đúng servlet
    } else {
        form.action = `${window.location.origin}${pageContext.request.contextPath}/PaymentController`;
    }

    return true;
}

let isPaymentConfirmed = false;

document.querySelector('form[onsubmit="return validateForm()"]')
        .addEventListener('submit', function () {
            isPaymentConfirmed = true;
        });

window.addEventListener('beforeunload', function (event) {
    if (!isPaymentConfirmed) {
        const orderIdInput = document.querySelector('input[name="orderId"]');
        if (!orderIdInput)
            return;

        const orderId = orderIdInput.value;
        const data = new FormData();
        data.append("action", "cancel");
        data.append("orderId", orderId);

        navigator.sendBeacon(
                `${window.location.origin}${pageContext.request.contextPath}/PaymentController`,
                new URLSearchParams(data)
                );
    }
});
