<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Log in"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-1">Log in</h1>
    <p class="text-secondary mb-4">Shoppers, shop owners and mall staff all log in here.</p>

    <c:if test="${param.error != null}">
        <div class="alert alert-danger">That email and password don't match an account. Check both and try again.</div>
    </c:if>
    <c:if test="${param.logout != null}">
        <div class="alert alert-success">You're logged out.</div>
    </c:if>
    <c:if test="${param.registered != null}">
        <div class="alert alert-success">Account created. Log in to continue.</div>
    </c:if>

    <%--
        action="/login" and both field names (username/password) are fixed by
        Spring Security's form-login filter -- it reads these exact names from
        the POST body. We never write a controller method to handle this submit.
    --%>
    <form action="${ctx}/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="mb-3">
            <label class="form-label" for="username">Email</label>
            <input id="username" type="email" name="username" class="form-control" autocomplete="email" required autofocus/>
        </div>
        <div class="mb-4">
            <label class="form-label" for="password">Password</label>
            <input id="password" type="password" name="password" class="form-control" autocomplete="current-password" required/>
        </div>
        <button type="submit" class="btn btn-primary w-100">Log in</button>
    </form>

    <hr class="my-4">
    <p class="mb-1">New here? <a href="${ctx}/register/customer">Create a shopper account</a></p>
    <p class="mb-0">Selling something? <a href="${ctx}/register/merchant">Register your shop</a></p>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
