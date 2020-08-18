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

import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
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
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }

    @AfterTest
    public void tearDown() {
        LoggerFactory.removeListener(logRecordListener);
    }

    @Test(threadPoolSize = 3, invocationCount = 3, timeOut = 1000)
    public void testLogRecords() {
        long id = Thread.currentThread().getId();
        logger.info(() -> "Test Started..." + id);
        logger.warn(() -> "This is sample warning!");
        logger.info(() -> "Login Successful..." + id);
        logger.error(() -> "This is sample error message");
        logger.debug(() -> "This is sample debug statement");
        logger.info(() -> "Test Completed..." + id);

        String allLogs = logRecordListener.getLogRecords()
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--ALL Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println("Thread with id: " + id + allLogs);

        String infoLogs = logRecordListener.getLogRecords(Level.INFO)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println("Thread with id: " + id + infoLogs);

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
