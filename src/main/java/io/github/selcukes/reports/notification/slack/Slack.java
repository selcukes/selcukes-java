package io.github.selcukes.reports.notification.slack;

public interface Slack {
    Slack pushNotification(String scenarioTitle, String scenarioStatus, String message, String screenshotPath);
}
