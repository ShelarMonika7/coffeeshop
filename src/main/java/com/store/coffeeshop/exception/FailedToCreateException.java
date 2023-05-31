package com.store.coffeeshop.exception;

public class FailedToCreateException extends RuntimeException{
    public FailedToCreateException(String message) {
        super(message);
    }
}
