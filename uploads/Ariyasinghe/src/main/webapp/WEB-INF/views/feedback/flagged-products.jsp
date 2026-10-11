<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Flagged Products"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-2">Flagged Products</h1>
<p class="text-muted">Products whose average rating has dropped below the low-rating threshold, for platform employee attention.</p>

<c:choose>
    <c:when test="${empty products}">
        <p>No products are currently flagged.</p>
    </c:when>
    <c:otherwise>
        <ul class="list-group">
            <c:forEach var="product" items="${products}">
                <li class="list-group-item"><c:out value="${product.name}"/></li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
