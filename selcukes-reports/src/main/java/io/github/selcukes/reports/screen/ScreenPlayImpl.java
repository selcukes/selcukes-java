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

package io.github.selcukes.reports.screen;

import io.cucumber.java.Scenario;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.enums.TestType;
import io.github.selcukes.reports.screenshot.SnapshotImpl;
import io.github.selcukes.reports.notification.Notifier;
import io.github.selcukes.reports.notification.NotifierFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

class ScreenPlayImpl implements ScreenPlay {

    private final Logger logger = LoggerFactory.getLogger(ScreenPlayImpl.class);

    private Scenario scenario;
    protected LogRecordListener loggerListener;
    protected Recorder recorder;
    protected Notifier notifier;
    private ScreenPlayResult result;
    private final SnapshotImpl capture;
    boolean isFailedOnly;
    private ITestResult iTestResult;

    public ScreenPlayImpl(WebDriver driver) {
        capture = new SnapshotImpl(driver);
        isFailedOnly = true;
        startReadingLogs();
    }

    @Override
    public String takeScreenshot() {
        return capture.shootPage();
    }

    @Override
    public ScreenPlay attachScreenshot() {

        if (result.getTestType().equals(TestType.CUCUMBER)) {
            attach(capture.shootFullPageAsBytes(), "image/png");

        } else {
            String screenshotPath = takeScreenshot();
            String htmlToEmbed = "<br>  <img src='" + screenshotPath + "' height='100' width='100' /><br>";
            attach(htmlToEmbed);
        }
        return this;
    }

    @Override
    public ScreenPlay attachVideo() {
        if (isAttachable()) {
            String videoPath = stop().getAbsolutePath();
            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
                "<source src=" + videoPath + " type=\"video/mp4\">" +
                "Your browser does not support the video tag." +
                "</video>";
            attach(htmlToEmbed);
        } else recorder.stopAndDelete();
        return this;
    }

    @Override
    public ScreenPlay start() {
        if (recorder == null) {
            logger.warn(() -> "RecorderType not configured. Using Default RecorderType as MONTE");
            withRecorder(RecorderType.MONTE);
        }
        recorder.start();
        return this;
    }

    @Override
    public File stop() {
        Preconditions.checkNotNull(recorder, "Recording not started...");
        return recorder.stopAndSave(result.getTestName());
    }

    @Override
    public ScreenPlay sendNotification(String message) {
        if (notifier == null) {
            logger.warn(() -> "NotifierType not configured. Using Default RecorderType as TEAMS");
            withNotifier(NotifierType.TEAMS);
        }

        if (result.getErrorMessage() != null)
            message = message + result.getErrorMessage();
        notifier.pushNotification(result.getTestName(), result.getStatus(), message, takeScreenshot());
        return this;
    }

    @Override
    public void attachLogs() {
        if (isAttachable()) {
            String infoLogs = loggerListener.getLogRecords(Level.INFO)
                .map(LogRecord::getMessage)
                .collect(Collectors.joining("<br/>  --> ", "<br/> Logs: <br/>   --> ", "<br/> --End Of Logs--"));
            write(infoLogs);
        }
        stopReadingLogs();
    }

    @Override
    public ScreenPlay withRecorder(RecorderType recorderType) {
        recorder = RecorderFactory.getRecorder(recorderType);
        return this;
    }

    @Override
    public ScreenPlay withNotifier(NotifierType notifierType) {
        notifier = NotifierFactory.getNotifier(notifierType);
        return this;
    }

    @Override
    public <T> ScreenPlay withResult(T scenario) {
        result = new ScreenPlayResult(scenario);
        if (scenario instanceof Scenario) {
            this.scenario = (Scenario) scenario;

        } else if (scenario instanceof ITestResult) {
            iTestResult = (ITestResult) scenario;
        }
        return this;
    }


    @Override
    public ScreenPlay ignoreCondition() {
        isFailedOnly = false;
        return this;
    }


    private void startReadingLogs() {
        this.loggerListener = new LogRecordListener();
        LoggerFactory.addListener(loggerListener);
    }

    private void write(String text) {
        if (result.getTestType().equals(TestType.CUCUMBER)) {

            scenario.write(text);
            logger.info(() -> "Attached Logs to Cucumber Report");
        } else {
            Reporter.log(text);
            logger.info(() -> "Attached Logs to TestNG Report");
        }
    }

    private void attach(String attachment) {
        if (isAttachable()) {
            if (result.getTestType().equals(TestType.CUCUMBER)) {
                byte[] objToEmbed = attachment.getBytes();
                attach(objToEmbed, "text/html");
                logger.info(() -> "Attached Video to Cucumber Report");
            } else {
                Reporter.setCurrentTestResult(iTestResult);
                Reporter.log(attachment);
                String contentType = attachment.contains("video") ? "Video" : "Screenshot";
                logger.info(() -> "Attached " + contentType + " to TestNG Report");
            }
        }
    }

    private void attach(byte[] objToEmbed, String mediaType) {
        scenario.embed(objToEmbed, mediaType, scenario.getName());
    }

    public void stopReadingLogs() {
        LoggerFactory.removeListener(loggerListener);
    }

    boolean isAttachable() {
        if (isFailedOnly) {
            return result.isFailed();
        } else return true;
    }
}
