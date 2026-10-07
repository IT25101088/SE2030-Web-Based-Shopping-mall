<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
    Shared top of every page. Pages set two optional variables before including this:
      pageTitle -- shown in the browser tab
      fullBleed -- "true" for pages (like the landing page) that draw their own
                   full-width sections instead of sitting in the normal container.

    currentPath is the URL the browser asked for (e.g. "/catalog"). JSPs are reached
    through an internal forward, so we read the ORIGINAL path from the forward
    attributes; it's used to highlight the current nav link.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="currentPath" value="${requestScope['jakarta.servlet.forward.servlet_path']}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:if test="${not empty pageTitle}"><c:out value="${pageTitle}"/> | </c:if>Serendib Central</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Atkinson+Hyperlegible:wght@400;700&family=Big+Shoulders+Display:wght@600;800;900&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/css/serendib.css" rel="stylesheet">
</head>
<body>
<a class="skip-link" href="#main">Skip to content</a>

<header class="site-header">
    <nav class="navbar navbar-expand-xl" aria-label="Main">
        <div class="container">
            <a class="brand" href="${ctx}/">
                <span class="brand__mark" aria-hidden="true">S</span>
                <span class="brand__name">Serendib Central</span>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav"
                    aria-controls="mainNav" aria-expanded="false" aria-label="Open menu">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link ${fn:startsWith(currentPath, '/catalog') ? 'active' : ''}" href="${ctx}/catalog">Products</a>
                    </li>
                    <sec:authorize access="hasRole('CUSTOMER')">
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/orders') ? 'active' : ''}" href="${ctx}/orders">My orders</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/inquiries') ? 'active' : ''}" href="${ctx}/inquiries">Help requests</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('MERCHANT')">
                        <li class="nav-item"><a class="nav-link ${currentPath == '/merchant/dashboard' ? 'active' : ''}" href="${ctx}/merchant/dashboard">My shop</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/merchant/products') ? 'active' : ''}" href="${ctx}/merchant/products">My products</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/merchant/orders') ? 'active' : ''}" href="${ctx}/merchant/orders">Orders</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/merchant/feedback') ? 'active' : ''}" href="${ctx}/merchant/feedback">Reviews</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/merchant/inquiries') ? 'active' : ''}" href="${ctx}/merchant/inquiries">Help requests</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('PLATFORM_EMPLOYEE')">
                        <li class="nav-item"><a class="nav-link ${currentPath == '/employee/dashboard' ? 'active' : ''}" href="${ctx}/employee/dashboard">Admin</a></li>
                        <li class="nav-item">
                            <a class="nav-link ${fn:startsWith(currentPath, '/employee/merchants') ? 'active' : ''}" href="${ctx}/employee/merchants">Shops<c:if test="${unseenPendingShops > 0}"> <span class="nav-badge"><c:out value="${unseenPendingShops}"/><span class="visually-hidden"> new shops waiting for approval</span></span></c:if></a>
                        </li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/employee/inquiries') ? 'active' : ''}" href="${ctx}/employee/inquiries">Help requests</a></li>
                        <li class="nav-item"><a class="nav-link ${fn:startsWith(currentPath, '/employee/flagged') ? 'active' : ''}" href="${ctx}/employee/flagged-products">Flagged products</a></li>
                    </sec:authorize>
                    <li class="nav-item">
                        <a class="nav-link ${fn:startsWith(currentPath, '/faq') ? 'active' : ''}" href="${ctx}/faq">FAQ</a>
                    </li>
                </ul>

                <form class="header-search me-xl-3" action="${ctx}/catalog" method="get" role="search">
                    <label class="visually-hidden" for="headerSearch">Search products</label>
                    <input id="headerSearch" type="search" name="keyword" placeholder="Search products"
                           value="<c:out value='${param.keyword}'/>">
                    <button type="submit">Search</button>
                </form>

                <div class="d-flex align-items-center gap-3 py-2 py-xl-0">
                    <sec:authorize access="isAuthenticated()">
                        <span class="user-chip"><sec:authentication property="name"/></span>
                        <%-- Cart lives here, beside Log out, rather than in the nav links so
                             it's always in the same easy-to-find spot. cartItemCount comes
                             from CartBadgeAdvice. --%>
                        <sec:authorize access="hasRole('CUSTOMER')">
                            <a class="btn btn-sm btn-saffron header-cart" href="${ctx}/cart"
                               title="Your cart" ${fn:startsWith(currentPath, '/cart') ? 'aria-current="page"' : ''}>
                                <svg class="header-cart__icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                    <path d="M3 4h2.2l2.4 11.2a1 1 0 0 0 1 .8h8.9a1 1 0 0 0 1-.76L20.5 8H6.4"
                                          fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                    <circle cx="9.5" cy="19.5" r="1.5" fill="currentColor"/>
                                    <circle cx="17" cy="19.5" r="1.5" fill="currentColor"/>
                                </svg>
                                <span class="visually-hidden">Your cart</span>
                                <c:if test="${cartItemCount > 0}">
                                    <span class="nav-badge"><c:out value="${cartItemCount}"/><span class="visually-hidden"> ${cartItemCount == 1 ? 'item' : 'items'}</span></span>
                                </c:if>
                            </a>
                        </sec:authorize>
                        <form action="${ctx}/logout" method="post" class="m-0">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-sm btn-outline-light">Log out</button>
                        </form>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        <a class="login-link" href="${ctx}/login">Log in</a>
                        <a class="btn btn-sm btn-saffron" href="${ctx}/register/customer">Sign up</a>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </nav>
</header>

<main id="main" class="${fullBleed == 'true' ? '' : 'container page'}">
