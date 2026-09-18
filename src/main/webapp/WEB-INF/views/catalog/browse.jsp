<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Product Catalog"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Product Catalog</h1>

<form action="${pageContext.request.contextPath}/catalog" method="get" class="row row-cols-lg-auto g-2 align-items-end mb-4">
    <div class="col-12">
        <label class="form-label">Keyword</label>
        <input type="text" name="keyword" class="form-control" value="${keyword}"/>
    </div>
    <div class="col-12">
        <label class="form-label">Category</label>
        <select name="categoryId" class="form-select">
            <option value="">-- all --</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}" ${category.id == categoryId ? 'selected' : ''}>
                    <c:out value="${category.name}"/>
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="col-12">
        <label class="form-label">Min Price</label>
        <input type="number" step="0.01" name="minPrice" class="form-control" value="${minPrice}"/>
    </div>
    <div class="col-12">
        <label class="form-label">Max Price</label>
        <input type="number" step="0.01" name="maxPrice" class="form-control" value="${maxPrice}"/>
    </div>
    <div class="col-12">
        <button type="submit" class="btn btn-primary">Search</button>
    </div>
</form>

<c:choose>
    <c:when test="${empty products}">
        <p>No products match your search.</p>
    </c:when>
    <c:otherwise>
        <div class="row row-cols-1 row-cols-md-3 g-3">
            <c:forEach var="product" items="${products}">
                <div class="col">
                    <div class="card h-100">
                        <c:if test="${not empty product.imageUrl}">
                            <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}"
                                 style="height: 180px; object-fit: cover;"/>
                        </c:if>
                        <div class="card-body">
                            <h5 class="card-title">
                                <a class="text-decoration-none" href="${pageContext.request.contextPath}/catalog/${product.id}">
                                    <c:out value="${product.name}"/>
                                </a>
                            </h5>
                            <p class="card-text mb-1"><c:out value="${product.price}"/></p>
                            <p class="card-text text-muted small"><c:out value="${product.merchant.shopName}"/></p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
