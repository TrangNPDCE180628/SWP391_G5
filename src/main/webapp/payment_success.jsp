<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Thanh toán thành công</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container py-5 text-center">
            <div class="alert alert-success">
                <h2>🎉 Thanh toán thành công!</h2>
                <p>${message}</p> <!-- Sửa tại đây -->
                <a href="OrderController?action=view" class="btn btn-primary mt-3">Xem đơn hàng</a>
            </div>
        </div>
    </body>
</html>
