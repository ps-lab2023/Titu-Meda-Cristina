package com.nagarro.af.bookingtablesystem.exception;

public class CorruptedFileException extends RuntimeException {
    public CorruptedFileException() {
    }

    public CorruptedFileException(String message) {
        super(message);
    }
}
