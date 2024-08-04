package com.guuri11.isi.application.command.service.Clipboard;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.*;

@Service
@AllArgsConstructor
public class ClipboardWrite {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void write(String input) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        logger.info("sendToClipboard result {}", input);
        StringSelection data = new StringSelection(input);
        clipboard.setContents(data, data);
    }
}
