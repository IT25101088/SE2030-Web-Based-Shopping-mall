<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Log In"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-5">
        <h1 class="mb-4">Log In</h1>

        <c:if test="${param.error != null}">
            <div class="alert alert-danger">Invalid email or password.</div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="alert alert-success">You have been logged out.</div>
        </c:if>
        <c:if test="${param.registered != null}">
            <div class="alert alert-success">Account created. You can log in now.</div>
        </c:if>

        <%--
            action="/login" and both field names (username/password) are fixed by
            Spring Security's form-login filter -- it reads these exact names from
            the POST body. We never write a controller method to handle this submit.
        --%>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="username" class="form-control" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required/>
            </div>
            <button type="submit" class="btn btn-primary">Log In</button>
        </form>

        <p class="mt-4">
            New customer? <a href="${pageContext.request.contextPath}/register/customer">Register here</a><br/>
            New merchant? <a href="${pageContext.request.contextPath}/register/merchant">Register here</a>
        </p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
