package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.dto.CustomerRegistrationForm;
import com.sliit.se2030.mall.user.dto.MerchantRegistrationForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.Role;
import com.sliit.se2030.mall.user.factory.NewUserDetails;
import com.sliit.se2030.mall.user.factory.UserFactory;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.stereotype.Service;

// Builds new accounts through UserFactory (Factory pattern) rather than
// calling the Customer/Merchant constructors directly.
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final UserFactory userFactory;

    public UserRegistrationService(UserRepository userRepository,
                                    CustomerRepository customerRepository,
                                    MerchantRepository merchantRepository,
                                    UserFactory userFactory) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.userFactory = userFactory;
    }

    public Customer registerCustomer(CustomerRegistrationForm form) {
        assertEmailAvailable(form.getEmail());
        NewUserDetails details = new NewUserDetails(
                form.getEmail(), form.getPassword(), form.getFullName(), form.getPhone(), null, null);
        // The cast is safe: the factory always returns a Customer for Role.CUSTOMER.
        Customer customer = (Customer) userFactory.createUser(Role.CUSTOMER, details);
        return customerRepository.save(customer);
    }

    public Merchant registerMerchant(MerchantRegistrationForm form) {
        assertEmailAvailable(form.getEmail());
        // A merchant can log in immediately but stays restricted until an
        // employee approves them (enforced later, in the merchant controllers).
        NewUserDetails details = new NewUserDetails(form.getEmail(), form.getPassword(), form.getFullName(),
                form.getPhone(), form.getShopName(), form.getShopDescription());
        Merchant merchant = (Merchant) userFactory.createUser(Role.MERCHANT, details);
        return merchantRepository.save(merchant);
    }

    private void assertEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleViolationException("An account with this email already exists.");
        }
    }
}
