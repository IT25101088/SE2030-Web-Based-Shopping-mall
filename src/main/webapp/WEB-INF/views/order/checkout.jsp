<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Checkout"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Checkout</h1>
        <p class="lede">Check your items, tell us where to deliver, and place your order.</p>
    </div>
</div>

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
        <div class="empty">
            <h2>Nothing to check out</h2>
            <p>Your cart is empty. Add products first, then come back here.</p>
            <a class="btn btn-primary" href="${ctx}/catalog">Browse products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row g-4">
            <div class="col-lg-7">
                <h2 class="h4 mb-3">Order summary</h2>
                <div class="table-wrap">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Product</th>
                            <th scope="col" class="num">Qty</th>
                            <th scope="col" class="num">Price (Rs.)</th>
                            <th scope="col" class="num">Subtotal (Rs.)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr>
                                <td><c:out value="${item.product.name}"/></td>
                                <td class="num"><c:out value="${item.quantity}"/></td>
                                <td class="num"><fmt:formatNumber value="${item.product.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td class="num"><fmt:formatNumber value="${item.product.price * item.quantity}" minFractionDigits="2" maxFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="table-total">
                    <span>Total</span>
                    <strong>Rs. <fmt:formatNumber value="${total}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                </div>
            </div>

            <div class="col-lg-5">
                <form action="${ctx}/checkout" method="post" class="panel">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <h2 class="h4 mb-3">Delivery</h2>
                    <div class="mb-3">
                        <label class="form-label" for="shippingAddress">Delivery address</label>
                        <textarea id="shippingAddress" name="shippingAddress" class="form-control" autocomplete="street-address" required><c:out value="${form.shippingAddress}"/></textarea>
                    </div>
                    <div class="alert alert-info" role="note">
                        Payment is simulated for this project. You won't be asked for card details and no money is charged.
                    </div>
                    <button type="submit" class="btn btn-saffron btn-lg w-100">Place order</button>
                </form>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<a class="back-link" href="${ctx}/cart">Back to cart</a>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
