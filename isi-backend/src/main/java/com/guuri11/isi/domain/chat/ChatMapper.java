package com.guuri11.isi.domain.chat;

import com.guuri11.isi.application.chat.ChatDto;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

  public ChatDto toDto(final Chat entity) {

    if (entity == null) {
      return null;
    }
    return new ChatDto(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt());
  }

  public Chat toEntity(final ChatDto dto) {

    if (dto == null) {
      return null;
    }
    return new Chat(dto.id(), dto.createdAt(), dto.updateAt());
  }

}