<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Open a shop"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-1">Open a shop</h1>
    <p class="text-secondary mb-4">Tell us about you and your shop. Our team reviews every new shop before it can list products.</p>

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

    <form action="${ctx}/register/merchant" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <h2 class="h4 mb-3">About you</h2>
        <div class="mb-3">
            <label class="form-label" for="fullName">Full name</label>
            <input id="fullName" type="text" name="fullName" class="form-control" value="<c:out value='${form.fullName}'/>" autocomplete="name" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="email">Email</label>
            <input id="email" type="email" name="email" class="form-control" value="<c:out value='${form.email}'/>" autocomplete="email" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="password">Password</label>
            <input id="password" type="password" name="password" class="form-control" autocomplete="new-password" required/>
        </div>
        <div class="mb-4">
            <label class="form-label" for="phone">Phone <span class="fw-normal text-secondary">(optional)</span></label>
            <input id="phone" type="tel" name="phone" class="form-control" value="<c:out value='${form.phone}'/>" autocomplete="tel"/>
        </div>

        <h2 class="h4 mb-3">Your shop</h2>
        <div class="mb-3">
            <label class="form-label" for="shopName">Shop name</label>
            <input id="shopName" type="text" name="shopName" class="form-control" value="<c:out value='${form.shopName}'/>" required/>
        </div>
        <div class="mb-4">
            <label class="form-label" for="shopDescription">What you sell <span class="fw-normal text-secondary">(optional)</span></label>
            <textarea id="shopDescription" name="shopDescription" class="form-control"><c:out value="${form.shopDescription}"/></textarea>
        </div>
        <button type="submit" class="btn btn-saffron w-100">Submit shop for approval</button>
    </form>

    <p class="mt-4 mb-0">Already registered? <a href="${ctx}/login">Log in</a></p>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
