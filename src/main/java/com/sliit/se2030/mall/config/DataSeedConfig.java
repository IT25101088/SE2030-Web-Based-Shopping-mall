package com.sliit.se2030.mall.config;

import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.repository.PlatformEmployeeRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds one hardcoded platform employee account so there's always a way to
 * log in and approve merchants, even before the registration UI exists.
 * Academic-project scale (see project docs): "single/small number of
 * hardcoded platform employee accounts for demo purposes" is explicitly
 * in scope, not a shortcut we're inventing.
 */
@Configuration
public class DataSeedConfig {

    private static final String SEED_EMAIL = "admin@mall.local";
    private static final String SEED_PASSWORD = "admin123";

    @Bean
    CommandLineRunner seedPlatformEmployee(UserRepository userRepository,
                                            PlatformEmployeeRepository platformEmployeeRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByEmail(SEED_EMAIL)) {
                return;
            }
            PlatformEmployee employee = new PlatformEmployee(
                    SEED_EMAIL, passwordEncoder.encode(SEED_PASSWORD), "Platform Admin");
            employee.setEmployeeCode("EMP-001");
            platformEmployeeRepository.save(employee);
            System.out.println("Seeded platform employee login: " + SEED_EMAIL + " / " + SEED_PASSWORD);
        };
    }
}
