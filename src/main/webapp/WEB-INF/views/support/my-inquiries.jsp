<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Help requests"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Help requests</h1>
        <p class="lede">Questions you've sent about orders or products, where each one stands, and the replies you've had.</p>
    </div>
    <a class="btn btn-saffron" href="${ctx}/inquiries/new">Ask a question</a>
</div>

<c:choose>
    <c:when test="${empty inquiries}">
        <div class="empty">
            <h2>No help requests</h2>
            <p>Problem with an order or a product? Ask us and we'll reply here.</p>
            <a class="btn btn-primary" href="${ctx}/inquiries/new">Ask a question</a>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="inquiry" items="${inquiries}">
            <article class="panel">
                <div class="d-flex flex-wrap justify-content-between align-items-start gap-2 mb-2">
                    <div>
                        <h2 class="h4 mb-1"><c:out value="${inquiry.subject}"/></h2>
                        <p class="text-secondary mb-0">
                            <c:if test="${not empty inquiry.relatedOrder}">About <a href="${ctx}/orders/${inquiry.relatedOrder.id}">order #<c:out value="${inquiry.relatedOrder.id}"/></a></c:if>
                            <c:if test="${not empty inquiry.relatedOrder and not empty inquiry.relatedProduct}">, </c:if>
                            <c:if test="${not empty inquiry.relatedProduct}"><c:if test="${empty inquiry.relatedOrder}">About </c:if><c:out value="${inquiry.relatedProduct.name}"/></c:if>
                        </p>
                    </div>
                    <span class="status status-${inquiry.status}"><c:out value="${fn:replace(inquiry.status, '_', ' ')}"/></span>
                </div>
                <p><c:out value="${inquiry.message}"/></p>

                <c:set var="replies" value="${inquiry.customerVisibleResponses}"/>
                <%@ include file="/WEB-INF/views/support/inquiry-thread.jspf" %>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
