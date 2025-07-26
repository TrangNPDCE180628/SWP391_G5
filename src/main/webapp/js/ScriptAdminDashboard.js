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

// --- Xử lý tìm kiếm Staff ---
    const searchInput = document.getElementById("searchStaffInput");
    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const keyword = this.value.trim().toLowerCase();
            const staffRows = document.querySelectorAll("#staff tbody tr");

            staffRows.forEach(row => {
                const staffId = row.dataset.staffId ? row.dataset.staffId.toLowerCase() : "";
                const staffName = row.dataset.staffName ? row.dataset.staffName.toLowerCase() : "";
                const fullName = row.dataset.staffFullname ? row.dataset.staffFullname.toLowerCase() : "";

                const isMatch =
                        staffId.includes(keyword) ||
                        staffName.includes(keyword) ||
                        fullName.includes(keyword);

                row.style.display = isMatch ? "" : "none";
            });
        });
    }
    /* ---- Kiểm tra ngày Voucher ---- */
    const addForm = document.querySelector('#addVoucherForm');
    if (addForm) {
        // Tạo alert lỗi và chèn vào đầu modal-body
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.style.display = 'none';
        addForm.querySelector('.modal-body').prepend(errorDiv);

        // Xử lý submit
        addForm.addEventListener('submit', function (e) {
            const start = new Date(document.getElementById('startDate').value);
            const end = new Date(document.getElementById('endDate').value);
            if (start >= end) {
                e.preventDefault();
                errorDiv.textContent = 'Start date must be before end date.';
                errorDiv.style.display = 'block';
            } else {
                errorDiv.style.display = 'none';
            }
        });

        // Ẩn alert khi chỉnh lại ngày
        document.getElementById('startDate').addEventListener('change', () => {
            errorDiv.style.display = 'none';
        });
        document.getElementById('endDate').addEventListener('change', () => {
            errorDiv.style.display = 'none';
        });
    }
    /* ---- Hết kiểm tra ngày Voucher ---- */

    /* ---- Vẽ charts revenue ---- */
    // Monthly Revenue Chart
    const monthlyRevenueCanvas = document.getElementById("monthlyRevenueChart");
    if (monthlyRevenueCanvas) {
        const ctx = monthlyRevenueCanvas.getContext("2d");
        const revenueData = JSON.parse(document.getElementById("monthlyRevenueData").textContent);
        const labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

        const data = [];
        for (let i = 1; i <= 12; i++) {
            const key = i < 10 ? "0" + i : "" + i;
            data.push(revenueData[key] || 0);
        }

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Revenue ($)',
                        data: data,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Monthly Revenue'}
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return '$' + value.toLocaleString();
                            }
                        }
                    }
                }
            }
        });
    }

    // Growth Rate Chart
    const growthRateCanvas = document.getElementById("growthRateChart");
    if (growthRateCanvas) {
        const ctx = growthRateCanvas.getContext("2d");
        const growthRateDataElement = document.getElementById("monthlyGrowthRateData");

        if (growthRateDataElement) {
            const growthRateData = JSON.parse(growthRateDataElement.textContent);
            const labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

            const data = [];
            for (let i = 1; i <= 12; i++) {
                const key = i < 10 ? "0" + i : "" + i;
                data.push(growthRateData[key] || 0);
            }

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Growth Rate (%)',
                            data: data,
                            borderColor: 'rgba(255, 99, 132, 1)',
                            backgroundColor: 'rgba(255, 99, 132, 0.2)',
                            borderWidth: 2,
                            fill: true,
                            tension: 0.4
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {position: 'top'},
                        title: {display: true, text: 'Monthly Growth Rate'}
                    },
                    scales: {
                        y: {
                            ticks: {
                                callback: function (value) {
                                    return value.toFixed(1) + '%';
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    // Product Revenue Chart
    const productRevenueCanvas = document.getElementById("productRevenueChart");
    if (productRevenueCanvas) {
        const ctx = productRevenueCanvas.getContext("2d");
        const productRevenueDataElement = document.getElementById("productRevenueData");

        if (productRevenueDataElement) {
            const productRevenueData = JSON.parse(productRevenueDataElement.textContent);

            const labels = productRevenueData.map(item => item.productName);
            const revenues = productRevenueData.map(item => item.revenue);
            const quantities = productRevenueData.map(item => item.quantity);

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Revenue ($)',
                            data: revenues,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1,
                            yAxisID: 'y'
                        }, {
                            label: 'Quantity Sold',
                            data: quantities,
                            type: 'line',
                            borderColor: 'rgba(255, 206, 86, 1)',
                            backgroundColor: 'rgba(255, 206, 86, 0.2)',
                            borderWidth: 2,
                            yAxisID: 'y1'
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {position: 'top'},
                        title: {display: true, text: 'Top Products by Revenue'}
                    },
                    scales: {
                        x: {
                            ticks: {
                                maxRotation: 45,
                                minRotation: 45
                            }
                        },
                        y: {
                            type: 'linear',
                            display: true,
                            position: 'left',
                            beginAtZero: true,
                            ticks: {
                                callback: function (value) {
                                    return '$' + value.toLocaleString();
                                }
                            }
                        },
                        y1: {
                            type: 'linear',
                            display: true,
                            position: 'right',
                            beginAtZero: true,
                            grid: {
                                drawOnChartArea: false,
                            },
                            ticks: {
                                callback: function (value) {
                                    return value + ' units';
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    initStockSearch();
    initFeedbackSearch();

}
);
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
    const replyId = row.getAttribute("data-reply-id") || null;

    document.getElementById("modalReplyStaffId").textContent = staffId;
    document.getElementById("modalReplyTime").textContent = replyTime;
    document.getElementById("modalReplyContent").textContent = content;

    // Store replyId globally for delete function
    window.currentReplyId = replyId;
    window.currentFeedbackId = feedbackId;

    // Show/hide delete button based on whether reply exists
    const deleteBtn = document.getElementById("deleteReplyBtn");
    if (replyId && replyId !== "null" && replyId !== "") {
        deleteBtn.style.display = "inline-block";
    } else {
        deleteBtn.style.display = "none";
    }

    const replyModal = new bootstrap.Modal(document.getElementById("replyModal"));
    replyModal.show();
}

// Delete reply feedback
function deleteReply() {
    const replyId = window.currentReplyId;
    const feedbackId = window.currentFeedbackId;

    if (!replyId || replyId === "null" || replyId === "") {
        alert("No reply to delete.");
        return;
    }

    if (confirm("Are you sure you want to delete this reply? This action cannot be undone.")) {
        // Show loading state
        const deleteBtn = document.getElementById("deleteReplyBtn");
        const originalText = deleteBtn.innerHTML;
        deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Deleting...';
        deleteBtn.disabled = true;

        // Send AJAX request to delete reply
        fetch('AdminController', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=deleteReply&replyId=${replyId}`
        })
                .then(response => {
                    if (response.redirected) {
                        // Close modal and reload page
                        const modal = bootstrap.Modal.getInstance(document.getElementById("replyModal"));
                        modal.hide();
                        window.location.href = response.url;
                    } else if (response.ok) {
                        // Close modal and reload page for successful deletion
                        const modal = bootstrap.Modal.getInstance(document.getElementById("replyModal"));
                        modal.hide();
                        window.location.reload();
                    } else {
                        return response.text();
                    }
                })
                .then(data => {
                    if (data) {
                        alert("Error deleting reply: " + data);
                        // Restore button state
                        deleteBtn.innerHTML = originalText;
                        deleteBtn.disabled = false;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("An error occurred while deleting the reply.");
                    // Restore button state
                    deleteBtn.innerHTML = originalText;
                    deleteBtn.disabled = false;
                });
    }
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
    document.getElementById('replyContent').value = ""; // Reset nội dung

    // Reset staff dropdown nếu là Admin (có thể element không tồn tại nếu là Staff)
    const staffSelect = document.getElementById('staffSelect');
    if (staffSelect) {
        staffSelect.value = ""; // Reset dropdown cho Admin
    }

    // Mở modal
    const modal = new bootstrap.Modal(document.getElementById('replyFeedbackModal'));
    modal.show();
}

function editProductAttribute(productId, attributeId) {
    const row = document.querySelector(`tr[data-product-id="${productId}"][data-attribute-id="${attributeId}"]`);
    if (!row) {
        alert("Cannot find product attribute row.");
        return;
    }

    const attributeValue = row.dataset.attributeValue || '';

    document.getElementById("editProId").value = productId;
    document.getElementById("editAttributeId").value = attributeId;
    document.getElementById("editAttributeValue").value = attributeValue;

    const editModal = new bootstrap.Modal(document.getElementById("editProductAttributeModal"));
    editModal.show();
}

function deleteProductAttribute(productId, attributeId) {
    if (confirm("Are you sure you want to delete this Product Attribute?")) {
        window.location.href = `AdminController?action=deleteProductAttribute&proId=${productId}&attributeId=${attributeId}&tab=productAttributes`;
    }
}

function viewProductAttribute(productId, attributeId) {
    const row = document.querySelector(`tr[data-product-id="${productId}"][data-attribute-id="${attributeId}"]`);
    if (!row) {
        alert("Cannot find row for viewing.");
        return;
    }

    document.getElementById("viewProId").innerText = row.dataset.productId || '';
    document.getElementById("viewProductName").innerText = row.dataset.productName || '';
    document.getElementById("viewProductType").innerText = row.dataset.productType || '';
    document.getElementById("viewAttributeId").innerText = row.dataset.attributeId || '';
    document.getElementById("viewAttributeName").innerText = row.dataset.attributeName || '';
    document.getElementById("viewAttributeValue").innerText = row.dataset.attributeValue || '';
    document.getElementById("viewUnit").innerText = row.dataset.unit || '';

    new bootstrap.Modal(document.getElementById('viewProductAttributeModal')).show();
}

function loadOrderDetails(orderId) {
    const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
    const content = document.getElementById('orderDetailContent');

    // Show loading state
    content.innerHTML = `
        <div class="text-center p-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-3 text-muted">Đang tải chi tiết đơn hàng #${orderId}...</p>
        </div>
    `;

    fetch(`AdminController?action=viewOrderDetails&orderId=${orderId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(data => {
                content.innerHTML = data;
            })
            .catch(error => {
                console.error('Error loading order details:', error);
                content.innerHTML = `
                    <div class="alert alert-danger m-4">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <strong>Lỗi:</strong> Không thể tải chi tiết đơn hàng #${orderId}.
                        <br><small class="text-muted">${error.message}</small>
                        <hr>
                        <button class="btn btn-sm btn-outline-danger" onclick="loadOrderDetails(${orderId})">
                            <i class="fas fa-redo me-1"></i>Thử lại
                        </button>
                    </div>
                `;
            });

    modal.show();
}

function initStockSearch() {
    const input = document.getElementById("stockSearchInput");
    const table = document.getElementById("stockTable");
    if (!input || !table)
        return;

    const rows = table.querySelector("tbody").getElementsByTagName("tr");

    input.addEventListener("keyup", function () {
        const filter = input.value.toLowerCase();
        Array.from(rows).forEach(row => {
            const text = row.innerText.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });
}



function deleteProductType(id) {
    if (confirm('Are you sure you want to delete this product type?')) {
        window.location.href = `${contextPath}/AdminController?action=deleteProductType&tab=productTypes&id=${id}`;
    }
}

// View Customer Information Function
function viewCustomerInfo(cusId) {
    if (!cusId) {
        alert('Customer ID is required');
        return;
    }

    // Show modal first
    const modal = new bootstrap.Modal(document.getElementById('viewCustomerModal'));
    modal.show();

    // Reset content
    document.getElementById('modalCusId').textContent = cusId;
    document.getElementById('modalCusFullName').textContent = 'Loading...';
    document.getElementById('modalCusEmail').textContent = 'Loading...';
    document.getElementById('modalCusPhone').textContent = 'Loading...';
    document.getElementById('modalCusGender').textContent = 'Loading...';

    // Show loading spinner for order history
    document.getElementById('customerOrderHistory').innerHTML = `
        <div class="text-center">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `;

    // Fetch customer info and order history
    fetch(`${contextPath}/AdminController?action=getCustomerInfo&cusId=${cusId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Update customer info
                    const customer = data.customer;
                    document.getElementById('modalCusFullName').textContent = customer.cusFullName || '-';
                    document.getElementById('modalCusEmail').textContent = customer.cusGmail || '-';
                    document.getElementById('modalCusPhone').textContent = customer.cusPhone || '-';
                    document.getElementById('modalCusGender').textContent = customer.cusGender || '-';

                    // Update order history
                    const orders = data.orders || [];
                    let orderHistoryHtml = '';

                    if (orders.length === 0) {
                        orderHistoryHtml = '<div class="alert alert-info">No orders found</div>';
                    } else {
                        orderHistoryHtml = '<div class="list-group">';
                        orders.forEach(order => {
                            const statusClass = order.orderStatus === 'Done' ? 'success' :
                                    order.orderStatus === 'Cancel' ? 'danger' : 'warning';
                            orderHistoryHtml += `
                            <div class="list-group-item">
                                <div class="d-flex w-100 justify-content-between">
                                    <h6 class="mb-1">Order #${order.orderId}</h6>
                                    <small class="badge bg-${statusClass}">${order.orderStatus}</small>
                                </div>
                                <p class="mb-1">Total: $${order.orderTotalAmount || 0}</p>
                                <small>Date: ${order.orderDate || 'N/A'}</small>
                            </div>
                        `;
                        });
                        orderHistoryHtml += '</div>';
                    }

                    document.getElementById('customerOrderHistory').innerHTML = orderHistoryHtml;
                } else {
                    // Handle error
                    document.getElementById('modalCusFullName').textContent = 'Error loading data';
                    document.getElementById('modalCusEmail').textContent = 'Error loading data';
                    document.getElementById('modalCusPhone').textContent = 'Error loading data';
                    document.getElementById('modalCusGender').textContent = 'Error loading data';
                    document.getElementById('customerOrderHistory').innerHTML =
                            '<div class="alert alert-danger">Failed to load customer information</div>';
                }
            })
            .catch(error => {
                console.error('Error fetching customer info:', error);
                document.getElementById('modalCusFullName').textContent = 'Error loading data';
                document.getElementById('modalCusEmail').textContent = 'Error loading data';
                document.getElementById('modalCusPhone').textContent = 'Error loading data';
                document.getElementById('modalCusGender').textContent = 'Error loading data';
                document.getElementById('customerOrderHistory').innerHTML =
                        '<div class="alert alert-danger">Error loading customer information</div>';
            });
}

// Initialize Feedback Search and Filter functionality
function initFeedbackSearch() {
    const searchInput = document.getElementById("feedbackSearchInput");
    const statusFilter = document.getElementById("feedbackStatusFilter");
    const sortOrder = document.getElementById("feedbackSortOrder");
    const table = document.getElementById("feedbackTable");
    const noResultsDiv = document.getElementById("noFeedbackResults");

    if (!searchInput || !statusFilter || !sortOrder || !table) {
        return; // Elements not found, probably not on feedback tab
    }

    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.getElementsByTagName("tr"));

    // Store original rows order for reference
    const originalRows = [...rows];

    function applyFilters() {
        const searchTerm = searchInput.value.toLowerCase().trim();
        const selectedStatus = statusFilter.value;
        const selectedSort = sortOrder.value;

        // Filter rows based on search and status
        let filteredRows = originalRows.filter(row => {
            // Search filter
            const customerName = (row.dataset.cusName || '').toLowerCase();
            const productName = (row.dataset.proName || '').toLowerCase();
            const content = (row.dataset.content || '').toLowerCase();
            const feedbackId = (row.dataset.feedbackId || '').toLowerCase();

            const matchesSearch = !searchTerm ||
                    customerName.includes(searchTerm) ||
                    productName.includes(searchTerm) ||
                    content.includes(searchTerm) ||
                    feedbackId.includes(searchTerm);

            // Status filter
            const rowStatus = row.dataset.status || '';
            const matchesStatus = selectedStatus === 'all' || rowStatus === selectedStatus;

            return matchesSearch && matchesStatus;
        });

        // Sort filtered rows
        filteredRows.sort((a, b) => {
            switch (selectedSort) {
                case 'newest':
                    // Assuming feedbackId is sequential, higher ID = newer
                    return parseInt(b.dataset.feedbackId || '0') - parseInt(a.dataset.feedbackId || '0');

                case 'oldest':
                    return parseInt(a.dataset.feedbackId || '0') - parseInt(b.dataset.feedbackId || '0');

                case 'highest-rating':
                    return parseInt(b.dataset.rate || '0') - parseInt(a.dataset.rate || '0');

                case 'lowest-rating':
                    return parseInt(a.dataset.rate || '0') - parseInt(b.dataset.rate || '0');

                default:
                    return 0;
            }
        });

        // Hide all rows first
        originalRows.forEach(row => {
            row.style.display = 'none';
        });

        // Show filtered and sorted rows
        if (filteredRows.length > 0) {
            filteredRows.forEach(row => {
                row.style.display = '';
                tbody.appendChild(row); // Re-append to maintain sort order
            });
            noResultsDiv.style.display = 'none';
        } else {
            noResultsDiv.style.display = 'block';
        }
    }

    // Add event listeners
    searchInput.addEventListener('input', applyFilters);
    statusFilter.addEventListener('change', applyFilters);
    sortOrder.addEventListener('change', applyFilters);

    // Initial filter application
    applyFilters();
}

// Delete Feedback Function
function deleteFeedback(feedbackId) {
    if (confirm('Are you sure you want to delete this feedback?')) {
        window.location.href = `${contextPath}/AdminController?action=deleteFeedback&feedbackId=${feedbackId}&tab=feedbacks`;
    }
}


