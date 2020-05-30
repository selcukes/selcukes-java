/*
 *
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
