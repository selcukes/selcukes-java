package io.github.selcukes.reports.notification.teams;

public class MicrosoftTeams {
    public static IncomingWebHookRequest forUrl(WebHook webhook) {
        return new IncomingWebHookRequest(webhook);
    }
}
