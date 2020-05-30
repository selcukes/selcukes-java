package io.github.selcukes.reports.screen;

public interface ScreenPlay {
    String takeScreenshot();
    void embedScreenshot();
    void attachScreenshot();
    ScreenPlay start();
    void attachVideo();
    void embedVideo();
    ScreenPlay stop();
    ScreenPlay slackNotification(String step);
    ScreenPlay teamsNotification(String step);
}
