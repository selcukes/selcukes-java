package io.github.selcukes.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.selcukes.core.exception.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;


public class ConfigFactory {
    private static final String DEFAULT_CONFIG_FILE = "selcukes.yaml";
    private static final String CONFIG_LOGGER_FILE = "logging.properties";

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
        InputStream stream = ConfigFactory.class.getClassLoader().getResourceAsStream(CONFIG_LOGGER_FILE);
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            throw new ConfigurationException("Failed loading logger properties: ", e);
        }
    }
}
