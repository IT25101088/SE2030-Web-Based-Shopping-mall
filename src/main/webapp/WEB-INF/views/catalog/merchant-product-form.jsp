<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${not empty productId ? 'Edit product' : 'Add a product'}"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-4"><c:out value="${pageTitle}"/></h1>

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
            <label class="form-label" for="name">Product name</label>
            <input id="name" type="text" name="name" class="form-control" value="<c:out value='${form.name}'/>" required/>
        </div>
        <div class="mb-3">
            <label class="form-label" for="description">Description</label>
            <textarea id="description" name="description" class="form-control"><c:out value="${form.description}"/></textarea>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label class="form-label" for="price">Price (Rs.)</label>
                <input id="price" type="number" step="0.01" min="0.01" name="price" class="form-control" value="${form.price}" required/>
            </div>
            <div class="col-sm-6">
                <label class="form-label" for="stockQuantity">Units in stock</label>
                <input id="stockQuantity" type="number" min="0" name="stockQuantity" class="form-control" value="${form.stockQuantity}" required/>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="imageUrl">Photo link <span class="fw-normal text-secondary">(optional)</span></label>
            <input id="imageUrl" type="text" name="imageUrl" class="form-control" value="<c:out value='${form.imageUrl}'/>"/>
            <p class="form-hint">Paste a link to an image that is already online. Without one, the product shows its first letter.</p>
        </div>
        <div class="mb-4">
            <label class="form-label" for="categoryId">Floor (category)</label>
            <select id="categoryId" name="categoryId" class="form-select">
                <option value="">No category</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}" ${category.id == form.categoryId ? 'selected' : ''}>
                        <c:out value="${category.name}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Save product</button>
    </form>

    <a class="back-link" href="${ctx}/merchant/products">Back to products</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
