<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register - Customer"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4">Create a Customer Account</h1>

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

        <form action="${pageContext.request.contextPath}/register/customer" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" value="${form.email}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Full Name</label>
                <input type="text" name="fullName" class="form-control" value="${form.fullName}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Phone</label>
                <input type="text" name="phone" class="form-control" value="${form.phone}"/>
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
