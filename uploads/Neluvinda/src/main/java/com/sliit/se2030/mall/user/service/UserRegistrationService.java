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
        assertEmailAvailable(form.getEmail());
        Customer customer = new Customer(
                form.getEmail(), passwordEncoder.encode(form.getPassword()), form.getFullName());
        customer.setPhone(form.getPhone());
        return customerRepository.save(customer);
    }

    public Merchant registerMerchant(MerchantRegistrationForm form) {
        assertEmailAvailable(form.getEmail());
        // VerificationStatus.PENDING by default -- see Merchant's field initializer.
        // A merchant can log in immediately but stays restricted until an
        // employee approves them (enforced later, in the merchant controllers).
        Merchant merchant = new Merchant(
                form.getEmail(), passwordEncoder.encode(form.getPassword()), form.getFullName(), form.getShopName());
        merchant.setPhone(form.getPhone());
        merchant.setShopDescription(form.getShopDescription());
        return merchantRepository.save(merchant);
    }

    private void assertEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleViolationException("An account with this email already exists.");
        }
    }
}
