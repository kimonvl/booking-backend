package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom {@link UserDetailsService} implementation used by Spring Security
 * to load user specific data during authentication.
 *
 * <p>This service retrieves a {@link User} entity by email and adapts it
 * to Spring Security's {@link UserDetails} contract using {@link UserPrincipal}</p>
 * */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    // Repos
    private final UserRepo userRepo;
    /**
     * Loads the user identified by the given username (email)
     *
     * @param username email address of the user
     * @return a fully populated {@link UserDetails} instance
     * @throws UsernameNotFoundException if the user doesn't exist
     * */
    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepo.findByEmailIgnoreCase(username);
        if (userOpt.isEmpty())
            throw new UsernameNotFoundException("Email not found");

        return new UserPrincipal(userOpt.get());
    }
}
