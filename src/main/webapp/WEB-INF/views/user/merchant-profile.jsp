<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Shop profile"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <div class="d-flex justify-content-between align-items-start gap-3 mb-4">
        <h1 class="mb-0">Shop profile</h1>
        <span class="status status-${verificationStatus}"><c:out value="${verificationStatus}"/></span>
    </div>

    <c:if test="${param.updated != null}">
        <div class="alert alert-success">Shop profile saved.</div>
    </c:if>
    <c:if test="${not empty fieldErrors}">
        <ul class="alert alert-danger">
            <c:forEach var="fieldError" items="${fieldErrors}">
                <li><c:out value="${fieldError.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </c:if>

    <form action="${ctx}/merchant/profile" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="mb-3">
            <label class="form-label" for="shopName">Shop name</label>
            <input id="shopName" type="text" name="shopName" class="form-control" value="<c:out value='${form.shopName}'/>" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="shopDescription">What you sell</label>
            <textarea id="shopDescription" name="shopDescription" class="form-control"><c:out value="${form.shopDescription}"/></textarea>
        </div>
        <div class="mb-4">
            <label class="form-label" for="logoUrl">Logo link <span class="fw-normal text-secondary">(optional)</span></label>
            <div class="d-flex align-items-center gap-3">
                <%-- ShopProfileForm has shopName and logoUrl, so the fragment can preview it directly. --%>
                <c:set var="shop" value="${form}"/>
                <span class="logo-preview"><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %></span>
                <input id="logoUrl" type="text" name="logoUrl" class="form-control" value="<c:out value='${form.logoUrl}'/>"/>
            </div>
            <p class="form-hint">Paste a link to an image that is already online. Without one, your shop shows its first letter.</p>
        </div>
        <button type="submit" class="btn btn-primary">Save shop profile</button>
    </form>

    <a class="back-link" href="${ctx}/merchant/dashboard">Back to my shop</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
