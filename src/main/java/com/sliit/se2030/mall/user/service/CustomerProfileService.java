package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.cart.repository.CartItemRepository;
import com.sliit.se2030.mall.cart.repository.CartRepository;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.dto.CustomerProfileForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerProfileService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;

    public CustomerProfileService(CustomerRepository customerRepository,
                                  CartRepository cartRepository,
                                  CartItemRepository cartItemRepository,
                                  CurrentUserProvider currentUserProvider,
                                  PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.currentUserProvider = currentUserProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer getOwnProfile() {
        return findOwnCustomerOrThrow();
    }

    @Transactional
    public void updateProfile(CustomerProfileForm form) {
        Customer customer = findOwnCustomerOrThrow();
        customer.setFullName(form.getFullName());
        customer.setPhone(form.getPhone());
        customer.setShippingAddress(form.getShippingAddress());
    }

    /**
     * Closes the logged-in customer's account. Orders, reviews and help
     * requests all point at the customer row (and merchants still need their
     * order history), so the row itself stays -- instead we empty the cart,
     * wipe every personal detail, release the email so it can be registered
     * again, and disable the account so it can never log in.
     */
    @Transactional
    public void deleteOwnAccount(String currentPassword) {
        Customer customer = findOwnCustomerOrThrow();
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, customer.getPasswordHash())) {
            throw new BusinessRuleViolationException("That password is not correct. Your account was not deleted.");
        }

        cartRepository.findByCustomer_Id(customer.getId()).ifPresent(cart -> {
            cartItemRepository.deleteAll(cartItemRepository.findByCart_Id(cart.getId()));
            cartRepository.delete(cart);
        });

        customer.setEmail("deleted-" + customer.getId() + "@deleted.invalid");
        customer.setFullName("Deleted customer");
        customer.setPhone(null);
        customer.setShippingAddress(null);
        // Random hash nobody knows the password for, on top of enabled = false.
        customer.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        customer.setEnabled(false);
    }

    // No id parameter anywhere in this class -- "which customer" always
    // comes from the authenticated session, never from client-supplied input.
    private Customer findOwnCustomerOrThrow() {
        Long id = currentUserProvider.getCurrentUserId();
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }
}
