/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */




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

// Product Type functions
function editProductType(id, name) {
    document.getElementById('editTypeId').value = id;
    document.getElementById('editTypeName').value = name;
    new bootstrap.Modal(document.getElementById('editProductTypeModal')).show();
}

function deleteProductType(id) {
    if (confirm('Are you sure you want to delete this product type?')) {
        window.location.href = '/AdminController?action=deleteProductType&id=' + id;
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

function editDiscount(id) {
    const row = document.querySelector(`tr[data-discount-id="${id}"]`);
    document.getElementById('editDiscountId').value = id;
    document.getElementById('editProId').value = row.dataset.proId;
    document.getElementById('editDiscountType').value = row.dataset.discountType;
    document.getElementById('editDiscountValue').value = row.dataset.discountValue;
    document.getElementById('editStartDate').value = row.dataset.startDate;
    document.getElementById('editEndDate').value = row.dataset.endDate;
    document.getElementById('editActive').value = row.dataset.active;
    document.getElementById('editAdminId').value = row.dataset.adminId;
    new bootstrap.Modal(document.getElementById('editDiscountModal')).show();
}

function deleteDiscount(id) {
    if (confirm('Are you sure you want to delete this discount?')) {
        window.location.href = `AdminController?action=deleteDiscount&id=${id}`;
    }
}

// NEW: Add viewDiscount function
function viewDiscount(id) {
    const row = document.querySelector(`tr[data-discount-id="${id}"]`);
    document.getElementById('viewDiscountId').textContent = id;
    document.getElementById('viewProId').textContent = row.dataset.proId;
    document.getElementById('viewDiscountType').textContent = row.dataset.discountType;
    document.getElementById('viewDiscountValue').textContent = row.dataset.discountValue;
    document.getElementById('viewStartDate').textContent = row.dataset.startDate;
    document.getElementById('viewEndDate').textContent = row.dataset.endDate;
    document.getElementById('viewActive').textContent = row.dataset.active === 'true' ? 'Yes' : 'No';
    document.getElementById('viewAdminId').textContent = row.dataset.adminId;
    new bootstrap.Modal(document.getElementById('viewDiscountModal')).show();
}