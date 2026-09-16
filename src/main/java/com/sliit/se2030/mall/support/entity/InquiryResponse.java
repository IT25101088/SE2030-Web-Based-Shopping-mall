package com.sliit.se2030.mall.support.entity;

import com.sliit.se2030.mall.common.entity.BaseEntity;
import com.sliit.se2030.mall.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// respondedBy is the abstract User type (not Customer/Merchant/PlatformEmployee
// specifically) because either an employee or a merchant may respond -- JPA
// resolves this fine against the single "users" table regardless of subtype.
@Entity
@Table(name = "inquiry_responses")
public class InquiryResponse extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responded_by_user_id", nullable = false)
    private User respondedBy;

    protected InquiryResponse() {
    }

    public InquiryResponse(Inquiry inquiry, String responseText, User respondedBy) {
        this.inquiry = inquiry;
        this.responseText = responseText;
        this.respondedBy = respondedBy;
    }

    public Inquiry getInquiry() {
        return inquiry;
    }

    public String getResponseText() {
        return responseText;
    }

    public User getRespondedBy() {
        return respondedBy;
    }
}
