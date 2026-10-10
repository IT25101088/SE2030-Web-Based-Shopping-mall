<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="pageTitle" value="${product.name}"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<a class="d-inline-block mb-3 fw-bold" href="${ctx}/catalog">Back to all products</a>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>

<div class="product">
    <div class="product__media">
        <c:choose>
            <c:when test="${not empty product.imageUrl}">
                <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>">
            </c:when>
            <c:otherwise>
                <span class="tile__monogram" aria-hidden="true"><c:out value="${fn:substring(product.name, 0, 1)}"/></span>
            </c:otherwise>
        </c:choose>
    </div>

    <div>
        <c:set var="shop" value="${product.merchant}"/>
        <p class="product__shop shop-with-logo"><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></p>
        <h1><c:out value="${product.name}"/></h1>
        <c:if test="${not empty reviews}">
            <p class="mb-0">
                <span class="stars" aria-hidden="true"><c:forEach begin="1" end="5" var="i"><span class="${i <= averageRating + 0.5 ? '' : 'off'}">&#9733;</span></c:forEach></span>
                <fmt:formatNumber value="${averageRating}" maxFractionDigits="1"/> out of 5,
                <a href="#reviews"><c:out value="${fn:length(reviews)}"/> ${fn:length(reviews) == 1 ? 'review' : 'reviews'}</a>
            </p>
        </c:if>

        <p class="product__price">Rs. <fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/></p>
        <c:choose>
            <c:when test="${product.stockQuantity > 0}">
                <p class="stock stock--in"><c:out value="${product.stockQuantity}"/> in stock</p>
            </c:when>
            <c:otherwise>
                <p class="stock stock--out">Sold out</p>
            </c:otherwise>
        </c:choose>

        <%-- Only shoppers get the buy form. Merchants and employees can't use a
             cart (SecurityConfig only lets customers into /cart/**). --%>
        <sec:authorize access="isAnonymous() or hasRole('CUSTOMER')">
            <c:choose>
                <c:when test="${product.onSale}">
                    <form action="${ctx}/cart/add" method="post" class="buy-row">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <div>
                            <label class="form-label" for="quantity">Quantity</label>
                            <input id="quantity" type="number" name="quantity" value="1" min="1"
                                   max="${product.stockQuantity > 0 ? product.stockQuantity : 1}" class="form-control qty"
                                   ${product.stockQuantity == 0 ? 'disabled' : ''}/>
                        </div>
                        <button type="submit" class="btn btn-saffron btn-lg" ${product.stockQuantity == 0 ? 'disabled' : ''}>Add to cart</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p class="stock stock--out">Not currently available</p>
                </c:otherwise>
            </c:choose>
        </sec:authorize>

        <%-- Employees moderate instead of buying: flagging hides the product from the catalog. --%>
        <sec:authorize access="hasRole('PLATFORM_EMPLOYEE')">
            <div class="panel mt-3">
                <c:choose>
                    <c:when test="${product.flaggedByAdmin}">
                        <p class="mb-2"><span class="status status-flagged">Hidden from the catalog</span></p>
                        <p>Reason: <c:out value="${product.flagReason}"/></p>
                        <form action="${ctx}/employee/products/${product.id}/unflag" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-success">Unflag and show again</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="${ctx}/employee/products/${product.id}/flag" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <label class="form-label" for="flagReason">Flag this product</label>
                            <input id="flagReason" type="text" name="reason" class="form-control mb-2" maxlength="255" required
                                   placeholder="Why should it be hidden? The shop owner will see this.">
                            <button type="submit" class="btn btn-danger">Flag and hide</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </sec:authorize>

        <c:if test="${not empty product.description}">
            <p class="product__desc mt-4"><c:out value="${product.description}"/></p>
        </c:if>

        <dl class="facts">
            <dt>Sold by</dt>
            <dd><c:out value="${product.merchant.shopName}"/></dd>
            <c:if test="${not empty product.category}">
                <dt>Floor</dt>
                <dd><a href="${ctx}/catalog?categoryId=${product.category.id}"><c:out value="${product.category.name}"/></a></dd>
            </c:if>
        </dl>
    </div>
</div>

<section id="reviews" class="mt-5 pt-4">
    <h2>Reviews</h2>
    <c:choose>
        <c:when test="${empty reviews}">
            <p class="text-secondary">No reviews yet. Customers can review this product after it's delivered to them.</p>
        </c:when>
        <c:otherwise>
            <div class="rating-summary">
                <strong><fmt:formatNumber value="${averageRating}" maxFractionDigits="1"/></strong>
                <span>out of 5, from <c:out value="${fn:length(reviews)}"/> ${fn:length(reviews) == 1 ? 'review' : 'reviews'}</span>
            </div>
            <c:forEach var="review" items="${reviews}">
                <article class="review">
                    <div class="review__meta">
                        <span class="stars" role="img" aria-label="${review.rating} out of 5 stars"><c:forEach begin="1" end="5" var="i"><span class="${i <= review.rating ? '' : 'off'}">&#9733;</span></c:forEach></span>
                        <span class="review__author"><c:out value="${review.customer.fullName}"/></span>
                    </div>
                    <c:if test="${not empty review.comment}"><p><c:out value="${review.comment}"/></p></c:if>
                </article>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
