<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Product Form"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4"><c:choose><c:when test="${not empty productId}">Edit Product</c:when><c:otherwise>Add Product</c:otherwise></c:choose></h1>

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

        <c:url var="formAction" value="${not empty productId ? '/merchant/products/'.concat(productId) : '/merchant/products'}"/>
        <form action="${formAction}" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Name</label>
                <input type="text" name="name" class="form-control" value="${form.name}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control">${form.description}</textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">Price</label>
                <input type="number" step="0.01" min="0.01" name="price" class="form-control" value="${form.price}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Stock Quantity</label>
                <input type="number" min="0" name="stockQuantity" class="form-control" value="${form.stockQuantity}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Image URL</label>
                <input type="text" name="imageUrl" class="form-control" value="${form.imageUrl}"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Category</label>
                <select name="categoryId" class="form-select">
                    <option value="">-- none --</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}" ${category.id == form.categoryId ? 'selected' : ''}>
                            <c:out value="${category.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/merchant/products">Back to My Products</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
