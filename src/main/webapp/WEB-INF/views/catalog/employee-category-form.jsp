<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Category Form"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4"><c:choose><c:when test="${not empty categoryId}">Edit Category</c:when><c:otherwise>Add Category</c:otherwise></c:choose></h1>

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

        <c:url var="formAction" value="${not empty categoryId ? '/employee/categories/'.concat(categoryId) : '/employee/categories'}"/>
        <form action="${formAction}" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Name</label>
                <input type="text" name="name" class="form-control" value="${form.name}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Parent Category</label>
                <select name="parentCategoryId" class="form-select">
                    <option value="">-- none --</option>
                    <c:forEach var="category" items="${categories}">
                        <c:if test="${category.id != categoryId}">
                            <option value="${category.id}" ${category.id == form.parentCategoryId ? 'selected' : ''}>
                                <c:out value="${category.name}"/>
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/employee/categories">Back to Categories</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
