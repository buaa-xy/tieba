package com.buaa.markpp.model;

public class MarkppException extends RuntimeException {
    protected String message;

    public MarkppException() {}

    public MarkppException(String message) {
        this.message = message;
    }

    public MarkppException(ExceptionType type) {
        this.message = type.getMessage();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
