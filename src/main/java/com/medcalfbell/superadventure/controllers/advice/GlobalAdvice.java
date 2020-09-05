package com.medcalfbell.superadventure.controllers.advice;

import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.DirectionNotFoundException;
import com.medcalfbell.superadventure.exceptions.DuplicateActionException;
import com.medcalfbell.superadventure.exceptions.DuplicateDirectionLocationException;
import com.medcalfbell.superadventure.exceptions.DuplicateLocationActionTargetException;
import com.medcalfbell.superadventure.exceptions.DuplicateTargetException;
import com.medcalfbell.superadventure.exceptions.InventoryActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.ItemNotFoundException;
import com.medcalfbell.superadventure.exceptions.StateHashExistsException;
import com.medcalfbell.superadventure.exceptions.TargetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(ActionNotFoundException.class)
    public String actionNotFoundException(ActionNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TargetNotFoundException.class)
    public String targetNotFoundException(TargetNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateDirectionLocationException.class)
    public String duplicateDirectionLocationException(DuplicateDirectionLocationException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DirectionNotFoundException.class)
    public String directionNotFoundException(DirectionNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateLocationActionTargetException.class)
    public String duplicateLocationActionTargetException(DuplicateLocationActionTargetException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateTargetException.class)
    public String duplicateTargetException(DuplicateTargetException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateActionException.class)
    public String duplicateActionException(DuplicateActionException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StateHashExistsException.class)
    public String stateHashExistsException(StateHashExistsException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InventoryActionNotFoundException.class)
    public String inventoryActionNotFoundException(InventoryActionNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ItemNotFoundException.class)
    public String itemNotFoundException(ItemNotFoundException e) {
        return e.getMessage();
    }
}
