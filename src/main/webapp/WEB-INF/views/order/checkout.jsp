<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Checkout"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Checkout</h1>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>
<c:if test="${not empty fieldErrors}">
    <ul class="alert alert-danger">
        <c:forEach var="fieldError" items="${fieldErrors}">
            <li><c:out value="${fieldError.defaultMessage}"/></li>
        </c:forEach>
    </ul>
</c:if>

<c:choose>
    <c:when test="${empty items}">
        <p>Your cart is empty. <a href="${pageContext.request.contextPath}/catalog">Browse the catalog</a>.</p>
    </c:when>
    <c:otherwise>
        <h2 class="h4">Order Summary</h2>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Subtotal</th>
            </tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.product.name}"/></td>
                    <td><c:out value="${item.quantity}"/></td>
                    <td><c:out value="${item.product.price}"/></td>
                    <td><c:out value="${item.product.price * item.quantity}"/></td>
                </tr>
            </c:forEach>
        </table>
        <p class="fs-5"><strong>Total: <c:out value="${total}"/></strong></p>

        <form action="${pageContext.request.contextPath}/checkout" method="post" class="col-md-6">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="mb-3">
                <label class="form-label">Shipping Address</label>
                <textarea name="shippingAddress" class="form-control" required>${form.shippingAddress}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">Place Order</button>
        </form>
    </c:otherwise>
</c:choose>

<p class="mt-3"><a href="${pageContext.request.contextPath}/cart">Back to cart</a></p>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
