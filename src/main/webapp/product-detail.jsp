<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">Product Detail</h3>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <img src="${product.image}" class="img-fluid rounded" alt="${product.productName}">
                                </div>
                                <div class="col-md-6">
                                    <h4>${product.productName}</h4>
                                    <p class="text-muted">ID: ${product.productId}</p>
                                    
                                    <div class="mb-3">
                                        <h5>Description</h5>
                                        <p>${product.description}</p>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <h5>Price</h5>
                                        <p class="h4 text-primary">$${product.price}</p>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <h5>Quantity</h5>
                                        <p>${product.quantity} units</p>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <h5>Status</h5>
                                        <span class="badge ${product.status ? 'bg-success' : 'bg-danger'}">
                                            ${product.status ? 'Active' : 'Inactive'}
                                        </span>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <h5>Created At</h5>
                                        <p>${product.createdAt}</p>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <h5>Last Updated</h5>
                                        <p>${product.updatedAt}</p>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="d-flex justify-content-between mt-4">
                                <a href="product-list" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Back to List
                                </a>
                                <div>
                                    <a href="update-product?id=${product.productId}" class="btn btn-warning">
                                        <i class="fas fa-edit"></i> Edit
                                    </a>
                                    <button type="button" class="btn btn-danger" onclick="confirmDelete(${product.productId})">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Delete</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this product?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form action="delete-product" method="POST" id="deleteForm">
                            <input type="hidden" name="id" id="deleteProductId">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function confirmDelete(productId) {
                document.getElementById('deleteProductId').value = productId;
                var deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
                deleteModal.show();
            }
        </script>
    </body>
</html> 