package com.store.coffeeshop.exception;

public class FailedToUpdateException extends RuntimeException{
    public FailedToUpdateException(String message) {
        super(message);
    }
}
