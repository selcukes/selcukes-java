package io.github.selcukes.core.exception;

public class WebDriverBinaryException extends SelcukesException {
    private static final long serialVersionUID = 1L;
    public WebDriverBinaryException(Throwable cause) {
        super(cause);
    }

    public WebDriverBinaryException(String message) {
        super(message);
    }
}
