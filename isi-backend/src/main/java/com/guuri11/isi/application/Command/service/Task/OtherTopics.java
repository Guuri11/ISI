package com.guuri11.isi.application.Command.service.Task;

import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.application.Command.CommandRequest;
import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.domain.Command.Command;
import com.guuri11.isi.infrastructure.persistance.CommandRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OtherTopics {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommandRepository repository;
    private final CallAiClient callAiClient;
    public ChatResponse handle(final CommandRequest request, Command entity) {
        List<Command> commands = getChatCommands(request, entity);

        logger.info("Getting all commands {}", commands);
        return callAiClient.call(commands, AiClient.GPT);
    }

    private @NotNull List<Command> getChatCommands(CommandRequest request, Command entity) {
        List<Command> commands = new ArrayList<>();
        if (request.chat() != null) {
            commands = repository.findByChat(request.chat());
        }

        commands.add(entity);
        return commands;
    }
}
