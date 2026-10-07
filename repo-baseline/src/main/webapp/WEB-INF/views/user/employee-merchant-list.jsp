<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Pending Merchants"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Pending Merchant Approvals</h1>

<c:choose>
    <c:when test="${empty merchants}">
        <p>No merchants are currently waiting for approval.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Shop Name</th>
                <th>Owner</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="merchant" items="${merchants}">
                <tr>
                    <td><c:out value="${merchant.shopName}"/></td>
                    <td><c:out value="${merchant.fullName}"/></td>
                    <td><c:out value="${merchant.email}"/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/employee/merchants/${merchant.id}/approve"
                              method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-success btn-sm">Approve</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/employee/merchants/${merchant.id}/reject"
                              method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/employee/merchants/${merchant.id}/suspend"
                              method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-warning btn-sm">Suspend</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
