package com.guuri11.isi.domain.command;

import com.guuri11.isi.application.command.CommandDto;
import com.guuri11.isi.application.command.CommandRequest;
import org.springframework.stereotype.Component;

@Component
public class CommandMapper {

  public CommandDto toDto(final Command entity) {

    if (entity == null) {
      return null;
    }
    return new CommandDto(
            entity.getId(),
            entity.getLog(),
            entity.getChat(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getTask(),
            entity.getContent(),
            entity.getMessageType()
    );
  }

  public Command toEntity(final CommandDto dto) {

    if (dto == null) {
      return null;
    }
    return new Command(
            dto.id(),
            dto.log(),
            dto.content(),
            dto.messageType(),
            dto.chat(),
            dto.task(),
            dto.createdAt(),
            dto.updateAt());
  }

  public Command toEntity(final CommandRequest request) {

    if (request == null) {
      return null;
    }
    final Command command = new Command();
    if (request.chat() != null) {
      command.setChat(request.chat());
    }
    command.setContent(request.request());
    command.setMessageType(request.messageType());
    return command;
  }
}
