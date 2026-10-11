package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.dto.CustomerRegistrationForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Plain unit test -- no Spring context, no database. @Mock creates fake
 * UserRepository/PasswordEncoder we fully control; @InjectMocks wires those
 * fakes into a real UserRegistrationService instance. Fast, and isolates
 * exactly this class's logic from everything it depends on.
 */
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Test
    void registerCustomer_hashesPasswordAndSavesCustomer() {
        CustomerRegistrationForm form = new CustomerRegistrationForm();
        form.setEmail("alice@example.com");
        form.setPassword("plaintext123");
        form.setFullName("Alice Customer");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintext123")).thenReturn("HASHED_VALUE");
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer saved = userRegistrationService.registerCustomer(form);

        // Capture exactly what was passed to save(), so we can assert on the
        // real object's state rather than just trusting the mock's return value.
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());

        assertThat(captor.getValue().getEmail()).isEqualTo("alice@example.com");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("HASHED_VALUE");
        assertThat(saved.getPasswordHash()).isEqualTo("HASHED_VALUE");
    }

    @Test
    void registerCustomer_rejectsDuplicateEmail() {
        CustomerRegistrationForm form = new CustomerRegistrationForm();
        form.setEmail("taken@example.com");
        form.setPassword("plaintext123");
        form.setFullName("Someone");

        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userRegistrationService.registerCustomer(form))
                .isInstanceOf(BusinessRuleViolationException.class);

        // The whole point of the early check: never even attempt to save a duplicate.
        verify(customerRepository, never()).save(any());
    }
}
