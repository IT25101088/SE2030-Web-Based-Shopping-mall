<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Mall admin"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Mall admin</h1>
        <p class="lede">Approve new shops, answer customers and keep an eye on product quality.</p>
    </div>
</div>

<%-- Status board: see the comment in customer-home.jsp. --%>
<nav class="board" data-board aria-label="The mall at a glance">
    <a class="board__item ${dashboard.shopsToApprove == 0 ? 'is-zero' : ''}" style="--i: 0" href="${ctx}/employee/merchants">
        <span class="board__num" data-count="${dashboard.shopsToApprove}"><c:out value="${dashboard.shopsToApprove}"/></span>
        <span class="board__label">Shops to approve
            <span class="board__note">New sign-ups waiting for a decision</span>
        </span>
    </a>
    <a class="board__item ${dashboard.inquiriesWaiting == 0 ? 'is-zero' : ''}" style="--i: 1" href="${ctx}/employee/inquiries">
        <span class="board__num" data-count="${dashboard.inquiriesWaiting}"><c:out value="${dashboard.inquiriesWaiting}"/></span>
        <span class="board__label">Help requests waiting
            <span class="board__note">New, or a shop has answered</span>
        </span>
    </a>
    <a class="board__item ${dashboard.flaggedProducts == 0 ? 'is-zero' : ''}" style="--i: 2" href="${ctx}/employee/flagged-products">
        <span class="board__num" data-count="${dashboard.flaggedProducts}"><c:out value="${dashboard.flaggedProducts}"/></span>
        <span class="board__label">Flagged products
            <span class="board__note">Hidden by the team or rated too low</span>
        </span>
    </a>
</nav>

<section class="dash-section" aria-labelledby="nextInquiriesHeading">
    <h2 id="nextInquiriesHeading">Answer these next</h2>
    <c:choose>
        <c:when test="${empty dashboard.nextInquiries}">
            <div class="empty">
                <h3>No help requests waiting</h3>
                <p>New questions from customers, and answers from shops, show up here.</p>
            </div>
        </c:when>
        <c:otherwise>
            <%-- Oldest first: those customers have waited longest. --%>
            <div class="panel">
                <ul class="queue">
                    <c:forEach var="inquiry" items="${dashboard.nextInquiries}">
                        <li>
                            <span class="queue__main">
                                <span class="queue__title"><c:out value="${inquiry.subject}"/></span>
                                <span class="queue__meta">
                                    From <c:out value="${inquiry.customer.fullName}"/><c:if test="${inquiry.status == 'AWAITING_REVIEW'}">. A shop has replied.</c:if>
                                </span>
                            </span>
                            <span class="status status-${inquiry.status}"><c:out value="${fn:replace(inquiry.status, '_', ' ')}"/></span>
                            <a class="btn btn-sm btn-outline-secondary" href="${ctx}/employee/inquiries">Answer</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <c:if test="${dashboard.inquiriesWaiting > fn:length(dashboard.nextInquiries)}">
                <p class="mt-3 mb-0"><a class="fw-bold" href="${ctx}/employee/inquiries">See all <c:out value="${dashboard.inquiriesWaiting}"/> waiting help requests</a></p>
            </c:if>
        </c:otherwise>
    </c:choose>
</section>

<nav class="hub" aria-label="Admin tasks">
    <a href="${ctx}/employee/merchants">
        <h2>Shops<c:if test="${unseenPendingShops > 0}"> <span class="nav-badge"><c:out value="${unseenPendingShops}"/><span class="visually-hidden"> new</span></span></c:if></h2>
        <p>See every shop, suspend or reinstate them, and approve new ones.</p>
    </a>
    <a href="${ctx}/employee/inquiries">
        <h2>Help requests</h2>
        <p>Reply to customers, forward questions to shops and mark them resolved.</p>
        <c:if test="${dashboard.inquiriesWithShops > 0}">
            <p class="hub__live"><c:out value="${dashboard.inquiriesWithShops}"/> with shops right now</p>
        </c:if>
    </a>
    <a href="${ctx}/employee/flagged-products">
        <h2>Flagged products</h2>
        <p>Products you've hidden, and products whose ratings have dropped too low.</p>
    </a>
</nav>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
