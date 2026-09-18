<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:if test="${not empty pageTitle}"><c:out value="${pageTitle}"/> - </c:if>Shopping Mall</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/catalog">Shopping Mall</a>
        <div class="navbar-nav me-auto flex-row flex-wrap">
            <a class="nav-link me-3" href="${pageContext.request.contextPath}/catalog">Catalog</a>
            <a class="nav-link me-3" href="${pageContext.request.contextPath}/faq">FAQ</a>
            <sec:authorize access="hasRole('CUSTOMER')">
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/customer/home">Home</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/cart">Cart</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/orders">My Orders</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/inquiries">My Inquiries</a>
            </sec:authorize>
            <sec:authorize access="hasRole('MERCHANT')">
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/merchant/dashboard">Dashboard</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/merchant/products">My Products</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/merchant/orders">My Orders</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/merchant/feedback">Feedback</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/merchant/profile">Shop Profile</a>
            </sec:authorize>
            <sec:authorize access="hasRole('PLATFORM_EMPLOYEE')">
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/employee/dashboard">Dashboard</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/employee/merchants/pending">Pending Merchants</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/employee/categories">Categories</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/employee/inquiries">Inquiries</a>
                <a class="nav-link me-3" href="${pageContext.request.contextPath}/employee/flagged-products">Flagged Products</a>
            </sec:authorize>
        </div>
        <div class="d-flex align-items-center">
            <sec:authorize access="isAuthenticated()">
                <span class="text-light small me-3"><sec:authentication property="name"/></span>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-outline-light btn-sm">Log Out</button>
                </form>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">Log In</a>
            </sec:authorize>
        </div>
    </div>
</nav>
<div class="container mb-5">
