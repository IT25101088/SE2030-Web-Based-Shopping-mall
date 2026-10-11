package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.cart.entity.Cart;
import com.sliit.se2030.mall.cart.entity.CartItem;
import com.sliit.se2030.mall.cart.repository.CartItemRepository;
import com.sliit.se2030.mall.cart.repository.CartRepository;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.dto.CustomerProfileForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@ExtendWith(MockitoExtension.class)
class CustomerProfileServiceTest {

    private static final Long CUSTOMER_ID = 7L;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerProfileService customerProfileService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        // getId() comes from BaseEntity and has no setter, so a mock stands in
        // for the id while the real setters below still record the changes.
        customer = mock(Customer.class, withSettings()
                .useConstructor("alice@example.com", "OLD_HASH", "Alice Customer")
                .defaultAnswer(CALLS_REAL_METHODS));
        when(customer.getId()).thenReturn(CUSTOMER_ID);
        when(currentUserProvider.getCurrentUserId()).thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
    }

    @Test
    void updateProfile_changesOnlyTheLoggedInCustomersDetails() {
        CustomerProfileForm form = new CustomerProfileForm();
        form.setFullName("Alice New");
        form.setPhone("0771234567");
        form.setShippingAddress("12 Galle Road, Colombo");

        customerProfileService.updateProfile(form);

        assertThat(customer.getFullName()).isEqualTo("Alice New");
        assertThat(customer.getPhone()).isEqualTo("0771234567");
        assertThat(customer.getShippingAddress()).isEqualTo("12 Galle Road, Colombo");
        assertThat(customer.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void deleteOwnAccount_wrongPassword_changesNothing() {
        when(passwordEncoder.matches("wrong", "OLD_HASH")).thenReturn(false);

        assertThatThrownBy(() -> customerProfileService.deleteOwnAccount("wrong"))
                .isInstanceOf(BusinessRuleViolationException.class);

        assertThat(customer.isEnabled()).isTrue();
        assertThat(customer.getEmail()).isEqualTo("alice@example.com");
        verify(cartRepository, never()).findByCustomer_Id(CUSTOMER_ID);
    }

    @Test
    void deleteOwnAccount_rightPassword_clearsCartAndAnonymisesAndDisables() {
        Cart cart = mock(Cart.class);
        when(cart.getId()).thenReturn(3L);
        List<CartItem> items = List.of(mock(CartItem.class));
        when(passwordEncoder.matches("secret", "OLD_HASH")).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("RANDOM_HASH");
        when(cartRepository.findByCustomer_Id(CUSTOMER_ID)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart_Id(3L)).thenReturn(items);

        customerProfileService.deleteOwnAccount("secret");

        verify(cartItemRepository).deleteAll(items);
        verify(cartRepository).delete(cart);
        assertThat(customer.isEnabled()).isFalse();
        assertThat(customer.getEmail()).isEqualTo("deleted-7@deleted.invalid");
        assertThat(customer.getFullName()).isEqualTo("Deleted customer");
        assertThat(customer.getPhone()).isNull();
        assertThat(customer.getShippingAddress()).isNull();
        assertThat(customer.getPasswordHash()).isEqualTo("RANDOM_HASH");
    }
}
