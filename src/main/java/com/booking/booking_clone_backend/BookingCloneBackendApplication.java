package com.booking.booking_clone_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookingCloneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingCloneBackendApplication.class, args);
	}

}
