package com.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchPlayerException extends RuntimeException {

    public NoSuchPlayerException() {
    }

    public NoSuchPlayerException(String message) {
        super(message);
    }
}
