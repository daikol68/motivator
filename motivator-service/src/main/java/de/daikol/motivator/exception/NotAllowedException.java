package de.daikol.motivator.exception;

public class NotAllowedException extends IllegalStateException {

    public NotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
