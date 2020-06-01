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

import io.github.selcukes.core.http.HttpClient;
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.notification.NotifierHelper;

public class MicrosoftTeamsBuilder {

    protected void pushNotification(String scenarioTitle, String scenarioStatus, String message) {


        String webhookUri = System.getProperty("selcukes.teams.hooksUrl");
        MicrosoftTeamsCard teamsCard = new MicrosoftTeamsCard();
        teamsCard.setContext("http://schema.org/extensions");
        teamsCard.setType("MessageCard");

        teamsCard.setThemeColor(NotifierHelper.getThemeColor(scenarioStatus));
        teamsCard.setTitle(scenarioTitle);
        teamsCard.setText(message);
        
        HttpClient client = IncomingWebHookRequest.forUrl(webhookUri);
        client.post(teamsCard);
        client.shutdown();

    }

}
