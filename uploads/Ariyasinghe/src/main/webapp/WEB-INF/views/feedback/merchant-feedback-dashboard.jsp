<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Feedback Dashboard"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-2">Feedback Dashboard</h1>
<p class="fs-5">Reputation score: <span class="badge bg-info text-dark"><c:out value="${reputationScore}"/> / 5</span></p>

<h2 class="h4 mt-4">Reviews on your products</h2>
<c:choose>
    <c:when test="${empty reviews}">
        <p>No reviews yet.</p>
    </c:when>
    <c:otherwise>
        <c:forEach var="review" items="${reviews}">
            <div class="card mb-3">
                <div class="card-body">
                    <p class="mb-1">
                        <strong><c:out value="${review.product.name}"/></strong>
                        - <c:out value="${review.rating}"/> / 5
                        (<c:out value="${review.customer.fullName}"/>)
                    </p>
                    <p><c:out value="${review.comment}"/></p>

                    <c:set var="response" value="${responses[review.id]}"/>
                    <c:choose>
                        <c:when test="${not empty response}">
                            <p class="mb-0"><em>Your response:</em> <c:out value="${response.responseText}"/></p>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/merchant/feedback/${review.id}/respond"
                                  method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <textarea name="responseText" class="form-control mb-2" placeholder="Write a response..." required></textarea>
                                <button type="submit" class="btn btn-sm btn-primary">Respond</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/dashboard">Back to dashboard</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
