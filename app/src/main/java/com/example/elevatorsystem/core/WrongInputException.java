package com.example.elevatorsystem.core;

public class WrongInputException extends Throwable {

    private final String message;

    public WrongInputException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
