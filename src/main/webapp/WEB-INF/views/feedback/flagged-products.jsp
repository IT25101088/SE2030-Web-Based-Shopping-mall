<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Flagged products"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Flagged products</h1>
        <p class="lede">Products hidden by an employee, and products whose rating has fallen below the low-rating limit.</p>
    </div>
</div>

<h2>Flagged by an employee</h2>
<c:choose>
    <c:when test="${empty adminFlagged}">
        <div class="empty mb-5">
            <h2>Nothing hidden</h2>
            <p>To flag a product, open it from the Products page.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap mb-5">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Product</th>
                    <th scope="col">Shop</th>
                    <th scope="col">Reason</th>
                    <th scope="col"><span class="visually-hidden">Actions</span></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${adminFlagged}">
                    <tr>
                        <td class="fw-bold"><a href="${ctx}/catalog/${product.id}"><c:out value="${product.name}"/></a></td>
                        <td><c:out value="${product.merchant.shopName}"/></td>
                        <td><c:out value="${product.flagReason}"/></td>
                        <td>
                            <div class="row-actions justify-content-end">
                                <form action="${ctx}/employee/products/${product.id}/unflag" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-sm btn-success">Unflag</button>
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

<h2>Low rating</h2>
<c:choose>
    <c:when test="${empty products}">
        <div class="empty">
            <h2>Nothing flagged</h2>
            <p>Every product is rated above the low-rating limit.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Product</th>
                    <th scope="col">Shop</th>
                    <th scope="col"><span class="visually-hidden">Open</span></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td class="fw-bold"><c:out value="${product.name}"/></td>
                        <td><c:out value="${product.merchant.shopName}"/></td>
                        <td class="text-end"><a class="btn btn-sm btn-outline-secondary" href="${ctx}/catalog/${product.id}">View reviews</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/employee/dashboard">Back to mall admin</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
