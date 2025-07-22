<%-- 
    Document   : voucher-manager-table
    Created on : Jul 22, 2025, 11:45:00 AM
    Author     : SE18-CE180628-Nguyen Pham Doan Trang (refactored to table view by Copilot, Toast notifications added)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- Voucher Manager (Table View) -->

<div id="vouchers">
    <!-- Toast notification for error -->
    <c:if test="${not empty sessionScope.error}">
        <div class="toast align-items-center text-bg-danger border-0 show" role="alert" aria-live="assertive" aria-atomic="true" id="errorToast" style="position: fixed; top: 20px; right: 20px; z-index: 1080;">
            <div class="d-flex">
                <div class="toast-body">
                    <strong>Error:</strong> ${sessionScope.error}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                var errorToastEl = document.getElementById('errorToast');
                var errorToast = new bootstrap.Toast(errorToastEl, {delay: 4000});
                errorToast.show();
            });
        </script>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Toast notification for success -->
    <c:if test="${not empty sessionScope.success}">
        <div class="toast align-items-center text-bg-success border-0 show" role="alert" aria-live="assertive" aria-atomic="true" id="successToast" style="position: fixed; top: 20px; right: 20px; z-index: 1080;">
            <div class="d-flex">
                <div class="toast-body">
                    <strong>Success:</strong> ${sessionScope.success}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                var successToastEl = document.getElementById('successToast');
                var successToast = new bootstrap.Toast(successToastEl, {delay: 4000});
                successToast.show();
            });
        </script>
        <c:remove var="success" scope="session"/>
    </c:if>

    <h2>Vouchers Management</h2>
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addVoucherModal">
        <i class="fas fa-plus"></i> Add New Voucher
    </button>

    <div class="table-responsive">
        <table class="table table-bordered table-hover align-middle">
            <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Code Name</th>
                    <th>Description</th>
                    <th>Quantity</th>
                    <th>Discount Type</th>
                    <th>Discount Value</th>
                    <th>Max Discount</th>
                    <th>Min Order</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${vouchers}" var="voucher" varStatus="loop">
                    <tr 
                        style="cursor:pointer;"
                        class="voucher-card"
                        data-voucher-id="${voucher.voucherId}"
                        data-voucher-code="${voucher.codeName}"
                        data-voucher-description="${voucher.voucherDescription}"
                        data-voucher-quantity="${voucher.quantity}"
                        data-voucher-discount-type="${voucher.discountType}"
                        data-voucher-discount-value="${voucher.discountValue}"
                        data-voucher-max-discount="${voucher.maxDiscountValue}"
                        data-voucher-min-order="${voucher.minOrderAmount}"
                        data-voucher-start-date="<fmt:formatDate value='${voucher.startDate}' pattern='yyyy-MM-dd' />"
                        data-voucher-end-date="<fmt:formatDate value='${voucher.endDate}' pattern='yyyy-MM-dd' />"
                        data-voucher-status="${voucher.voucherActive}"
                        >
                        <td>${loop.index + 1}</td>
                        <td>${voucher.codeName}</td>
                        <td>
                            <span class="text-truncate" style="max-width: 200px; display: inline-block;">
                                ${voucher.voucherDescription}
                            </span>
                        </td>
                        <td>${voucher.quantity}</td>
                        <td>
                            ${voucher.discountType == 'percentage' ? 'Percentage (%)' : 'Fixed (₫)'}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${voucher.discountType == 'percentage'}">
                                    <fmt:formatNumber value="${voucher.discountValue}" maxFractionDigits="2" minFractionDigits="0"/>%
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber value="${voucher.discountValue}" maxFractionDigits="2" minFractionDigits="0"/>₫
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatNumber value="${voucher.maxDiscountValue}" maxFractionDigits="2" minFractionDigits="0"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${voucher.minOrderAmount}" maxFractionDigits="2" minFractionDigits="0"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${voucher.startDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>
                            <span class="badge ${voucher.voucherActive ? 'bg-success' : 'bg-secondary'}">
                                <c:choose>
                                    <c:when test="${voucher.voucherActive}">
                                        Active
                                    </c:when>
                                    <c:otherwise>
                                        Inactive
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-warning edit-btn" data-voucher-id="${voucher.voucherId}">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button type="button" class="btn btn-sm btn-danger" onclick="deleteVoucher('${voucher.voucherId}'); event.stopPropagation();">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Voucher Detail Modal -->
<div class="modal fade" id="voucherDetailModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Voucher Details</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="voucherDetailBody">
                <!-- Voucher details filled by JS -->
            </div>
            <div class="modal-footer" id="voucherDetailFooter">
                <!-- Edit/Delete buttons filled by JS -->
            </div>
        </div>
    </div>
</div>

