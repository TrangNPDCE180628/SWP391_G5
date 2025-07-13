<%-- 
    Document   : Product Inventory Management
    Created on : Jul 13, 2025, 12:29:53 PM
    Author     : trang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inventory Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
        <div class="container">
            <h1>Product Inventory Management</h1>

            <!-- Error Message -->
            <c:if test="${not empty ERROR}">
                <div class="alert alert-danger error">${ERROR}</div>
            </c:if>

            <!-- Create New Stock Button -->
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#createStockModal">Create New Stock</button>

            <!-- Stock List -->
            <h2>Stock List</h2>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Stock Quantity</th>
                        <th>Last Updated</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="stock" items="${stocks}">
                    <tr>
                        <td>${stock.proId}</td>
                        <td>${stock.stockQuantity}</td>
                        <td><fmt:formatDate value="${stock.lastUpdated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#addStockModal${stock.proId}">Add</button>
                        <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteStockModal${stock.proId}">Delete</button>
                        <button class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#updateStockModal${stock.proId}">Update</button>
                    </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Create New Stock Modal -->
            <div class="modal fade" id="createStockModal" tabindex="-1" aria-labelledby="createStockModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="createStockModalLabel">Create New Stock</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="AdminController" method="post" class="form-container">
                                <input type="hidden" name="action" value="createStock">
                                <div class="mb-3">
                                    <label for="proId" class="form-label">Product ID</label>
                                    <input type="text" class="form-control" id="proId" name="proId" required>
                                </div>
                                <div class="mb-3">
                                    <label for="quantity" class="form-label">Initial Quantity</label>
                                    <input type="number" class="form-control" id="quantity" name="quantity" min="0" required>
                                </div>
                                <button type="submit" class="btn btn-primary">Create Stock</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Add Stock Quantity Modal -->
            <c:forEach var="stock" items="${stocks}">
                <div class="modal fade" id="addStockModal${stock.proId}" tabindex="-1" aria-labelledby="addStockModalLabel${stock.proId}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="addStockModalLabel${stock.proId}">Add Stock for Product ${stock.proId}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="AdminController" method="post" class="form-container">
                                    <input type="hidden" name="action" value="addStockQuantity">
                                    <input type="hidden" name="proId" value="${stock.proId}">
                                    <div class="mb-3">
                                        <label for="quantity${stock.proId}" class="form-label">Quantity to Add</label>
                                        <input type="number" class="form-control" id="quantity${stock.proId}" name="quantity" min="1" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Add Stock</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <!-- Delete Stock Modal -->
            <c:forEach var="stock" items="${stocks}">
                <div class="modal fade" id="deleteStockModal${stock.proId}" tabindex="-1" aria-labelledby="deleteStockModalLabel${stock.proId}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteStockModalLabel${stock.proId}">Delete Stock for Product ${stock.proId}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Are you sure you want to delete the stock record for Product ${stock.proId}?</p>
                                <form action="AdminController" method="post" class="form-container">
                                    <input type="hidden" name="action" value="deleteStock">
                                    <input type="hidden" name="proId" value="${stock.proId}">
                                    <button type="submit" class="btn btn-danger">Confirm Delete</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <!-- Update Stock Quantity Modal -->
            <c:forEach var="stock" items="${stocks}">
                <div class="modal fade" id="updateStockModal${stock.proId}" tabindex="-1" aria-labelledby="updateStockModalLabel${stock.proId}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateStockModalLabel${stock.proId}">Update Stock for Product ${stock.proId}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="AdminController" method="post" class="form-container">
                                    <input type="hidden" name="action" value="updateStockQuantity">
                                    <input type="hidden" name="proId" value="${stock.proId}">
                                    <div class="mb-3">
                                        <label for="newQuantity${stock.proId}" class="form-label">New Stock Quantity</label>
                                        <input type="number" class="form-control" id="newQuantity${stock.proId}" name="newQuantity" min="0" value="${stock.stockQuantity}" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Update Stock</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>