package com.guuri11.isi.application.command;

import com.guuri11.isi.domain.chat.Chat;
import org.springframework.ai.chat.messages.MessageType;

public record CommandRequest(
        String request,
        Chat chat,
        MessageType messageType
) {

}