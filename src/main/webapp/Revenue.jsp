<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Monthly Revenue</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4">View Revenue by Month</h2>
            <h4 class="mt-4">Total Revenue</h4>
            <p><strong>$<fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="" /></strong></p>
            <form method="GET" action="AdminController" class="row g-3">
                <input type="hidden" name="action" value="RevenueByMonth" />
                <input type="hidden" name="tab" value="revenue">
                <div class="col-auto">
                    <label for="month" class="col-form-label">Select Month:</label>
                </div>
                <div class="col-auto">
                    <input type="month" id="month" name="month" class="form-control" value="${selectedMonth}" required>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">View Revenue</button>
                    <a href="AdminController?tab=revenue" class="btn btn-secondary">Reset</a>
                </div>
            </form>

            <c:if test="${not empty monthlyRevenue}">
                <div class="alert alert-success mt-4" role="alert">
                    Revenue for <strong>${selectedMonth}</strong>:
                    <strong>
                        <fmt:formatNumber value="${monthlyRevenue}" type="currency" currencySymbol="$" maxFractionDigits="2"/>
                    </strong>
                </div>
            </c:if>

            <c:if test="${not empty monthlyRevenueMap}">
                <script type="application/json" id="monthlyRevenueData">
                    {
                    <c:forEach var="entry" items="${monthlyRevenueMap}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                    }
                </script>
                <canvas id="revenueChart" width="400" height="200"></canvas>
            </c:if>

        </div>

    </body>
</html>
