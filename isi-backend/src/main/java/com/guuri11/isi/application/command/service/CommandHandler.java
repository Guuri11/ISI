package com.guuri11.isi.application.command.service;

import com.guuri11.isi.application.aiclient.CallAiClient;
import com.guuri11.isi.application.chat.service.ChatServiceCreateEntity;
import com.guuri11.isi.application.command.CommandRequest;
import com.guuri11.isi.application.command.service.Task.AddBookmark;
import com.guuri11.isi.application.command.service.Task.LinkedinRejection;
import com.guuri11.isi.application.command.service.Task.OtherTopics;
import com.guuri11.isi.application.command.service.Task.Refactor;
import com.guuri11.isi.domain.chat.Chat;
import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.domain.command.Task;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommandHandler {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CallAiClient callAiClient;
    private final CommandRepository commandRepository;
    private final AddBookmark addBookmark;
    private final Refactor refactor;
    private final OtherTopics otherTopics;
    private final LinkedinRejection linkedinRejection;

    private final ChatServiceCreateEntity chatServiceCreate;

    public Command handle(Command entity, CommandRequest request) {
        ChatResponse taskManagerOutput = callAiClient.call(Prompts.COMMAND_MANAGER_PROMPT + request.request(), AiClient.GPT);
        Task task = getTask(taskManagerOutput.getResult().getOutput().getContent());
        entity.setTask(task);
        Command entitySaved = commandRepository.save(entity);

        ChatResponse chatResponse = switch (task) {
            case Task.REFACTOR -> refactor.refactor();
            // case Task.OPEN_APP -> logger.info("Open app requested"); // TODO
            // case Task.WEATHER -> logger.info("Weather requested"); // TODO
            case Task.BOOKMARK_RECOMMENDATIONS -> addBookmark.add(entitySaved);
            case Task.LINKEDIN_OFFER_REJECTION -> linkedinRejection.reject();
            default -> otherTopics.handle(request, entity);
        };

        return createEntity(chatResponse, request.chat(), task);
    }

    private Command createEntity(ChatResponse response, Chat chat, Task task) {
        logger.info("Response: {}", response.getResult().getOutput().getContent());
        Toast.toast(ToastType.INFO, "ISI \uD83D\uDE3A", response.getResult().getOutput().getContent());
        Command entity = new Command();

        entity.setContent(response.getResult().getOutput().getContent());
        entity.setMessageType(response.getResult().getOutput().getMessageType());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setTask(task);
        entity.setChat(chat != null ? chat : chatServiceCreate.createEntity());

        commandRepository.save(entity);

        return entity;
    }

    private Task getTask(String input) {
        for (Task task : Task.values()) {
            if (input.contains(task.name())) {
                return task;
            }
        }
        return null;
    }
}
