package com.booking.booking_clone_backend.config.filters;

import com.booking.booking_clone_backend.services.JwtService;
import com.booking.booking_clone_backend.services.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authenticates requests using the ACCESS token from:
 * Authorization: Bearer <token>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailsService userDetailsService;

    /**
     * Processes the request attempting to extract and validate the jwt, granting access in case of a valid token,
     * not granting access and clearing the cookies in case of an invalid one.
     *
     * @param request      the incoming HTTP request
     * @param response     the HTTP response to modify if necessary
     * @param chain  the remaining filter chain to execute
     * @throws ServletException if the filter fails during processing
     * @throws IOException      if I/O errors occur during filtering
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring("Bearer ".length()).trim();

        try {
            Jws<Claims> jws = jwtService.parseAndValidate(token);
            String email = jws.getPayload().getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            // invalid/expired -> treat as unauthenticated
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
