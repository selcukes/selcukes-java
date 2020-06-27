package io.github.selcukes.core.tests;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ConfigTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public void beforeClass() {
        ConfigFactory.loadLoggerProperties();
    }

    @Test
    public void configTest() {

        logger.info(() -> ConfigFactory.getConfig().getBrowserName());
        logger.info(() -> ConfigFactory.getConfig().getEnv());
        logger.info(() -> ConfigFactory.getConfig().getBaseUrl());
        logger.info(() -> ConfigFactory.getConfig().getIsProxy());
        logger.info(() -> ConfigFactory.getConfig().getProjectName());
        logger.info(() -> ConfigFactory.getConfig().getRemoteGridUrl());
        logger.info(() -> ConfigFactory.getConfig().getHeadLess().toString());
        logger.info(() -> ConfigFactory.getConfig().getNotification().toString());
        ConfigFactory.getConfig().getNotifier().forEach((k,v)->logger.info(()->String.format("Key :[%s]   Value :[%s]" , k , v)));
    }
}
