package io.github.selcukes.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;


public class ConfigFactory {
    private static final String CONFIG_FILE = "src/test/resources/selcukes.yaml";

    private ConfigFactory() {

    }

    public static Environment getConfig() throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            File defaultConfigFile = new File(CONFIG_FILE);
            return objectMapper.readValue(defaultConfigFile, Environment.class);
        } catch (IOException e) {
            throw new IOException("Failed loading selcukes properties: ", e);
        }
    }
}
