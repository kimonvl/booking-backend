package com.booking.booking_clone_backend.controllers.partner;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.PropertyOperationRowDTO;
import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.SummaryTileDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.services.PrimaryAccountService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/partner/primaryAccount")
public class PrimaryAccountDashboardController {
    private final PrimaryAccountService primaryAccountService;

    @GetMapping("/getOperationsTable")
    public ResponseEntity<@NonNull GenericResponse<List<PropertyOperationRowDTO>>> getOperationsTable(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseFactory.createResponse(
                primaryAccountService.getOperationsTable(principal.getUsername()),
                MessageConstants.OPERATIONS_TABLE_FETCHED,
                HttpStatus.ACCEPTED,
                true
        );
    }

    @GetMapping("/getSummaryTiles")
    public ResponseEntity<@NonNull GenericResponse<List<SummaryTileDTO>>> getSummaryTiles(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseFactory.createResponse(
                primaryAccountService.getSummaryTiles(principal.getUsername()),
                MessageConstants.OPERATIONS_TABLE_FETCHED,
                HttpStatus.ACCEPTED,
                true
        );
    }
}
