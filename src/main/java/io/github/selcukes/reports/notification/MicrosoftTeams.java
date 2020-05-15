package io.github.selcukes.reports.notification;

public class MicrosoftTeams {
    public static IncomingWebHookRequest forUrl(WebHook webhook) {
        return new IncomingWebHookRequest(webhook);
    }
}
