package com.guuri11.isi.application.command.service.Clipboard;

import com.guuri11.isi.application.exception.RefactorException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ClipboardRead {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String read() {
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
