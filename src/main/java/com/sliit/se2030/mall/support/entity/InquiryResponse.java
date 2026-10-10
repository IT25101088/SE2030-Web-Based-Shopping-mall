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
//
// respondedBy is EAGER on purpose: a LAZY User reference is loaded as a plain
// User proxy, so the pages couldn't read Merchant.shopName from it to label a
// shop's reply. EAGER loads the real Merchant / PlatformEmployee object.
//
// internalNote = true means only the mall team and shops see it, never the
// customer. A shop's answer is always an internal note: the mall team reviews
// it and decides what to tell the customer.
@Entity
@Table(name = "inquiry_responses")
public class InquiryResponse extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseText;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "responded_by_user_id", nullable = false)
    private User respondedBy;

    @Column(nullable = false)
    private boolean internalNote;

    protected InquiryResponse() {
    }

    public InquiryResponse(Inquiry inquiry, String responseText, User respondedBy, boolean internalNote) {
        this.inquiry = inquiry;
        this.responseText = responseText;
        this.respondedBy = respondedBy;
        this.internalNote = internalNote;
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

    public boolean isInternalNote() {
        return internalNote;
    }
}
