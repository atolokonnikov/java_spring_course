package org.example.app.exceptions;

public class UploadFileException extends Exception {

    public final String message;


    public UploadFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
