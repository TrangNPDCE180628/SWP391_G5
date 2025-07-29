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
            <h2 class="mb-4">Revenue Analytics Dashboard</h2>
            <h4 class="mt-4">Total Revenue</h4>
            <p><strong><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </strong></p>

            <!-- Year Filter Form -->
            <form method="GET" action="AdminController" class="row g-3">
                <input type="hidden" name="action" value="RevenueByYear" />
                <input type="hidden" name="tab" value="revenue">
                <div class="col-auto">
                    <label for="year" class="col-form-label">Select Year:</label>
                </div>
                <div class="col-auto">
                    <select id="year" name="year" class="form-select" required>
                        <option value="">Choose year...</option>
                        <c:forEach var="yearEntry" items="${yearlyRevenueMap}">
                            <option value="${yearEntry.key}" ${selectedYear == yearEntry.key ? 'selected' : ''}>
                                ${yearEntry.key}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">View Year Revenue</button>
                    <a href="AdminController?action=RevenueByYear&tab=revenue" class="btn btn-secondary">Reset</a>
                </div>
            </form>

            <!-- Selected Year Revenue Display -->
            <c:if test="${not empty yearlyRevenue}">
                <div class="alert alert-success mt-4" role="alert">
                    Revenue for <strong>${selectedYear}</strong>:
                    <strong>
                        <fmt:formatNumber value="${yearlyRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>

                    </strong>
                </div>
            </c:if>

            <!-- Yearly Overview Charts (Always show) -->
            <c:if test="${not empty yearlyRevenueMap}">
                <div class="row mt-5">
                    <div class="col-md-12">
                        <h4>Yearly Revenue Overview</h4>
                    </div>
                </div>

                <div class="row mt-4">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Yearly Revenue Trend</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="yearlyRevenueChart" width="400" height="300"></canvas>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Yearly Growth Rate (%)</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="yearlyGrowthChart" width="400" height="300"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Monthly Details for Selected Year -->
            <c:if test="${not empty selectedYear && not empty monthlyRevenueMap}">
                <div class="row mt-5">
                    <div class="col-md-12">
                        <h4>Monthly Breakdown for ${selectedYear}</h4>
                    </div>
                </div>

                <!-- Monthly Revenue Chart -->
                <div class="row mt-4">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Monthly Revenue - ${selectedYear}</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="monthlyRevenueChart" width="400" height="300"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Monthly Growth Rate Chart -->
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Monthly Growth Rate (%) - ${selectedYear}</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="growthRateChart" width="400" height="300"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Product Revenue Chart -->
                <div class="row mt-4">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Top 10 Products by Revenue - ${selectedYear}</h5>
                            </div>
                            <div class="card-body">
                                <canvas id="productRevenueChart" width="400" height="200"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Charts Section -->
            <c:if test="${not empty monthlyRevenueMap}">
                <!-- Data Scripts -->
                <script type="application/json" id="monthlyRevenueData">
                    {
                    <c:forEach var="entry" items="${monthlyRevenueMap}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                    }
                </script>

                <script type="application/json" id="monthlyGrowthRateData">
                    {
                    <c:forEach var="entry" items="${monthlyGrowthRateMap}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                    }
                </script>

                <script type="application/json" id="productRevenueData">
                    [
                    <c:forEach var="product" items="${productRevenueList}" varStatus="status" end="9">
                        {
                        "productName": "${product.productName}",
                        "revenue": ${product.revenue},
                        "quantity": ${product.quantity}
                        }<c:if test="${!status.last && status.index < 9}">,</c:if>
                    </c:forEach>
                    ]
                </script>
            </c:if>

            <!-- Yearly Data Scripts -->
            <c:if test="${not empty yearlyRevenueMap}">
                <script type="application/json" id="yearlyRevenueData">
                    {
                    <c:forEach var="entry" items="${yearlyRevenueMap}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                    }
                </script>

                <script type="application/json" id="yearlyGrowthRateData">
                    {
                    <c:forEach var="entry" items="${yearlyGrowthRateMap}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                    }
                </script>
            </c:if>

        </div>

        <script>
document.addEventListener('DOMContentLoaded', function () {
    // Yearly Revenue Chart
    const yearlyRevenueCanvas = document.getElementById("yearlyRevenueChart");
    if (yearlyRevenueCanvas && document.getElementById("yearlyRevenueData")) {
        const ctx = yearlyRevenueCanvas.getContext("2d");
        const yearlyRevenueData = JSON.parse(document.getElementById("yearlyRevenueData").textContent);

        const years = Object.keys(yearlyRevenueData).reverse(); // Show chronologically
        const revenues = years.map(year => yearlyRevenueData[year]);

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: years,
                datasets: [{
                        label: 'Yearly Revenue (đ)',
                        data: revenues,
                        borderColor: 'rgba(54, 162, 235, 1)',
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.4
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Yearly Revenue Trend'}
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return value.toLocaleString('vi-VN') + ' ₫';
                            }

                        }
                    }
                }
            }
        });
    }

    // Yearly Growth Rate Chart
    const yearlyGrowthCanvas = document.getElementById("yearlyGrowthChart");
    if (yearlyGrowthCanvas && document.getElementById("yearlyGrowthRateData")) {
        const ctx = yearlyGrowthCanvas.getContext("2d");
        const yearlyGrowthData = JSON.parse(document.getElementById("yearlyGrowthRateData").textContent);

        const years = Object.keys(yearlyGrowthData).reverse();
        const growthRates = years.map(year => yearlyGrowthData[year]);

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: years,
                datasets: [{
                        label: 'Growth Rate (%)',
                        data: growthRates,
                        backgroundColor: growthRates.map(rate => rate >= 0 ? 'rgba(75, 192, 192, 0.6)' : 'rgba(255, 99, 132, 0.6)'),
                        borderColor: growthRates.map(rate => rate >= 0 ? 'rgba(75, 192, 192, 1)' : 'rgba(255, 99, 132, 1)'),
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Yearly Growth Rate'}
                },
                scales: {
                    y: {
                        ticks: {
                            callback: function (value) {
                                return value.toFixed(1) + '%';
                            }
                        }
                    }
                }
            }
        });
    }

    // Monthly Revenue Chart
    const monthlyRevenueCanvas = document.getElementById("monthlyRevenueChart");
    if (monthlyRevenueCanvas && document.getElementById("monthlyRevenueData")) {
        const ctx = monthlyRevenueCanvas.getContext("2d");
        const revenueData = JSON.parse(document.getElementById("monthlyRevenueData").textContent);
        const labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

        const data = [];
        for (let i = 1; i <= 12; i++) {
            const key = i < 10 ? "0" + i : "" + i;
            data.push(revenueData[key] || 0);
        }

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Revenue (đ)',
                        data: data,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Monthly Revenue'}
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return value.toLocaleString('vi-VN') + ' ₫'
;
                            }
                        }
                    }
                }
            }
        });
    }

    // Growth Rate Chart
    const growthRateCanvas = document.getElementById("growthRateChart");
    if (growthRateCanvas && document.getElementById("monthlyGrowthRateData")) {
        const ctx = growthRateCanvas.getContext("2d");
        const growthRateData = JSON.parse(document.getElementById("monthlyGrowthRateData").textContent);
        const labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

        const data = [];
        for (let i = 1; i <= 12; i++) {
            const key = i < 10 ? "0" + i : "" + i;
            data.push(growthRateData[key] || 0);
        }

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Growth Rate (%)',
                        data: data,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Monthly Growth Rate'}
                },
                scales: {
                    y: {
                        ticks: {
                            callback: function (value) {
                                return value.toFixed(1) + '%';
                            }
                        }
                    }
                }
            }
        });
    }

    // Product Revenue Chart
    const productRevenueCanvas = document.getElementById("productRevenueChart");
    if (productRevenueCanvas && document.getElementById("productRevenueData")) {
        const ctx = productRevenueCanvas.getContext("2d");
        const productRevenueData = JSON.parse(document.getElementById("productRevenueData").textContent);

        const labels = productRevenueData.map(item => item.productName);
        const revenues = productRevenueData.map(item => item.revenue);
        const quantities = productRevenueData.map(item => item.quantity);

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Revenue (đ)',
                        data: revenues,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                        yAxisID: 'y'
                    }, {
                        label: 'Quantity Sold',
                        data: quantities,
                        type: 'line',
                        borderColor: 'rgba(255, 206, 86, 1)',
                        backgroundColor: 'rgba(255, 206, 86, 0.2)',
                        borderWidth: 2,
                        yAxisID: 'y1'
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Top 10 Products by Revenue'}
                },
                scales: {
                    x: {
                        ticks: {
                            maxRotation: 45,
                            minRotation: 45
                        }
                    },
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return value.toLocaleString('vi-VN') + ' ₫'
;
                            }
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        beginAtZero: true,
                        grid: {
                            drawOnChartArea: false,
                        },
                        ticks: {
                            callback: function (value) {
                                return value + ' units';
                            }
                        }
                    }
                }
            }
        });
    }
});
        </script>

    </body>
</html>
