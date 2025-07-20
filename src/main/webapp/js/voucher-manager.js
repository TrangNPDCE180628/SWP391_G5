document.addEventListener("DOMContentLoaded", function () {
    // ------ Voucher Date Validation for Add Form ------
    const addForm = document.querySelector('#addVoucherForm');
    if (addForm) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.style.display = 'none';
        addForm.querySelector('.modal-body').prepend(errorDiv);

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

        document.getElementById('startDate').addEventListener('change', () => {
            errorDiv.style.display = 'none';
        });
        document.getElementById('endDate').addEventListener('change', () => {
            errorDiv.style.display = 'none';
        });
    }

    // ------ Voucher Date Validation for Edit Form ------
    const editForm = document.querySelector('#editVoucherForm');
    if (editForm) {
        const errorDivEdit = document.createElement('div');
        errorDivEdit.className = 'alert alert-danger';
        errorDivEdit.style.display = 'none';
        editForm.querySelector('.modal-body').prepend(errorDivEdit);

        editForm.addEventListener('submit', function (e) {
            const start = new Date(document.getElementById('editStartDate').value);
            const end = new Date(document.getElementById('editEndDate').value);
            if (start >= end) {
                e.preventDefault();
                errorDivEdit.textContent = 'Start date must be before end date.';
                errorDivEdit.style.display = 'block';
            } else {
                errorDivEdit.style.display = 'none';
            }
        });

        document.getElementById('editStartDate').addEventListener('change', () => {
            errorDivEdit.style.display = 'none';
        });
        document.getElementById('editEndDate').addEventListener('change', () => {
            errorDivEdit.style.display = 'none';
        });
    }

    // ------ Show Voucher Details Modal ------
    document.querySelectorAll('.voucher-card').forEach(function (card) {
        card.addEventListener('click', function (e) {
            // Get voucher data from card attributes
            const voucherId = card.getAttribute('data-voucher-id');
            const codeName = card.getAttribute('data-voucher-code');
            const description = card.getAttribute('data-voucher-description');
            const quantity = card.getAttribute('data-voucher-quantity');
            const discountType = card.getAttribute('data-voucher-discount-type');
            const discountValue = card.getAttribute('data-voucher-discount-value');
            const maxDiscount = card.getAttribute('data-voucher-max-discount');
            const minOrder = card.getAttribute('data-voucher-min-order');
            const startDate = card.getAttribute('data-voucher-start-date');
            const endDate = card.getAttribute('data-voucher-end-date');
            const status = card.getAttribute('data-voucher-status');

            const typeText = discountType === 'percentage' ? 'Percentage (%)' : 'Fixed (₫)';
            const valueText = discountType === 'percentage' ? discountValue + '%' : formatMoney(discountValue) + '₫';
            const statusText = status === 'true' ? 'Active' : 'Inactive';
            const statusBadge = status === 'true' ? 'bg-success' : 'bg-secondary';

            document.getElementById('voucherDetailBody').innerHTML = `
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><strong>ID:</strong> ${voucherId}</li>
                    <li class="list-group-item"><strong>Code:</strong> ${codeName}</li>
                    <li class="list-group-item"><strong>Description:</strong> ${description}</li>
                    <li class="list-group-item"><strong>Quantity:</strong> ${quantity}</li>
                    <li class="list-group-item"><strong>Type:</strong> ${typeText}</li>
                    <li class="list-group-item"><strong>Value:</strong> ${valueText}</li>
                    <li class="list-group-item"><strong>Max Discount:</strong> ${formatMoney(maxDiscount)}₫</li>
                    <li class="list-group-item"><strong>Min Order:</strong> ${formatMoney(minOrder)}₫</li>
                    <li class="list-group-item"><strong>Start Date:</strong> ${startDate}</li>
                    <li class="list-group-item"><strong>End Date:</strong> ${endDate}</li>
                    <li class="list-group-item"><strong>Status:</strong> <span class="badge ${statusBadge}">${statusText}</span></li>
                </ul>
            `;
            document.getElementById('voucherDetailFooter').innerHTML = `
                <button type="button" class="btn btn-warning edit-btn-modal" data-voucher-id="${voucherId}"><i class="fas fa-edit"></i> Edit</button>
                <button type="button" class="btn btn-danger" onclick="deleteVoucher('${voucherId}')"><i class="fas fa-trash"></i> Delete</button>
            `;
            var modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('voucherDetailModal'));
            modal.show();
        });
    });

    // Card edit button
    document.querySelectorAll('.edit-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            editVoucher(btn.getAttribute('data-voucher-id'));
        });
    });
    // Modal edit button
    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('edit-btn-modal')) {
            editVoucher(e.target.getAttribute('data-voucher-id'));
            var detailModal = bootstrap.Modal.getInstance(document.getElementById('voucherDetailModal'));
            if (detailModal)
                detailModal.hide();
        }
    });

    // Format money with commas for VND
    function formatMoney(value) {
        if (!value)
            return "0";
        return Number(value).toLocaleString('vi-VN');
    }
});

