package io.github.selcukes.reports.notification;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class IncomingWebHookRequest {
    private final WebHook webhook;

    public IncomingWebHookRequest(WebHook webhook) {
        this.webhook = webhook;
    }

    public void sendMessage(String text) throws UnirestException {
        String url = webhook.getUrl();
        JSONObject object = new JSONObject();
        object.put("text", text);
        Unirest.post(url).header("Content-Type", "application/json").body(object).asString();
    }
}
