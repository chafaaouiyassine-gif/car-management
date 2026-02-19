package com.renault.garagemiscroservice.exception_handler;

import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GarageExceptionAdvice {
    @ExceptionHandler(ArgumentNotValidException.class)
    public ResponseEntity<String> handleNotValideException(ArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleValidation(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(MaxVehiculeExceedException.class)
    public ResponseEntity<String> handleValidation(MaxVehiculeExceedException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}
