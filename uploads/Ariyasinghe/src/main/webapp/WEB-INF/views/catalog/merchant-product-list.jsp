<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Products"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">My Products</h1>

<a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/merchant/products/new">Add a new product</a>

<c:choose>
    <c:when test="${empty products}">
        <p>You haven't listed any products yet.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td><c:out value="${product.name}"/></td>
                    <td><c:out value="${product.price}"/></td>
                    <td><c:out value="${product.stockQuantity}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${product.active}"><span class="badge bg-success">Active</span></c:when>
                            <c:otherwise><span class="badge bg-secondary">Hidden</span></c:otherwise>
                        </c:choose>
                        <c:if test="${product.flaggedForReview}"><span class="badge bg-danger">Flagged</span></c:if>
                    </td>
                    <td>
                        <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/products/${product.id}/edit">Edit</a>
                        <c:if test="${product.active}">
                            <form action="${pageContext.request.contextPath}/merchant/products/${product.id}/delete"
                                  method="post" class="d-inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-sm btn-danger">Remove</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/merchant/dashboard">Back to dashboard</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
