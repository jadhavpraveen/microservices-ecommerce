package com.renaissance.orderservice.exception;

public class ResourceNotFoundException extends RuntimeException{

    private static  final long serialVersionID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
