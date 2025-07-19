document.addEventListener("DOMContentLoaded", () => {
    // --- Phục hồi trạng thái đã chọn khi load lại trang ---
    function restoreSelectedProducts() {
        const selected = JSON.parse(localStorage.getItem('selectedProductIds') || '[]');
        document.querySelectorAll('input[name="selectedProductIds"]').forEach(cb => {
            cb.checked = selected.includes(cb.value);
        });
    }
    restoreSelectedProducts();

    // --- Khi tick chọn sản phẩm, lưu vào localStorage ---
    function saveSelectedProducts() {
        const selected = Array.from(document.querySelectorAll('input[name="selectedProductIds"]:checked'))
                .map(cb => cb.value);
        localStorage.setItem('selectedProductIds', JSON.stringify(selected));
    }
    document.querySelectorAll('input[name="selectedProductIds"]').forEach(cb => {
        cb.addEventListener('change', saveSelectedProducts);
    });

    // --- Code còn lại của giỏ hàng ---
    const checkboxes = document.querySelectorAll('input[name="selectedProductIds"]');
    const selectedTotal = document.getElementById('selectedTotal');
    const subtotalEl = document.getElementById('subtotal');
    const discountEl = document.getElementById('discount');
    const appliedVoucher = document.getElementById('appliedVoucher');
    const cartForm = document.getElementById('cartForm');
    let voucher = null;
    const currencyFormatter = new Intl.NumberFormat('vi-VN');

    const formatCurrency = (number) => currencyFormatter.format(number) + " ₫";

    function updateCheckoutButton() {
        if (!cartForm)
            return;
        const checkoutBtn = cartForm.querySelector('button[type="submit"].btn-danger, #checkoutBtn');
        if (checkoutBtn) {
            const checkedBoxes = document.querySelectorAll('input[name="selectedProductIds"]:checked');
            const hasAnyCheckbox = document.querySelectorAll('input[name="selectedProductIds"]').length > 0;
            checkoutBtn.disabled = checkedBoxes.length === 0 || !hasAnyCheckbox;
        }
    }

    const updateTotal = () => {
        let subtotal = 0;
        checkboxes.forEach(cb => {
            if (cb.checked) {
                const row = cb.closest("tr");
                const priceText = row.querySelector("td:nth-child(6)").textContent.trim();
                const price = parseInt(priceText.replace(/\D/g, ''));
                subtotal += price;
            }
        });

        let discount = 0;
        if (voucher && subtotal >= voucher.minOrder) {
            discount = voucher.type === "percentage"
                    ? subtotal * voucher.value / 100
                    : voucher.value;
        }

        subtotalEl.textContent = formatCurrency(subtotal);
        discountEl.textContent = formatCurrency(discount);
        selectedTotal.textContent = formatCurrency(subtotal - discount);

        updateCheckoutButton();
    };

    checkboxes.forEach(cb => cb.addEventListener("change", updateTotal));
    updateTotal();

    document.querySelectorAll(".select-voucher-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const subtotal = parseInt(subtotalEl.textContent.replace(/\D/g, '') || '0');
            const code = btn.dataset.code;
            const type = btn.dataset.type;
            const value = parseFloat(btn.dataset.value);
            const minOrder = parseFloat(btn.dataset.min);

            if (subtotal < minOrder) {
                alert(`Cannot select voucher '${code}'. Minimum order must be from ${currencyFormatter.format(minOrder)} ₫`);
                return;
            }

            voucher = {code, type, value, minOrder};
            appliedVoucher.textContent = `Selected ${voucher.code}`;
            bootstrap.Modal.getInstance(document.getElementById('voucherModal')).hide();
            updateTotal();
        });
    });

    // Chỉ kiểm tra checkbox khi submit là nút Checkout - KHÔNG kiểm tra khi submit là nút xóa!
    if (cartForm) {
        cartForm.addEventListener("submit", (e) => {
            const submitter = e.submitter;
            if (
                    submitter &&
                    (
                            (submitter.classList.contains("btn-danger") && submitter.innerText.includes("Checkout")) ||
                            submitter.id === "checkoutBtn"
                            )
                    ) {
                const checkedBoxes = document.querySelectorAll('input[name="selectedProductIds"]:checked');
                if (checkedBoxes.length === 0) {
                    e.preventDefault();
                    alert('Please select at least one product to checkout.');
                    return;
                }

                cartForm.querySelectorAll("input[name='selectedProductIds']").forEach(el => el.remove());
                cartForm.querySelectorAll("input[name='voucherCode']").forEach(el => el.remove());

                checkedBoxes.forEach(cb => {
                    const hidden = document.createElement("input");
                    hidden.type = "hidden";
                    hidden.name = "selectedProductIds";
                    hidden.value = cb.value;
                    cartForm.appendChild(hidden);
                });

                if (voucher) {
                    const hiddenVoucher = document.createElement("input");
                    hiddenVoucher.type = "hidden";
                    hiddenVoucher.name = "voucherCode";
                    hiddenVoucher.value = voucher.code;
                    cartForm.appendChild(hiddenVoucher);
                }
            }
            // Nếu submit là nút xóa, không kiểm tra gì, cho submit bình thường!
        });
    }

    document.querySelectorAll(".update-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.dataset.id;
            const change = btn.dataset.change;

            const updateForm = document.getElementById("updateForm");
            const updateProductId = document.getElementById("updateProductId");
            const updateChange = document.getElementById("updateChange");

            updateProductId.value = productId;
            updateChange.value = change;

            updateForm.submit();
        });
    });

    updateCheckoutButton();
});