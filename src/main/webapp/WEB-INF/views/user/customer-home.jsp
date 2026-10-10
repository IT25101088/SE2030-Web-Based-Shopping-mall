<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Your account"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Welcome back</h1>
        <p class="lede">Here's where your cart, orders and help requests stand.</p>
    </div>
    <a class="btn btn-saffron" href="${ctx}/catalog">Start shopping</a>
</div>

<%-- Status board: each plate is a link to the page behind the number.
     "is-zero" dims a plate when there's nothing there; --i sets the order the
     plates switch on in (see .board.is-booting in serendib.css). --%>
<nav class="board" data-board aria-label="Your account at a glance">
    <a class="board__item ${dashboard.cartItemCount == 0 ? 'is-zero' : ''}" style="--i: 0" href="${ctx}/cart">
        <span class="board__num" data-count="${dashboard.cartItemCount}"><c:out value="${dashboard.cartItemCount}"/></span>
        <span class="board__label">In your cart
            <span class="board__note">
                <c:choose>
                    <c:when test="${dashboard.cartItemCount == 0}">Nothing added yet</c:when>
                    <c:otherwise>Rs. <fmt:formatNumber value="${dashboard.cartTotal}" minFractionDigits="2" maxFractionDigits="2"/></c:otherwise>
                </c:choose>
            </span>
        </span>
    </a>
    <a class="board__item ${dashboard.itemsOnTheWay == 0 ? 'is-zero' : ''}" style="--i: 1" href="${ctx}/orders">
        <span class="board__num" data-count="${dashboard.itemsOnTheWay}"><c:out value="${dashboard.itemsOnTheWay}"/></span>
        <span class="board__label">On the way
            <span class="board__note">Items ordered but not delivered yet</span>
        </span>
    </a>
    <a class="board__item ${dashboard.openInquiries == 0 ? 'is-zero' : ''}" style="--i: 2" href="${ctx}/inquiries">
        <span class="board__num" data-count="${dashboard.openInquiries}"><c:out value="${dashboard.openInquiries}"/></span>
        <span class="board__label">Open help requests
            <span class="board__note">Questions we're still working on</span>
        </span>
    </a>
</nav>

<section class="dash-section" aria-labelledby="latestOrderHeading">
    <h2 id="latestOrderHeading">Your latest order</h2>
    <c:set var="order" value="${dashboard.latestOrder}"/>
    <c:choose>
        <c:when test="${empty order}">
            <div class="empty">
                <h3>No orders yet</h3>
                <p>When you place an order, you can follow it from here.</p>
                <a class="btn btn-primary" href="${ctx}/catalog">Browse products</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel latest-order">
                <div class="latest-order__head">
                    <div>
                        <h3>Order #<c:out value="${order.id}"/></h3>
                        <span class="text-secondary">
                            <c:out value="${dashboard.latestOrderItemCount}"/> ${dashboard.latestOrderItemCount == 1 ? 'item' : 'items'},
                            Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                    </div>
                    <a class="btn btn-sm btn-outline-secondary" href="${ctx}/orders/${order.id}">View order</a>
                </div>
                <%-- Same progress tracker as the order page (order/order-detail.jsp). --%>
                <c:choose>
                    <c:when test="${order.status == 'CANCELLED'}">
                        <div class="alert alert-danger mb-0">This order was cancelled.</div>
                    </c:when>
                    <c:otherwise>
                        <ol class="track" aria-label="Order progress">
                            <c:set var="passed" value="${false}"/>
                            <c:forEach var="stage" items="PENDING,CONFIRMED,SHIPPED,DELIVERED">
                                <li class="${passed ? '' : 'is-done'} ${stage == order.status ? 'is-current' : ''}"
                                    ${stage == order.status ? 'aria-current="step"' : ''}>
                                    <span class="status-word"><c:out value="${stage}"/></span>
                                </li>
                                <c:if test="${stage == order.status}"><c:set var="passed" value="${true}"/></c:if>
                            </c:forEach>
                        </ol>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
