<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My shop"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>My shop</h1>
        <p class="lede">Manage your listings, fulfil orders and reply to customers.</p>
    </div>
    <a class="btn btn-saffron" href="${ctx}/merchant/products/new">Add a product</a>
</div>

<%-- Status board: see the comment in customer-home.jsp. --%>
<nav class="board" data-board aria-label="Your shop at a glance">
    <a class="board__item ${dashboard.ordersToSend == 0 ? 'is-zero' : ''}" style="--i: 0" href="${ctx}/merchant/orders">
        <span class="board__num" data-count="${dashboard.ordersToSend}"><c:out value="${dashboard.ordersToSend}"/></span>
        <span class="board__label">Orders to send out
            <span class="board__note">Pending or confirmed, not shipped yet</span>
        </span>
    </a>
    <a class="board__item ${dashboard.openInquiries == 0 ? 'is-zero' : ''}" style="--i: 1" href="${ctx}/merchant/inquiries">
        <span class="board__num" data-count="${dashboard.openInquiries}"><c:out value="${dashboard.openInquiries}"/></span>
        <span class="board__label">Help requests
            <span class="board__note">Passed to you by the mall team</span>
        </span>
    </a>
    <a class="board__item ${dashboard.lowStockProducts == 0 ? 'is-zero' : ''}" style="--i: 2" href="${ctx}/merchant/products">
        <span class="board__num" data-count="${dashboard.lowStockProducts}"><c:out value="${dashboard.lowStockProducts}"/></span>
        <span class="board__label">Low on stock
            <span class="board__note">Products with <c:out value="${lowStockLimit}"/> or fewer left</span>
        </span>
    </a>
    <%-- Rating: already rounded to one decimal place by DashboardService, or "New" (and no count-up) before the first review. --%>
    <a class="board__item ${empty dashboard.rating ? 'is-zero' : ''}" style="--i: 3" href="${ctx}/merchant/feedback">
        <c:choose>
            <c:when test="${empty dashboard.rating}">
                <span class="board__num">New</span>
                <span class="board__label">Shop rating
                    <span class="board__note">No reviews yet</span>
                </span>
            </c:when>
            <c:otherwise>
                <span class="board__num" data-count="${dashboard.rating}"><c:out value="${dashboard.rating}"/></span>
                <span class="board__label">Shop rating
                    <span class="board__note">Average stars across all reviews</span>
                </span>
            </c:otherwise>
        </c:choose>
    </a>
</nav>

<section class="dash-section" aria-labelledby="nextOrdersHeading">
    <h2 id="nextOrdersHeading">Send these out next</h2>
    <c:choose>
        <c:when test="${empty dashboard.nextOrders}">
            <div class="empty">
                <h3>Nothing to send out</h3>
                <p>New orders show up here as soon as a customer pays.</p>
            </div>
        </c:when>
        <c:otherwise>
            <%-- Oldest first: those customers have waited longest. --%>
            <div class="panel">
                <ul class="queue">
                    <c:forEach var="item" items="${dashboard.nextOrders}">
                        <li>
                            <span class="queue__main">
                                <span class="queue__title">Order #<c:out value="${item.order.id}"/>: <c:out value="${item.product.name}"/></span>
                                <span class="queue__meta">Quantity <c:out value="${item.quantitySnapshot}"/></span>
                            </span>
                            <span class="status status-${item.status}"><c:out value="${fn:replace(item.status, '_', ' ')}"/></span>
                            <a class="btn btn-sm btn-outline-secondary" href="${ctx}/merchant/orders">Update status</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <c:if test="${dashboard.ordersToSend > fn:length(dashboard.nextOrders)}">
                <p class="mt-3 mb-0"><a class="fw-bold" href="${ctx}/merchant/orders">See all <c:out value="${dashboard.ordersToSend}"/> orders to send out</a></p>
            </c:if>
        </c:otherwise>
    </c:choose>
</section>

<nav class="hub" aria-label="Shop management">
    <a href="${ctx}/merchant/products">
        <h2>Products</h2>
        <p>Edit prices, stock and photos, or hide a listing.</p>
        <p class="hub__live"><c:out value="${dashboard.productCount}"/> listed<c:if test="${dashboard.hiddenProducts > 0}">, <c:out value="${dashboard.hiddenProducts}"/> hidden</c:if></p>
    </a>
    <a href="${ctx}/merchant/orders">
        <h2>Orders</h2>
        <p>See what customers bought and update each item's status.</p>
    </a>
    <a href="${ctx}/merchant/feedback">
        <h2>Reviews</h2>
        <p>Check your rating and reply to customer reviews.</p>
    </a>
    <a href="${ctx}/merchant/inquiries">
        <h2>Help requests</h2>
        <p>Answer customer questions the mall team has passed to you.</p>
    </a>
    <a href="${ctx}/merchant/profile">
        <h2>Shop profile</h2>
        <p>Change your shop name and description.</p>
    </a>
</nav>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
