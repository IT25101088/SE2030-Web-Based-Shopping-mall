<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Employee Dashboard"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Welcome, Platform Employee</h1>
<div class="d-flex gap-2 flex-wrap">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/employee/merchants/pending">Pending Merchants</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/employee/categories">Categories</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/employee/inquiries">Inquiries</a>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/employee/flagged-products">Flagged Products</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
