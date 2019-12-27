package io.github.selcukes.core.exception;

public class ConfigurationException extends SelcukesException {
    private static final long serialVersionUID = 1L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String configurationType) {

        super("Configuration section for {"+configurationType+"} was not found.");
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
