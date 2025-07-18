/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", () => {
    const checkboxes = document.querySelectorAll('input[name="selectedProductIds"]');
    const selectedTotal = document.getElementById('selectedTotal');
    const subtotalEl = document.getElementById('subtotal');
    const discountEl = document.getElementById('discount');
    const appliedVoucher = document.getElementById('appliedVoucher');
    const cartForm = document.getElementById('cartForm');
    let voucher = null;
    const currencyFormatter = new Intl.NumberFormat('vi-VN');

    const formatCurrency = (number) => currencyFormatter.format(number) + " ₫";

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

    cartForm.addEventListener("submit", (e) => {
        const submitter = e.submitter; // <-- Lấy nút đã nhấn
        if (!submitter || !submitter.innerText.includes("Checkout")) {
            return; // nếu không phải nút Checkout thì bỏ qua
        }

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

        // Form will submit automatically after this
    });
});
document.querySelectorAll(".update-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        const productId = btn.dataset.id;
        const change = btn.dataset.change;

        // Lấy form ẩn chung
        const updateForm = document.getElementById("updateForm");
        const updateProductId = document.getElementById("updateProductId");
        const updateChange = document.getElementById("updateChange");

        // Gán giá trị
        updateProductId.value = productId;
        updateChange.value = change;

        // Submit form
        updateForm.submit();
    });
});
