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

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.annotations.Test;
@Test(enabled = false)
public class LoggerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Exception exception = new Exception();

    @Test
    private void error() {
        logger.error(() -> "Error");
        logger.error(exception, () -> "Error Exception");

    }

    @Test
    private void warn() {
        logger.warn(() -> "Warn");
        logger.warn(exception, () -> "Warn Exception");
    }

    @Test
    private void info() {
        logger.info(() -> "Info");
        logger.info(exception, () -> "Info Exception");
    }

    @Test
    private void config() {
        logger.config(() -> "Config");
        logger.config(exception, () -> "Config Exception");
    }

    @Test
    private void debug() {
        logger.debug(() -> "Debug");
        logger.debug(exception, () -> "Debug Exception");
    }

    @Test
    private void trace() {
        logger.trace(() -> "Trace");
        logger.trace(exception, () -> "Trace Exception");
    }
}
