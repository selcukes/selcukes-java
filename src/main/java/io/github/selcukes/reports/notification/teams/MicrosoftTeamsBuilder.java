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
import io.github.selcukes.reports.enums.NotifierEnum;
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.notification.NotifierHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MicrosoftTeamsBuilder {

    protected void sendMessage(String scenarioTitle, String scenarioStatus, String message, String screenshotPath) {

        String error = null;
        if (message.contains("Exception:")) {
            error = message.split("Exception:")[1];
            message = message.split("Exception:")[0];
        }
        String attachmentValue = "[Screenshot.jpg](" + screenshotPath + ")";
        if (screenshotPath.endsWith(".avi") || screenshotPath.endsWith(".mp4")) {
            attachmentValue = "Video Path: " + screenshotPath;
        }

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
            .value(attachmentValue)
            .build();

        Field field3 = Field.builder()
            .name("Exception ")
            .value(error)
            .build();

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(field);
        fieldList.add(field1);
        fieldList.add(field2);
        if (scenarioStatus.equalsIgnoreCase("FAILED") && error != null) fieldList.add(field3);

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

        String hookUri = ConfigFactory.getConfig().getNotifier().get("webhook-token");
        MicrosoftTeamsCard teamsCard = MicrosoftTeamsCard.builder()
            .type(NotifierEnum.MESSAGE_CARD.getValue())
            .themeColor(NotifierHelper.getThemeColor(scenarioStatus))
            .title(ConfigFactory.getConfig().getProjectName())
            .text(" ")
            .sections(Collections.singletonList(section))
            .build();

        IncomingWebHookRequest.forUrl(hookUri)
            .post(teamsCard);

    }

}
