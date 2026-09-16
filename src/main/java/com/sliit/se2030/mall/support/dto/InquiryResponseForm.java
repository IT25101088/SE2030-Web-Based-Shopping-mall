package com.sliit.se2030.mall.support.dto;

import jakarta.validation.constraints.NotBlank;

public class InquiryResponseForm {

    @NotBlank(message = "Response cannot be empty")
    private String responseText;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
