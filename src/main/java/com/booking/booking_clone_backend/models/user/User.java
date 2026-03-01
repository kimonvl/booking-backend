package com.booking.booking_clone_backend.models.user;

import com.booking.booking_clone_backend.models.AbstractEntity;
import com.booking.booking_clone_backend.models.static_data.Country;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Change to uuid for equals and hashcode
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "users",
        indexes = @Index(name = "idx_users_email", columnList = "email", unique = true))
public class User extends AbstractEntity implements UserDetails {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // TODO move the above to personal info table and make it one-to-one with user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", referencedColumnName = "code", nullable = false)
    private Country country;

    @Column(name = "first_name", length = 120, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 120, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled = true;

    @NullMarked
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities =  new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        role.getCapabilities()
                .forEach(capability -> grantedAuthorities.add(new SimpleGrantedAuthority(capability.getName())));
        return grantedAuthorities;
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @NullMarked
    @Override
    public String getUsername() {
        return email;
    }
}
