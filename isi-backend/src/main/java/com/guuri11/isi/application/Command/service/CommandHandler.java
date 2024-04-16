package com.guuri11.isi.application.Command.service;

import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommandHandler {
    private final CallAiClient callAiClient;

    public void handle(String request) {
        ChatResponse taskManagerOutput = callAiClient.call(Prompts.COMMAND_MANAGER_PROMPT + request, AiClient.GPT);
        switch (taskManagerOutput.getResult().getOutput().getContent()) {
            case "REFACTOR":
                System.out.println("Refactor solicitado");
                break;
            case "WEATHER":
                System.out.println("Weather solicitado");
                break;
            case "OPEN_APP":
                System.out.println("Open app solicitado");
                break;
            default: break;
        }
    }
}
