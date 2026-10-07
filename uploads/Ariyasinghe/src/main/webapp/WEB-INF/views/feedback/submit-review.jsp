<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Leave a Review"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-2">Leave a Review</h1>
        <p class="text-muted">Product: <c:out value="${orderItem.product.name}"/></p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <c:if test="${not empty fieldErrors}">
            <ul class="alert alert-danger">
                <c:forEach var="fieldError" items="${fieldErrors}">
                    <li><c:out value="${fieldError.defaultMessage}"/></li>
                </c:forEach>
            </ul>
        </c:if>

        <form action="${pageContext.request.contextPath}/reviews/submit" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="orderItemId" value="${form.orderItemId}"/>

            <div class="mb-3">
                <label class="form-label">Rating (1-5)</label>
                <input type="number" name="rating" class="form-control" min="1" max="5" value="${form.rating}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Comment</label>
                <textarea name="comment" class="form-control">${form.comment}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit Review</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/orders">Back to order history</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
