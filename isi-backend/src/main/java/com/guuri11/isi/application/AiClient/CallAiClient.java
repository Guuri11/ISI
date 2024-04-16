package com.guuri11.isi.application.AiClient;

import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.domain.Command.Command;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.ChatMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CallAiClient {
    private final OpenAiChatClient openAiClient;
    private final OllamaChatClient ollamaClient;

    public ChatResponse call(List<Command> commands, AiClient aiClient) {
        if (aiClient.equals(AiClient.GPT)) {
            return openAiClient.call(
                    new Prompt(
                            getMessageList(commands),
                            OpenAiChatOptions.builder()
                                    .withModel("gpt-3.5-turbo")
                                    .build()
                    ));
        } else {
            // condition: aiClient.equals(AiClient.OLLAMA)
            return ollamaClient.call(
                    new Prompt(
                            getMessageList(commands),
                            OllamaOptions.create()
                                    .withModel("llama2")
                    ));
        }
    }

    public ChatResponse call(String command, AiClient aiClient) {
        if (aiClient.equals(AiClient.GPT)) {
            return openAiClient.call(
                    new Prompt(
                            command,
                            OpenAiChatOptions.builder()
                                    .withModel("gpt-3.5-turbo")
                                    .build()
                    ));
        } else {
            // condition: aiClient.equals(AiClient.OLLAMA)
            return ollamaClient.call(
                    new Prompt(
                            command,
                            OllamaOptions.create()
                                    .withModel("llama2")
                    ));
        }
    }

    private static @NotNull List<Message> getMessageList(List<Command> commands) {
        return commands.stream()
                .map(c -> new ChatMessage(c.getMessageType(), c.getContent()))
                .collect(Collectors.toList());
    }
}
