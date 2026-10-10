<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Cart"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Your cart</h1>
        <p class="lede">Items from every shop check out together.</p>
    </div>
</div>

<c:choose>
    <c:when test="${empty items}">
        <div class="empty">
            <h2>Your cart is empty</h2>
            <p>Add products from any shop and they'll wait here until you check out.</p>
            <a class="btn btn-primary" href="${ctx}/catalog">Browse products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-wrap">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th scope="col">Product</th>
                    <th scope="col" class="num">Price (Rs.)</th>
                    <th scope="col">Quantity</th>
                    <th scope="col" class="num">Subtotal (Rs.)</th>
                    <th scope="col"><span class="visually-hidden">Remove</span></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td>
                            <a class="fw-bold" href="${ctx}/catalog/${item.product.id}"><c:out value="${item.product.name}"/></a>
                            <c:set var="shop" value="${item.product.merchant}"/>
                            <div class="text-secondary small shop-with-logo"><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></div>
                        </td>
                        <td class="num"><fmt:formatNumber value="${item.product.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>
                            <%-- Setting quantity to 0 removes the item. --%>
                            <form action="${ctx}/cart/${item.id}/update" method="post" class="d-flex gap-2">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <label class="visually-hidden" for="qty${item.id}">Quantity</label>
                                <input id="qty${item.id}" type="number" name="quantity" value="${item.quantity}" min="0"
                                       class="form-control form-control-sm" style="width:5rem"/>
                                <button type="submit" class="btn btn-sm btn-outline-secondary">Update</button>
                            </form>
                        </td>
                        <td class="num fw-bold"><fmt:formatNumber value="${item.product.price * item.quantity}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td class="text-end">
                            <form action="${ctx}/cart/${item.id}/remove" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn-link-quiet">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="table-total">
            <span>Total</span>
            <strong>Rs. <fmt:formatNumber value="${total}" minFractionDigits="2" maxFractionDigits="2"/></strong>
        </div>
        <div class="d-flex flex-wrap justify-content-end gap-2 mt-3">
            <a class="btn btn-outline-secondary" href="${ctx}/catalog">Keep shopping</a>
            <a class="btn btn-saffron btn-lg" href="${ctx}/checkout">Check out</a>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
