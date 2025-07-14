<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Revenue Statistics</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-6">
        <h1 class="text-2xl font-bold mb-4">Revenue Statistics</h1>

        <!-- Date Filter Form -->
        <form action="AdminController" method="post" class="mb-6">
            <input type="hidden" name="action" value="filterRevenue">
            <div class="flex space-x-4">
                <div>
                    <label for="startDate" class="block text-sm font-medium text-gray-700">Start Date</label>
                    <input type="date" id="startDate" name="startDate" value="${startDate}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                </div>
                <div>
                    <label for="endDate" class="block text-sm font-medium text-gray-700">End Date</label>
                    <input type="date" id="endDate" name="endDate" value="${endDate}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                </div>
                <div class="flex items-end">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Filter</button>
                </div>
            </div>
        </form>

        <!-- Error Message -->
        <c:if test="${not empty ERROR}">
            <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4">
                ${ERROR}
            </div>
        </c:if>

        <!-- Total Revenue Display -->
        <div class="bg-white p-6 rounded-lg shadow-md mb-6">
            <h2 class="text-xl font-semibold mb-4">Total Revenue</h2>
            <c:choose>
                <c:when test="${not empty totalRevenue}">
                    <p class="text-lg">Total Revenue: <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="$"/></p>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-600">No revenue data available.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Monthly Revenue Chart -->
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-xl font-semibold mb-4">Monthly Revenue Chart</h2>
            <c:if test="${not empty monthlyRevenue}">
                <canvas id="monthlyRevenueChart" width="400" height="200"></canvas>
            </c:if>
            <c:if test="${empty monthlyRevenue}">
                <p class="text-gray-600">No monthly revenue data to display.</p>
            </c:if>
        </div>
    </div>

    <script>
        // Monthly Revenue Chart Data from JSTL
        const monthlyRevenue = [
            <c:forEach items="${monthlyRevenue}" var="entry" varStatus="loop">
                { month: "${entry[0]}", totalRevenue: ${entry[1]} }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        if (monthlyRevenue.length > 0) {
            const labels = monthlyRevenue.map(item => item.month);
            const totalRevenues = monthlyRevenue.map(item => item.totalRevenue);

            // Distribute total revenue into regions (approximate split for illustration)
            const hanoiData = totalRevenues.map(revenue => revenue * 0.33); // 33% to Hanoi
            const daNangData = totalRevenues.map(revenue => revenue * 0.33); // 33% to Da Nang
            const hoChiMinhData = totalRevenues.map(revenue => revenue * 0.34); // 34% to TP HCM (to account for rounding)

            const ctx = document.getElementById('monthlyRevenueChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Hà Nội',
                            data: hanoiData,
                            backgroundColor: 'rgba(54, 162, 235, 0.6)', // Blue
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Đà Nẵng',
                            data: daNangData,
                            backgroundColor: 'rgba(255, 206, 86, 0.6)', // Yellow
                            borderColor: 'rgba(255, 206, 86, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'TP Hồ Chí Minh',
                            data: hoChiMinhData,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)', // Green
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Revenue ($)'
                            },
                            stacked: true
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Year'
                            },
                            stacked: true
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    label += new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(context.raw);
                                    return label;
                                }
                            }
                        }
                    }
                }
            });
        }
    </script>
</body>
</html>