package com.guuri11.isi.application.command;

import com.guuri11.isi.domain.chat.Chat;
import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.domain.command.Task;
import org.springframework.ai.chat.messages.MessageType;

public record CommandRequest(
        String request,
        Chat chat,
        MessageType messageType,
        Task task
) {

}