package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Chat;

import java.time.LocalDateTime;
import java.util.List;

public record ChatResponse(
    Long id,
    List<MessageResponse> messages,
    LocalDateTime createdAt
){

    public static ChatResponse from(Chat chat) {
       return new ChatResponse(
          chat.getId(),
          chat.getMessages().stream().map(MessageResponse::from).toList(),
          chat.getCreatedAt()
        );
    }
}
