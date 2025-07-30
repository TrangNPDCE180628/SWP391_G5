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
    const receiverName = document.getElementById('receiverName');
    const receiverPhone = document.getElementById('receiverPhone');
    const shippingAddress = document.getElementById('shippingAddress');
    const paymentMethodRadios = document.querySelectorAll('input[name="paymentMethod"]');

    let isValid = true;

    // Validate Receiver's Name
    if (receiverName.value.trim() === '') {
        alert('Receiver\'s Name cannot be empty.');
        receiverName.focus();
        isValid = false;
        return false;
    }

    // Validate Receiver's Phone
    const phonePattern = /^0\d{9,14}$/; // Regex for phone number starting with 0 and 10-15 digits
    if (receiverPhone.value.trim() === '') {
        alert('Receiver\'s Phone cannot be empty.');
        receiverPhone.focus();
        isValid = false;
        return false;
    } else if (!phonePattern.test(receiverPhone.value.trim())) {
        alert('Invalid phone number format. It must start with 0 and contain 10-15 digits.');
        receiverPhone.focus();
        isValid = false;
        return false;
    }

    // Validate Shipping Address
    if (shippingAddress.value.trim() === '') {
        alert('Shipping address cannot be empty.');
        shippingAddress.focus();
        isValid = false;
        return false;
    }

    // Validate Payment Method (at least one must be selected)
    let paymentMethodSelected = false;
    for (const radio of paymentMethodRadios) {
        if (radio.checked) {
            paymentMethodSelected = true;
            break;
        }
    }

    if (!paymentMethodSelected) {
        alert('Please select a payment method.');
        isValid = false;
        return false;
    }

    return isValid; // Return true if all validations pass
}

// Function to handle payment method selection (from your existing HTML)
function selectPaymentMethod(methodId) {
    document.getElementById(methodId).checked = true;
}
// Function to handle payment method selection (from your existing HTML)
function selectPaymentMethod(methodId) {
    document.getElementById(methodId).checked = true;
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
