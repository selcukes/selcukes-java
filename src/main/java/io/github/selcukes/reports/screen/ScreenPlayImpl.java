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
import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.notification.Notifier;
import io.github.selcukes.reports.notification.NotifierFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

class ScreenPlayImpl implements ScreenPlay {

    private final WebDriver driver;
    private final Scenario scenario;
    private LogRecordListener loggerListener;
    private Recorder recorder;
    private Notifier notifier;
    private boolean isOldCucumber;

    public ScreenPlayImpl(WebDriver driver, Scenario scenario) {
        this.driver = driver;
        this.scenario = scenario;
        isOldCucumber = false;
        startReadingLogs();
    }

    @Override
    public String takeScreenshot() {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return srcFile.getAbsolutePath();
    }

    @Override
    public void embedScreenshot() {
        isOldCucumber = true;
        attachScreenshot();
    }

    @Override
    public void attachScreenshot() {
        ChromeDevToolsService devToolsService = DevToolsService.getDevToolsService(driver);
        byte[] screenshot;
        try {
            screenshot = Screenshot.captureFullPageAsBytes(devToolsService);
            attach(screenshot, "image/png");
        } catch (IOException e) {
            throw new RecorderException("Failed Capturing Screenshot..", e);
        }
    }

    @Override
    public void attachVideo() {
        if (scenario.isFailed()) {
            String videoPath = recorder.stopAndSave(scenario.getName().replace(" ", "_")).getAbsolutePath();

            String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
                "<source src=" + videoPath + " type=\"video/mp4\">" +
                "Your browser does not support the video tag." +
                "</video>";
            byte[] objToEmbed = htmlToEmbed.getBytes();
            attach(objToEmbed, "text/html");
        } else recorder.stopAndDelete(scenario.getName());
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
    public ScreenPlay stop() {
        recorder.stopAndSave(scenario.getName().replace(" ", "_"));
        return this;
    }

    @Override
    public ScreenPlay sendNotification(String message) {
        notifier.pushNotification(scenario.getName(), scenario.getStatus().toString(), message, takeScreenshot());
        return this;
    }

    @Override
    public void attachLogs() {
        String infoLogs = loggerListener.getLogRecords(Level.INFO)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));
        scenario.write(infoLogs);
        stopReadingLogs();
    }

    @Override
    public ScreenPlay getRecorder(RecorderType recorderType) {
        recorder = RecorderFactory.getRecorder(recorderType);
        return this;
    }

    @Override
    public ScreenPlay getNotifier(NotifierType notifierType) {
        Notifier notifier = NotifierFactory.getNotifier(notifierType);
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

    private void stopReadingLogs() {
        LoggerFactory.removeListener(loggerListener);
    }

}
