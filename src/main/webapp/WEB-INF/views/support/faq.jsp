<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="FAQ"/>
<%@ include file="/WEB-INF/views/common/layout-header.jsp" %>

<div class="page-head">
    <div>
        <h1>Questions and answers</h1>
        <p class="lede">Answers to what shoppers ask us most. Can't find yours? Send us a question.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${ctx}/inquiries/new">Ask a question</a>
</div>

<c:choose>
    <c:when test="${empty faqs}">
        <div class="empty">
            <h2>No answers published yet</h2>
            <p>Send us your question and our team will reply directly.</p>
            <a class="btn btn-primary" href="${ctx}/inquiries/new">Ask a question</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="accordion" id="faqAccordion">
            <c:forEach var="faq" items="${faqs}" varStatus="loop">
                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq${loop.index}" aria-expanded="false" aria-controls="faq${loop.index}">
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
