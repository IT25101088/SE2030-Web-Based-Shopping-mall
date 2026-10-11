package com.sliit.se2030.mall.feedback.dto;

import jakarta.validation.constraints.NotBlank;

public class MerchantResponseForm {

    @NotBlank(message = "Response cannot be empty")
    private String responseText;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
