package io.github.selcukes.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.selcukes.core.exception.ConfigurationException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;


public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
    private static final String DEFAULT_CONFIG_FILE = "selcukes.yaml";
    private static final String DEFAULT_LOG_BACK_FILE = "logback.yaml";
    private static final String CONFIG_FILE = "config.properties";
    private ConfigFactory() {

    }

    public static Environment getConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            File defaultConfigFile = new File(Objects.requireNonNull(ConfigFactory.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE)).getFile());
            return objectMapper.readValue(defaultConfigFile, Environment.class);
        } catch (IOException e) {
            throw new ConfigurationException("Failed loading selcukes properties: ", e);
        }
    }

    public static void loadLoggerProperties() {
        InputStream stream = ConfigFactory.class.getClassLoader().getResourceAsStream(DEFAULT_LOG_BACK_FILE);
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            throw new ConfigurationException("Failed loading logger properties: ", e);
        }
    }

    private static InputStream getStream() {
        try {
            logger.debug(()->String.format("Attempting to read %s as resource.", CONFIG_FILE));
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
            if (stream == null) {
                logger.debug(()->String.format("Re-attempting to read %s as a local file.", CONFIG_FILE));
                return new FileInputStream(new File(CONFIG_FILE));
            }
        } catch (Exception ignored) {
            //Gobble exception
        }
        return null;
    }
}
