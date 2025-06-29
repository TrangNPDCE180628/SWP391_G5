/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
// Lưu ý: bạn cần khai báo biến contextPath ở file JSP như sau:
// <script>const contextPath = '${pageContext.request.contextPath}';</script>
document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const activeTab = urlParams.get("tab");
    if (activeTab) {
        const tabTrigger = document.querySelector(`a[href="#${activeTab}"]`);
        if (tabTrigger) {
            new bootstrap.Tab(tabTrigger).show();
        }
    }
});
function editProfile(role, id) {
    const row = document.querySelector(`tr[data-user-id="${id}"][data-user-role="${role}"]`);
    if (!row)
        return;

    // Lấy dữ liệu chung từ thẻ <tr>
    const fullName = row.getAttribute('data-user-fullname') || '';
    const email = row.getAttribute('data-user-email') || '';
    const image = row.getAttribute('data-user-image') || '';

    // Set giá trị vào input
    document.getElementById('editUserRole').value = role;
    document.getElementById('editUserId').value = id;
    document.getElementById('editProfileFullName').value = fullName;
    document.getElementById('editProfileEmail').value = email;
    document.getElementById('currentProfileImagePath').value = image;

    // Set preview ảnh
    document.getElementById('previewProfileImage').src = `${contextPath}/images/${image}`;

    // Nếu là Staff thì set thêm các trường riêng
    if (role === 'Staff') {
        document.getElementById('editProfileGender').value = row.getAttribute('data-user-gender') || '';
        document.getElementById('editProfilePhone').value = row.getAttribute('data-user-phone') || '';
        document.getElementById('editProfilePosition').value = row.getAttribute('data-user-position') || '';
        document.getElementById('staffFields').style.display = 'block';
    } else {
        document.getElementById('staffFields').style.display = 'none';
    }

    // Hiển thị modal
    const modal = new bootstrap.Modal(document.getElementById('editProfileModal'));
    modal.show();
}

// Xử lý xem trước ảnh mới khi người dùng chọn ảnh mới
document.addEventListener('DOMContentLoaded', function () {
    const imageInput = document.getElementById('editProfileImage');
    const previewImg = document.getElementById('previewProfileImage');

    if (imageInput && previewImg) {
        imageInput.addEventListener('change', function (e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (evt) {
                    previewImg.src = evt.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }
});

// Product functions
function editProduct(productId) {

    const row = document.querySelector(`tr[data-product-id="${productId}"]`);
    if (!row)
        return;

    const name = row.getAttribute('data-product-name') || '';
    const description = row.getAttribute('data-product-description') || '';
    const price = row.getAttribute('data-product-price') || '';
    const quantity = row.getAttribute('data-product-quantity') || '';
    const type = row.getAttribute('data-product-type-id') || '';
    const image = row.getAttribute('data-product-image') || '';

    // Set giá trị vào form
    document.getElementById('editProductId').value = productId;
    document.getElementById('editProductName').value = name;
    document.getElementById('editProductDescription').value = description;
    document.getElementById('editProductPrice').value = price;
    document.getElementById('editProductQuantity').value = quantity;
    document.getElementById('editProductType').value = type;
    document.getElementById('currentImagePath').value = image;

    // Hiển thị ảnh hiện tại
    const imgPreview = document.getElementById('currentProductImage');
    imgPreview.src = '/images/products/' + image;
    imgPreview.style.display = 'block';

    // Xóa preview ảnh mới nếu có
    const fileInput = document.getElementById('editProductImage');
    fileInput.value = ''; // reset input
    fileInput.onchange = function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                imgPreview.src = e.target.result; // preview ảnh mới
            };
            reader.readAsDataURL(file);
        } else {
            // Nếu bỏ chọn ảnh, thì trả lại ảnh gốc
            imgPreview.src = '/images/products/' + image;
        }
    };

    new bootstrap.Modal(document.getElementById('editProductModal')).show();
}


function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        window.location.href = '/AdminController?action=deleteProduct&id=' + id;
    }
}

// User functions
function editUser(userId) {
    const row = document.querySelector(`tr[data-user-id="${userId}"]`);
    if (row) {
        // Get attributes safely
        const username = row.getAttribute('data-username') || '';
        const fullname = row.getAttribute('data-fullname') || '';
        const role = (row.getAttribute('data-role') || '').toLowerCase();  // tránh null.toLowerCase()
        const password = row.getAttribute('data-password') || '';
        // Gán vào input
        document.getElementById('editUserId').value = userId;
        document.getElementById('displayUsername').value = username;
        document.getElementById('editFullname').value = fullname;
        document.getElementById('editRole').value = role;
        document.getElementById('editPassword').value = password;

        console.log('User data:', {
            id: userId,
            username,
            fullname,
            role,
            password
        });
    } else {
        console.warn(`User row with ID ${userId} not found.`);
    }

    new bootstrap.Modal(document.getElementById('editUserModal')).show();
}

