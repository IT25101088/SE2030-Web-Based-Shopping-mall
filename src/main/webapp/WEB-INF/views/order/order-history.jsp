<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My orders"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>My orders</h1>
        <p class="lede">Open an order to track each item or leave a review once it arrives.</p>
    </div>
</div>

<c:choose>
    <c:when test="${empty items}">
        <div class="empty">
            <h2>No orders yet</h2>
            <p>When you place an order, you can track it from here.</p>
            <a class="btn btn-primary" href="${ctx}/catalog">Browse products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Order</th>
                    <th scope="col">Product</th>
                    <th scope="col">Item</th>
                    <th scope="col" class="num">Qty</th>
                    <th scope="col" class="num">Price (Rs.)</th>
                    <th scope="col">Status</th>
                    <th scope="col"><span class="visually-hidden">Details</span></th>
                </tr>
                </thead>
                <tbody>
                <%-- One row per item, so an order with several products spans several rows. --%>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td class="fw-bold">#<c:out value="${item.order.id}"/></td>
                        <td>#<c:out value="${item.product.id}"/></td>
                        <td><c:out value="${item.product.name}"/></td>
                        <td class="num"><c:out value="${item.quantitySnapshot}"/></td>
                        <td class="num"><fmt:formatNumber value="${item.unitPriceSnapshot}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><span class="status status-${item.status}"><c:out value="${fn:replace(item.status, '_', ' ')}"/></span></td>
                        <td class="text-end"><a class="btn btn-sm btn-outline-secondary" href="${ctx}/orders/${item.order.id}">View order</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
