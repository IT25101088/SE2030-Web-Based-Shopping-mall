<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Ask a question"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-1">Ask a question</h1>
    <p class="text-secondary mb-4">Our team replies to every question. Tell us which order or product it's about.</p>

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

    <form action="${ctx}/inquiries" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="mb-3">
            <label class="form-label" for="subject">Subject</label>
            <input id="subject" type="text" name="subject" class="form-control" value="<c:out value='${form.subject}'/>" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="message">Your question</label>
            <textarea id="message" name="message" class="form-control" required><c:out value="${form.message}"/></textarea>
        </div>

        <fieldset class="mb-4">
            <legend class="form-label fs-6">What is it about?</legend>
            <p class="form-hint mt-0 mb-2">Choose an order, enter a product number, or both.</p>
            <div class="row g-3">
                <div class="col-sm-6">
                    <label class="form-label fw-normal" for="relatedOrderId">Order</label>
                    <%-- Lists only this customer's own orders (see InquiryService.getOrdersForCurrentCustomer). --%>
                    <select id="relatedOrderId" name="relatedOrderId" class="form-select">
                        <option value="">Not about an order</option>
                        <c:forEach var="order" items="${orders}">
                            <option value="${order.id}" ${form.relatedOrderId == order.id ? 'selected' : ''}>
                                Order #<c:out value="${order.id}"/> &middot; Rs <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/> &middot; <c:out value="${fn:toLowerCase(fn:replace(order.status, '_', ' '))}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty orders}">
                        <p class="form-hint mb-0">You haven't placed any orders yet.</p>
                    </c:if>
                </div>
                <div class="col-sm-6">
                    <label class="form-label fw-normal" for="relatedProductId">Product number</label>
                    <input id="relatedProductId" type="number" name="relatedProductId" class="form-control" value="${form.relatedProductId}"/>
                </div>
            </div>
        </fieldset>
        <button type="submit" class="btn btn-primary">Send question</button>
    </form>

    <a class="back-link" href="${ctx}/inquiries">Back to help requests</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
