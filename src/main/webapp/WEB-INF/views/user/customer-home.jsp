<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Customer Home"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Welcome, Customer</h1>
<div class="d-flex gap-2">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalog">Browse Catalog</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/cart">View Cart</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/orders">Order History</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/inquiries">My Inquiries</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
