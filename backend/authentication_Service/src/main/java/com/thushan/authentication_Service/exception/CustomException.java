package com.thushan.authentication_Service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CustomException  extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
