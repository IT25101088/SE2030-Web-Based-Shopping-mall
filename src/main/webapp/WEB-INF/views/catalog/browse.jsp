<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="All products"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>
            <c:choose>
                <c:when test="${not empty keyword}">Results for &ldquo;<c:out value="${keyword}"/>&rdquo;</c:when>
                <c:otherwise>All products</c:otherwise>
            </c:choose>
        </h1>
        <p class="lede">Everything on sale at Serendib Central, from every shop in the mall.</p>
    </div>
</div>

<%-- Shown after "Add to cart": CartController sends shoppers back here to keep browsing. --%>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success d-flex flex-wrap align-items-center justify-content-between gap-2" role="status">
        <span><c:out value="${successMessage}"/></span>
        <a class="btn btn-sm btn-primary" href="${ctx}/cart">View cart and check out</a>
    </div>
</c:if>

<div class="catalog">
    <%-- Filter form. Every field is optional; blank means "don't filter on this". --%>
    <aside class="filters panel" aria-labelledby="filterHeading">
        <h2 id="filterHeading">Filter</h2>
        <form action="${ctx}/catalog" method="get">
            <div class="mb-3">
                <label class="form-label" for="keyword">Product name</label>
                <input id="keyword" type="search" name="keyword" class="form-control" value="<c:out value='${keyword}'/>"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="categoryId">Floor</label>
                <select id="categoryId" name="categoryId" class="form-select">
                    <option value="">All floors</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}" ${category.id == categoryId ? 'selected' : ''}>
                            <c:out value="${category.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <fieldset class="mb-3">
                <legend class="form-label fs-6">Price (Rs.)</legend>
                <div class="price-pair">
                    <div>
                        <label class="visually-hidden" for="minPrice">Minimum price</label>
                        <input id="minPrice" type="number" step="0.01" min="0" name="minPrice" class="form-control" placeholder="Min" value="${minPrice}"/>
                    </div>
                    <div>
                        <label class="visually-hidden" for="maxPrice">Maximum price</label>
                        <input id="maxPrice" type="number" step="0.01" min="0" name="maxPrice" class="form-control" placeholder="Max" value="${maxPrice}"/>
                    </div>
                </div>
            </fieldset>
            <button type="submit" class="btn btn-primary">Show products</button>
            <a class="filters__reset" href="${ctx}/catalog">Clear filters</a>
        </form>
    </aside>

    <section aria-label="Products">
        <%-- Quick links to each category ("floor"). --%>
        <c:if test="${not empty categories}">
            <ul class="floor-chips">
                <li><a href="${ctx}/catalog" aria-current="${empty categoryId}">All floors</a></li>
                <c:forEach var="category" items="${categories}">
                    <li>
                        <a href="${ctx}/catalog?categoryId=${category.id}" aria-current="${category.id == categoryId}">
                            <c:out value="${category.name}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:choose>
            <c:when test="${empty products}">
                <div class="empty">
                    <h2>No products match these filters</h2>
                    <p>Try a shorter product name, another floor, or a wider price range.</p>
                    <a class="btn btn-outline-secondary" href="${ctx}/catalog">Clear filters</a>
                </div>
            </c:when>
            <c:otherwise>
                <p class="result-count mb-3">
                    <c:out value="${fn:length(products)}"/> ${fn:length(products) == 1 ? 'product' : 'products'}
                </p>
                <div class="tiles">
                    <c:forEach var="product" items="${products}">
                        <%@ include file="/WEB-INF/views/common/product-tile.jspf" %>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
