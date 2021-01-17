package org.example.app.exceptions;

public class DownloadFileException extends Exception {

    public final String message;

    public DownloadFileException(String message) {this.message = message;}

    @Override
    public String getMessage() {
        return message;
    }
}
