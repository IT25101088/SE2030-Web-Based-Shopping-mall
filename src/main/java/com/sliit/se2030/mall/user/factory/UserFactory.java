package com.sliit.se2030.mall.user.factory;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.Role;
import com.sliit.se2030.mall.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Factory pattern -- the Factory class. The one place that turns a Role into
 * the right User subclass (Customer, Merchant or PlatformEmployee) and does
 * the setup every new account needs, such as hashing the password. Callers
 * (registration, data seeding) never call those constructors themselves.
 *
 * It only builds the object; saving it is up to the caller.
 */
@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(Role role, NewUserDetails details) {
        String passwordHash = passwordEncoder.encode(details.rawPassword());
        User user = switch (role) {
            case CUSTOMER -> new Customer(details.email(), passwordHash, details.fullName());
            case MERCHANT -> createMerchant(details, passwordHash);
            case PLATFORM_EMPLOYEE -> new PlatformEmployee(details.email(), passwordHash, details.fullName());
        };
        user.setPhone(details.phone());
        return user;
    }

    // A new shop starts as VerificationStatus.PENDING (Merchant's field
    // initializer): it can log in but stays restricted until an employee approves it.
    private Merchant createMerchant(NewUserDetails details, String passwordHash) {
        if (details.shopName() == null || details.shopName().isBlank()) {
            throw new BusinessRuleViolationException("A shop name is required.");
        }
        Merchant merchant = new Merchant(details.email(), passwordHash, details.fullName(), details.shopName());
        merchant.setShopDescription(details.shopDescription());
        return merchant;
    }
}
