<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="FAQ"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<h1 class="mb-4">Frequently Asked Questions</h1>

<c:choose>
    <c:when test="${empty faqs}">
        <p>No FAQs published yet.</p>
    </c:when>
    <c:otherwise>
        <div class="accordion" id="faqAccordion">
            <c:forEach var="faq" items="${faqs}" varStatus="loop">
                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq${loop.index}">
                            <c:out value="${faq.question}"/>
                        </button>
                    </h2>
                    <div id="faq${loop.index}" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                        <div class="accordion-body"><c:out value="${faq.answer}"/></div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/layout-footer.jsp" %>
