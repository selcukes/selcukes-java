/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.extent.report;

import io.github.selcukes.commons.helper.SingletonContext;
import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.snapshot.Snapshot;
import io.github.selcukes.snapshot.SnapshotImpl;
import org.openqa.selenium.WebDriver;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

import static io.github.selcukes.collections.StringHelper.isEmpty;

public class Reporter {
    private static final SingletonContext<Reporter> REPORTER_CONTEXT = SingletonContext.with(Reporter::new);
    private Snapshot snapshot;
    private LogRecordListener logRecordListener;

    public static void log(String message) {
        if (!isEmpty(message)) {
            SelcukesExtentAdapter.addTestStepLog(message);
        }
    }

    public static Reporter getReporter() {
        return REPORTER_CONTEXT.get();
    }

    Reporter start() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
        return this;
    }

    public void initSnapshot(WebDriver driver) {
        snapshot = new SnapshotImpl(driver);
    }

    public String getLogRecords() {
        if (logRecordListener != null) {
            return logRecordListener.getLogRecords()
                    .filter(logRecord -> logRecord.getLevel() == Level.INFO || logRecord.getLevel() == Level.SEVERE)
                    .filter(logRecord -> !isEmpty(logRecord.getMessage()))
                    .map(this::mapLogMessage)
                    .collect(Collectors.joining("</li><li>", "<ul><li> ", "</li></ul><br/>"));
        }
        return "";
    }

    private String mapLogMessage(LogRecord logRecord) {
        String message = logRecord.getMessage().replace("\n", "<br/>");
        return (logRecord.getLevel() == Level.SEVERE) ? "<span style=\"color:red;\">" + message + "</span>" : message;
    }

    private Reporter attachLog() {
        String infoLogs = getLogRecords();
        if (!infoLogs.equalsIgnoreCase("<ul><li> </li></ul><br/>")) {
            Reporter.log(infoLogs);
        }
        return this;
    }

    private Reporter stop() {
        if (logRecordListener != null) {
            LoggerFactory.removeListener(logRecordListener);
        }
        return this;
    }

    void removeReporter() {
        REPORTER_CONTEXT.remove();
    }

    void attachAndClear() {
        attachLog().stop();

    }

    void attachAndRestart() {
        attachLog().stop().start();
    }

    public void attachScreenshot() {
        SelcukesExtentAdapter.attachScreenshot(snapshot.shootPageAsBytes());
    }

    public void attachVisiblePageScreenshot() {
        SelcukesExtentAdapter.attachScreenshot(snapshot.shootVisiblePageAsBytes());
    }
}
