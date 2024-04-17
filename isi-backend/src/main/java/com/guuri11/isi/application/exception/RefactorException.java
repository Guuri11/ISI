package com.guuri11.isi.application.exception;

public class RefactorException extends RuntimeException {

    public RefactorException() {

        super("Could not refactor item");
    }
}
