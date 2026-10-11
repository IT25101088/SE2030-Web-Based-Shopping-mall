<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Reviews"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Reviews</h1>
        <p class="lede">What customers say about your products. A public reply shows you're listening.</p>
    </div>
    <div class="rating-summary mb-0">
        <strong><fmt:formatNumber value="${reputationScore}" maxFractionDigits="1"/></strong>
        <span>out of 5<br><span class="text-secondary">shop rating</span></span>
    </div>
</div>

<c:choose>
    <c:when test="${empty reviews}">
        <div class="empty">
            <h2>No reviews yet</h2>
            <p>Customers can review a product once it's delivered to them.</p>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="review" items="${reviews}">
            <article class="panel">
                <div class="review__meta">
                    <span class="stars" role="img" aria-label="${review.rating} out of 5 stars"><c:forEach begin="1" end="5" var="i"><span class="${i <= review.rating ? '' : 'off'}">&#9733;</span></c:forEach></span>
                    <strong><c:out value="${review.product.name}"/></strong>
                    <span class="text-secondary"><c:out value="${review.customer.fullName}"/></span>
                </div>
                <c:if test="${not empty review.comment}"><p class="mb-0"><c:out value="${review.comment}"/></p></c:if>

                <c:choose>
                    <c:when test="${not empty review.merchantResponse}">
                        <div class="review__reply">
                            <strong>Your reply:</strong> <c:out value="${review.merchantResponse}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <form action="${ctx}/merchant/feedback/${review.id}/respond" method="post" class="mt-3">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <label class="visually-hidden" for="reply${review.id}">Reply to this review</label>
                            <textarea id="reply${review.id}" name="responseText" class="form-control mb-2" placeholder="Write a public reply" required></textarea>
                            <button type="submit" class="btn btn-sm btn-primary">Post reply</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/merchant/dashboard">Back to my shop</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
