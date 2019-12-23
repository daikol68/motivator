package de.daikol.motivator.exception;

public class MessageException extends IllegalStateException {

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
