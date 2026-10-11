<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Pending approvals"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Pending approvals</h1>
        <p class="lede">New shops can't list products until you approve them.</p>
    </div>
</div>

<c:choose>
    <c:when test="${empty merchants}">
        <div class="empty">
            <h2>No shops waiting</h2>
            <p>New shop registrations will appear here for review.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Shop</th>
                    <th scope="col">Owner</th>
                    <th scope="col">Email</th>
                    <th scope="col">Decision</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="merchant" items="${merchants}">
                    <tr>
                        <c:set var="shop" value="${merchant}"/>
                        <td class="fw-bold"><span class="shop-with-logo"><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></span>
                            <%-- "New" = registered since this employee last opened this page. --%>
                            <c:if test="${empty previouslySeenAt or merchant.createdAt.isAfter(previouslySeenAt)}"> <span class="status status-PENDING">New</span></c:if>
                        </td>
                        <td><c:out value="${merchant.fullName}"/></td>
                        <td><c:out value="${merchant.email}"/></td>
                        <td>
                            <div class="row-actions">
                                <form action="${ctx}/employee/merchants/${merchant.id}/approve" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                </form>
                                <form action="${ctx}/employee/merchants/${merchant.id}/reject" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/employee/merchants">Back to all shops</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
