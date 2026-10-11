package com.sliit.se2030.mall.user.factory;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.Role;
import com.sliit.se2030.mall.user.entity.User;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Plain unit test of UserFactory. The encoder just prefixes "hashed:" so we can see it was used. */
class UserFactoryTest {

    private final PasswordEncoder fakeEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return "hashed:" + rawPassword;
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encodedPassword.equals(encode(rawPassword));
        }
    };

    private final UserFactory factory = new UserFactory(fakeEncoder);

    @Test
    void eachRole_getsItsOwnUserClass_withAHashedPassword() {
        NewUserDetails details = new NewUserDetails("a@mail.com", "secret1", "A", "0771234567", "A Shop", null);

        User customer = factory.createUser(Role.CUSTOMER, details);
        User merchant = factory.createUser(Role.MERCHANT, details);
        User employee = factory.createUser(Role.PLATFORM_EMPLOYEE, details);

        assertThat(customer).isInstanceOf(Customer.class);
        assertThat(merchant).isInstanceOf(Merchant.class);
        assertThat(employee).isInstanceOf(PlatformEmployee.class);
        assertThat(customer.getPasswordHash()).isEqualTo("hashed:secret1");
        assertThat(customer.getPhone()).isEqualTo("0771234567");
    }

    @Test
    void newMerchant_startsPendingWithItsShopDetails() {
        Merchant merchant = (Merchant) factory.createUser(Role.MERCHANT,
                new NewUserDetails("m@mail.com", "secret1", "M", null, "Tea House", "Fine Ceylon tea"));

        assertThat(merchant.getShopName()).isEqualTo("Tea House");
        assertThat(merchant.getShopDescription()).isEqualTo("Fine Ceylon tea");
        assertThat(merchant.getVerificationStatus()).isEqualTo(VerificationStatus.PENDING);
    }

    @Test
    void merchantWithoutShopName_isRejected() {
        assertThatThrownBy(() -> factory.createUser(Role.MERCHANT, NewUserDetails.of("m@mail.com", "secret1", "M")))
                .isInstanceOf(BusinessRuleViolationException.class);
    }
}
