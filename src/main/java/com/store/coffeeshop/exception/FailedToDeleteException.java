package com.store.coffeeshop.exception;

public class FailedToDeleteException extends RuntimeException{
    public FailedToDeleteException(String message) {
        super(message);
    }
}
