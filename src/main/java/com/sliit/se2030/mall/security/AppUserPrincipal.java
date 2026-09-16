package com.sliit.se2030.mall.security;

import com.sliit.se2030.mall.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapts our User entity to what Spring Security actually needs to know
 * about a logged-in principal. Spring Security only ever talks to this
 * interface -- it has no idea a "User" entity exists.
 */
public class AppUserPrincipal implements UserDetails {

    private final User user;

    public AppUserPrincipal(User user) {
        this.user = user;
    }

    /** Escape hatch back to the real entity, e.g. from a controller via @AuthenticationPrincipal. */
    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ROLE_" prefix is a Spring Security convention: hasRole("MERCHANT") in
        // SecurityConfig checks for an authority literally named "ROLE_MERCHANT".
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // We log in by email, not a separate "username" field.
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Ties straight into User.enabled -- a suspended merchant simply can't authenticate.
        return user.isEnabled();
    }
}
