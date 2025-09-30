package com.nsteuerberg.backend.virma.presentation.advice;

import com.nsteuerberg.backend.virma.presentation.dto.exceptions.ExceptionResponse;
import com.nsteuerberg.backend.virma.service.exceptions.ContentNotExistsException;
import com.nsteuerberg.backend.virma.service.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse onFileNotFoundException(FileNotFoundException fileNotFoundException) {
        return new ExceptionResponse(
                "FILE ERROR",
                fileNotFoundException.getMessage()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse onUserAlreadyExists(UserAlreadyExistsException userAlreadyExistsException) {
        return new ExceptionResponse(
                "USER ALREADY EXISTS",
                userAlreadyExistsException.getMessage()
        );
    }

    @ExceptionHandler(ContentNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse onContentNotExists(ContentNotExistsException e){
        return new ExceptionResponse(
                "CONTENT NOT EXISTS",
                e.getMessage()
        );
    }
}
