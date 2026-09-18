<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="FAQ Form"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4"><c:choose><c:when test="${not empty faqId}">Edit FAQ</c:when><c:otherwise>Add FAQ</c:otherwise></c:choose></h1>

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

        <c:url var="formAction" value="${not empty faqId ? '/employee/faqs/'.concat(faqId) : '/employee/faqs'}"/>
        <form action="${formAction}" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Question</label>
                <input type="text" name="question" class="form-control" value="${form.question}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Answer</label>
                <textarea name="answer" class="form-control" rows="5" required>${form.answer}</textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">Category</label>
                <input type="text" name="category" class="form-control" value="${form.category}"/>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/employee/faqs">Back to FAQs</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
