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

package io.github.selcukes.reports.notification.slack;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.notification.NotifierEnum;
import io.github.selcukes.reports.notification.NotifierHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SlackMessageBuilder {
    private final Logger logger = LoggerFactory.getLogger(SlackMessageBuilder.class);


    public void sendMessage(String scenarioTitle, String scenarioStatus, String error, String screenshotPath) {


        Long startTime = System.currentTimeMillis() / 1000;

        Field field = Field.builder()
            .title(NotifierEnum.PROJECT.getValue())
            .value(ConfigFactory.getConfig().getProjectName())
            .shortValue(true)
            .build();

        Field field1 = Field.builder()
            .title(NotifierEnum.ENVIRONMENT.getValue())
            .value(ConfigFactory.getConfig().getEnv())
            .shortValue(true)
            .build();

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(field);
        fieldList.add(field1);

        Attachment attachment = Attachment.builder()
            .fallback(NotifierEnum.PRETEXT.getValue())
            .callbackId(NotifierEnum.CALL_BACK.getValue())
            .color(NotifierHelper.getThemeColor(scenarioStatus))
            .pretext(NotifierEnum.PRETEXT.getValue())
            .authorName(NotifierEnum.AUTHOR.getValue())
            .authorLink(NotifierEnum.TECHYWORKS.getValue())
            .authorIcon(NotifierEnum.AUTHOR_ICON.getValue())
            .title(scenarioTitle)
            .titleLink(NotifierEnum.TECHYWORKS.getValue())
            .text(error)
            .fields(fieldList)
            .imageUrl(screenshotPath)
            .footer(NotifierEnum.FOOTER_TEXT.getValue())
            .footerIcon(NotifierEnum.FOOTER_ICON.getValue())
            .timeStamp(startTime)
            .build();

        SlackMessage slackMessage = SlackMessage.builder()
            .attachments(Collections.singletonList(attachment)).build();

        IncomingWebHookRequest.forUrl(getSlackWebHookUrl())
            .post(slackMessage);
    }

    private String getSlackWebHookUrl() {
        StringBuilder slackWebHookUrl = new StringBuilder();
        slackWebHookUrl.append(NotifierEnum.WEB_HOOKS_URL.getValue())
            .append(ConfigFactory.getConfig().getSlack().get("webhook-token"));

        return slackWebHookUrl.toString();
    }


}
