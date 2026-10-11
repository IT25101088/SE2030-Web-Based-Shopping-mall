<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Products"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Products</h1>
        <p class="lede">Everything your shop has listed. Removed products leave the catalog but stay in past orders.</p>
    </div>
    <a class="btn btn-saffron" href="${ctx}/merchant/products/new">Add a product</a>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="empty">
            <h2>No products yet</h2>
            <p>Add your first product and it goes on sale right away.</p>
            <a class="btn btn-primary" href="${ctx}/merchant/products/new">Add a product</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Product</th>
                    <th scope="col" class="num">Price (Rs.)</th>
                    <th scope="col" class="num">Stock</th>
                    <th scope="col">Status</th>
                    <th scope="col"><span class="visually-hidden">Actions</span></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td class="fw-bold"><c:out value="${product.name}"/></td>
                        <td class="num"><fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td class="num"><c:out value="${product.stockQuantity}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${product.active}"><span class="status status-active">On sale</span></c:when>
                                <c:otherwise><span class="status status-hidden">Removed</span></c:otherwise>
                            </c:choose>
                            <c:if test="${product.flaggedForReview}"><span class="status status-flagged">Low rating</span></c:if>
                            <c:if test="${product.flaggedByAdmin}"><span class="status status-flagged">Hidden by mall admin</span>
                                <div class="form-hint">Reason: <c:out value="${product.flagReason}"/></div></c:if>
                        </td>
                        <td>
                            <div class="row-actions justify-content-end">
                                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/merchant/products/${product.id}/edit">Edit</a>
                                <c:if test="${product.active}">
                                    <form action="${ctx}/merchant/products/${product.id}/delete" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="btn btn-sm btn-danger">Remove</button>
                                    </form>
                                </c:if>
                            </div>
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
