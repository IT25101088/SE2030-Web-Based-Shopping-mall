<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Open Inquiries"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Open Inquiries</h1>

<c:choose>
    <c:when test="${empty inquiries}">
        <p>No open inquiries.</p>
    </c:when>
    <c:otherwise>
        <c:forEach var="inquiry" items="${inquiries}">
            <div class="card mb-3">
                <div class="card-body">
                    <p class="mb-1">
                        <strong><c:out value="${inquiry.subject}"/></strong>
                        <span class="badge bg-info text-dark"><c:out value="${inquiry.status}"/></span>
                        (from <c:out value="${inquiry.customer.fullName}"/>)
                    </p>
                    <p><c:out value="${inquiry.message}"/></p>
                    <c:if test="${not empty inquiry.relatedOrder}">
                        <p class="text-muted">Related order: #<c:out value="${inquiry.relatedOrder.id}"/></p>
                    </c:if>
                    <c:if test="${not empty inquiry.relatedProduct}">
                        <p class="text-muted">Related product: <c:out value="${inquiry.relatedProduct.name}"/></p>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/employee/inquiries/${inquiry.id}/respond"
                          method="post" class="mb-2">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <textarea name="responseText" class="form-control mb-2" placeholder="Write a response..." required></textarea>
                        <button type="submit" class="btn btn-sm btn-primary">Respond</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/employee/inquiries/${inquiry.id}/resolve"
                          method="post" class="d-inline">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-sm btn-success">Mark Resolved</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/employee/dashboard">Back to dashboard</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
