package io.github.selcukes.wdb.exception;


public class WebDriverBinaryException extends RuntimeException {

    public WebDriverBinaryException(Throwable cause) {
        super(cause);
    }

    public WebDriverBinaryException(String message) {
        super(message);
    }
}
