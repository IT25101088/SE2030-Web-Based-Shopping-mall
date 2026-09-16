<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Submit an Inquiry"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h1 class="mb-4">Submit an Inquiry</h1>

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

        <form action="${pageContext.request.contextPath}/inquiries" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <label class="form-label">Subject</label>
                <input type="text" name="subject" class="form-control" value="${form.subject}" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Message</label>
                <textarea name="message" class="form-control" required>${form.message}</textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">Related Order Id (optional)</label>
                <input type="number" name="relatedOrderId" class="form-control" value="${form.relatedOrderId}"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Related Product Id (optional)</label>
                <input type="number" name="relatedProductId" class="form-control" value="${form.relatedProductId}"/>
            </div>
            <p class="text-muted"><em>You must provide at least one of the above.</em></p>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>

        <p class="mt-4"><a href="${pageContext.request.contextPath}/inquiries">Back to my inquiries</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
