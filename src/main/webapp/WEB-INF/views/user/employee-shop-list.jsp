<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Shops"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Shops</h1>
        <p class="lede">Every shop in the mall. Suspending a shop hides its products and stops the owner logging in.</p>
    </div>
    <a class="btn btn-saffron" href="${ctx}/employee/merchants/pending">Pending approvals<c:if test="${unseenPendingShops > 0}"> <span class="nav-badge"><c:out value="${unseenPendingShops}"/><span class="visually-hidden"> new</span></span></c:if></a>
</div>

<c:choose>
    <c:when test="${empty merchants}">
        <div class="empty">
            <h2>No shops yet</h2>
            <p>Shops appear here as soon as someone registers one.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Shop</th>
                    <th scope="col">Owner</th>
                    <th scope="col">Email</th>
                    <th scope="col">Joined</th>
                    <th scope="col">Status</th>
                    <th scope="col"><span class="visually-hidden">Actions</span></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="merchant" items="${merchants}">
                    <tr>
                        <c:set var="shop" value="${merchant}"/>
                        <td class="fw-bold"><span class="shop-with-logo"><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></span></td>
                        <td><c:out value="${merchant.fullName}"/></td>
                        <td><c:out value="${merchant.email}"/></td>
                        <%-- createdAt is an Instant like 2026-10-07T11:53:33Z; the first 10 characters are the date. --%>
                        <td><c:out value="${fn:substring(merchant.createdAt, 0, 10)}"/></td>
                        <td><span class="status status-${merchant.verificationStatus}"><c:out value="${merchant.verificationStatus}"/></span></td>
                        <td>
                            <div class="row-actions justify-content-end">
                                <c:choose>
                                    <c:when test="${merchant.verificationStatus == 'APPROVED'}">
                                        <form action="${ctx}/employee/merchants/${merchant.id}/suspend" method="post">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-danger">Suspend</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${merchant.verificationStatus == 'SUSPENDED'}">
                                        <form action="${ctx}/employee/merchants/${merchant.id}/reinstate" method="post">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-success">Reinstate</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${merchant.verificationStatus == 'PENDING'}">
                                        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/employee/merchants/pending">Review</a>
                                    </c:when>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/employee/dashboard">Back to mall admin</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
