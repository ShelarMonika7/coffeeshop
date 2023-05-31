package com.store.coffeeshop.exception;

public class FailedToFetchException extends RuntimeException{
    public FailedToFetchException(String message) {
        super(message);
    }
}
