/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);
    private static final String DEFAULT_CONFIG_FILE = "selcukes.yaml";
    private static final String DEFAULT_LOG_BACK_FILE = "selcukes-logback.yaml";
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
        LogManager logManager = LogManager.getLogManager();
        try (InputStream inputStream = ConfigFactory.class.getClassLoader().getResourceAsStream(DEFAULT_LOG_BACK_FILE)) {
            if (inputStream != null)
                logManager.readConfiguration(inputStream);
        } catch (IOException e) {
            LOGGER.warn(() -> "Failed loading selcukes-logback property file. Using default logger properties");
        }
    }

    public static InputStream getStream() {
        try {
            LOGGER.config(() -> String.format("Attempting to read %s as resource.", CONFIG_FILE));
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
            if (stream == null) {
                LOGGER.config(() -> String.format("Re-attempting to read %s as a local file.", CONFIG_FILE));
                return new FileInputStream(new File(CONFIG_FILE));
            }
        } catch (Exception ignored) {
            //Gobble exception
        }
        return null;
    }
}
