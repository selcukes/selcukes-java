package io.github.selcukes.core.exception;

public class CommandException extends SelcukesException {
    private static final long serialVersionUID = 1L;

    public CommandException() {
        super();
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
