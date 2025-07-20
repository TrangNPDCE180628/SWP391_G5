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
                                <a class="nav-link position-relative" href="CartController?action=view" title="Xem giỏ hàng">
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

                                            <!-- Orders (moved below Cart) -->
                                            <li class="nav-item">
                                                <a class="nav-link" href="OrderController?action=view">
                                                    My Orders
                                                </a>
                                            </li>
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
            <div class="container content">
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
                                                <button type="button" class="btn btn-sm btn-outline-secondary update-btn" data-id="${item.key}" data-change="-1" aria-label="Giảm số lượng">−</button>
                                                <input type="text" readonly class="form-control form-control-sm mx-2 text-center" 
                                                       style="width: 100px; user-select:none;" value="${item.value.quantity} / ${stockMap[item.key] - item.value.quantity}" />
                                                <button type="button" class="btn btn-sm btn-outline-secondary update-btn" data-id="${item.key}" data-change="1" aria-label="Tăng số lượng">+</button>
                                            </div>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${item.value.proPrice * item.value.quantity}" type="currency" currencySymbol="" /> ₫
                                        </td>
                                        <td>
                                            <form method="post" action="CartController" style="display:inline;">
                                                <input type="hidden" name="action" value="remove"/>
                                                <input type="hidden" name="productId" value="${item.key}"/>
                                                <button class="btn btn-sm btn-danger" type="submit" title="Xóa sản phẩm"><i class="fas fa-trash"></i></button>
                                            </form>
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
                                                    <fmt:formatNumber value="${voucher.minOrderAmount}" type="currency" currencySymbol="" /> ₫
                                                </p>
                                                <button type="button" class="btn btn-outline-success select-voucher-btn"
                                                        data-code="${voucher.codeName}" 
                                                        data-type="${voucher.discountType}" 
                                                        data-value="${voucher.discountValue}" 
                                                        data-min="${voucher.minOrderAmount}">
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
        </script>
    </body>
</html>