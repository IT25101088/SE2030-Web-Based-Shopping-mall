<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Your details"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-4">Your details</h1>

    <c:if test="${param.updated != null}">
        <div class="alert alert-success">Your details are saved.</div>
    </c:if>
    <c:if test="${not empty fieldErrors}">
        <ul class="alert alert-danger">
            <c:forEach var="fieldError" items="${fieldErrors}">
                <li><c:out value="${fieldError.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </c:if>

    <form action="${ctx}/customer/profile" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="mb-3">
            <label class="form-label" for="email">Email</label>
            <input id="email" type="email" class="form-control" value="<c:out value='${email}'/>" readonly/>
            <p class="form-hint">You log in with this, so it can't be changed here.</p>
        </div>
        <div class="mb-3">
            <label class="form-label" for="fullName">Full name</label>
            <input id="fullName" type="text" name="fullName" class="form-control" value="<c:out value='${form.fullName}'/>" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="phone">Phone <span class="fw-normal text-secondary">(optional)</span></label>
            <input id="phone" type="tel" name="phone" class="form-control" value="<c:out value='${form.phone}'/>"/>
        </div>
        <div class="mb-4">
            <label class="form-label" for="shippingAddress">Shipping address <span class="fw-normal text-secondary">(optional)</span></label>
            <textarea id="shippingAddress" name="shippingAddress" class="form-control"><c:out value="${form.shippingAddress}"/></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Save details</button>
    </form>

    <a class="back-link" href="${ctx}/customer/home">Back to your account</a>
</div>

<div class="panel panel--narrow">
    <h2>Delete your account</h2>
    <p>This logs you out and removes your name, phone, address and cart. Your past orders stay on record for the shops
        that filled them, but they will no longer be linked to you. This can't be undone.</p>

    <c:if test="${not empty deleteError}">
        <div class="alert alert-danger"><c:out value="${deleteError}"/></div>
    </c:if>

    <form action="${ctx}/customer/profile/delete" method="post"
          onsubmit="return confirm('Delete your account for good? This cannot be undone.');">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="mb-3">
            <label class="form-label" for="currentPassword">Type your password to confirm</label>
            <input id="currentPassword" type="password" name="currentPassword" class="form-control"
                   autocomplete="current-password" required/>
        </div>
        <button type="submit" class="btn btn-danger">Delete my account</button>
    </form>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
