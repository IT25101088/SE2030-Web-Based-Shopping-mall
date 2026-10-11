package com.sliit.se2030.mall.user.dto;

import jakarta.validation.constraints.NotBlank;

// Deliberately has NO id field -- which merchant this applies to is never
// taken from client input at all. See MerchantProfileService.
public class ShopProfileForm {

    @NotBlank(message = "Shop name is required")
    private String shopName;

    private String shopDescription;

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
}
