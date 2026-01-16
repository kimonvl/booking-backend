package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.language.Language;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepo extends JpaRepository<@NonNull Language, @NonNull Long> {
}
