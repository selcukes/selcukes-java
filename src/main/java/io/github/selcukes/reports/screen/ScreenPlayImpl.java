/*
 *
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
 *
 */

package io.github.selcukes.reports.screen;

import io.cucumber.java.Scenario;
import io.github.selcukes.core.helper.ExceptionHelper;
import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.enums.TestStatus;
import io.github.selcukes.reports.enums.TestType;
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
    private final ScreenCapture capture;
    private String errorMessage;

    public ScreenPlayImpl(WebDriver driver) {
        capture = new ScreenCapture(driver);
        result = new ScreenPlayResult(TestType.TESTNG, "DEFAULT TEST NAME", "PASSED", false);
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
        if (result.isAttach()) {
            String videoPath = stop().getAbsolutePath();
            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
                "<source src=" + videoPath + " type=\"video/mp4\">" +
                "Your browser does not support the video tag." +
                "</video>";
            attach(htmlToEmbed);
        } else recorder.stopAndDelete(result.getScenarioName());
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
        return recorder.stopAndSave(result.getScenarioName());
    }

    @Override
    public ScreenPlay sendNotification(String message) {
        if (notifier == null) {
            logger.warn(() -> "NotifierType not configured. Using Default RecorderType as TEAMS");
            withNotifier(NotifierType.TEAMS);
        }
        if (errorMessage != null) message = message + errorMessage;
        notifier.pushNotification(result.getScenarioName(), result.getScenarioStatus(), message, takeScreenshot());
        return this;
    }

    @Override
    public void attachLogs() {
        String infoLogs = loggerListener.getLogRecords(Level.INFO)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));
        write(infoLogs);
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
        if (scenario instanceof Scenario) {
            this.scenario = (Scenario) scenario;
            result = new ScreenPlayResult(TestType.CUCUMBER,
                this.scenario.getName().replace(" ", "_"),
                this.scenario.getStatus().toString(),
                this.scenario.isFailed());
        } else if (scenario instanceof ITestResult) {
            ITestResult iTestResult = (ITestResult) scenario;
            result = new ScreenPlayResult(TestType.TESTNG,
                iTestResult.getName().replace(" ", "_"),
                getTestStatus(iTestResult),
                !iTestResult.isSuccess());
            errorMessage = getErrorMessage(iTestResult);
        }
        return this;
    }

    private String getErrorMessage(ITestResult result) {
        if (!result.isSuccess()) {
            return "Exception: " + ExceptionHelper.getExceptionTitle(result.getThrowable());
        }
        return null;
    }

    @Override
    public ScreenPlay withResult(String scenarioName, String scenarioStatus, boolean isFailed) {
        result = new ScreenPlayResult(TestType.TESTNG, scenarioName, scenarioStatus, isFailed);
        return this;
    }

    @Override
    public ScreenPlay attachWhen(TestStatus testStatus) {
        if (testStatus == TestStatus.ALL) {
            result.setAttach(true);
        }
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
        if (result.getTestType().equals(TestType.CUCUMBER)) {
            byte[] objToEmbed = attachment.getBytes();
            attach(objToEmbed, "text/html");
            logger.info(() -> "Attached Video to Cucumber Report");
        } else {
            Reporter.log(attachment);
            String contentType = attachment.contains("video") ? "Video" : "Image";
            logger.info(() -> "Attached " + contentType + " to TestNG Report");
        }
    }

    private void attach(byte[] objToEmbed, String mediaType) {
        scenario.embed(objToEmbed, mediaType, scenario.getName());
    }

    public void stopReadingLogs() {
        LoggerFactory.removeListener(loggerListener);
    }

    private String getTestStatus(ITestResult result) {
        String status;
        if (result.isSuccess()) {
            status = "PASSED";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            status = "FAILED";
        } else {
            status = "SKIPPED";
        }
        return status;
    }

}
