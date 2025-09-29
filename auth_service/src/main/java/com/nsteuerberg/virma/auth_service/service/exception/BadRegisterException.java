package com.nsteuerberg.virma.auth_service.service.exception;

public class BadRegisterException extends RuntimeException {
    public BadRegisterException(String message) {
        super(message);
    }
}
