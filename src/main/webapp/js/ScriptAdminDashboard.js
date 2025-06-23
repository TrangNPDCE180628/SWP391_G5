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
    const row = document.querySelector(`#staff-row-${staffId}`);
    if (!row)
        return;

    // Lấy dữ liệu từ các data-* attributes
    const staffName = row.dataset.staffName || '';
    const staffFullName = row.dataset.staffFullname || '';
    const staffPassword = row.dataset.staffPassword || '';
    const staffGender = row.dataset.staffGender || '';
    const staffGmail = row.dataset.staffGmail || '';
    const staffPhone = row.dataset.staffPhone || '';
    const staffPosition = row.dataset.staffPosition || '';
    const staffImage = row.dataset.staffImage || '';

    // Gán dữ liệu vào form trong modal
    document.getElementById('edit-id-hidden').value = staffId;
    document.getElementById('edit-username').value = staffName;
    document.getElementById('edit-password').value = staffPassword;
    document.getElementById('edit-fullname').value = staffFullName;
    document.getElementById('edit-gender').value = staffGender;
    document.getElementById('edit-gmail').value = staffGmail;
    document.getElementById('edit-phone').value = staffPhone;
    document.getElementById('edit-position').value = staffPosition;


    // Preview ảnh cũ
    const preview = document.getElementById('editStaffImagePreview');
    if (preview) {
        preview.src = `${contextPath}/images/staff/${staffImage}`;
        preview.style.display = 'block';
    }

    // Cập nhật khi chọn ảnh mới
    const fileInput = document.getElementById('edit-image');
    if (fileInput) {
        fileInput.value = '';
        fileInput.onchange = function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                };
                reader.readAsDataURL(file);
            } else {
                preview.src = `${contextPath}/images/staff/${staffImage}`;
            }
        };
    }

    // Hiển thị modal
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