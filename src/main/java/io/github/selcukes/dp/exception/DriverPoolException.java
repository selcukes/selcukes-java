package io.github.selcukes.dp.exception;


/**
 * The type Driver pool exception.
 */
public class DriverPoolException extends RuntimeException {

    /**
     * Instantiates a new Driver pool exception.
     *
     * @param cause the cause
     */
    public DriverPoolException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Driver pool exception.
     *
     * @param message the message
     */
    public DriverPoolException(String message) {
        super(message);
    }
}
