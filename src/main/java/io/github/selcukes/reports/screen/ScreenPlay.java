package io.github.selcukes.reports.screen;

public interface ScreenPlay {
    String takeScreenshot();
    void attachScreenshot();
    ScreenPlay start();
    void attachVideo();
    ScreenPlay stop();
    ScreenPlay slackNotification(String step);
    ScreenPlay teamsNotification(String step);
}
