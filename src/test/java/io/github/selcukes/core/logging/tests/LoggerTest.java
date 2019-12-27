package io.github.selcukes.core.logging.tests;

import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.core.logging.LoggerReporter;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.logging.Level;

public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    private final Exception exception = new Exception();
    private LogRecordListener logRecordListener;
    @BeforeTest
    public void beforeTest() {
        LoggerReporter.setLoggerFormat(Level.ALL);
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }
    @AfterTest
    public void afterClass()
    {
        LoggerFactory.removeListener(logRecordListener);
    }

    @Test
    void error() {
        logger.error(() -> "Error");
        logger.error(exception, () -> "Error Exception");
        //Assert.assertEquals(logRecordListener.getLogRecords().get(1).getMessage(),"Error Exception");
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
