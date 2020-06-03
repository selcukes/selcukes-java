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

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.http.HttpClient;
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.notification.NotifierEnum;
import io.github.selcukes.reports.notification.NotifierHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MicrosoftTeamsBuilder {

    protected void sendMessage(String scenarioTitle, String scenarioStatus, String message, String screenshotPath) {

        Field field = Field.builder()
            .name(NotifierEnum.ENVIRONMENT.getValue())
            .value(ConfigFactory.getConfig().getEnv())
            .build();

        Field field1 = Field.builder()
            .name(NotifierEnum.TIME_STAMP.getValue())
            .value(DateHelper.get().timeStamp())
            .build();

        Field field2 = Field.builder()
            .name(NotifierEnum.ATTACHMENT.getValue())
            .value("[Screenshot.jpg](" + screenshotPath + ")")
            .build();

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(field);
        fieldList.add(field1);
        fieldList.add(field2);

        Images image = Images.builder()
            .image(screenshotPath)
            .build();

        String activityTitle = "Scenario : " + scenarioTitle;
        String activitySubtitle = "Step : " + message;
        String activityText = "Status: " + scenarioStatus;
        Section section = Section.builder()
            .activityTitle(activityTitle)
            .activitySubtitle(activitySubtitle)
            .activityText(activityText)
            .activityImage(NotifierEnum.AUTHOR_ICON.getValue())
            .facts(fieldList)
            .images(Collections.singletonList(image))
            .build();

        String webhookUri = System.getProperty("selcukes.teams.hooksUrl");
        MicrosoftTeamsCard teamsCard = MicrosoftTeamsCard.builder()
            .type("MessageCard")
            .themeColor(NotifierHelper.getThemeColor(scenarioStatus))
            .title(NotifierEnum.PRETEXT.getValue())
            .text(" ")
            .sections(Collections.singletonList(section))
            .build();

        HttpClient client = IncomingWebHookRequest.forUrl(webhookUri);
        client.post(teamsCard);

    }

}
