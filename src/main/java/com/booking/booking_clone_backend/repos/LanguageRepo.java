package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.static_data.Language;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepo extends JpaRepository<@NonNull Language, @NonNull Long> {
    List<Language> findByCodeIn(List<String> codes);
}
