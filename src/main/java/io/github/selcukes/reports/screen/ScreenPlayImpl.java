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
import io.github.selcukes.core.exception.RecorderException;
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.helper.FileHelper;
import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.enums.TestType;
import io.github.selcukes.reports.notification.Notifier;
import io.github.selcukes.reports.notification.NotifierFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

class ScreenPlayImpl implements ScreenPlay {

    private final WebDriver driver;
    private Scenario scenario;
    protected LogRecordListener loggerListener;
    protected Recorder recorder;
    protected Notifier notifier;
    private boolean isOldCucumber;
    private String scenarioName;
    private String scenarioStatus;
    private TestType testType;
    private boolean isFailed;

    public ScreenPlayImpl(WebDriver driver) {
        this.driver = driver;
        isOldCucumber = false;
        startReadingLogs();
    }

    @Override
    public String takeScreenshot() {
        File destFile = getScreenshotPath();
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            throw new RecorderException("Failed Capturing Screenshot..", e);
        }
        return destFile.getAbsolutePath();
    }

    @Override
    public void embedScreenshot() {
        isOldCucumber = true;
        attachScreenshot();
    }

    private ChromeDevToolsService getDevTools() {
        return DevToolsService.getDevToolsService(driver);
    }

    @Override
    public ScreenPlay attachScreenshot() {

        if (testType.equals(TestType.Cucumber)) {
            byte[] screenshot;
            try {
                screenshot = Screenshot.captureFullPageAsBytes(getDevTools());
                attach(screenshot, "image/png");
            } catch (IOException e) {
                throw new RecorderException("Failed Capturing Screenshot..", e);
            }
        } else {
            String screenshotPath = takeScreenshot();
            String htmlToEmbed = "<br>  <img src='" + screenshotPath + "' height='100' width='100' /><br>";
            attach(htmlToEmbed);
        }
        return this;
    }

    @Override
    public void attachVideo() {
        if (isFailed) {
            String videoPath = stop().getAbsolutePath();

            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
                "<source src=" + videoPath + " type=\"video/mp4\">" +
                "Your browser does not support the video tag." +
                "</video>";
            attach(htmlToEmbed);
        } else recorder.stopAndDelete(scenarioName);
    }

    @Override
    public void embedVideo() {
        isOldCucumber = true;
        attachVideo();
    }

    @Override
    public ScreenPlay start() {
        recorder.start();
        return this;
    }

    @Override
    public File stop() {
        return recorder.stopAndSave(scenarioName.replace(" ", "_"));
    }

    @Override
    public ScreenPlay sendNotification(String message) {
        notifier.pushNotification(scenarioName, scenarioStatus, message, takeScreenshot());
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
    public ScreenPlay getRecorder(RecorderType recorderType) {
        recorder = RecorderFactory.getRecorder(recorderType);
        return this;
    }

    @Override
    public ScreenPlay getNotifier(NotifierType notifierType) {
        notifier = NotifierFactory.getNotifier(notifierType);
        return this;
    }

    @Override
    public <T> ScreenPlay readResult(T scenario) {
        if (scenario instanceof Scenario) {
            this.scenario = (Scenario) scenario;
            scenarioName = ((Scenario) scenario).getName();
            scenarioStatus = ((Scenario) scenario).getStatus().toString();
            isFailed = ((Scenario) scenario).isFailed();
            testType = TestType.Cucumber;
        } else if (scenario instanceof ITestResult) {
            testType = TestType.TestNG;
            scenarioName = ((ITestResult) scenario).getName();
            scenarioStatus = getTestStatus((ITestResult) scenario);
            isFailed = !((ITestResult) scenario).isSuccess();
        }
        return this;
    }

    private void attach(byte[] objToEmbed, String mediaType) {
        if (isOldCucumber)
            scenario.embed(objToEmbed, mediaType);
        else
            scenario.embed(objToEmbed, mediaType, scenario.getName());
    }

    private void startReadingLogs() {
        this.loggerListener = new LogRecordListener();
        LoggerFactory.addListener(loggerListener);
    }

    private void write(String text) {
        if (testType.equals(TestType.Cucumber)) {
            scenario.write(text);
        } else Reporter.log(text);
    }

    private void attach(String attachment) {
        if (testType.equals(TestType.Cucumber)) {
            byte[] objToEmbed = attachment.getBytes();
            attach(objToEmbed, "text/html");
        } else Reporter.log(attachment);
    }

    public void stopReadingLogs() {
        LoggerFactory.removeListener(loggerListener);
    }

    private String getTestStatus(ITestResult result) {
        String status;
        if (result.isSuccess()) {
            status = "PASS";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            status = "FAILED";
        } else {
            status = "SKIPPED";
        }
        return status;
    }

    public File getScreenshotPath() {

        File reportDirectory = new File("screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";
        return new File(filePath);
    }

}
