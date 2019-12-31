package io.github.selcukes.reports.slack;

import io.github.selcukes.core.runtime.HttpClientUtils;

public interface SlackClientFactory {
    public static HttpClientUtils createWebHookClient(String webHookUrl) {
        return new HttpClientUtils(webHookUrl);
    }
}
