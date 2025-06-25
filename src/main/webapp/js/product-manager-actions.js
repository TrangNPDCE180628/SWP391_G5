document.addEventListener('DOMContentLoaded', function () {
    initializeProductManager();
    initializeSearchEnhancements();
});

function initializeProductManager() {
    initializeViewButtons();
    initializeEditButtons();
    initializeDeleteButtons();
}

function initializeViewButtons() {
    document.querySelectorAll('.view-product-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const row = btn.closest('tr');
            const data = {
                id: row.dataset.productId,
                name: row.dataset.productName,
                description: row.dataset.productDescription,
                price: row.dataset.productPrice,
                image: row.dataset.productImage,
                category: row.dataset.productCategory
            };
            alert(`Product Details:\n\nID: ${data.id}\nName: ${data.name}\nPrice: ${data.price}\nDescription: ${data.description}`);
        });
    });
}

function initializeEditButtons() {
    document.querySelectorAll('.edit-product-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const row = btn.closest('tr');
            const form = document.querySelector('#addProductModal form');
            form.querySelector('[name=action]').value = 'edit';
            form.querySelector('[name=proId]').value = row.dataset.productId;
            form.querySelector('[name=proName]').value = row.dataset.productName;
            form.querySelector('[name=proDescription]').value = row.dataset.productDescription;
            form.querySelector('[name=proPrice]').value = row.dataset.productPrice;
            form.querySelector('[name=proImageMain]').value = row.dataset.productImage;
            form.querySelector('[name=proTypeId]').value = row.dataset.productCategory;
            const modal = new bootstrap.Modal(document.getElementById('addProductModal'));
            modal.show();
        });
    });
}

function initializeDeleteButtons() {
    document.querySelectorAll('.delete-product-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const proId = btn.closest('tr').dataset.productId;
            if (confirm('Are you sure you want to delete product ID: ' + proId + '?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'ProductManagerController';

                const inputAction = document.createElement('input');
                inputAction.type = 'hidden';
                inputAction.name = 'action';
                inputAction.value = 'delete';

                const inputId = document.createElement('input');
                inputId.type = 'hidden';
                inputId.name = 'proId';
                inputId.value = proId;

                form.appendChild(inputAction);
                form.appendChild(inputId);
                document.body.appendChild(form);
                form.submit();
            }
        });
    });
}

function initializeSearchEnhancements() {
    const searchInput = document.querySelector('input[name="searchTerm"]');
    if (searchInput) {
        let timeout;
        searchInput.addEventListener('input', function () {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                if (searchInput.value.length >= 3 || searchInput.value.length === 0) {
                    searchInput.closest('form').submit();
                }
            }, 1000);
        });
    }
}
