package com.sliit.se2030.mall.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Called by Spring Security's form-login filter immediately after a
 * successful authentication -- this replaces SecurityConfig's old
 * defaultSuccessUrl("/", true) with a role-aware redirect instead.
 */
@Component
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {
        String targetPath = "/";
        if (authentication.getPrincipal() instanceof AppUserPrincipal appUserPrincipal) {
            targetPath = switch (appUserPrincipal.getUser().getRole()) {
                case CUSTOMER -> "/customer/home";
                case MERCHANT -> "/merchant/dashboard";
                case PLATFORM_EMPLOYEE -> "/employee/dashboard";
            };
        }
        response.sendRedirect(request.getContextPath() + targetPath);
    }
}
