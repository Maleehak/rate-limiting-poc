package com.example.ratelimitingpoc.exception;

public class TooManyRequests extends RuntimeException {

    private static final long serialVersionUID = 6799407647303756615L;

    public TooManyRequests(String message) {
        super(message);
    }
}