package com.nsteuerberg.virma.auth_service.presentation.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nsteuerberg.virma.auth_service.presentation.advice.responses.AuthExceptionResponse;
import com.nsteuerberg.virma.auth_service.service.exception.BadRegisterException;
import com.nsteuerberg.virma.auth_service.service.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthExceptionResponse usernameNotFoundException(UsernameNotFoundException exception) {
        return new AuthExceptionResponse(
                "Authentication",
                exception.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthExceptionResponse badCredentialsException(BadCredentialsException exception) {
        return new AuthExceptionResponse(
                "Authentication",
                exception.getMessage()
        );
    }

    @ExceptionHandler(BadRegisterException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public AuthExceptionResponse badRegisterException(BadRegisterException exception){
        return new AuthExceptionResponse(
                "Sign up",
                exception.getMessage()
        );
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthExceptionResponse jwtVerificationException(JWTVerificationException e){
        return new AuthExceptionResponse(
                "JWT Token",
                e.getMessage()
        );
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthExceptionResponse refreshTokenException(RefreshTokenException e) {
        return new AuthExceptionResponse(
                "Refresh Token",
                e.getMessage()
        );
    }


}
