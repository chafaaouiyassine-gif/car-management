package com.renault.garagemiscroservice.exceptions;

import lombok.Getter;

@Getter
public class MethodArgumentNotValidException extends Exception{
    private final String message;
    public MethodArgumentNotValidException(String message){
        super(message);
        this.message=message;
    }
}
