<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Inquiries"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">My Inquiries</h1>

<a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/inquiries/new">Submit a new inquiry</a>

<c:choose>
    <c:when test="${empty inquiries}">
        <p>You haven't submitted any inquiries yet.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Subject</th>
                <th>Status</th>
                <th>Order</th>
                <th>Product</th>
            </tr>
            <c:forEach var="inquiry" items="${inquiries}">
                <tr>
                    <td><c:out value="${inquiry.subject}"/></td>
                    <td><span class="badge bg-info text-dark"><c:out value="${inquiry.status}"/></span></td>
                    <td>
                        <c:if test="${not empty inquiry.relatedOrder}">#<c:out value="${inquiry.relatedOrder.id}"/></c:if>
                    </td>
                    <td>
                        <c:if test="${not empty inquiry.relatedProduct}"><c:out value="${inquiry.relatedProduct.name}"/></c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/customer/home">Back to home</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
