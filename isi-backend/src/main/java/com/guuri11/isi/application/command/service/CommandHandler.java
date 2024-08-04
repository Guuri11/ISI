package com.guuri11.isi.application.command.service;

import com.guuri11.isi.application.aiclient.CallAiClient;
import com.guuri11.isi.application.command.CommandRequest;
import com.guuri11.isi.application.command.service.Task.AddBookmark;
import com.guuri11.isi.application.command.service.Task.LinkedinRejection;
import com.guuri11.isi.application.command.service.Task.OtherTopics;
import com.guuri11.isi.application.command.service.Task.Refactor;
import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.domain.command.Task;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

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

    public ChatResponse handle(Command entity, CommandRequest request) {
        ChatResponse taskManagerOutput = callAiClient.call(Prompts.COMMAND_MANAGER_PROMPT + request.request(), AiClient.GPT);
        Task task = getTask(taskManagerOutput.getResult().getOutput().getContent());
        entity.setTask(task);
        commandRepository.save(entity);

        return switch (task) {
            case Task.REFACTOR -> refactor.refactor();
            // case Task.OPEN_APP -> logger.info("Open app requested"); // TODO
            // case Task.WEATHER -> logger.info("Weather requested"); // TODO
            case Task.BOOKMARK_RECOMMENDATIONS -> addBookmark.add();
            case Task.LINKEDIN_OFFER_REJECTION -> linkedinRejection.reject();
            default -> otherTopics.handle(request, entity);
        };
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