function editVoucher(voucherId) {
    let el = document.querySelector(`.voucher-card[data-voucher-id="${voucherId}"]`);
    if (!el)
        el = document.querySelector(`tr[data-voucher-id="${voucherId}"]`);
    if (!el)
        return;

    const codeName = el.getAttribute('data-voucher-code') || '';
    const description = el.getAttribute('data-voucher-description') || '';
    const quantity = el.getAttribute('data-voucher-quantity') || '';
    const discountType = el.getAttribute('data-voucher-discount-type') || '';
    const discountValue = el.getAttribute('data-voucher-discount-value') || '';
    const maxDiscountValue = el.getAttribute('data-voucher-max-discount') || '';
    const minOrderAmount = el.getAttribute('data-voucher-min-order') || '';
    const startDateRaw = el.getAttribute('data-voucher-start-date') || '';
    const endDateRaw = el.getAttribute('data-voucher-end-date') || '';
    const voucherActive = el.getAttribute('data-voucher-status') || '';

    // Format yyyy-MM-dd for input type date
    const formatDate = (dateStr) => {
        if (!dateStr)
            return '';
        if (/^\d{4}-\d{2}-\d{2}$/.test(dateStr))
            return dateStr;
        let date = new Date(dateStr);
        if (!isNaN(date.getTime())) {
            let yyyy = date.getFullYear();
            let mm = String(date.getMonth() + 1).padStart(2, '0');
            let dd = String(date.getDate()).padStart(2, '0');
            return `${yyyy}-${mm}-${dd}`;
        }
        return '';
    };

    document.getElementById('editVoucherId').value = voucherId;
    document.getElementById('editCodeName').value = codeName;
    document.getElementById('editDescription').value = description;
    document.getElementById('editQuantity').value = quantity;
    document.getElementById('editDiscountType').value = discountType;
    document.getElementById('editDiscountValue').value = discountValue;
    document.getElementById('editMaxDiscountValue').value = maxDiscountValue;
    document.getElementById('editMinOrderAmount').value = minOrderAmount;
    document.getElementById('editStartDate').value = formatDate(startDateRaw);
    document.getElementById('editEndDate').value = formatDate(endDateRaw);
    document.getElementById('editVoucherActive').value = voucherActive;

    // Đảm bảo các trường có thể chỉnh sửa
    [
        'editCodeName',
        'editDescription',
        'editQuantity',
        'editDiscountType',
        'editDiscountValue',
        'editMaxDiscountValue',
        'editMinOrderAmount',
        'editStartDate',
        'editEndDate',
        'editVoucherActive'
    ].forEach(id => {
        var input = document.getElementById(id);
        if (input) {
            input.removeAttribute('readonly');
            input.removeAttribute('disabled');
            input.style.pointerEvents = 'auto';
        }
    });

    var modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('editVoucherModal'));
    modal.show();
}

function deleteVoucher(id) {
    if (confirm('Are you sure you want to delete this voucher?')) {
        window.location.href = '/VoucherController?action=deleteVoucher&id=' + id;
    }
}