package com.renault.garagemiscroservice.exceptions;

import lombok.Getter;

@Getter
public class ArgumentNotValidException extends Exception {

    static final String MESSAGE_ARGUMENT_NOT_VALID = "Argument not valid";

    public ArgumentNotValidException() {
        super(MESSAGE_ARGUMENT_NOT_VALID);
    }
}
