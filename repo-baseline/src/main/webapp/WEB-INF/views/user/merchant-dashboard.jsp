<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Merchant Dashboard"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Welcome, Merchant</h1>
<div class="d-flex gap-2 flex-wrap">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/merchant/products">My Products</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/orders">My Orders</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/feedback">Feedback</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/profile">Edit Shop Profile</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
