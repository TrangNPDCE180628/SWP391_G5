<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Your Cart</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
        <style>
            html, body {
                height: 100%;
            }
            .page-wrapper {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            .content {
                flex: 1;
            }
            body {
                background-color: #f8f9fa;
            }
            .table th, .table td {
                vertical-align: middle;
                text-align: center;
            }
            img.product-img {
                width: 60px;
                height: 60px;
                object-fit: cover;
            }
            .navbar {
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
            }
            .navbar-brand {
                font-weight: 600;
                color: #333;
            }
            .search-form {
                width: 300px;
            }
            .selected-voucher {
                background-color: #f0f0f0;
                border: 2px solid #6c757d;
            }

        </style>
    </head>
    <body>
        <div class="page-wrapper">
            <!-- Navigation Bar -->
            <nav class="navbar navbar-expand-lg navbar-light sticky-top mb-4">
                <div class="container py-2">
                    <a class="navbar-brand" href="HomeController">
                        <i class="fas fa-microchip me-2"></i>Tech Store
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <form class="d-flex search-form mx-auto" action="HomeController" method="GET">
                            <input class="form-control me-2" type="search" name="searchTerm"
                                   placeholder="Search for products..." value="${searchTerm}">
                            <button class="btn btn-outline-success" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                        <ul class="navbar-nav ms-auto">
                            <!-- Cart icon (moved up) -->
                            <li class="nav-item">
                                <a class="nav-link position-relative" href="CartController?action=view" title="View Cart">
                                    <i class="fas fa-shopping-cart fa-lg"></i>
                                    <c:if test="${sessionScope.cartSize > 0}">
                                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                            ${sessionScope.cartSize}
                                        </span>
                                    </c:if>
                                </a>
                            </li>

                            <!-- Account dropdown -->
                            <c:choose>
                                <c:when test="${not empty sessionScope.LOGIN_USER}">
                                    <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                           data-bs-toggle="dropdown">
                                            <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                            <li><a class="dropdown-item" href="ProfileCustomerController">Profile</a></li>
                                            <li><a class="dropdown-item" href="OrderHistoryController">My Orders</a></li>
                                                <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin' or sessionScope.LOGIN_USER.role eq 'Staff'}">
                                                <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                                </c:if>
                                            <li><hr class="dropdown-divider"></li>
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutController">
                                                    <i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                                        </ul>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
                                    <li class="nav-item"><a class="nav-link" href="register.jsp">Register</a></li>
                                    </c:otherwise>
                                </c:choose>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Main content -->
            <div class="container content">
                <h2 class="mb-4"><i class="fas fa-cart-shopping"></i> Your Shopping Cart</h2>

                <c:if test="${empty sessionScope.cart}">
                    <div class="alert alert-info">
                        Your cart is currently empty.
                        <a href="HomeController" class="btn btn-sm btn-primary ms-3">Continue Shopping</a>
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.cart}">
                    <form id="cartForm" action="PaymentController" method="post">
                        <input type="hidden" name="action" value="create"/>

                        <table class="table table-bordered table-hover bg-white align-middle text-center">
                            <thead class="table-dark">
                                <tr>
                                    <th style="width: 5%;">Select</th>
                                    <th style="width: 10%;">Image</th>
                                    <th style="width: 30%;">Product ame</th>
                                    <th style="width: 15%;">Price</th>
                                    <th style="width: 15%;">Quantity</th>
                                    <th style="width: 15%;">Total amount</th>
                                    <th style="width: 10%;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${sessionScope.cart}">
                                    <tr class="align-middle" style="vertical-align: middle;">
                                        <td>
                                            <input type="checkbox" name="selectedProductIds" value="${item.key}" />
                                        </td>
                                        <td>
                                            <img src="${item.value.proImageUrl}" class="product-img rounded" alt="${item.value.proName}">
                                        </td>
                                        <td class="text-start">${item.value.proName}</td>
                                        <td>
                                            <fmt:formatNumber value="${item.value.proPrice}" type="currency" currencySymbol="" /> ₫
                                        </td>
                                        <td>
                                            <div class="d-inline-flex align-items-center">
                                                <button type="button" class="btn btn-sm btn-outline-secondary update-btn" data-id="${item.key}" data-change="-1" aria-label="Decrease quantity">−</button>
                                                <input type="text" readonly class="form-control form-control-sm mx-2 text-center" 
                                                       style="width: 100px; user-select:none;" value="${item.value.quantity}" />
                                                <button type="button" class="btn btn-sm btn-outline-secondary update-btn" data-id="${item.key}" data-change="1" aria-label="Increase quantity">+</button>
                                            </div>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${item.value.proPrice * item.value.quantity}" type="currency" currencySymbol="" /> ₫
                                        </td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-danger delete-btn" data-id="${item.key}" title="Remove product">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Checkout Bar -->
                        <div class="fixed-bottom bg-white border-top shadow-sm p-3 d-flex justify-content-between align-items-center">
                            <div>
                                <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#voucherModal">
                                    <i class="fas fa-ticket-alt"></i> Select or enter voucher
                                </button>
                                <span id="appliedVoucher" class="ms-2 text-success fw-bold"></span>
                            </div>
                            <div class="text-end">
                                <div><strong>Subtotal:</strong> <span id="subtotal">₫0</span></div>
                                <div><strong>Discount:</strong> <span id="discount">₫0</span></div>
                                <div><strong>Shipping Fee:</strong> <span id="shippingFee">30000₫</span></div>
                                <div><strong>Total (selected):</strong> <span id="selectedTotal">₫0</span></div>
                                <button type="submit" class="btn btn-danger ms-3">
                                    Checkout <i class="fas fa-arrow-right"></i>
                                </button>
                            </div>

                        </div>
                    </form>
                </c:if>
            </div>

            <!-- Voucher Modal -->
            <div class="modal fade" id="voucherModal" tabindex="-1" aria-labelledby="voucherModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-scrollable modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Select voucher</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <c:forEach var="voucher" items="${sessionScope.vouchers}">
                                    <div class="col-md-6 mb-3">
                                        <div class="card shadow-sm position-relative">
                                            <!-- Quantity Badge at top-right -->
                                            <span class="badge bg-primary position-absolute top-0 end-0 m-2">
                                                x${voucher.quantity}
                                            </span>

                                            <div class="card-body">
                                                <h5 class="card-title text-primary">${voucher.codeName}</h5>
                                                <p class="card-text">
                                                    Discount:
                                                    <c:choose>
                                                        <c:when test="${voucher.discountType == 'percentage'}">${voucher.discountValue}%</c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${voucher.discountValue}" type="currency" currencySymbol="" /> ₫
                                                        </c:otherwise>
                                                    </c:choose><br>
                                                    Minimum order:
                                                    <fmt:formatNumber value="${voucher.minOrderAmount}" type="currency" currencySymbol="" /> ₫<br>
                                                    Max Discount Value: 
                                                    <fmt:formatNumber value="${voucher.maxDiscountValue}" type="currency" currencySymbol=""/> ₫
                                                    <br>
                                                    <span style="color:#ed7b2f;">
                                                        End Date: 
                                                        <fmt:formatDate value="${voucher.endDate}" pattern="dd/MM/yyyy"/>
                                                    </span>

                                                </p>
                                                <button type="button" class="btn btn-outline-success select-voucher-btn"
                                                        data-code="${voucher.codeName}" 
                                                        data-name="${voucher.codeName}"
                                                        data-type="${voucher.discountType}" 
                                                        data-value="${voucher.discountValue}" 
                                                        data-min="${voucher.minOrderAmount}"
                                                        data-max="${voucher.maxDiscountValue}">
                                                    Select Voucher
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <form id="updateForm" action="CartController" method="post" style="display:none;">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="productId" id="updateProductId"/>
                <input type="hidden" name="change" id="updateChange"/>
            </form>
            <form id="deleteForm" method="post" action="CartController" style="display:none;">
                <input type="hidden" name="action" value="remove"/>
                <input type="hidden" name="productId" id="deleteProductId"/>
            </form>

            <!-- Footer -->
            <footer class="bg-dark text-light py-4 mt-5">
                <div class="container">
                    <div class="row">
                        <div class="col-md-4">
                            <h5><i class="fas fa-microchip me-2"></i>Tech Store</h5>
                            <p>Your ultimate destination for quality tech products.</p>
                        </div>
                        <div class="col-md-4">
                            <h5>Quick Links</h5>
                            <ul class="list-unstyled">
                                <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>About Us</a></li>
                                <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>Contact</a></li>
                                <li><a href="#" class="text-light"><i class="fas fa-angle-right me-2"></i>FAQs</a></li>
                            </ul>
                        </div>
                        <div class="col-md-4">
                            <h5>Connect With Us</h5>
                            <div class="social-links">
                                <a href="#" class="text-light me-3"><i class="fab fa-facebook fa-lg"></i></a>
                                <a href="#" class="text-light me-3"><i class="fab fa-instagram fa-lg"></i></a>
                                <a href="#" class="text-light me-3"><i class="fab fa-twitter fa-lg"></i></a>
                            </div>
                        </div>
                    </div>
                    <hr class="mt-4">
                    <div class="text-center">
                        <small>© 2025 Tech Store. All rights reserved.</small>
                    </div>
                </div>
            </footer>
        </div>
        <!-- Toast Container -->
        <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 9999">
            <div id="toastMessage" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div id="toastBody" class="toast-body">
                        <!-- Message content -->
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>



        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Khôi phục trạng thái đã chọn khi load lại trang
            function restoreSelectedProducts() {

                const selected = JSON.parse(localStorage.getItem('selectedProductIds') || '\[]');
                document.querySelectorAll('input\[name="selectedProductIds"]').forEach(cb => {

                    cb.checked = selected.includes(cb.value);
                });
            }

            restoreSelectedProducts();
            // Khi tick chọn sản phẩm, lưu vào localStorage
            function saveSelectedProducts() {

                const selected = Array.from(document.querySelectorAll('input\[name="selectedProductIds"]:checked'))

                        .map(cb => cb.value);
                localStorage.setItem('selectedProductIds', JSON.stringify(selected));
                updateCheckoutButton();
                updateTotal();
            }
            document.addEventListener("DOMContentLoaded", function () {
            <% if (session.getAttribute("message") != null) {%>
                showToast("<%= session.getAttribute("message")%>", true);
            <% session.removeAttribute("message"); %>
            <% } else if (session.getAttribute("error") != null) {%>
                showToast("<%= session.getAttribute("error")%>", false);
            <% session.removeAttribute("error"); %>
            <% }%>
            });

            function showToast(message, isSuccess = true) {
                const toastEl = document.getElementById('toastMessage');
                const toastBody = document.getElementById('toastBody');
                toastBody.textContent = message;
                toastEl.classList.remove("bg-success", "bg-danger");
                toastEl.classList.add(isSuccess ? "bg-success" : "bg-danger");

                const toast = new bootstrap.Toast(toastEl);
                toast.show();
            }

            document.querySelectorAll('input\[name="selectedProductIds"]').forEach(cb => {

                cb.addEventListener('change', saveSelectedProducts);
            });
            document.addEventListener("DOMContentLoaded", () => {
                const checkboxes = document.querySelectorAll('input[name="selectedProductIds"]');
                const selectedTotal = document.getElementById('selectedTotal');
                const subtotalEl = document.getElementById('subtotal');
                const discountEl = document.getElementById('discount');
                const appliedVoucher = document.getElementById('appliedVoucher');
                const cartForm = document.getElementById('cartForm');
                let voucher = null;
                const currencyFormatter = new Intl.NumberFormat('vi-VN');
                const formatCurrency = (number) => {
                    return new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND',
                        minimumFractionDigits: 0, // bỏ phần lẻ .00
                    }).format(number);
                };
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
                        if (voucher.maxDiscountValue && voucher.maxDiscountValue > 0 && discount >= voucher.maxDiscountValue) {

                            discount = voucher.maxDiscountValue;
                        }
                    }

                    subtotalEl.textContent = formatCurrency(subtotal);
                    discountEl.textContent = formatCurrency(discount);
                    const shippingFee = 30000;
                    selectedTotal.textContent = formatCurrency(subtotal - discount + shippingFee);

                };
                checkboxes.forEach(cb => cb.addEventListener("change", updateTotal));
                updateTotal();
                document.querySelectorAll(".select-voucher-btn").forEach(btn => {
                    btn.addEventListener("click", () => {
                        const card = btn.closest(".card");

                        // Nếu đã chọn và đang là "Cancel Voucher"
                        if (card.classList.contains("selected-voucher")) {
                            card.classList.remove("selected-voucher");
                            btn.textContent = "Select Voucher";
                            voucher = null;
                            appliedVoucher.textContent = "";
                            updateTotal();
                            return;
                        }

                        const subtotal = parseInt(subtotalEl.textContent.replace(/\D/g, '') || '0');
                        const code = btn.dataset.code;
                        const name = btn.dataset.name;
                        const type = btn.dataset.type;
                        const value = parseFloat(btn.dataset.value);
                        const minOrder = parseFloat(btn.dataset.min);
                        const maxDiscountValue = parseFloat(btn.dataset.max);
                        if (subtotal < minOrder) {
                            showToast(
                                    `❌ Voucher cannot be applied.`,
                                    false // isSuccess = false ⇒ sẽ có class bg-danger
                                    );
                            return;
                        }

                        // Đặt voucher
                        voucher = {code, type, value, minOrder, maxDiscountValue, name};

                        // Xóa chọn các card khác
                        document.querySelectorAll(".select-voucher-btn").forEach(b => {
                            b.closest(".card").classList.remove("selected-voucher");
                            b.textContent = "Select Voucher";
                        });

                        // Đặt nút hiện tại là đã chọn
                        card.classList.add("selected-voucher");
                        btn.textContent = "Cancel Voucher";
//                        appliedVoucher.textContent = `Applied: ${voucher.name}`;
                        bootstrap.Modal.getInstance(document.getElementById('voucherModal')).hide();
                        updateTotal();
                    });

                });

                cartForm.addEventListener("submit", (e) => {
                    const submitter = e.submitter;
                    if (!submitter || !submitter.innerText.includes("Checkout")) {
                        return;
                    }

                    const checkedBoxes = document.querySelectorAll('input[name="selectedProductIds"]:checked');
                    if (checkedBoxes.length === 0) {
                        e.preventDefault();
                        showToast('Please select at least one product to checkout.');
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

                    const hiddenShipping = document.createElement("input");
                    hiddenShipping.type = "hidden";
                    hiddenShipping.name = "shippingFee";
                    hiddenShipping.value = 30000;
                    cartForm.appendChild(hiddenShipping);

                    // Form sẽ submit sau đó
                });

            });
            document.querySelectorAll(".delete-btn").forEach(btn => {
                btn.addEventListener("click", () => {
                    const productId = btn.dataset.id;
                    const deleteForm = document.getElementById("deleteForm");
                    const deleteProductId = document.getElementById("deleteProductId");
                    deleteProductId.value = productId;
                    deleteForm.submit();
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
        </script>
    </body>
</html>