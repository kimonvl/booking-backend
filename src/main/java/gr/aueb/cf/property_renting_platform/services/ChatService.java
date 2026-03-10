package gr.aueb.cf.property_renting_platform.services;

import gr.aueb.cf.property_renting_platform.DTOs.requests.partner.chat.ChatSearchRequest;
import gr.aueb.cf.property_renting_platform.DTOs.responses.realtime.ChatDTO;
import gr.aueb.cf.property_renting_platform.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface ChatService {
    Page<ChatDTO> getMyPropertyFilteredAndPaginatedChatsPartner(ChatSearchRequest filters, String userEmail) throws EntityNotFoundException;
}
