package com.guuri11.isi.application.command.service.Task;

import com.guuri11.isi.application.aiclient.CallAiClient;
import com.guuri11.isi.application.command.service.Clipboard.ClipboardRead;
import com.guuri11.isi.application.command.service.Clipboard.ClipboardWrite;
import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class Refactor {
    private final CallAiClient callAiClient;
    private final ClipboardRead clipboardRead;
    private final ClipboardWrite clipboardWrite;

    public ChatResponse refactor() {

        ChatResponse response = callAiClient.call(Prompts.REFACTOR + clipboardRead.read(), AiClient.GPT);
        String output = response.getResult().getOutput().getContent();
        clipboardWrite.write(output);

        return callAiClient.call(Prompts.REFACTOR_DONE, AiClient.GPT);
    }
}