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
import io.github.selcukes.commons.helper.SingletonContext;
import io.github.selcukes.databind.DataMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@UtilityClass
public class ConfigFactory {
    private static final String DEFAULT_LOG_BACK_FILE = "selcukes-logback.yaml";
    private static final SingletonContext<Environment> ENVIRONMENT_CONTEXT = SingletonContext
            .with(() -> DataMapper.parse(Environment.class));

    public static Environment getConfig() {
        return ENVIRONMENT_CONTEXT.get();
    }

    public static void cleanupConfig() {
        ENVIRONMENT_CONTEXT.remove();
    }

    public void loadLoggerProperties() {
        LogManager logManager = LogManager.getLogManager();
        try (InputStream inputStream = FileHelper.loadResourceAsStream(DEFAULT_LOG_BACK_FILE)) {
            if (inputStream != null) {
                logManager.readConfiguration(inputStream);
            }
        } catch (IOException ignored) {
            // Gobble exception
        }
    }
}
