package com.guuri11.isi.application.Command.service.Task;

import com.guuri11.isi.application.AiClient.CallAiClient;
import com.guuri11.isi.application.exception.RefactorException;
import com.guuri11.isi.domain.Command.AiClient;
import com.guuri11.isi.infrastructure.persistance.Prompts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

@Service
@AllArgsConstructor
public class Refactor {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CallAiClient callAiClient;

    public ChatResponse refactor() {

        ChatResponse response = callAiClient.call(Prompts.REFACTOR + readClipboard(), AiClient.GPT);
        String output = response.getResult().getOutput().getContent();
        sendToClipboard(output);

        return callAiClient.call(Prompts.REFACTOR_DONE, AiClient.GPT);
    }

    private void sendToClipboard(String input) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        logger.info("sendToClipboard result {}", input);
        StringSelection data = new StringSelection(input);
        clipboard.setContents(data, data);
    }

    private String readClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String result = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                logger.info("readClipboard result {}", result);
                return result;
            } else {
                throw new RefactorException();
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            throw new RefactorException();
        }
    }
}