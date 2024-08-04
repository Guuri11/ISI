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
public class LinkedinRejection {
    private final CallAiClient callAiClient;
    private final ClipboardRead clipboardRead;
    private final ClipboardWrite clipboardWrite;

    public ChatResponse reject() {

        ChatResponse response = callAiClient.call(Prompts.LINKEDIN_OFFER_REJECTION + clipboardRead.read(), AiClient.GPT);
        String output = response.getResult().getOutput().getContent();
        clipboardWrite.write(output);

        return callAiClient.call(Prompts.LINKEDIN_OFFER_REJECTION_DONE + output, AiClient.GPT);
    }
}