/*  function editUser(userId) {
 const row = document.querySelector(`tr[data-user-id="${userId}"]`);
 if (row) {
 // Set values directly in function call
 const username = row.getAttribute('data-username');
 const fullname = row.getAttribute('data-fullname');
 const role = row.getAttribute('data-role');
 
 document.getElementById('editUserId').value = userId;
 document.getElementById('displayUsername').value = username;
 document.getElementById('editFullname').value = fullname;
 document.getElementById('editRole').value = role.toLowerCase();
 
 console.log('User data:', {
 id: userId,
 username: username,
 fullname: fullname,
 role: role
 });
 }
 new bootstrap.Modal(document.getElementById('editUserModal')).show();
 }**/


function deleteUser(id) {
    if (confirm('Are you sure you want to delete this user?')) {
        window.location.href = '/AdminController?action=deleteUser&id=' + id;
    }
}

function editProductType(id, name) {
    document.getElementById('editTypeId').value = id;
    document.getElementById('editTypeName').value = name;
    new bootstrap.Modal(document.getElementById('editProductTypeModal')).show();
}

function deleteProductType(id) {
    if (confirm('Are you sure you want to delete this product type?')) {
        window.location.href = `${contextPath}/AdminController?action=deleteProductType&id=${id}&tab=productTypes`;
    }
}

