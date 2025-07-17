<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Product Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="product-manager-actions.js"></script>
        <style>
            body {
                overflow-x: hidden;
                padding-right: 0 !important;
            }

            html {
                overflow-x: hidden;
            }

            .container {
                max-width: 100%;
                overflow-x: hidden;
            }

            .table th, .table td {
                vertical-align: middle;
                text-align: center;
                white-space: normal;
                word-break: break-word;
            }

            table.table {
                table-layout: auto;
                width: 100%;
            }
        </style>
    </head>
    <body>
        <div class="container py-4">
            <h2 class="mb-4">Product Manager</h2>

            <!-- Search -->
            <form method="get" action="ProductManagerController" class="mb-3 d-flex gap-2">
                <input type="hidden" name="action" value="search"/>
                <input type="text" name="searchTerm" class="form-control" placeholder="🔍 Search by product name..." value="${searchTerm}">
                <button class="btn btn-outline-primary" type="submit">Search</button>
            </form>

            <!-- Success/Error messages -->
            <c:if test="${not empty SUCCESS}">
                <div class="alert alert-success">${SUCCESS}</div>
            </c:if>
            <c:if test="${not empty ERROR}">
                <div class="alert alert-danger">${ERROR}</div>
            </c:if>

            <!-- Product table -->
            <table class="table table-bordered table-hover table-striped">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price (₫)</th>
                        <th>Description</th>
                        <th>Image</th>
                        <th>Category ID</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${products}">
                        <tr data-product-id="${product.proId}"
                            data-product-name="${product.proName}"
                            data-product-description="${product.proDescription}"
                            data-product-price="${product.proPrice}"
                            data-product-image="${product.proImageMain}"
                            data-product-category="${product.proTypeId}">
                            <td>${product.proId}</td>
                            <td>${product.proName}</td>
                            <td>${product.proPrice}</td>
                            <td>${product.proDescription}</td>
                            <td>
                                <img src="images/products/${product.proImageMain}" 
                                     alt="Image" 
                                     style="width: 100px; height: 100px; object-fit: cover; border-radius: 6px; border: 1px solid #ccc;" />
                            </td>
                            <td>
                                <c:forEach var="type" items="${productTypes}">
                                    <c:if test="${type.id == product.proTypeId}">
                                        ${type.name}
                                    </c:if>
                                </c:forEach>
                            </td>

                            <td>
                                <button class="btn btn-info btn-sm view-product-btn">👁️ View</button>
                                <button class="btn btn-warning btn-sm edit-product-btn">✏️ Edit</button>
                                <button class="btn btn-danger btn-sm delete-product-btn">🗑️ Delete</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Pagination -->
            <div class="mt-3">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="ProductManagerController?page=${i}" class="btn ${currentPage == i ? 'btn-primary' : 'btn-outline-primary'} btn-sm">${i}</a>
                </c:forEach>
            </div>

            <!-- Add Product Button -->
            <button class="btn btn-success mt-4" data-bs-toggle="modal" data-bs-target="#addProductModal">➕ Add Product</button>
        </div>

        <!-- Add/Edit Product Modal (dùng chung) -->
        <div class="modal fade" id="addProductModal" tabindex="-1">
            <div class="modal-dialog">
                <form class="modal-content" action="ProductManagerController" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add"/>
                    <div class="modal-header">
                        <h5 class="modal-title">📝 Product Form</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-2">
                            <label>Product ID</label>
                            <input type="text" name="proId" class="form-control"/>
                        </div>
                        <div class="mb-2">
                            <label>Product Name</label>
                            <input type="text" name="proName" class="form-control" required/>
                        </div>
                        <div class="mb-2">
                            <label>Description</label>
                            <textarea name="proDescription" class="form-control" rows="3"></textarea>
                        </div>
                        <div class="mb-2">
                            <label>Price</label>
                            <input type="number" step="0.01" name="proPrice" class="form-control" required/>
                        </div>
                        <div class="mb-2">
                            <label>Upload Image</label>
                            <input type="file" name="proImageMain" class="form-control" accept="image/*"/>
                        </div>

                        <div class="mb-2">
                            <label>Type ID</label>
                            <input type="number" name="proTypeId" class="form-control" min="1" required/>
                        </div>


                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button class="btn btn-primary" type="submit">Save</button>
                    </div>
                </form>
            </div>
        </div>
        <!-- View Product Modal -->
        <div class="modal fade" id="viewProductModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">📘 Product Detail</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>🆔 ID:</strong> <span id="view-proId"></span></p>
                        <p><strong>📛 Name:</strong> <span id="view-proName"></span></p>
                        <p><strong>📝 Description:</strong> <span id="view-proDescription"></span></p>
                        <p><strong>💵 Price:</strong> <span id="view-proPrice"></span></p>
                        <p><strong>📂 Category ID:</strong> <span id="view-proTypeId"></span></p>
                        <p><strong>🖼️ Image:</strong><br>
                            <img id="view-proImage" src="" class="img-thumbnail mt-1" style="width: 100px; height: 100px; object-fit: cover;"/>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script >

        document.addEventListener('DOMContentLoaded', function () {
            initializeProductManager();
            initializeSearchEnhancements();
        });

        function initializeProductManager() {
            initializeViewButtons();
            initializeEditButtons();
            initializeDeleteButtons();
            initializeAddButton(); // 👈 THÊM DÒNG NÀY

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

                    // Gán dữ liệu vào modal
                    document.getElementById('view-proId').textContent = data.id;
                    document.getElementById('view-proName').textContent = data.name;
                    document.getElementById('view-proDescription').textContent = data.description;
                    document.getElementById('view-proPrice').textContent = new Intl.NumberFormat('vi-VN').format(data.price) + ' ₫';
                    document.getElementById('view-proTypeId').textContent = data.category;
                    document.getElementById('view-proImage').src = 'images/products/' + data.image;

                    // Hiển thị modal
                    const modal = new bootstrap.Modal(document.getElementById('viewProductModal'));
                    modal.show();
                });
            });
        }


        function initializeEditButtons() {
            document.querySelectorAll('.edit-product-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    const row = btn.closest('tr');
                    const form = document.querySelector('#addProductModal form');
                    form.querySelector('[name=action]').value = 'edit';
                    form.querySelector('[name=proId]').value = row.dataset.productId; // Không cho sửa khi edit
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

        function initializeAddButton() {
            const addBtn = document.querySelector('[data-bs-target="#addProductModal"]');
            if (!addBtn)
                return;

            addBtn.addEventListener('click', () => {
                const form = document.querySelector('#addProductModal form');
                form.querySelector('[name=action]').value = 'add';
                form.querySelector('[name=proId]').value = '';
                form.querySelector('[name=proId]').readOnly = false;

                form.querySelector('[name=proName]').value = '';
                form.querySelector('[name=proDescription]').value = '';
                form.querySelector('[name=proPrice]').value = '';
                form.querySelector('[name=proImageMain]').value = '';
                form.querySelector('[name=proTypeId]').value = ''; // Cho phép nhập ID khi thêm
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


    </script>

</html>