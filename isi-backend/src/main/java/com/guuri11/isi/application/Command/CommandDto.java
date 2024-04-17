package com.guuri11.isi.application.Command;

import com.guuri11.isi.domain.Chat.Chat;
import com.guuri11.isi.domain.Command.Task;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommandDto(
        UUID id,
        String log,
        Chat chat,
        LocalDateTime createdAt,
        LocalDateTime updateAt,
        Task task,
        String content,
        MessageType messageType
) {

}