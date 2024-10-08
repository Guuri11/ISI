package com.guuri11.isi.application.command.service;


import com.guuri11.isi.application.chat.service.ChatServiceCreateEntity;
import com.guuri11.isi.application.command.CommandDto;
import com.guuri11.isi.application.command.CommandRequest;
import com.guuri11.isi.domain.chat.Chat;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.domain.command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.ChatRepository;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommandCreate {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommandRepository repository;
    private final CommandMapper mapper;
    private final ChatServiceCreateEntity chatServiceCreate;
    private final CommandHandler commandHandler;
    private final ChatRepository chatRepository;

    public CommandDto create(final CommandRequest request, MessageType messageType) {
        final Command entity = createEntity(request);
        entity.setMessageType(messageType);

        return mapper.toDto(commandHandler.handle(entity, request));
    }

    private @NotNull Command createEntity(CommandRequest request) {
        final Command entity = mapper.toEntity(request);

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setChat(getChat(request));
        if (request.task() != null) {
            entity.setTask(request.task());
        }

        repository.save(entity);

        return entity;
    }

    private Chat getChat(CommandRequest request) {
        if (request.chat() != null) {
            Optional<Chat> chat = chatRepository.findById(request.chat().getId());
            if (chat.isPresent()) {
                return chat.get();
            }
        }
        return chatServiceCreate.createEntity();
    }
}
