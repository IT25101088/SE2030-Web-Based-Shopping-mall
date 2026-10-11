package com.sliit.se2030.mall.feedback.entity;

import com.sliit.se2030.mall.common.entity.BaseEntity;
import com.sliit.se2030.mall.user.entity.Merchant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// One response per review. Service layer must check respondedBy equals
// review.getProduct().getMerchant() -- a merchant can only respond to
// reviews on their own products.
@Entity
@Table(name = "merchant_responses")
public class MerchantResponse extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false, unique = true)
    private Review review;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responded_by_merchant_id", nullable = false)
    private Merchant respondedBy;

    protected MerchantResponse() {
    }

    public MerchantResponse(Review review, String responseText, Merchant respondedBy) {
        this.review = review;
        this.responseText = responseText;
        this.respondedBy = respondedBy;
    }

    public Review getReview() {
        return review;
    }

    public String getResponseText() {
        return responseText;
    }

    public Merchant getRespondedBy() {
        return respondedBy;
    }
}
