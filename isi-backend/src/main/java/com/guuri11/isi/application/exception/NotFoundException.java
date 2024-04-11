package com.guuri11.isi.application.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {

        super("Could not find item");
    }

    public NotFoundException(final UUID id) {

        super("Could not find item with ID " + id);
    }
}
