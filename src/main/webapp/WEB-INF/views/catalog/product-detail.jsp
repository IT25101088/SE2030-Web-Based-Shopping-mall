<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="${product.name}"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1><c:out value="${product.name}"/></h1>

<c:if test="${not empty product.imageUrl}">
    <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid mb-3" style="max-height: 400px;"/>
</c:if>

<p><c:out value="${product.description}"/></p>
<p class="fs-4">Price: <c:out value="${product.price}"/></p>
<p>
    <c:choose>
        <c:when test="${product.stockQuantity > 0}">
            <span class="badge bg-success">In stock (<c:out value="${product.stockQuantity}"/> available)</span>
        </c:when>
        <c:otherwise><span class="badge bg-secondary">Out of stock</span></c:otherwise>
    </c:choose>
</p>
<p>Sold by: <c:out value="${product.merchant.shopName}"/></p>
<c:if test="${not empty product.category}">
    <p>Category: <c:out value="${product.category.name}"/></p>
</c:if>

<form action="${pageContext.request.contextPath}/cart/add" method="post" class="mb-4">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="productId" value="${product.id}"/>
    <button type="submit" class="btn btn-primary" ${product.stockQuantity == 0 ? 'disabled' : ''}>Add to Cart</button>
</form>

<h2 class="h4">Reviews</h2>
<c:choose>
    <c:when test="${empty reviews}">
        <p>No reviews yet.</p>
    </c:when>
    <c:otherwise>
        <p>Average rating: <c:out value="${averageRating}"/> / 5 (<c:out value="${fn:length(reviews)}"/> review(s))</p>
        <c:forEach var="review" items="${reviews}">
            <div class="card mb-2">
                <div class="card-body">
                    <strong><c:out value="${review.rating}"/> / 5</strong>
                    - <c:out value="${review.customer.fullName}"/>
                    <p class="mb-0"><c:out value="${review.comment}"/></p>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary mt-3" href="${pageContext.request.contextPath}/catalog">Back to catalog</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
