<%-- 
    Document   : ajaxRedirect
    Created on : Jul 22, 2025, 1:39:57 PM
    Author     : SE18-CE180628-Nguyen Pham Doan Trang
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Redirecting to VNPay...</title>
    </head>
    <body>
        <form id="vnpayForm" action="ajaxServlet" method="post">
            <input type="hidden" name="orderId" value="${orderId}" />
            <input type="hidden" name="totalBill" value="${totalBill}" />
            <input type="hidden" name="language" value="vn" />
            <input type="hidden" name="bankCode" value="" />
        </form>

        <script>
            document.getElementById("vnpayForm").submit();
        </script>
    </body>
</html>