<!-- Add Voucher Modal -->
<div class="modal fade" id="addVoucherModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Voucher</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form id="addVoucherForm" action="VoucherController" method="post">
                <div class="modal-body">
                    <c:if test="${not empty sessionScope.addError}">
                        <div class="alert alert-danger" role="alert">
                            ${sessionScope.addError}
                        </div>
                        <c:remove var="addError" scope="session"/>
                    </c:if>

                    <input type="hidden" name="action" value="addVoucher">
                    <input type="hidden" name="tab" value="vouchers">
                    <div class="mb-3">
                        <label for="codeName" class="form-label">Voucher Code</label>
                        <input type="text" class="form-control" id="codeName" name="codeName" required>
                    </div>

                    <div class="mb-3">
                        <label for="voucherDescription" class="form-label">Description</label>
                        <textarea class="form-control" id="voucherDescription" name="voucherDescription"></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" min="0" required>
                    </div>
                    <div class="mb-3">
                        <label for="discountType" class="form-label">Discount Type</label>
                        <select class="form-select" id="discountType" name="discountType" required>
                            <option value="percentage">Percentage (%)</option>
                            <option value="fixed">Fixed Amount ($)</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="discountValue" class="form-label">Discount Value</label>
                        <input type="number" step="0.01" class="form-control" id="discountValue" name="discountValue" required>
                    </div>

                    <div class="mb-3">
                        <label for="maxDiscountValue" class="form-label">Max Discount Value</label>
                        <input type="number" step="0.01" class="form-control" id="maxDiscountValue" name="maxDiscountValue">
                    </div>

                    <div class="mb-3">
                        <label for="minOrderAmount" class="form-label">Min Order Amount</label>
                        <input type="number" step="0.01" class="form-control" id="minOrderAmount" name="minOrderAmount" value="0">
                    </div>

                    <div class="mb-3">
                        <label for="startDate" class="form-label">Start Date</label>
                        <input type="date" class="form-control" id="startDate" name="startDate" required>
                    </div>

                    <div class="mb-3">
                        <label for="endDate" class="form-label">End Date</label>
                        <input type="date" class="form-control" id="endDate" name="endDate" required>
                    </div>

                    <div class="mb-3">
                        <label for="voucherActive" class="form-label">Status</label>
                        <select class="form-select" id="voucherActive" name="voucherActive" required>
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Edit Voucher Modal -->
<div class="modal fade" id="editVoucherModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="editVoucherForm" action="VoucherController" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Voucher</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <input type="hidden" name="action" value="updateVoucher">
                <input type="hidden" name="tab" value="vouchers">
                <div class="modal-body">
                    <!-- Báo lỗi nếu có -->
                    <c:if test="${not empty sessionScope.editError}">
                        <div class="alert alert-danger" role="alert">
                            ${sessionScope.editError}
                        </div>
                    </c:if>

                    <input type="hidden" id="editVoucherId" name="voucherId"
                           value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.voucherId : ''}">

                    <div class="mb-3">
                        <label for="editCodeName" class="form-label">Voucher Code</label>
                        <input type="text" class="form-control" id="editCodeName" name="codeName" required
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.codeName : ''}">
                    </div>

                    <div class="mb-3">
                        <label for="editDescription" class="form-label">Description</label>
                        <textarea class="form-control" id="editDescription" name="voucherDescription" rows="3">${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.voucherDescription : ''}</textarea>
                    </div>
                    <div class="mb-3">
                        <label for="editQuantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="editQuantity" name="quantity" min="0" required
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.quantity : ''}">
                    </div>
                    <div class="mb-3">
                        <label for="editDiscountType" class="form-label">Discount Type</label>
                        <select class="form-select" id="editDiscountType" name="discountType" required>
                            <option value="percentage" <c:if test="${not empty sessionScope.editVoucherData && sessionScope.editVoucherData.discountType == 'percentage'}">selected</c:if>>Percentage (%)</option>
                            <option value="fixed" <c:if test="${not empty sessionScope.editVoucherData && sessionScope.editVoucherData.discountType == 'fixed'}">selected</c:if>>Fixed (₫)</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="editDiscountValue" class="form-label">Discount Value</label>
                            <input type="number" class="form-control" id="editDiscountValue" name="discountValue" step="0.01" required
                                   value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.discountValue : ''}">
                    </div>
                    <div class="mb-3">
                        <label for="editMaxDiscountValue" class="form-label">Max Discount Value</label>
                        <input type="number" step="0.01" class="form-control" id="editMaxDiscountValue" name="maxDiscountValue"
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.maxDiscountValue : ''}">
                    </div>

                    <div class="mb-3">
                        <label for="editMinOrderAmount" class="form-label">Minimum Order Amount</label>
                        <input type="number" class="form-control" id="editMinOrderAmount" name="minOrderAmount" step="0.01" required
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.minOrderAmount : ''}">
                    </div>

                    <div class="mb-3">
                        <label for="editStartDate" class="form-label">Start Date</label>
                        <input type="date" class="form-control" id="editStartDate" name="startDate" required
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.startDate : ''}">
                    </div>

                    <div class="mb-3">
                        <label for="editEndDate" class="form-label">End Date</label>
                        <input type="date" class="form-control" id="editEndDate" name="endDate" required
                               value="${not empty sessionScope.editVoucherData ? sessionScope.editVoucherData.endDate : ''}">
                    </div>

                    <div class="mb-3">
                        <label for="editVoucherActive" class="form-label">Status</label>
                        <select class="form-select" id="editVoucherActive" name="voucherActive" required>
                            <option value="true" <c:if test="${not empty sessionScope.editVoucherData && sessionScope.editVoucherData.voucherActive == true}">selected</c:if>>Active</option>
                            <option value="false" <c:if test="${not empty sessionScope.editVoucherData && sessionScope.editVoucherData.voucherActive == false}">selected</c:if>>Inactive</option>
                            </select>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="js/voucher-manager.js"></script>
<c:if test="${not empty sessionScope.openAddModal}">
    <script>
                                document.addEventListener("DOMContentLoaded", function () {
                                    var addModal = new bootstrap.Modal(document.getElementById('addVoucherModal'));
                                    addModal.show();
                                });
    </script>
    <c:remove var="openAddModal" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.openEditModalId}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            var editModal = new bootstrap.Modal(document.getElementById('editVoucherModal'));
            editModal.show();
        });
    </script>
    <c:remove var="openEditModalId" scope="session"/>
</c:if>