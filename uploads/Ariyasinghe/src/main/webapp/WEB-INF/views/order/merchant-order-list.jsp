<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Order Items"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">My Order Items</h1>

<%-- Each row is one OrderItem, not a whole Order -- an order can include
     other merchants' items too, so you only ever see and update your own. --%>
<c:choose>
    <c:when test="${empty orderItems}">
        <p>No orders yet for your products.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Order #</th>
                <th>Product</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Status</th>
                <th>Update Status</th>
            </tr>
            <c:forEach var="item" items="${orderItems}">
                <tr>
                    <td><c:out value="${item.order.id}"/></td>
                    <td><c:out value="${item.product.name}"/></td>
                    <td><c:out value="${item.quantitySnapshot}"/></td>
                    <td><c:out value="${item.unitPriceSnapshot}"/></td>
                    <td><span class="badge bg-info text-dark"><c:out value="${item.status}"/></span></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/merchant/orders/items/${item.id}/status"
                              method="post" class="d-flex gap-2">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <select name="status" class="form-select form-select-sm">
                                <c:forEach var="s" items="${statuses}">
                                    <option value="${s}" ${s == item.status ? 'selected' : ''}>
                                        <c:out value="${s}"/>
                                    </option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/dashboard">Back to dashboard</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
