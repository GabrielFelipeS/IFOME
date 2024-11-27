package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Message;

import java.time.LocalDateTime;

public record MessageResponse(
    String content,
    String senderType,
    LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
          message.getContent(),
          message.getSenderType(),
          message.getCreatedAt()
        );
    }
}
