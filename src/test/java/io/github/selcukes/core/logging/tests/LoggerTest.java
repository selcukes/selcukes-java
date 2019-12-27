package io.github.selcukes.core.logging.tests;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    private final Exception exception = new Exception();

    @BeforeTest
    public void beforeTest() {
        ConfigFactory.loadLoggerProperties();
    }

    @Test
    void error() {
        logger.error(() -> "Error");
        logger.error(exception, () -> "Error Exception");

    }

    @Test
    void warn() {
        logger.warn(() -> "Warn");
        logger.warn(exception, () -> "Warn Exception");
    }

    @Test
    void info() {
        logger.info(() -> "Info");
        logger.info(exception, () -> "Info Exception");
    }

    @Test
    void config() {
        logger.config(() -> "Config");
        logger.config(exception, () -> "Config Exception");
    }

    @Test
    void debug() {
        logger.debug(() -> "Debug");
        logger.debug(exception, () -> "Debug Exception");
    }

    @Test
    void trace() {
        logger.trace(() -> "Trace");
        logger.trace(exception, () -> "Trace Exception");
    }
}
