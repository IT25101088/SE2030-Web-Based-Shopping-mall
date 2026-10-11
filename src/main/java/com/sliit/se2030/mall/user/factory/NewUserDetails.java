package com.sliit.se2030.mall.user.factory;

/**
 * What UserFactory needs to build an account. shopName and shopDescription
 * are only used for merchants; phone and both shop fields may be null.
 * The password is the raw one typed in -- the factory hashes it.
 */
public record NewUserDetails(String email, String rawPassword, String fullName, String phone,
                             String shopName, String shopDescription) {

    public static NewUserDetails of(String email, String rawPassword, String fullName) {
        return new NewUserDetails(email, rawPassword, fullName, null, null, null);
    }
}
