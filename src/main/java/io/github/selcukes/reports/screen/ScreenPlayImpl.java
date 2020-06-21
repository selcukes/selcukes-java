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
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import io.github.selcukes.reports.notification.Notifier;
import io.github.selcukes.reports.notification.slack.Slack;
import io.github.selcukes.reports.notification.teams.MicrosoftTeams;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.VideoRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

class ScreenPlayImpl implements ScreenPlay {
    WebDriver driver;
    Scenario scenario;
    Recorder recorder;
    boolean isOldCucumber;

    public ScreenPlayImpl(WebDriver driver, Scenario scenario) {
        this.driver = driver;
        this.scenario = scenario;
        recorder = VideoRecorder.getFFmpegRecorder();
        isOldCucumber = false;
    }

    @Override
    public String takeScreenshot() {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return srcFile.getAbsolutePath();
    }

    /**
     * scenario.embed(objToEmbed, mediaType);
     */
    @Override
    public void embedScreenshot() {
        isOldCucumber = true;
        attachScreenshot();
    }

    /**
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */
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

    /**
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */

    @Override
    public void attachVideo() {
        String videoPath = recorder.stopAndSave(scenario.getName().replace(" ", "_")).getAbsolutePath();

        String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
            "<source src=" + videoPath + " type=\"video/mp4\">" +
            "Your browser does not support the video tag." +
            "</video>";
        byte[] objToEmbed = htmlToEmbed.getBytes();
        attach(objToEmbed, "text/html");
    }

    /**
     * scenario.embed(objToEmbed, mediaType);
     */
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
    public ScreenPlay slackNotification(String message) {
        Notifier slack = new Slack();
        slack.pushNotification(scenario.getName(), scenario.getStatus().toString(), message, takeScreenshot());
        return this;
    }

    @Override
    public ScreenPlay teamsNotification(String message) {
        Notifier teams = new MicrosoftTeams();
        teams.pushNotification(scenario.getName(), scenario.getStatus().toString(), message, takeScreenshot());
        return this;
    }

    private void attach(byte[] objToEmbed, String mediaType) {
        if (isOldCucumber)
            scenario.embed(objToEmbed, mediaType);
        else
            scenario.embed(objToEmbed, mediaType, scenario.getName());
    }

}