// Order functions
function filterOrders() {
    const status = document.getElementById('orderStatusFilter').value;
    const rows = document.querySelectorAll('tr[data-order-id]');

    rows.forEach(row => {
        if (status === 'all' || row.getAttribute('data-status') === status) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function viewOrderDetails(orderId) {
    // Fetch order details via AJAX
    fetch(`/AdminController?action=getOrderDetails&id=${orderId}`)
            .then(response => response.json())
            .then(data => {
                // Populate order information
                document.getElementById('detailOrderId').textContent = data.order.id;
                document.getElementById('detailCustomer').textContent = data.customerName;
                document.getElementById('detailOrderDate').textContent = data.order.orderDate;
                document.getElementById('detailStatus').textContent = data.order.status;
                document.getElementById('detailTotalPrice').textContent = data.order.totalPrice;

                // Populate order items
                const itemsTable = document.getElementById('orderItemsTable');
                itemsTable.innerHTML = '';
                data.orderItems.forEach(item => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                            <td>${item.productName}</td>
                            <td>${item.quantity}</td>
                            <td>$${item.unitPrice}</td>
                            <td>$${item.totalPrice}</td>
                        `;
                    itemsTable.appendChild(row);
                });

                // Show modal
                new bootstrap.Modal(document.getElementById('orderDetailsModal')).show();
            })
            .catch(error => {
                console.error('Error fetching order details:', error);
                alert('Failed to load order details. Please try again.');
            });
}

function updateOrderStatus(orderId, newStatus) {
    console.log('updateOrderStatus called with:', orderId, newStatus);

    // Make sure orderId is not empty
    if (!orderId || orderId === 'null' || orderId === 'undefined') {
        alert('Error: Order ID is missing or invalid!');
        return;
    }

    // Convert to integer if it's a string
    const id = parseInt(orderId, 10);

    if (isNaN(id)) {
        alert('Error: Order ID must be a number!');
        return;
    }

    if (confirm(`Are you sure you want to ${newStatus} this order?`)) {
        const formData = new FormData();
        formData.append('action', 'updateOrderStatus');
        formData.append('id', id);
        formData.append('status', newStatus);

        fetch('/AdminController', {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        return response.text().then(text => {
                            throw new Error(text);
                        });
                    }
                })
                .catch(error => {
                    alert('Failed to update order status: ' + error.message);
                    console.error('Error updating order status:', error);
                });
    }
}

function togglePasswordVisibility() {
    const passwordInput = document.getElementById('editPassword');
    const icon = document.getElementById('passwordToggleIcon');
    const isPassword = passwordInput.type === 'password';

    passwordInput.type = isPassword ? 'text' : 'password';
    icon.classList.toggle('fa-eye', isPassword);
    icon.classList.toggle('fa-eye-slash', !isPassword);
}

// Voucher functions
function editVoucher(voucherId) {
    const row = document.querySelector(`tr[data-voucher-id="${voucherId}"]`);
    if (!row)
        return;

    const codeName = row.getAttribute('data-voucher-code') || '';
    const description = row.getAttribute('data-voucher-description') || '';
    const discountType = row.getAttribute('data-voucher-discount-type') || '';
    const discountValue = row.getAttribute('data-voucher-discount-value') || '';
    const minOrderAmount = row.getAttribute('data-voucher-min-order') || '';
    const startDateRaw = row.getAttribute('data-voucher-start-date') || '';
    const endDateRaw = row.getAttribute('data-voucher-end-date') || '';
    const voucherActive = row.getAttribute('data-voucher-status') || '';

    const formatDate = (dateStr) => {
        if (!dateStr)
            return '';
        const date = new Date(dateStr);
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0');
        const dd = String(date.getDate()).padStart(2, '0');
        return `${yyyy}-${mm}-${dd}`;
    };

    document.getElementById('editVoucherId').value = voucherId;
    document.getElementById('editDescription').value = description;
    document.getElementById('editCodeName').value = codeName;
    document.getElementById('editDiscountType').value = discountType;
    document.getElementById('editDiscountValue').value = discountValue;
    document.getElementById('editMinOrderAmount').value = minOrderAmount;
    document.getElementById('editStartDate').value = formatDate(startDateRaw);
    document.getElementById('editEndDate').value = formatDate(endDateRaw);
    document.getElementById('editVoucherActive').value = voucherActive;

    new bootstrap.Modal(document.getElementById('editVoucherModal')).show();
}


function deleteVoucher(id) {
    if (confirm('Are you sure you want to delete this voucher?')) {
        window.location.href = '/AdminController?action=deleteVoucher&id=' + id;
    }
}

function editStaff(staffId) {

    console.log("entered");
    console.log("Editing staffId:", staffId);
    const actionValue = document.querySelector('#editStaffModal form input[name="action"]').value;
    console.log("Action value from modal:", actionValue);



    const row = document.querySelector(`tr[data-staff-id="${staffId}"]`);


    if (row) {

        const staffName = row.getAttribute('data-staff-name') || '';
        const fullName = row.getAttribute('data-staff-fullname') || '';
        const password = row.getAttribute('data-staff-password') || '';
        const gender = row.getAttribute('data-staff-gender') || '';
        const gmail = row.getAttribute('data-staff-gmail') || '';
        const phone = row.getAttribute('data-staff-phone') || '';
        const position = row.getAttribute('data-staff-position') || '';
        const image = row.getAttribute('data-staff-image') || '';



        // Gửi dữ liệu thật bằng hidden input
        document.getElementById('editStaffIdHidden').value = staffId;
        console.log("Set hidden staffId:", document.getElementById('editStaffIdHidden').value);
        document.getElementById('editStaffNameHidden').value = staffName;

        document.getElementById('editStaffFullName').value = fullName;
        document.getElementById('editStaffPassword').value = password;
        const genderSelect = document.getElementById('editStaffGender');
        Array.from(genderSelect.options).forEach(opt => {
            opt.selected = opt.value === gender.trim();
        });
        console.log("Giá trị được set vào select:", gender.trim());


        document.getElementById('editStaffGmail').value = gmail;
        document.getElementById('editStaffPhone').value = phone;
        document.getElementById('editStaffPosition').value = position;
        document.getElementById('currentImagePath').value = image;


        const imgPreview = document.getElementById('editStaffImagePreview');
        imgPreview.src = '/images/staff/' + image;
        imgPreview.style.display = 'block';

        const fileInput = document.getElementById('editStaffImage');
        fileInput.value = '';
        fileInput.onchange = function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imgPreview.src = e.target.result;
                };
                reader.readAsDataURL(file);
            } else {
                imgPreview.src = '/images/staff/' + image;
            }
        };
    }
    const modal = new bootstrap.Modal(document.getElementById('editStaffModal'));
    modal.show();

}
function deleteStaff(staffId) {
    if (confirm('Are you sure you want to delete this staff?')) {
        window.location.href = '/AdminController?action=deleteStaff&id=' + staffId;
    }
}


//view reply feedback
function viewReply(feedbackId) {
    const row = document.querySelector(`tr[data-feedback-id="${feedbackId}"]`);

    if (!row) {
        console.error("No matching row for feedbackId:", feedbackId);
        return;
    }

    const content = row.getAttribute("data-reply-content") || "No reply content.";
    const staffId = row.getAttribute("data-staff-id") || "Unknown";
    const replyTime = row.getAttribute("data-reply-time") || "Unknown";

    document.getElementById("modalReplyStaffId").textContent = staffId;
    document.getElementById("modalReplyTime").textContent = replyTime;
    document.getElementById("modalReplyContent").textContent = content;

    const replyModal = new bootstrap.Modal(document.getElementById("replyModal"));
    replyModal.show();
}
//reply feedback
function replyFeedback(feedbackId) {
    const row = document.querySelector(`tr[data-feedback-id="${feedbackId}"]`);
    if (!row)
        return;

    // Lấy thông tin từ data-attribute
    const cusId = row.getAttribute('data-cus-id');

    // Gán vào form
    document.getElementById('replyFeedbackId').value = feedbackId;
    document.getElementById('replyCusId').value = cusId;
    document.getElementById('staffSelect').value = ""; // Reset dropdown
    document.getElementById('replyContent').value = ""; // Reset nội dung

    // Mở modal
    const modal = new bootstrap.Modal(document.getElementById('replyFeedbackModal'));
    modal.show();
}