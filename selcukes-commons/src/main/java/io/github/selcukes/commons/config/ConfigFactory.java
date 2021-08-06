/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.config;

import io.github.selcukes.commons.exception.ConfigurationException;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.properties.LinkedProperties;
import io.github.selcukes.commons.resource.FileLoader;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);
    private static final String DEFAULT_LOG_BACK_FILE = "selcukes-logback.yaml";
    private static Environment environment;

    public static Environment getConfig() {
        if (environment == null) {
            environment = FileLoader.parse(Environment.class);
        }
        return environment;
    }

    public static void loadLoggerProperties() {
        LogManager logManager = LogManager.getLogManager();
        try (InputStream inputStream = FileHelper.loadResourceAsStream(DEFAULT_LOG_BACK_FILE)) {
            if (inputStream != null)
                logManager.readConfiguration(inputStream);
        } catch (IOException ignored) {
            //Gobble exception
        }
    }

    private static InputStream getStream(final String propertiesFile) {
        try {
            LOGGER.config(() -> String.format("Attempting to read %s as resource.", propertiesFile));
            InputStream stream = FileHelper.loadThreadResourceAsStream(propertiesFile);
            if (stream == null) {
                LOGGER.config(() -> String.format("Re-attempting to read %s as a local file.", propertiesFile));
                return new FileInputStream(new File(propertiesFile));
            }
            return stream;
        } catch (Exception ignored) {
            //Gobble exception
        }
        return null;
    }

    public static Map<String, String> loadPropertiesMap(final String propertiesFile) {
        LinkedHashMap<String, String> propertiesMap;
        LinkedProperties properties = new LinkedProperties();
        try {
            properties.load(getStream(propertiesFile));
        } catch (IOException e) {
            throw new ConfigurationException("Could not parse properties file '" + propertiesFile + "'", e);
        }
        propertiesMap = properties.entrySet().stream()
            .collect(Collectors.toMap(propertyEntry -> (String) propertyEntry.getKey(),
                propertyEntry -> (String) propertyEntry.getValue(), (a, b) -> b, LinkedHashMap::new));
        return propertiesMap;
    }
}
