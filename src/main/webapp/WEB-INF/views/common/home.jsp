<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sri Lanka's online mall"/>
<c:set var="fullBleed" value="true"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<%-- 1. Hero: headline + search on the left, the mall directory board on the right. --%>
<section class="hero">
    <div class="container hero__grid">
        <div>
            <h1 class="hero__title">Every shop on the island, under one roof.</h1>
            <p class="hero__lede">
                Serendib Central brings sellers from across Sri Lanka into one online mall.
                Browse every shop, check out once, and follow each order to your door.
            </p>
            <form class="hero__search" action="${ctx}/catalog" method="get" role="search">
                <label class="visually-hidden" for="heroSearch">Search products</label>
                <input id="heroSearch" type="search" name="keyword" placeholder="Try &ldquo;batik&rdquo; or &ldquo;tea&rdquo;">
                <button type="submit" class="btn btn-saffron">Search</button>
            </form>
            <div class="hero__links">
                <a href="${ctx}/catalog">Browse all products</a>
                <a href="${ctx}/register/merchant">Open your own shop</a>
            </div>
        </div>

        <%-- data-directory tells serendib.js to animate this board. --%>
        <nav class="directory" data-directory aria-label="Mall directory">
            <div class="directory__head">
                <h2 class="directory__title">Mall directory</h2>
                <span class="lift-display" aria-hidden="true">
                    <span class="lift-display__arrow" data-lift-arrow>&#9650;</span>
                    <span data-lift-level>L1</span>
                </span>
            </div>
            <div class="directory__shaft">
            <span class="directory__car" aria-hidden="true"></span>
            <ol class="directory__floors">
                <c:choose>
                    <c:when test="${not empty categories}">
                        <%-- Each category is a "floor". Capped at 7 so the board stays a sensible height. --%>
                        <c:forEach var="category" items="${categories}" varStatus="loop" end="6">
                            <li class="floor" data-level="L${loop.count}" style="--i:${loop.index}">
                                <a href="${ctx}/catalog?categoryId=${category.id}">
                                    <span class="floor__level">L${loop.count}</span>
                                    <span class="floor__name"><c:out value="${category.name}"/></span>
                                    <span class="floor__go">Visit floor</span>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <%-- No categories in the database yet: show typical mall floors that open the full catalog. --%>
                        <c:forEach var="name" items="Fashion,Electronics,Home &amp; living,Tea &amp; spices,Gems &amp; jewellery,Books" varStatus="loop">
                            <li class="floor" data-level="L${loop.count}" style="--i:${loop.index}">
                                <a href="${ctx}/catalog">
                                    <span class="floor__level">L${loop.count}</span>
                                    <span class="floor__name">${name}</span>
                                    <span class="floor__go">Visit floor</span>
                                </a>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ol>
            </div>
        </nav>
    </div>
</section>

<%-- 2. Scrolling strip of shop logos and names. The list is printed twice so the CSS
        animation can loop seamlessly; the copy is hidden from screen readers. --%>
<c:if test="${not empty shops}">
    <section class="marquee" aria-labelledby="shopsHeading">
        <h2 id="shopsHeading" class="visually-hidden">Shops in the mall</h2>
        <div class="marquee__track">
            <ul class="marquee__list">
                <c:forEach var="shop" items="${shops}">
                    <li><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></li>
                </c:forEach>
            </ul>
            <ul class="marquee__list" aria-hidden="true">
                <c:forEach var="shop" items="${shops}">
                    <li><%@ include file="/WEB-INF/views/common/shop-logo.jspf" %><c:out value="${shop.shopName}"/></li>
                </c:forEach>
            </ul>
        </div>
    </section>
</c:if>

<%-- 3. A first look at what's on sale. --%>
<section class="section">
    <div class="container">
        <div class="section__head">
            <div>
                <h2>New in the mall</h2>
                <p>Fresh listings from shops across the island.</p>
            </div>
            <a class="btn btn-outline-secondary" href="${ctx}/catalog">See all products</a>
        </div>
        <c:choose>
            <c:when test="${empty products}">
                <div class="empty">
                    <h2>The shelves are still being stocked</h2>
                    <p>Products appear here once an approved shop lists them.</p>
                    <a class="btn btn-primary" href="${ctx}/register/merchant">Open a shop</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="tiles">
                    <c:forEach var="product" items="${products}" end="7">
                        <%@ include file="/WEB-INF/views/common/product-tile.jspf" %>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%-- 4. How buying works. These really are steps in order, hence the numbers. --%>
<section class="section section--white">
    <div class="container">
        <div class="section__head"><h2>How buying works</h2></div>
        <ol class="steps">
            <li>
                <h3>Find it</h3>
                <p>Search the whole mall or walk a floor. Every product shows which shop sells it.</p>
            </li>
            <li>
                <h3>Check out once</h3>
                <p>Fill one cart from as many shops as you like and pay in a single checkout.</p>
            </li>
            <li>
                <h3>Track each parcel</h3>
                <p>Each shop updates its part of your order, so you always know what has shipped.</p>
            </li>
        </ol>
    </div>
</section>

<%-- 5. The two audiences besides shoppers: sellers, and people who need help. --%>
<section class="section">
    <div class="container split">
        <div class="split__item split__item--ink">
            <h2>Sell at Serendib Central</h2>
            <p>Register your shop, get approved by our team, and list your products to shoppers island-wide.</p>
            <a class="btn btn-saffron" href="${ctx}/register/merchant">Open a shop</a>
        </div>
        <div class="split__item split__item--soft">
            <h2>Need help with an order?</h2>
            <p>Most answers are in the FAQ. If yours isn't, send us a question and our team will reply.</p>
            <a class="btn btn-primary" href="${ctx}/faq">Read the FAQ</a>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
