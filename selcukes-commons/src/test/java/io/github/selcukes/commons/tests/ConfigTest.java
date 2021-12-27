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

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.annotations.Test;

public class ConfigTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void configTest() {

        logger.info(() -> ConfigFactory.getConfig().getEnv());
        logger.info(() -> ConfigFactory.getConfig().getBaseUrl());
        logger.info(() -> ConfigFactory.getConfig().getProjectName());
        ConfigFactory.getConfig().getWeb().forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
        ConfigFactory.getConfig().getWindows().forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
        ConfigFactory.getConfig().getMobile().forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
        ConfigFactory.getConfig().getVideo().forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
        ConfigFactory.getConfig().getNotifier().forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
    }
}
