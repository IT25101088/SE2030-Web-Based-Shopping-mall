<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Order #${order.id}"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Order #<c:out value="${order.id}"/></h1>
        <p class="lede">Delivering to <c:out value="${order.shippingAddress}"/></p>
    </div>
    <div class="text-end">
        <div class="text-secondary">Total</div>
        <div class="product__price m-0">Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/></div>
    </div>
</div>

<%--
    Progress tracker. Walks the stages in order; every stage up to and including
    the order's current status is marked done. "passed" flips to true once we've
    gone beyond the current status, so later stages stay grey.
--%>
<c:choose>
    <c:when test="${order.status == 'CANCELLED'}">
        <div class="alert alert-danger">This order was cancelled.</div>
    </c:when>
    <c:otherwise>
        <ol class="track" aria-label="Order progress">
            <c:set var="passed" value="${false}"/>
            <c:forEach var="stage" items="PENDING,CONFIRMED,SHIPPED,DELIVERED">
                <li class="${passed ? '' : 'is-done'} ${stage == order.status ? 'is-current' : ''}"
                    ${stage == order.status ? 'aria-current="step"' : ''}>
                    <span class="status-word"><c:out value="${stage}"/></span>
                </li>
                <c:if test="${stage == order.status}"><c:set var="passed" value="${true}"/></c:if>
            </c:forEach>
        </ol>
    </c:otherwise>
</c:choose>

<h2 class="h4 mt-4 mb-3">Items</h2>
<p class="text-secondary">Each shop ships its own items, so they can move at different speeds.</p>
<div class="table-wrap">
    <table class="table align-middle">
        <thead>
        <tr>
            <th scope="col">Product</th>
            <th scope="col" class="num">Qty</th>
            <th scope="col" class="num">Price (Rs.)</th>
            <th scope="col">Status</th>
            <th scope="col"><span class="visually-hidden">Review</span></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${items}">
            <tr>
                <td class="fw-bold"><c:out value="${item.product.name}"/></td>
                <td class="num"><c:out value="${item.quantitySnapshot}"/></td>
                <td class="num"><fmt:formatNumber value="${item.unitPriceSnapshot}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td><span class="status status-${item.status}"><c:out value="${fn:replace(item.status, '_', ' ')}"/></span></td>
                <td class="text-end">
                    <c:if test="${item.status == 'DELIVERED'}">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/reviews/submit?orderItemId=${item.id}">Write a review</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<a class="back-link" href="${ctx}/orders">Back to my orders</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
