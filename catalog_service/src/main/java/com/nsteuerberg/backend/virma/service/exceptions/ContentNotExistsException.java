package com.nsteuerberg.backend.virma.service.exceptions;

public class ContentNotExistsException extends RuntimeException {
    public ContentNotExistsException(String message) {
        super(message);
    }
}
