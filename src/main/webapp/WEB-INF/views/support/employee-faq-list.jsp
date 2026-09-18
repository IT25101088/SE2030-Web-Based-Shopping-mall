<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="FAQ Management"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="mb-0">FAQ Management</h1>
    <a href="${pageContext.request.contextPath}/employee/faqs/new" class="btn btn-primary">Add FAQ</a>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
</c:if>

<c:choose>
    <c:when test="${empty faqs}">
        <p>No FAQs have been created yet.</p>
    </c:when>
    <c:otherwise>
        <table class="table table-striped table-bordered bg-white">
            <tr>
                <th>Question</th>
                <th>Category</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="faq" items="${faqs}">
                <tr>
                    <td><c:out value="${faq.question}"/></td>
                    <td><c:out value="${faq.category}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${faq.published}">
                                <span class="badge bg-success">Published</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Unpublished</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/employee/faqs/${faq.id}/edit"
                           class="btn btn-secondary btn-sm">Edit</a>
                        <c:choose>
                            <c:when test="${faq.published}">
                                <form action="${pageContext.request.contextPath}/employee/faqs/${faq.id}/unpublish"
                                      method="post" class="d-inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-warning btn-sm">Unpublish</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/employee/faqs/${faq.id}/publish"
                                      method="post" class="d-inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-success btn-sm">Publish</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                        <form action="${pageContext.request.contextPath}/employee/faqs/${faq.id}/delete"
                              method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm"
                                    onclick="return confirm('Remove this FAQ?');">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
