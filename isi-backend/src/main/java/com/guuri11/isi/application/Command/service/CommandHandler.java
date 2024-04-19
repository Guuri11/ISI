package com.guuri11.isi.application.Command.service;

import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.application.Command.CommandRequest;
import com.guuri11.isi.application.Command.service.Task.AddBookmark;
import com.guuri11.isi.application.Command.service.Task.OtherTopics;
import com.guuri11.isi.application.Command.service.Task.Refactor;
import com.guuri11.isi.application.Fav.FavCreate;
import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.domain.Command.Command;
import com.guuri11.isi.domain.Command.Task;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;

@Service
@AllArgsConstructor
public class CommandHandler {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CallAiClient callAiClient;
    private final CommandRepository commandRepository;
    private final AddBookmark addBookmark;
    private final Refactor refactor;
    private final OtherTopics otherTopics;

    public ChatResponse handle(Command entity, CommandRequest request) {
        ChatResponse taskManagerOutput = callAiClient.call(Prompts.COMMAND_MANAGER_PROMPT + request, AiClient.GPT);
        Task task = getTask(taskManagerOutput.getResult().getOutput().getContent());
        entity.setTask(task);
        commandRepository.save(entity);

        if (task == Task.REFACTOR) {
            logger.info("Refactor requested");
            return refactor.refactor();
        } else if (task == Task.OPEN_APP) {
            logger.info("Open app requested");
        } else if (task == Task.WEATHER) {
            logger.info("Weather requested");
        } else if (task == Task.BOOKMARK_RECOMMENDATIONS) {
            logger.info("Book recommendation requested");
            return addBookmark.add();
        } else if (task == Task.OTHER_TOPICS) {
            return otherTopics.handle(request, entity);
        }

        // Default case
        return otherTopics.handle(request, entity);
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
