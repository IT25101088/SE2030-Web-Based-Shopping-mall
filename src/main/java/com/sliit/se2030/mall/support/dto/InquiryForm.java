package com.sliit.se2030.mall.support.dto;

import jakarta.validation.constraints.NotBlank;

public class InquiryForm {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message is required")
    private String message;

    // At least one of these two should be set -- enforced in InquiryService, not here.
    private Long relatedOrderId;
    private Long relatedProductId;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRelatedOrderId() {
        return relatedOrderId;
    }

    public void setRelatedOrderId(Long relatedOrderId) {
        this.relatedOrderId = relatedOrderId;
    }

    public Long getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(Long relatedProductId) {
        this.relatedProductId = relatedProductId;
    }
}
