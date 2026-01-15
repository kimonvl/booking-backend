package com.booking.booking_clone_backend.models.user;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Security principal implementation that adapts the {@link User} entity
 * to Spring Security's {@link UserDetails} contract.
 *
 * <p>This class acts as a bridge between the application's domain user model
 * and Spring Security, providing authentication and authorization details
 * required by the security framework.</p>
 *
 */
public record UserPrincipal(User user) implements UserDetails {

    /**
     * Creates a new security principal for the given user.
     *
     * @param user the domain user entity for the given user
     *
     */
    public UserPrincipal {
    }

    /**
     * Returns the authorities granted to the user.
     *
     * <p>Currently, all users are assigned a default {@code USER} authority.</p>
     *
     * @return the collection of granted authorities
     *
     */
    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Returns the user's encrypted password.
     *
     * @return the encrypted password
     *
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /**
     * Returns the user's identifier.
     *
     * @return the user's unique identifier
     *
     */
    public long getId() {
        return user.getId();
    }

    /**
     * Returns the user's identifier.
     *
     * @return the user's unique identifier
     *
     */
    public Role getRole() {
        return user.getRole();
    }

    /**
     * Returns the username used for authentication.
     *
     * <p>The user's email address is used as the username.</p>
     *
     * @return the user's email address
     *
     */
    @Override
    @NullMarked
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} if the account is not expired
     *
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicated whether the user's account is locked.
     *
     * @return {@code true} if the account is not locked
     *
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return {@code true} if the credentials are not expired
     *
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return {@code true} if the user is enabled
     *
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
