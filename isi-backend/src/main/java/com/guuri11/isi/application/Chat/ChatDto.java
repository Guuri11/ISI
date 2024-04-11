package com.guuri11.isi.application.Chat;

import com.guuri11.isi.domain.Chat.Chat;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatDto(UUID id, LocalDateTime createdAt, LocalDateTime updateAt) {

}