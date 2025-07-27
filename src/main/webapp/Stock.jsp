<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Inventory Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .form-container {
                max-width: 500px;
                margin-bottom: 20px;
            }
            .error {
                color: red;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Product Quantity Management</h1>

            <!-- Error Message -->
            <c:if test="${not empty ERROR}">
                <div class="alert alert-danger error">${ERROR}</div>
            </c:if>

            <!-- Search & Filter -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <label class="form-label">Search</label>
                    <input type="text" id="stockSearchInput" class="form-control" placeholder="Search by name, type, etc.">
                </div>
                <div class="col-md-4">
                    <label class="form-label">Filter Quantity</label>
                    <select id="filterBy" class="form-select">
                        <option value="">All</option>
                        <option value="low">Low Stock (≤10)</option>
                        <option value="out">Out of Stock (=0)</option>
                    </select>
                </div>
            </div>

            <!-- Create New Stock Button -->
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#createStockModal">Create New Stock</button>
<!--            <form action="AdminController" method="get" onsubmit="return validateDateRange()">
                <input type="hidden" name="action" value="excelcreate" >
                <div class="date-range-container" style="margin-bottom: 1rem;">
                    <label for="startDate">Start Date:</label>
                    <input type="date" id="startDate" name="startDate" required>

                    <label for="endDate" style="margin-left: 1rem;">End Date:</label>
                    <input type="date" id="endDate" name="endDate" required>

                    <button type="submit" style="margin-left: 1rem;">Generate Excel</button>
                </div>
            </form>-->

            <!-- Stock Table -->
            <div class="table-responsive">
                <table id="stockTable" class="table table-bordered table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Image</th>
                            <th>Type</th>
                            <th>Stock</th>
                            <th>Last Updated</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${productStockList}">
                            <tr>
                                <td>${item.proId}</td>
                                <td>${item.proName}</td>
                                <td><fmt:formatNumber value="${item.proPrice}" type="currency" currencySymbol="₫"/></td>
                                <td><img src="/images/products/${item.proImageUrl}" class="img-thumbnail" style="max-width: 80px;" /></td>
                                <td>${item.proTypeName}</td>
                                <td>${item.stockQuantity}</td>
                                <td><fmt:formatDate value="${item.lastUpdated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>
                                    <button class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#importModal${item.proId}">Import</button>
                                    <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#exportModal${item.proId}">Export</button>
                                    <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal${item.proId}">Delete</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Stock Action Modals -->
            <c:forEach var="item" items="${productStockList}">
                <!-- Import Modal -->
                <div class="modal fade" id="importModal${item.proId}" tabindex="-1">
                    <div class="modal-dialog">
                        <form method="post" action="AdminController">
                            <input type="hidden" name="action" value="ImportStockQuantity"/>
                            <input type="hidden" name="proId" value="${item.proId}"/>
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Import Stock - ${item.proName}</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <label>Quantity to Import:</label>
                                    <input type="number" class="form-control" name="quantity" min="1" required/>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-success">Confirm</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Export Modal -->
                <div class="modal fade" id="exportModal${item.proId}" tabindex="-1">
                    <div class="modal-dialog">
                        <form method="post" action="AdminController">
                            <input type="hidden" name="action" value="ExportStockQuantity"/>
                            <input type="hidden" name="proId" value="${item.proId}"/>
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Export Stock - ${item.proName}</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <label>Quantity to Export:</label>
                                    <input type="number" class="form-control" name="quantity" min="1" max="${item.stockQuantity}" required/>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-warning">Confirm</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Delete Modal -->
                <div class="modal fade" id="deleteModal${item.proId}" tabindex="-1">
                    <div class="modal-dialog">
                        <form method="post" action="AdminController">
                            <input type="hidden" name="action" value="deleteStock"/>
                            <input type="hidden" name="proId" value="${item.proId}"/>
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title text-danger">Confirm Delete</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to delete stock for <strong>${item.proName}</strong>?
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </c:forEach>

            <!-- Create New Stock Modal -->
            <div class="modal fade" id="createStockModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Create New Stock</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form action="AdminController" method="post" class="form-container needs-validation" novalidate>
                                <input type="hidden" name="action" value="createStock"/>
                                <div class="mb-3">
                                    <label for="proId" class="form-label">Product ID</label>
                                    <select class="form-select" id="proId" name="proId" required>
                                        <option value="" disabled selected>Select Product ID</option>
                                        <c:forEach var="pid" items="${noStockProIds}">
                                            <option value="${pid}">${pid}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="invalid-feedback">Please select a valid Product ID.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="quantity" class="form-label">Initial Quantity</label>
                                    <input type="number" class="form-control" id="quantity" name="quantity" min="1" required>
                                    <div class="invalid-feedback">Please enter a quantity greater than 0.</div>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Create Stock</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <!-- JS Scripts -->
        <script>
            function validateDateRange() {
                const startInput = document.getElementById('startDate');
                const endInput = document.getElementById('endDate');
                const startDate = new Date(startInput.value);
                const endDate = new Date(endInput.value);

                if (!startInput.value || !endInput.value) {
                    alert("Please select both start and end dates.");
                    return false;
                }

                if (startDate > endDate) {
                    alert("Start date cannot be after end date.");
                    return false;
                }

                return true;
            }
            function initStockSearch() {
                const input = document.getElementById("stockSearchInput");
                const rows = document.querySelectorAll("#stockTable tbody tr");
                input.addEventListener("keyup", () => {
                    const filter = input.value.toLowerCase();
                    rows.forEach(row => {
                        row.style.display = row.innerText.toLowerCase().includes(filter) ? "" : "none";
                    });
                });
            }

            function initClientSideFilter() {
                const filterSelect = document.getElementById("filterBy");
                const rows = document.querySelectorAll("#stockTable tbody tr");
                filterSelect.addEventListener("change", () => {
                    const val = filterSelect.value;
                    rows.forEach(row => {
                        const qty = parseInt(row.cells[5].textContent.trim());
                        row.style.display = (
                                val === "low" ? (qty <= 10 && qty > 0) :
                                val === "out" ? (qty === 0) : true
                                ) ? "" : "none";
                    });
                });
            }

            document.addEventListener("DOMContentLoaded", () => {
                initStockSearch();
                initClientSideFilter();

                // Bootstrap validation
                const forms = document.querySelectorAll('.needs-validation');
                forms.forEach(form => {
                    form.addEventListener('submit', e => {
                        if (!form.checkValidity()) {
                            e.preventDefault();
                            e.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    });
                });

                document.getElementById("quantity")?.addEventListener("input", function () {
                    this.setCustomValidity(this.value <= 0 ? "Quantity must be > 0" : "");
                });
            });
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
