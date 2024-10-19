package com.guuri11.isi.application.aiclient;

import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.domain.command.Command;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final AiClient model = AiClient.GPT;
  private final OpenAiChatClient openAiClient;
  private final OllamaChatClient ollamaClient;

  public ChatResponse call(List<Command> commands) {
    if (isGptModel()) {
      return openAiClient.call(
              new Prompt(
                      getMessageList(commands),
                      OpenAiChatOptions.builder()
                              .withModel("gpt-3.5-turbo-0125")
                              .build()
              ));
    }
    return ollamaClient.call(
            new Prompt(
                    getMessageList(commands),
                    OllamaOptions.create()
                            .withModel("llama3")
            ));
  }

  public ChatResponse call(String command) {
    if (isGptModel()) {
      return openAiClient.call(
              new Prompt(
                      command,
                      OpenAiChatOptions.builder()
                              .withModel("gpt-3.5-turbo-0125")
                              .build()
              ));
    }
    return ollamaClient.call(
            new Prompt(
                    command,
                    OllamaOptions.create()
                            .withModel("llama3")
            ));
  }

  private boolean isGptModel() {
    return model.equals(AiClient.GPT);
  }

  private static @NotNull List<Message> getMessageList(List<Command> commands) {
    return commands.stream()
            .map(c -> new ChatMessage(c.getMessageType(), c.getContent()))
            .collect(Collectors.toList());
  }
}
