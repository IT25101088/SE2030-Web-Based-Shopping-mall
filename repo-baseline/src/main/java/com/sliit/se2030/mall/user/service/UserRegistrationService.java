package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.dto.CustomerRegistrationForm;
import com.sliit.se2030.mall.user.dto.MerchantRegistrationForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository,
                                    CustomerRepository customerRepository,
                                    MerchantRepository merchantRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerCustomer(CustomerRegistrationForm form) {
        // TODO: implement registerCustomer -- see your NOTES.md,
        // "UserRegistrationService.registerCustomer()" for the required steps
        // (check email availability, hash the password, save).
        throw new UnsupportedOperationException("TODO: implement registerCustomer()");
    }

    public Merchant registerMerchant(MerchantRegistrationForm form) {
        // TODO: implement registerMerchant -- see your NOTES.md,
        // "UserRegistrationService.registerMerchant()" for the required steps
        // (check email availability, hash the password, save; verificationStatus
        // defaults to PENDING via Merchant's field initializer).
        throw new UnsupportedOperationException("TODO: implement registerMerchant()");
    }

    private void assertEmailAvailable(String email) {
        // TODO: implement assertEmailAvailable -- throw BusinessRuleViolationException
        // if userRepository.existsByEmail(email) is true.
        throw new UnsupportedOperationException("TODO: implement assertEmailAvailable()");
    }
}
