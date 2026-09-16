<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Shop Profile"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4">My Shop Profile</h1>

        <p>Verification status: <span class="badge bg-secondary"><c:out value="${verificationStatus}"/></span></p>

        <c:if test="${param.updated != null}">
            <div class="alert alert-success">Profile updated.</div>
        </c:if>
        <c:if test="${not empty fieldErrors}">
            <ul class="alert alert-danger">
                <c:forEach var="fieldError" items="${fieldErrors}">
                    <li><c:out value="${fieldError.defaultMessage}"/></li>
                </c:forEach>
            </ul>
        </c:if>

        <form action="${pageContext.request.contextPath}/merchant/profile" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Shop Name</label>
                <input type="text" name="shopName" class="form-control" value="${form.shopName}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Shop Description</label>
                <textarea name="shopDescription" class="form-control">${form.shopDescription}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
