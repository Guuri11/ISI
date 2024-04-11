package com.guuri11.isi.application.Command;

import com.guuri11.isi.domain.Chat.Chat;
import org.springframework.ai.chat.messages.MessageType;

public record CommandRequest(
        String request,
        Chat chat,
        MessageType messageType
) {

}