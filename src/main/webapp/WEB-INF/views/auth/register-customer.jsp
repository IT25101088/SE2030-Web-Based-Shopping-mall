<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create an account"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-1">Create a shopper account</h1>
    <p class="text-secondary mb-4">One account for every shop in the mall.</p>

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

    <form action="${ctx}/register/customer" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

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
        <button type="submit" class="btn btn-primary w-100">Create account</button>
    </form>

    <p class="mt-4 mb-0">Already have an account? <a href="${ctx}/login">Log in</a></p>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
