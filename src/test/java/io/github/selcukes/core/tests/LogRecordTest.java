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

package io.github.selcukes.core.tests;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.helper.ExceptionHelper;
import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class LogRecordTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private LogRecordListener logRecordListener;

    @BeforeTest
    public void setup() {
        ConfigFactory.loadLoggerProperties();
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }

    @AfterTest
    public void tearDown() {
        LoggerFactory.removeListener(logRecordListener);
    }

    @Test
    public void testLogRecords() {
        logger.info(() -> "Test Started...");
        logger.warn(() -> "This is sample warning!");
        logger.info(() -> "Login Successful...");
        logger.error(() -> "This is sample error message");
        logger.debug(() -> "This is sample debug statement");
        logger.info(() -> "Test Completed...");

        String allLogs = logRecordListener.getLogStream()
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--ALL Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println(allLogs);

        String infoLogs = logRecordListener.getLogStream(Level.INFO)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println(infoLogs);

    }

    @Test
    private void exceptionTest() {
        try {
            throw new Exception("This is sample exception");
        } catch (Exception exception) {
            logger.error(() -> ExceptionHelper.getExceptionTitle(exception));
        }
    }
}
