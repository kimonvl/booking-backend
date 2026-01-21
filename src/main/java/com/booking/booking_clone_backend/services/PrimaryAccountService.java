package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.PropertyOperationRowDTO;
import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.SummaryTileDTO;

import java.util.List;

public interface PrimaryAccountService {
    List<PropertyOperationRowDTO> getOperationsTable(String userEmail);
    List<SummaryTileDTO> getSummaryTiles(String userEmail);
}
