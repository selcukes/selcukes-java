package io.github.selcukes.reports.screen;

import com.mashape.unirest.http.exceptions.UnirestException;
import io.cucumber.java.Scenario;
import io.github.selcukes.core.exception.RecorderException;
import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import io.github.selcukes.reports.notification.slack.Slack;
import io.github.selcukes.reports.notification.slack.SlackUtils;
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

    public ScreenPlayImpl(WebDriver driver, Scenario scenario) {
        this.driver = driver;
        this.scenario = scenario;
        recorder = new VideoRecorder();
    }

    @Override
    public String takeScreenshot() {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return srcFile.getAbsolutePath();
    }

    @Override
    public void attachScreenshot() {
        ChromeDevToolsService devToolsService = DevToolsService.getDevToolsService(driver);
        byte[] screenshot = new byte[0];
        try {
            screenshot = Screenshot.captureFullPageAsBytes(devToolsService);
            attach(screenshot, "image/png");
        } catch (IOException e) {
            throw new RecorderException("Failed Capturing Screenshot..", e);
        }
    }

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
    public ScreenPlay slackNotification(String step) {
        Slack slack = new SlackUtils();
        slack.pushNotification(scenario.getName(), "FAILED", step, takeScreenshot());
        return this;
    }

    @Override
    public ScreenPlay teamsNotification(String message) {

        try {
            MicrosoftTeams.forUrl(() -> {
                return System.getProperty("selcukes.teams.hooksUrl");
            }).sendMessage(message);
        } catch (UnirestException e) {
            throw new SelcukesException(e);
        }
        return this;
    }


    private void attach(byte[] objToEmbed, String mediaType) {
        scenario.embed(objToEmbed, mediaType, scenario.getName());
    }

}
