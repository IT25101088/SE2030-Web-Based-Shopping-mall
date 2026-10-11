package com.sliit.se2030.mall.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity
@DiscriminatorValue("MERCHANT")
public class Merchant extends User {

    // NOT marked nullable=false: with single-table inheritance this column is
    // shared by Customer/PlatformEmployee rows too, where it's legitimately NULL.
    // "Required for a merchant" is enforced by the constructor and by Bean
    // Validation on the registration form, not by a DB constraint.
    private String shopName;

    private String shopDescription;

    // Same reasoning as shopName above: no nullable=false, since Customer/PlatformEmployee
    // rows in this shared table legitimately have no verification status at all.
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private Instant verifiedAt;

    // Not a real foreign key relationship on purpose -- we only need "who approved
    // this" for an audit trail, not to navigate back to the employee object.
    private Long verifiedByEmployeeId;

    protected Merchant() {
        super();
    }

    public Merchant(String email, String passwordHash, String fullName, String shopName) {
        super(email, passwordHash, fullName, Role.MERCHANT);
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Long getVerifiedByEmployeeId() {
        return verifiedByEmployeeId;
    }

    public void setVerifiedByEmployeeId(Long verifiedByEmployeeId) {
        this.verifiedByEmployeeId = verifiedByEmployeeId;
    }
}
