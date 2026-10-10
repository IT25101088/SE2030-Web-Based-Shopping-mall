<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Help requests"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Open help requests</h1>
        <p class="lede">Answer a request yourself or forward it to the shop involved. When a shop answers, the request comes back here for you to review and resolve.</p>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>

<c:choose>
    <c:when test="${empty inquiries}">
        <div class="empty">
            <h2>No open requests</h2>
            <p>New customer questions will appear here.</p>
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
                        <p class="text-secondary mb-0">
                            Currently with
                            <c:choose>
                                <c:when test="${not empty inquiry.merchant}"><strong><c:out value="${inquiry.merchant.shopName}"/></strong></c:when>
                                <c:otherwise><strong>Mall support</strong></c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <span class="status status-${inquiry.status}"><c:out value="${fn:replace(inquiry.status, '_', ' ')}"/></span>
                </div>
                <p><c:out value="${inquiry.message}"/></p>

                <c:set var="replies" value="${inquiry.responses}"/>
                <%@ include file="/WEB-INF/views/support/inquiry-thread.jspf" %>

                <form action="${ctx}/employee/inquiries/${inquiry.id}/respond" method="post" class="mb-3">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <label class="visually-hidden" for="reply${inquiry.id}">Reply to this request</label>
                    <textarea id="reply${inquiry.id}" name="responseText" class="form-control mb-2" placeholder="Write a reply to the customer" required></textarea>
                    <button type="submit" class="btn btn-sm btn-primary">Send reply</button>
                </form>

                <%-- Shops this request is likely about (the product's shop, or the shops in the order),
                     from InquiryService.suggestShops. One click forwards; the employee still decides. --%>
                <c:set var="suggestions" value="${suggestedShops[inquiry.id]}"/>
                <c:if test="${not empty suggestions}">
                    <div class="d-flex flex-wrap align-items-center gap-2 mb-3">
                        <span class="text-secondary">Suggested shop${fn:length(suggestions) > 1 ? 's' : ''}:</span>
                        <c:forEach var="shop" items="${suggestions}">
                            <c:choose>
                                <c:when test="${not empty inquiry.merchant and inquiry.merchant.id == shop.id}">
                                    <span class="status status-IN_PROGRESS">With <c:out value="${shop.shopName}"/></span>
                                </c:when>
                                <c:otherwise>
                                    <form action="${ctx}/employee/inquiries/${inquiry.id}/forward" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="merchantId" value="${shop.id}"/>
                                        <button type="submit" class="btn btn-sm btn-primary">Forward to <c:out value="${shop.shopName}"/></button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </c:if>

                <div class="d-flex flex-wrap align-items-end gap-2">
                    <form action="${ctx}/employee/inquiries/${inquiry.id}/forward" method="post" class="d-flex gap-2 align-items-end">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div>
                            <label class="form-label mb-1" for="shop${inquiry.id}">${empty suggestions ? 'Forward to shop' : 'Or another shop'}</label>
                            <select id="shop${inquiry.id}" name="merchantId" class="form-select form-select-sm" required>
                                <option value="">Choose a shop</option>
                                <c:forEach var="shop" items="${merchants}">
                                    <option value="${shop.id}"><c:out value="${shop.shopName}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-sm btn-outline-primary">Forward</button>
                    </form>
                    <c:if test="${not empty inquiry.merchant}">
                        <form action="${ctx}/employee/inquiries/${inquiry.id}/take-back" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Take back from shop</button>
                        </form>
                    </c:if>
                    <form action="${ctx}/employee/inquiries/${inquiry.id}/resolve" method="post" class="ms-auto">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-sm btn-success">Mark resolved</button>
                    </form>
                </div>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/employee/dashboard">Back to mall admin</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
