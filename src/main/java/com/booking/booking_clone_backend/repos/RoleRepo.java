package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
        Role findByName(String name);
}
