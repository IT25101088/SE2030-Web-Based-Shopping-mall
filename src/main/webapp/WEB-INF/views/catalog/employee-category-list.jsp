<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Product Categories"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="mb-0">Product Categories</h1>
    <a href="${pageContext.request.contextPath}/employee/categories/new" class="btn btn-primary">Add Category</a>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>

<c:choose>
    <c:when test="${empty categories}">
        <p>No categories have been created yet.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Name</th>
                <th>Parent Category</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="category" items="${categories}">
                <tr>
                    <td><c:out value="${category.name}"/></td>
                    <td><c:out value="${category.parentCategory.name}"/></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/employee/categories/${category.id}/edit"
                           class="btn btn-secondary btn-sm">Edit</a>
                        <form action="${pageContext.request.contextPath}/employee/categories/${category.id}/delete"
                              method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm"
                                    onclick="return confirm('Remove this category?');">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
