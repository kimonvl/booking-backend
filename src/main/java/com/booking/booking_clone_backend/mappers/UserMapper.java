package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper responsible for converting {@link User} entities
 * into {@link UserDTO} objects.
 *
 * <p>This mapper is used to transform user persistence models
 * into API-facing data transfer objects.</p>
 * */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserMapper {

    /**
     * Converts a {@link User} into a {@link UserDTO}.
     *
     * @param user the user entity to convert
     * @return the mapped user DTO
     * */
    @Mapping(target = "country", source = "country.code")
    UserDTO toDto(User user);

    /**
     * Converts a List of {@link User}s into a List of {@link UserDTO}s.
     *
     * @param users the List of user entities to convert
     * @return the List of mapped user DTOs
     * */
    @Mapping(target = "country", source = "country.code")
    List<UserDTO> toDtoList(List<User> users);
}

