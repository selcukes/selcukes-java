package io.github.selcukes.reports.notification.slack;

public interface SlackClientFactory {
    static SlackWebHookClient createWebHookClient(String webHookUrl) {
        return new SlackWebHookClient(webHookUrl);
    }
}
