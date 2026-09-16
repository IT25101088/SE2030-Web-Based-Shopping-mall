<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Cart"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">My Cart</h1>

<c:choose>
    <c:when test="${empty items}">
        <p>Your cart is empty. <a href="${pageContext.request.contextPath}/catalog">Browse the catalog</a>.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Product</th>
                <th>Unit Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.product.name}"/></td>
                    <td><c:out value="${item.product.price}"/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/cart/${item.id}/update" method="post"
                              class="d-flex gap-2">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="number" name="quantity" value="${item.quantity}" min="0" class="form-control form-control-sm" style="width:5em;"/>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Update</button>
                        </form>
                    </td>
                    <td><c:out value="${item.product.price * item.quantity}"/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/cart/${item.id}/remove" method="post"
                              class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-sm btn-danger">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p class="fs-5"><strong>Total: <c:out value="${total}"/></strong></p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/checkout">Proceed to checkout</a>
    </c:otherwise>
</c:choose>

<p class="mt-3"><a href="${pageContext.request.contextPath}/catalog">Continue shopping</a></p>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
