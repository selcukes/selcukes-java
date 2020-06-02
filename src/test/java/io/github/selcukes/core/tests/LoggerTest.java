package io.github.selcukes.core.tests;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoggerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Exception exception = new Exception();

    @BeforeTest
    public void beforeTest() {
        ConfigFactory.loadLoggerProperties();
    }

    @Test
    private void error() {
        logger.error(() -> "Error");
        logger.error(exception, () -> "Error Exception");

    }

    @Test
    private void warn() {
        logger.warn(() -> "Warn");
        logger.warn(exception, () -> "Warn Exception");
    }

    @Test
    private void info() {
        logger.info(() -> "Info");
        logger.info(exception, () -> "Info Exception");
    }

    @Test
    private void config() {
        logger.config(() -> "Config");
        logger.config(exception, () -> "Config Exception");
    }

    @Test
    private void debug() {
        logger.debug(() -> "Debug");
        logger.debug(exception, () -> "Debug Exception");
    }

    @Test
    private void trace() {
        logger.trace(() -> "Trace");
        logger.trace(exception, () -> "Trace Exception");
    }
}
