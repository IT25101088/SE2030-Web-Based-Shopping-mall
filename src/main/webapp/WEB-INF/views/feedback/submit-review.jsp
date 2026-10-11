<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Write a review"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="panel panel--narrow">
    <h1 class="mb-1">Write a review</h1>
    <p class="text-secondary mb-4"><c:out value="${orderItem.product.name}"/></p>

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

    <form action="${ctx}/reviews/submit" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="orderItemId" value="${form.orderItemId}"/>

        <%--
            Star picker: five ordinary radio buttons named "rating" (values 1-5),
            so the server receives exactly what the old number box sent.
            CSS hides the round buttons and draws each label as a star.
            They're printed 5..1 and flipped back with flex-direction: row-reverse,
            which lets the CSS "~" selector light up every star below the chosen one.
        --%>
        <fieldset class="mb-3">
            <legend class="form-label fs-6">Your rating</legend>
            <div class="star-picker">
                <c:forEach var="n" begin="1" end="5">
                    <c:set var="value" value="${6 - n}"/>
                    <input type="radio" id="star${value}" name="rating" value="${value}" ${form.rating == value ? 'checked' : ''} required>
                    <label for="star${value}" title="${value} out of 5"><span class="visually-hidden">${value} out of 5</span>&#9733;</label>
                </c:forEach>
            </div>
        </fieldset>
        <div class="mb-4">
            <label class="form-label" for="comment">Your review <span class="fw-normal text-secondary">(optional)</span></label>
            <textarea id="comment" name="comment" class="form-control" placeholder="What did you like, and what could be better?"><c:out value="${form.comment}"/></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Post review</button>
    </form>

    <a class="back-link" href="${ctx}/orders">Back to my orders</a>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
