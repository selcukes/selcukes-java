package io.github.selcukes.core.exception;

public class SelcukesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SelcukesException() {
        super();
    }

    public SelcukesException(String message, Throwable cause) {
        super(message, cause);
    }

    public SelcukesException(String message) {
        super(message);
    }

    public SelcukesException(Throwable cause) {
        super(cause);
    }
}

