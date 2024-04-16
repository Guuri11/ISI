package com.guuri11.isi.application.Command.service;


import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.application.Chat.service.ChatServiceCreateEntity;
import com.guuri11.isi.application.Command.CommandDto;
import com.guuri11.isi.application.Command.CommandRequest;
import com.guuri11.isi.domain.Chat.Chat;
import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.domain.Command.Command;
import com.guuri11.isi.domain.Command.CommandMapper;
import com.guuri11.isi.infrastructure.persistance.ChatRepository;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommandCreate {
    private final CommandRepository repository;
    private final CommandMapper mapper;
    private final CallAiClient callAiClient;
    private final ChatServiceCreateEntity chatServiceCreate;
    private final CommandHandler commandHandler;
    private final ChatRepository chatRepository;

    public CommandDto create(final CommandRequest request, MessageType messageType) {
        final Command entity = createEntity(request);
        entity.setMessageType(messageType);
        List<Command> commands = getChatCommands(request, entity);

        commandHandler.handle(request.request());

        ChatResponse response = callAiClient.call(commands, AiClient.GPT);

        return mapper.toDto(createEntity(response, entity.getChat()));
    }

    private @NotNull List<Command> getChatCommands(CommandRequest request, Command entity) {
        List<Command> commands = new ArrayList<>();
        if (request.chat() != null) {
            commands = repository.findByChat(request.chat());
        }

        commands.add(entity);
        return commands;
    }

    private @NotNull Command createEntity(CommandRequest request) {
        final Command entity = mapper.toEntity(request);

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setChat(getChat(request));
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

    private Command createEntity(ChatResponse response, Chat chat) {
        Command entity = new Command();

        entity.setContent(response.getResult().getOutput().getContent());
        entity.setMessageType(response.getResult().getOutput().getMessageType());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setChat(chat != null ? chat : chatServiceCreate.createEntity());

        repository.save(entity);

        return entity;
    }


}
