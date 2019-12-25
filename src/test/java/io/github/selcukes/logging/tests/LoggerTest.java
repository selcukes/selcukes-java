package io.github.selcukes.logging.tests;

import io.github.selcukes.logging.Logger;
import io.github.selcukes.logging.LoggerFactory;

public class LoggerTest {
    private static Logger logger= LoggerFactory.getLogger(LoggerTest.class);
    public static void main(String[] args) {
        logger.info(()->"This is sample info");
        logger.error(()->"This is sample error");
        logger.debug(()->"This is sample debug");
        logger.config(()->"This is sample config");
    }
}
