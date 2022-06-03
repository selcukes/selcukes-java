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

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.cucumber.java.Scenario;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.notifier.Notifier;
import io.github.selcukes.notifier.NotifierFactory;
import io.github.selcukes.notifier.enums.NotifierType;
import io.github.selcukes.reports.enums.TestType;
import io.github.selcukes.snapshot.SnapshotImpl;
import io.github.selcukes.video.Recorder;
import io.github.selcukes.video.RecorderFactory;
import io.github.selcukes.video.enums.RecorderType;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.time.Duration;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

class ScreenPlayImpl implements ScreenPlay {

    private final Logger logger = LoggerFactory.getLogger(ScreenPlayImpl.class);
    private final SnapshotImpl capture;
    protected LogRecordListener loggerListener;
    protected Recorder recorder;
    protected Notifier notifier;
    boolean isFailedOnly;
    private Scenario scenario;
    private ScreenPlayResult result;
    private ITestResult iTestResult;
    boolean isNativeDevice;
    WebDriver driver;

    public ScreenPlayImpl(WebDriver driver) {
        this.driver = driver;
        capture = new SnapshotImpl(driver);
        isNativeDevice = driver instanceof AndroidDriver || driver instanceof IOSDriver;
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
            attach(capture.shootPageAsBytes(), "image/png");

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
            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" + "<source src=" + videoPath + " type=\"video/mp4\">" + "Your browser does not support the video tag." + "</video>";
            attach(htmlToEmbed);
        } else {
            if (isNativeDevice) {
                stopAndDeleteVideo();
            } else recorder.stopAndDelete();
        }
        return this;
    }

    private void startNativeVideo() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).startRecordingScreen(new AndroidStartScreenRecordingOptions().withVideoSize("540x960").withBitRate(2000000).withTimeLimit(Duration.ofMinutes(30)));
        } else if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).startRecordingScreen(new IOSStartScreenRecordingOptions().withVideoType("libx264").withVideoQuality(IOSStartScreenRecordingOptions.VideoQuality.MEDIUM).withTimeLimit(Duration.ofMinutes(30)));

        }
        logger.info(() -> "Native Recording started");
    }

    private void stopAndDeleteVideo() {
        File tempVideo = stopAndSaveNativeVideo(UUID.randomUUID().toString());
        tempVideo.deleteOnExit();
        logger.info(() -> "Deleting recorded video file...");
    }

    private File stopAndSaveNativeVideo(String fileName) {
        String encodedVideo = "";
        if (driver instanceof AndroidDriver) {
            encodedVideo = ((AndroidDriver) driver).stopRecordingScreen();
        } else if (driver instanceof IOSDriver) {
            encodedVideo = ((IOSDriver) driver).stopRecordingScreen();
        }
        String path = "video-report/" + fileName + ".mp4";
        File video = FileHelper.createFile(encodedVideo, path);
        logger.info(() -> "Recording finished to " + video.getAbsolutePath());
        return video;
    }

    @Override
    public ScreenPlay start() {

        if (isNativeDevice) {
            startNativeVideo();
            return this;
        } else if (recorder == null) {
            logger.warn(() -> "RecorderType not configured. Using Default RecorderType as MONTE");
            withRecorder(RecorderType.MONTE);
        }
        recorder.start();
        return this;
    }

    @Override
    public File stop() {
        if (isNativeDevice) {
            return stopAndSaveNativeVideo(result.getTestName());
        }
        Preconditions.checkNotNull(recorder, "Recording not started...");
        return recorder.stopAndSave(result.getTestName());
    }

    @Override
    public ScreenPlay sendNotification(String message) {
        if (notifier == null) {
            logger.warn(() -> "NotifierType not configured. Using Default RecorderType as TEAMS");
            withNotifier(NotifierType.TEAMS);
        }

        notifier.scenarioName(result.getTestName()).scenarioStatus(result.getStatus()).stepDetails(message).path(takeScreenshot());

        if (result.getErrorMessage() != null) notifier.errorMessage(result.getErrorMessage());

        notifier.pushNotification();
        return this;
    }


    @Override
    public void attachLogs() {
        if (isAttachable()) {
            String infoLogs = loggerListener.getLogRecords(Level.INFO).map(LogRecord::getMessage).collect(Collectors.joining("</li><li>", "<ul><li> ", "</li></ul><br/>"));
            write(infoLogs);
        }
        stopReadingLogs();
    }

    @Override
    public void attachLogs(Level level) {
        if (isAttachable()) {
            String logs = loggerListener.getLogRecords(level).map(LogRecord::getMessage).collect(Collectors.joining("</li><li>", "<ul><li> ", "</li></ul><br/>"));
            write(logs);
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

            scenario.log(text);
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
        scenario.attach(objToEmbed, mediaType, scenario.getName());
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
