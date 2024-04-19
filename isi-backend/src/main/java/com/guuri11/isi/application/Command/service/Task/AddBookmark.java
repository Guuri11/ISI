package com.guuri11.isi.application.Command.service.Task;

import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.application.Command.service.Clipboard.ClipboardRead;
import com.guuri11.isi.application.Fav.FavCreate;
import com.guuri11.isi.application.Fav.FavDto;
import com.guuri11.isi.application.Fav.FavRequest;
import com.guuri11.isi.domain.Command.AiClient;
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

    public ChatResponse add() {
        try {
            favCreate.create(new FavRequest(clipboardRead.read()));
            return callAiClient.call(Prompts.FAV_CREATED, AiClient.GPT);
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
