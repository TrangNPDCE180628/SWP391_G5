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
            /* Custom cart style */
            .cart-section-title {
                font-weight: 600;
                font-size: 1.2rem;
                color: #333;
                background: #fff;
                padding: 20px 24px 10px 24px;
                border-radius: 8px 8px 0 0;
                border-bottom: 1px solid #eee;
                margin-bottom: 0;
            }
            .cart-header-row {
                background: #f5f5f5;
                padding: 12px 24px;
                border-radius: 0 0 0 0;
                font-weight: 500;
                margin-bottom: 0px;
                border-bottom: 1px solid #eee;
            }
            .cart-product-group {
                background: #fff;
                margin-bottom: 1.5rem;
                border-radius: 10px;
                box-shadow: 0 1px 4px rgba(0,0,0,0.04);
                padding: 0 0 10px 0;
                border: 1px solid #eee;
            }
            .cart-product-title-bar {
                background: #fff7f3;
                padding: 8px 32px;
                color: #ee4d2d;
                font-size: 1rem;
                border-radius: 10px 10px 0 0;
                border-bottom: 1px solid #ffe1d2;
                margin-bottom: 0;
            }
            .cart-product-item-row {
                display: flex;
                align-items: center;
                padding: 20px 32px;
                border-bottom: 1px solid #f1f1f1;
                gap: 18px;
            }
            .cart-product-img {
                width: 70px;
                height: 70px;
                object-fit: cover;
                border-radius: 6px;
                border: 1px solid #eee;
            }
            .cart-product-info {
                flex: 2 1 220px;
                text-align: left;
            }
            .cart-product-name {
                font-weight: 500;
                font-size: 1.08rem;
                color: #212529;
                margin-bottom: 3px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .cart-product-category {
                font-size: 0.92rem;
                color: #6c757d;
                margin-bottom: 2px;
            }
            .cart-product-price, .cart-product-total, .cart-product-action {
                flex: 1 1 120px;
                text-align: center;
                font-size: 1.08rem;
            }
            .cart-product-qty {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 8px;
            }
            .cart-product-qty input[type="text"] {
                width: 42px;
                text-align: center;
                border: 1px solid #ddd;
                border-radius: 4px;
                padding: 2px 0;
                font-size: 1rem;
                background: #f9f9f9;
            }
            .cart-product-qty button {
                width: 28px;
                height: 28px;
                border: 1px solid #ddd;
                background: #fff;
                border-radius: 4px;
                font-size: 1rem;
                color: #444;
                transition: background .15s;
            }
            .cart-product-qty button:hover {
                background: #f5f5f5;
            }
            .cart-product-total {
                color: #ee4d2d;
                font-weight: 600;
            }
            .cart-product-action .btn-danger {
                color: #ee4d2d;
                background: none;
                border: none;
            }
            .cart-product-action .btn-danger:hover {
                text-decoration: underline;
                background: none;
            }
            .cart-empty-box {
                background: #fff;
                border-radius: 10px;
                text-align: center;
                padding: 70px 0;
                margin-top: 40px;
                box-shadow: 0 1px 4px rgba(0,0,0,0.04);
            }
            /* Voucher Card Shopee Style */
            .voucher-card {
                min-height: 110px;
                box-shadow: 0 2px 8px rgba(56,56,56,0.07);
                border-radius: 16px;
                background: #fff;
                overflow: hidden;
                border: none;
                position: relative;
                display: flex;
            }
            .voucher-ticket-shape {
                min-width: 80px;
                background: #c1e5e0;
                border-radius: 16px 0 0 16px;
                text-align: center;
                padding: 0;
                border-right: 1px dashed #d6f3eb;
                position: relative;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
            }
            .voucher-main {
                background: #fff;
                border-radius: 0 16px 16px 0;
                border-left: none;
                min-width: 220px;
                padding: 18px 18px 18px 18px;
                flex-grow: 1;
                position: relative;
            }
            .voucher-quantity {
                position: absolute;
                top: 8px;
                right: 12px;
                background: #ff6842;
                color: #fff;
                font-weight: 600;
                font-size: 1.08em;
                border-radius: 0 10px 0 14px;
                padding: 2px 10px;
                z-index: 2;
            }
            .voucher-radio {
                position: absolute;
                right: 18px;
                top: 50%;
                transform: translateY(-50%);
            }
            .voucher-radio span {
                display: inline-block;
                width: 14px;
                height: 14px;
                border: 1px solid #c2c2c2;
                border-radius: 50%;
                background: #fff;
            }
            .voucher-progress-bar div {
                height: 5px;
                background: #ffe6d2;
                border-radius: 3px;
                overflow: hidden;
                margin-right: 24px;
                margin-top: 2px;
            }
            .voucher-progress-bar div > div {
                background: #ffb357;
                border-radius: 3px;
                height: 100%;
            }
        </style>
    </head>
    <body>
        <div class="page-wrapper">
            <!-- Navbar -->
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
                            <li class="nav-item">
                                <a class="nav-link position-relative" href="CartController?action=view" title="View Cart">
                                    <i class="fas fa-shopping-cart fa-lg"></i>
                                    <c:if test="${sessionScope.cartTotalQuantity > 0}">
                                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                            ${sessionScope.cartTotalQuantity}
                                        </span>
                                    </c:if>
                                </a>
                            </li>
                            <c:choose>
                                <c:when test="${not empty sessionScope.LOGIN_USER}">
                                    <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                           data-bs-toggle="dropdown">
                                            <i class="fas fa-user"></i> ${sessionScope.LOGIN_USER.fullName}
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                            <li><a class="dropdown-item" href="#">Profile</a></li>
                                            <li><a class="dropdown-item" href="OrderController?action=view">My Orders</a></li>
                                                <c:if test="${sessionScope.LOGIN_USER.role eq 'Admin'}">
                                                <li><a class="dropdown-item" href="AdminController">Admin Panel</a></li>
                                                </c:if>
                                            <li><hr class="dropdown-divider"></li>
                                            <li><a class="dropdown-item" href="MainController?action=Logout">Logout</a></li>
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
            <div class="container content py-4">
                <h2 class="mb-4"><i class="fas fa-cart-shopping"></i> Your Shopping Cart</h2>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger">${sessionScope.error}</div>
                    <% session.removeAttribute("error"); %>
                </c:if>

                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success">${sessionScope.message}</div>
                    <% session.removeAttribute("message");%>
                </c:if>

                <c:if test="${empty sessionScope.cart}">
                    <div class="cart-empty-box">
                        <p class="mb-3">Your cart is currently empty.</p>
                        <a href="HomeController" class="btn btn-primary">Continue Shopping</a>
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.cart}">
                    <form id="cartForm" action="PaymentController" method="post" class="mb-5">
                        <input type="hidden" name="action" value="create"/>
                        <!-- Cart header (only once) -->
                        <div class="cart-header-row d-flex align-items-center mb-2" style="border-radius:10px 10px 0 0;">
                            <div class="flex-shrink-0" style="width:40px;"><input type="checkbox" disabled></div>
                            <div class="flex-grow-2" style="min-width:510px;">Product</div>
                            <div class="flex-grow-1 text-center" style="min-width:110px;">Đơn Giá</div>
                            <div class="flex-grow-1 text-center" style="min-width:100px;">Số Lượng</div>
                            <div class="flex-grow-1 text-center" style="min-width:100px;">Số Tiền</div>
                            <div class="flex-grow-1 text-center" style="min-width:90px;">Thao Tác</div>
                        </div>
                        <!-- Each product as a card-like box -->
                        <c:forEach var="item" items="${sessionScope.cart}">
                            <div class="cart-product-group mb-3 p-0">
                                <div class="cart-product-title-bar d-flex align-items-center">
                                    <span class="badge bg-danger me-2" style="font-size:0.93em;">Mall</span>
                                    <span style="font-weight:500;">Tech Store</span>
                                </div>
                                <div class="cart-product-item-row">
                                    <div class="flex-shrink-0" style="width:40px;">
                                        <input type="checkbox" name="selectedProductIds" value="${item.key}" />
                                    </div>
                                    <div class="flex-shrink-0">
                                        <img src="${item.value.proImageUrl}" class="cart-product-img" alt="${item.value.proName}">
                                    </div>
                                    <div class="cart-product-info">
                                        <div class="cart-product-name">${item.value.proName}</div>
                                        <div class="cart-product-category">Phân Loại Hàng: Combo ${item.value.quantity} cuốn</div>
                                    </div>
                                    <div class="cart-product-price">
                                        <span style="font-weight:600;color:#222;">
                                            <fmt:formatNumber value="${item.value.proPrice}" type="currency" currencySymbol="₫"/>
                                        </span>
                                    </div>
                                    <div class="cart-product-qty">
                                        <button type="button" class="update-btn" data-id="${item.key}" data-change="-1" aria-label="Giảm số lượng">−</button>
                                        <input type="text" readonly value="${item.value.quantity}" />
                                        <button type="button" class="update-btn" data-id="${item.key}" data-change="1" aria-label="Tăng số lượng">+</button>
                                    </div>
                                    <div class="cart-product-total">
                                        <fmt:formatNumber value="${item.value.proPrice * item.value.quantity}" type="currency" currencySymbol="₫"/>
                                    </div>
                                    <div class="cart-product-action">
                                        <form method="post" action="CartController" style="display:inline;">
                                            <input type="hidden" name="action" value="remove"/>
                                            <input type="hidden" name="productId" value="${item.key}"/>
                                            <button class="btn btn-danger" type="submit" title="Xóa sản phẩm">Delete</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
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
                                        <div class="voucher-card d-flex position-relative">
                                            <div class="voucher-ticket-shape d-flex flex-column align-items-center justify-content-center">
                                                <img src="https://i.imgur.com/2DPxq3j.png" alt="Làm Đẹp" style="width:44px; opacity:0.4; margin-bottom:6px;">
                                                <div style="font-size:0.93em; color:#fff; font-weight:500;">All forms of payment</div>
                                            </div>
                                            <div class="voucher-main flex-grow-1 p-3">
                                                <div class="voucher-title fw-bold" style="font-size: 1.12em; color:#464646;">
                                                    <c:choose>
                                                        <c:when test="${voucher.discountType == 'percentage'}">
                                                            Discount ${voucher.discountValue}%
                                                        </c:when>
                                                        <c:otherwise>
                                                            Max Discount <fmt:formatNumber value="${voucher.discountValue}" type="currency" currencySymbol="₫"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="voucher-min-order" style="font-size:0.97em; color:#8d8d8d;">
                                                    Min Order Amout <fmt:formatNumber value="${voucher.minOrderAmount}" type="currency" currencySymbol="₫"/>
                                                </div>
                                                <input type="text" value="${voucher.codeName}" class="form-control form-control-sm my-1" style="max-width: 160px; background: #f6f6f6; border:none; color:#b1b1b1;" readonly>
                                                <div class="voucher-progress-bar mt-2 mb-1">
                                                    <div>
                                                        <div style="width:86%;"></div>
                                                    </div>
                                                </div>
                                                <div class="voucher-meta d-flex justify-content-between align-items-center" style="font-size:0.93em;">
                                                    <span style="color:#ed7b2f;">
                                                        <!-- Replace this with the voucher's expiry date as formatted string -->
                                                        End Date: 
                                                        <fmt:formatDate value="${voucher.endDate}" pattern="dd/MM/yyyy"/>
                                                    </span>
                                                    <a href="#" class="text-primary text-decoration-none fw-bold" style="font-size:0.95em;">Prerequisite</a>
                                                </div>
                                            </div>
                                            <span class="voucher-quantity">x${voucher.quantity}</span>
                                            <div class="voucher-radio">
                                                <button type="button" 
                                                        class="btn btn-inline-success select-voucher-btn"
                                                        data-code="${voucher.codeName}" 
                                                        data-type="${voucher.discountType}" 
                                                        data-value="${voucher.discountValue}" 
                                                        data-min="${voucher.minOrderAmount}">
                                                    <span></span>
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
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
                            const row = cb.closest(".cart-product-item-row");
                            const priceText = row.querySelector(".cart-product-total").textContent.trim();
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
                    const submitter = e.submitter;
                    if (!submitter || !submitter.innerText.includes("Checkout")) {
                        return;
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
                });
            });
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
        </script>
    </body>
</html>