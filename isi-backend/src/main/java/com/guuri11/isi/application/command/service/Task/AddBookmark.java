package com.guuri11.isi.application.command.service.Task;

import com.guuri11.isi.application.aiclient.CallAiClient;
import com.guuri11.isi.application.command.service.Clipboard.ClipboardRead;
import com.guuri11.isi.application.fav.FavCreate;
import com.guuri11.isi.application.fav.FavRequest;
import com.guuri11.isi.domain.command.AiClient;
import com.guuri11.isi.domain.command.Command;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;


@Service
@AllArgsConstructor
public class AddBookmark {
    private final CallAiClient callAiClient;
    private final ClipboardRead clipboardRead;
    private final FavCreate favCreate;

    public ChatResponse add(final Command command) {
        try {
            favCreate.create(new FavRequest(clipboardRead.read()), command);
            return callAiClient.call(Prompts.FAV_CREATED, AiClient.GPT);
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
