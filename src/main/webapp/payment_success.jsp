<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Success</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-5 text-center">
    <div class="alert alert-success">
        <h2 class="mb-3"><i class="fas fa-check-circle"></i> Payment Successful!</h2>
        <p>${param.message != null ? param.message : message}</p>
        <a href="${pageContext.request.contextPath}/OrderController" class="btn btn-primary mt-3">View My Orders</a>
    </div>
</div>
</body>
</html> 