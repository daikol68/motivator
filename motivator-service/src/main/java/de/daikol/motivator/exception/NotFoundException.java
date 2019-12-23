package de.daikol.motivator.exception;

public class NotFoundException extends IllegalArgumentException {

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
