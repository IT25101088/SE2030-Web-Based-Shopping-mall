<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Order History"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Order History</h1>

<c:choose>
    <c:when test="${empty orders}">
        <p>You haven't placed any orders yet.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Order #</th>
                <th>Total</th>
                <th>Status</th>
                <th></th>
            </tr>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td><c:out value="${order.id}"/></td>
                    <td><c:out value="${order.totalAmount}"/></td>
                    <td><span class="badge bg-info text-dark"><c:out value="${order.status}"/></span></td>
                    <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/orders/${order.id}">View</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/catalog">Continue shopping</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
