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
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.Scenario;
import io.github.selcukes.commons.fixture.SelcukesFixture;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.notifier.Notifier;
import io.github.selcukes.notifier.NotifierFactory;
import io.github.selcukes.notifier.enums.NotifierType;
import io.github.selcukes.reports.enums.TestType;
import io.github.selcukes.snapshot.SnapshotImpl;
import io.github.selcukes.video.Recorder;
import io.github.selcukes.video.RecorderFactory;
import io.github.selcukes.video.enums.RecorderType;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static io.github.selcukes.extent.report.Reporter.getReporter;

@CustomLog
class ScreenPlayImpl implements ScreenPlay {

    private final SnapshotImpl capture;

    protected Recorder recorder;
    protected Notifier notifier;
    boolean isFailedOnly;
    private Scenario scenario;
    private ScreenPlayResult result;
    boolean isNativeDevice;
    boolean isDesktop;
    WebDriver driver;

    public ScreenPlayImpl(WebDriver driver) {
        this.driver = driver;
        capture = new SnapshotImpl(driver);
        isNativeDevice = driver instanceof AndroidDriver || driver instanceof IOSDriver;
        isDesktop = driver instanceof WindowsDriver;
        isFailedOnly = true;
    }

    @Override
    public String takeScreenshot() {
        try {
            return isNativeDevice || isDesktop ? capture.shootVisiblePage() : capture.shootPage();
        } catch (Exception e) {
            logger.warn(e::getMessage);
            return "";
        }

    }

    @Override
    public ScreenPlay attachScreenshot() {

        if (result.getTestType().equals(TestType.CUCUMBER)) {
            try {
                byte[] screenshot = isNativeDevice || isDesktop ? capture.shootVisiblePageAsBytes() : capture.shootPageAsBytes();
                attach(screenshot, "image/png");
            } catch (Exception e) {
                logger.warn(e::getMessage);
            }

        } else {
            String screenshotPath = takeScreenshot();
            if (!screenshotPath.isEmpty()) {
                String htmlToEmbed = "<br>  <img src='" + screenshotPath + "' height='100' width='100' /><br>";
                attach(htmlToEmbed);
            }

        }
        return this;
    }

    @Override
    public ScreenPlay attachVideo() {
        if (isAttachable()) {
            String videoPath = stop().getAbsolutePath();
            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>"
                + "<source src=" + videoPath + " type=\"video/mp4\">"
                + "Your browser does not support the video tag." + "</video>";
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
            ((AndroidDriver) driver)
                .startRecordingScreen(new AndroidStartScreenRecordingOptions()
                    .withVideoSize("540x960").withBitRate(2000000)
                    .withTimeLimit(Duration.ofMinutes(30)));
        } else if (driver instanceof IOSDriver) {
            ((IOSDriver) driver)
                .startRecordingScreen(new IOSStartScreenRecordingOptions()
                    .withVideoType("libx264")
                    .withVideoQuality(IOSStartScreenRecordingOptions.VideoQuality.MEDIUM)
                    .withTimeLimit(Duration.ofMinutes(30)));

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

        notifier.scenarioName(result.getTestName())
            .scenarioStatus(result.getStatus())
            .stepDetails(message)
            .path(takeScreenshot());

        if (result.getErrorMessage() != null)
            notifier.errorMessage(result.getErrorMessage());

        notifier.pushNotification();
        return this;
    }


    @Override
    public void attachLogs() {
        if (isAttachable()) {
            write(getReporter().getLogRecords());
        }
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

        }
        return this;
    }


    @Override
    public ScreenPlay ignoreCondition() {
        isFailedOnly = false;
        return this;
    }


    private void write(String text) {
        if (result.getTestType().equals(TestType.CUCUMBER)) {

            scenario.log(text);
            logger.info(() -> "Attached Logs to Cucumber Report");
        } else {
            SelcukesFixture.attach(text);
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
                SelcukesFixture.attach(attachment);
                String contentType = attachment.contains("video") ? "Video" : "Screenshot";
                logger.info(() -> "Attached " + contentType + " to TestNG Report");
            }
        }
    }

    private void attach(byte[] objToEmbed, String mediaType) {
        scenario.attach(objToEmbed, mediaType, scenario.getName());
    }

    boolean isAttachable() {
        if (isFailedOnly) {
            return result.isFailed();
        } else return true;
    }
}
