<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Orders"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Orders</h1>
        <p class="lede">Each row is one of your products in a customer's order. Update its status as you pack and ship it.</p>
    </div>
</div>

<%-- Each row is one OrderItem, not a whole Order -- an order can include
     other merchants' items too, so you only ever see and update your own. --%>
<c:choose>
    <c:when test="${empty orderItems}">
        <div class="empty">
            <h2>No orders yet</h2>
            <p>When a customer buys one of your products, it shows up here.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Order</th>
                    <th scope="col">Product</th>
                    <th scope="col" class="num">Qty</th>
                    <th scope="col" class="num">Price (Rs.)</th>
                    <th scope="col">Status</th>
                    <th scope="col">Change status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${orderItems}">
                    <tr>
                        <td class="fw-bold">#<c:out value="${item.order.id}"/></td>
                        <td><c:out value="${item.product.name}"/></td>
                        <td class="num"><c:out value="${item.quantitySnapshot}"/></td>
                        <td class="num"><fmt:formatNumber value="${item.unitPriceSnapshot}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><span class="status status-${item.status}"><c:out value="${fn:replace(item.status, '_', ' ')}"/></span></td>
                        <td>
                            <form action="${ctx}/merchant/orders/items/${item.id}/status" method="post" class="d-flex gap-2">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <label class="visually-hidden" for="status${item.id}">New status</label>
                                <select id="status${item.id}" name="status" class="form-select form-select-sm">
                                    <c:forEach var="s" items="${statuses}">
                                        <option value="${s}" ${s == item.status ? 'selected' : ''}><c:out value="${s}"/></option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn btn-sm btn-outline-secondary">Save</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/merchant/dashboard">Back to my shop</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
