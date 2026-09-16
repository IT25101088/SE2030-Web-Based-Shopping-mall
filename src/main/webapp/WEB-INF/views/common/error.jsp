<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Error"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="alert alert-danger">
    <h1 class="h4">Something went wrong (<c:out value="${statusCode}"/>)</h1>
    <p class="mb-0"><c:out value="${message}"/></p>
</div>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/">Back to home</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
