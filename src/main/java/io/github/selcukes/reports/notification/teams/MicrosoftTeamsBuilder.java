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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.reports.notification.NotifierHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MicrosoftTeamsBuilder {
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String METHOD_POST = "POST";
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void pushNotification(String scenarioTitle, String scenarioStatus, String message) {
        try {

            String webhookUri = System.getProperty("selcukes.teams.hooksUrl");
            MicrosoftTeamsCard msTeamsCard = new MicrosoftTeamsCard();
            msTeamsCard.setContext("http://schema.org/extensions");
            msTeamsCard.setType("MessageCard");

            msTeamsCard.setThemeColor(NotifierHelper.getThemeColor(scenarioStatus));
            msTeamsCard.setTitle(scenarioTitle);
            msTeamsCard.setText(message);

            final byte[] bytes = objectMapper.writeValueAsBytes(msTeamsCard);

            postMessage(webhookUri, bytes);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private void postMessage(String uri, byte[] bytes) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
        int timeout = 30000;
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setRequestMethod(METHOD_POST);

        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

        final OutputStream os = conn.getOutputStream();
        os.write(bytes);

        os.flush();
        os.close();
    }
}
