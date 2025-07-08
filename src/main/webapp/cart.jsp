<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <title>🛒 Giỏ hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <style>
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
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h2 class="mb-4"><i class="bi bi-cart4"></i> Giỏ hàng của bạn</h2>

            <!-- Thông báo lỗi -->
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">${sessionScope.error}</div>
                <% session.removeAttribute("error"); %>
            </c:if>

            <!-- Thông báo thành công -->
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">${sessionScope.message}</div>
                <% session.removeAttribute("message");%>
            </c:if>

            <c:if test="${empty sessionScope.cart}">
                <div class="alert alert-info">
                    🛒 Giỏ hàng của bạn đang trống.
                    <a href="HomeController" class="btn btn-sm btn-primary ms-3">Tiếp tục mua sắm</a>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.cart}">
                <form action="CartController" method="post">
                    <input type="hidden" name="action" value="makePayment" />
                    <table class="table table-bordered table-hover bg-white">
                        <thead class="table-dark">
                            <tr>
                                <th>Chọn</th>
                                <th>Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach var="item" items="${sessionScope.cart}">
                                <tr>
                                    <td>
                                        <input type="checkbox" name="selectedProductIds" value="${item.key}" />
                                    </td>
                                    <td>
                                        <img src="${item.value.proImageUrl}" class="product-img" alt="${item.value.proName}">
                                    </td>
                                    <td>${item.value.proName}</td>
                                    <td>${item.value.proPrice} ₫</td>
                                    <td>
                                        <div class="d-flex justify-content-center align-items-center gap-1">
                                            <form method="post" action="CartController" class="d-inline">
                                                <input type="hidden" name="action" value="update"/>
                                                <input type="hidden" name="productId" value="${item.key}"/>
                                                <input type="hidden" name="change" value="-1"/>
                                                <button class="btn btn-sm btn-outline-secondary" type="submit">-</button>
                                            </form>
                                            <span>${item.value.quantity}</span>
                                            <form method="post" action="CartController" class="d-inline">
                                                <input type="hidden" name="action" value="update"/>
                                                <input type="hidden" name="productId" value="${item.key}"/>
                                                <input type="hidden" name="change" value="1"/>
                                                <button class="btn btn-sm btn-outline-secondary" type="submit">+</button>
                                            </form>
                                        </div>
                                    </td>
                                    <td>${item.value.proPrice * item.value.quantity} ₫</td>
                                    <td>
                                        <form method="post" action="CartController">
                                            <input type="hidden" name="action" value="remove"/>
                                            <input type="hidden" name="productId" value="${item.key}"/>
                                            <button class="btn btn-sm btn-danger" type="submit"><i class="bi bi-trash"></i></button>
                                        </form>
                                    </td>
                                </tr>
                                <c:set var="total" value="${total + (item.value.proPrice * item.value.quantity)}"/>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="5" class="text-end"><strong>Tổng cộng:</strong></td>
                                <td colspan="2"><strong>${total} ₫</strong></td>
                            </tr>
                        </tfoot>
                    </table>

                    <div class="d-flex justify-content-between mt-3">
                        <a href="HomeController" class="btn btn-secondary">
                            <i class="bi bi-arrow-left"></i> Tiếp tục mua sắm
                        </a>
                        <button type="submit" class="btn btn-primary">
                            Thanh toán các mục đã chọn <i class="bi bi-cash-stack"></i>
                        </button>
                    </div>
                </form>
            </c:if>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
