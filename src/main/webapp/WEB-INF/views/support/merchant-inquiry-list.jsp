<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Help requests"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Help requests</h1>
        <p class="lede">Customer questions the mall team has passed to your shop. Tell the mall team what you've done; they review it and reply to the customer.</p>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>

<c:choose>
    <c:when test="${empty inquiries}">
        <div class="empty">
            <h2>No help requests</h2>
            <p>When the mall team forwards a customer question to your shop, it will appear here.</p>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="inquiry" items="${inquiries}">
            <article class="panel">
                <div class="d-flex flex-wrap justify-content-between align-items-start gap-2 mb-2">
                    <div>
                        <h2 class="h4 mb-1"><c:out value="${inquiry.subject}"/></h2>
                        <p class="text-secondary mb-0">
                            From <c:out value="${inquiry.customer.fullName}"/><c:if test="${not empty inquiry.relatedOrder}">, about order #<c:out value="${inquiry.relatedOrder.id}"/></c:if><c:if test="${not empty inquiry.relatedProduct}">, about <c:out value="${inquiry.relatedProduct.name}"/></c:if>
                        </p>
                    </div>
                    <span class="status status-${inquiry.status}"><c:out value="${fn:replace(inquiry.status, '_', ' ')}"/></span>
                </div>
                <p><c:out value="${inquiry.message}"/></p>

                <c:set var="replies" value="${inquiry.responses}"/>
                <%@ include file="/WEB-INF/views/support/inquiry-thread.jspf" %>

                <form action="${ctx}/merchant/inquiries/${inquiry.id}/respond" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <label class="form-label" for="reply${inquiry.id}">Your answer for the mall team</label>
                    <textarea id="reply${inquiry.id}" name="responseText" class="form-control mb-2" placeholder="What did you do, or what should the customer be told?" required></textarea>
                    <p class="form-hint">The customer won't see this. Sending it hands the request back to the mall team.</p>
                    <button type="submit" class="btn btn-sm btn-primary">Send to mall team</button>
                </form>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/merchant/dashboard">Back to my shop</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
