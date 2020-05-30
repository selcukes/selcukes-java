package io.github.selcukes.reports.notification.teams;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class IncomingWebHookRequest {
    private final WebHook webhook;

    public IncomingWebHookRequest(WebHook webhook) {
        this.webhook = webhook;
    }

    public void sendMessage(String message) throws UnirestException {
        String url = webhook.getUrl();
        JSONObject object = new JSONObject();
        object.put("text", message);
        Unirest.post(url).header("Content-Type", "application/json").body(object).asString();
    }
}
