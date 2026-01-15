package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partner")
public class TestController {

    @GetMapping("/getTest")
    public ResponseEntity<@NonNull GenericResponse<String>> register(HttpServletResponse response) {

        return ResponseFactory.createSuccessResponse(
                "test passed",
                MessageConstants.REGISTERED,
                HttpStatus.CREATED
        );
    }
}
