package com.renault.garagemiscroservice.exceptions;

public class EntityNotFoundException extends Exception{
    static final String MESSAGE_ARGUMENT_NOT_VALID = "Entity not found";
    public EntityNotFoundException(){
        super(MESSAGE_ARGUMENT_NOT_VALID);
    }
}
