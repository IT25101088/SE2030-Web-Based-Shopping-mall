<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Access denied"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="empty">
    <h1 class="h2">This area isn't open to your account</h1>
    <p><c:out value="${message}"/></p>
    <a class="btn btn-primary" href="${ctx}/">Go to the mall entrance</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
