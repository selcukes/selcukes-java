package io.github.selcukes.dp.exception;


public class DriverPoolException extends RuntimeException {

    public DriverPoolException(Throwable cause) {
        super(cause);
    }

    public DriverPoolException(String message) {
        super(message);
    }
}
