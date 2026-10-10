<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Order Detail"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Order #<c:out value="${order.id}"/></h1>

<p>Status: <span class="badge bg-info text-dark"><c:out value="${order.status}"/></span></p>
<p>Shipping Address: <c:out value="${order.shippingAddress}"/></p>
<p>Total: <c:out value="${order.totalAmount}"/></p>

<h2 class="h4">Items</h2>
<table class="table table-striped table-bordered bg-white">
    <tr>
        <th>Product</th>
        <th>Quantity</th>
        <th>Unit Price</th>
        <th>Status</th>
        <th></th>
    </tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td><c:out value="${item.product.name}"/></td>
            <td><c:out value="${item.quantitySnapshot}"/></td>
            <td><c:out value="${item.unitPriceSnapshot}"/></td>
            <td><span class="badge bg-info text-dark"><c:out value="${item.status}"/></span></td>
            <td>
                <c:if test="${item.status == 'DELIVERED'}">
                    <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/reviews/submit?orderItemId=${item.id}">Leave a review</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/orders">Back to order history</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
