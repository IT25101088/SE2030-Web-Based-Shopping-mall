package com.sliit.se2030.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.sliit.se2030.mall.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The one shared security config for the whole app. Other modules don't
 * touch this file -- they just add their own URL prefix to the matching
 * hasRole(...) block below as they build their controllers.
 *
 * Note on wiring: we only declare a PasswordEncoder and (separately, in
 * AppUserDetailsService) a UserDetailsService as beans. Spring Boot's
 * auto-configuration detects both and builds the AuthenticationManager /
 * DaoAuthenticationProvider for us -- we never construct those by hand.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                     RoleBasedAuthenticationSuccessHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public: home, auth pages, static assets, product browsing.
                        // "/error" is Spring Boot's internal forward target for unhandled
                        // requests (e.g. a 404). "/WEB-INF/**" is where every JSP view
                        // actually lives -- rendering ANY view (even on an otherwise-public
                        // page) is itself an internal forward to that path, which Spring
                        // Security re-checks unless it's explicitly public. Both are safe
                        // to permit: a browser can never reach /WEB-INF/** directly, the
                        // servlet container blocks that regardless of Spring Security.
                        .requestMatchers("/", "/login", "/register/**", "/css/**", "/js/**", "/images/**",
                                "/catalog/**", "/faq/**", "/error", "/WEB-INF/**")
                        .permitAll()

                        // Customer-only areas.
                        .requestMatchers("/customer/**", "/cart/**", "/checkout/**", "/orders/**",
                                "/reviews/submit/**", "/inquiries/**")
                        .hasRole("CUSTOMER")

                        // Merchant-only areas.
                        .requestMatchers("/merchant/**")
                        .hasRole("MERCHANT")

                        // Platform employee-only areas.
                        .requestMatchers("/employee/**")
                        .hasRole("PLATFORM_EMPLOYEE")

                        // Anything else: must at least be logged in.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );

        return http.build();
    }
}
