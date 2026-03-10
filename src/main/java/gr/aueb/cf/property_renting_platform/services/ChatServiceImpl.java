package gr.aueb.cf.property_renting_platform.services;

import gr.aueb.cf.property_renting_platform.DTOs.requests.partner.chat.ChatSearchRequest;
import gr.aueb.cf.property_renting_platform.DTOs.responses.realtime.ChatDTO;
import gr.aueb.cf.property_renting_platform.exceptions.EntityNotFoundException;
import gr.aueb.cf.property_renting_platform.mappers.ChatCustomMapper;
import gr.aueb.cf.property_renting_platform.models.user.User;
import gr.aueb.cf.property_renting_platform.repos.ChatRepo;
import gr.aueb.cf.property_renting_platform.repos.UserRepo;
import gr.aueb.cf.property_renting_platform.repos.specifications.ChatSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final GuestPropertyService propertyService;
    private final ChatRepo chatRepo;
    private final UserRepo userRepo;
    private final ChatCustomMapper chatCustomMapper;

    // TODO: check what queries are generated and optimize if needed.
    // TODO: add PARTNER_CHAT capability to partner role
    @Override
    @PreAuthorize("hasAuthority('PARTNER_CHAT')  and @securityService.isUserOwnerOfProperty(#filters.propertyId(), authentication)")
    public Page<ChatDTO> getMyPropertyFilteredAndPaginatedChatsPartner(ChatSearchRequest filters, String userEmail) throws EntityNotFoundException {
        try {
            User user = userRepo.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("getChatsUser", "User with email " + userEmail + " not found"));
            if (!propertyService.isPropertyExists(filters.propertyId()))
                throw new EntityNotFoundException("getChatsProperty", "Property with id " + filters.propertyId() + " not found");

            var filtered = chatRepo.findAll(ChatSpecification.build(filters, user.getId()), PageRequest.of(filters.page(), filters.size()));
            var result = filtered.map(chatCustomMapper::chatToChatDTO);
            log.info("Chats retrieved for user {}: propertyId={}, totalElements={}, totalPages={}", userEmail, filters.propertyId(), result.getTotalElements(), result.getTotalPages());
            return result;
        } catch (EntityNotFoundException e) {
            log.warn("Failed to get chats for user {}: Message={} ", userEmail, e.getMessage());
            throw e;
        }
    }
}
