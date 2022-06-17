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

import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.DataMapper;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@UtilityClass
public class ConfigFactory {
    private static final String DEFAULT_LOG_BACK_FILE = "selcukes-logback.yaml";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);
    private static final ThreadLocal<Environment> ENVIRONMENT = new InheritableThreadLocal<>();

    public static Environment getConfig() {
        if (ENVIRONMENT.get() == null) {
            ENVIRONMENT.set(DataMapper.parse(Environment.class));
        }
        return ENVIRONMENT.get();
    }

    public static void cleanupConfig() {
        ENVIRONMENT.remove();
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

    public static InputStream getStream(final String fileName) {
        try {
            LOGGER.config(() -> String.format("Attempting to read %s as resource.", fileName));
            InputStream stream = FileHelper.loadThreadResourceAsStream(fileName);
            if (stream == null) {
                LOGGER.config(() -> String.format("Re-attempting to read %s as a local file.", fileName));
                return new FileInputStream(fileName);
            }
            return stream;
        } catch (Exception ignored) {
            //Gobble exception
        }
        return null;
    }
}
