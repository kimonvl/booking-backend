package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.PropertyOperationRowDTO;
import com.booking.booking_clone_backend.DTOs.responses.partner.primary_account.SummaryTileDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;
import com.booking.booking_clone_backend.mappers.AddressMapper;
import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrimaryAccountServiceImpl implements PrimaryAccountService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    PropertyRepo propertyRepo;
    @Autowired
    BookingRepo bookingRepo;
    @Autowired
    AddressMapper addressMapper;

    @Override
    public List<PropertyOperationRowDTO> getOperationsTable(String userEmail) {
        User owner = userRepo.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("primary_account.operations_table.user.not_found"));


        List<PropertyOperationRowDTO> propertyOperationTable = new ArrayList<>();
        List<Property> properties = propertyRepo.findByOwner(owner);

        var from = LocalDate.now();
        var to = from.plusDays(2);
        var arrivalsNext48 = bookingRepo.countArrivalsByProperty(
                owner.getId(),
                BookingStatus.CONFIRMED,
                from,
                to
        );
        for (var item : arrivalsNext48){
            System.out.println(item.getPropertyId());
        }
        var departuresNext48 = bookingRepo.countDeparturesByProperty(
                owner.getId(),
                BookingStatus.CONFIRMED,
                from,
                to
        );
        for (Property property : properties) {
            Long arrivalsCount = 0L;
            Long departuresCount = 0L;
            var arrivals = arrivalsNext48.stream().filter((row) -> row.getPropertyId() == property.getId()).toList();
            var departures = departuresNext48.stream().filter((row) -> row.getPropertyId() == property.getId()).toList();
            if (!arrivals.isEmpty()){
                arrivalsCount = arrivals.getFirst().getCount();
            }
            if (!departures.isEmpty()){
                departuresCount = departures.getFirst().getCount();
            }
            PropertyOperationRowDTO propertyOperationRowDTO = new PropertyOperationRowDTO(
                    property.getId(),
                    property.getName(),
                    addressMapper.toDto(property.getAddress()),
                    property.getStatus(),
                    arrivalsCount,
                    departuresCount,
                    0L,
                    0L
            );
            propertyOperationTable.add(propertyOperationRowDTO);
        }

        return propertyOperationTable;
    }

    @Override
    public List<SummaryTileDTO> getSummaryTiles(String userEmail) {
        Optional<User> ownerOpt = userRepo.findByEmailIgnoreCase(userEmail);
        if (ownerOpt.isEmpty())
            throw new EntityNotFoundException(MessageConstants.USER_NOT_FOUND);
        User owner = ownerOpt.get();

        ZoneId zone = ZoneId.of("Europe/Athens");
        Instant fromInstant = LocalDate.now(zone).atStartOfDay(zone).toInstant();
        Instant toInstant = LocalDate.now(zone).plusDays(1).atStartOfDay(zone).toInstant();
        LocalDate today = LocalDate.now(zone);

        List<SummaryTileDTO> tiles = new ArrayList<>();
        tiles.add(new SummaryTileDTO("Reservations", bookingRepo.countReservationsCreatedBetween(
                owner.getId(),
                BookingStatus.CONFIRMED,
                fromInstant,
                toInstant
        )));
        tiles.add(new SummaryTileDTO("Arrivals", bookingRepo.countArrivalsBetween(
                owner.getId(),
                BookingStatus.CONFIRMED,
                today,
                today
        )));
        tiles.add(new SummaryTileDTO("Departures", bookingRepo.countDeparturesBetween(
                owner.getId(),
                BookingStatus.CONFIRMED,
                today,
                today
        )));
        // fix this
        tiles.add(new SummaryTileDTO("Reviews", 0L));
        tiles.add(new SummaryTileDTO("Cancellations", 0L));
        return tiles;
    }
}
