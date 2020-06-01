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
import io.github.selcukes.reports.http.HttpClient;
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.notification.NotifierHelper;

import java.util.ArrayList;
import java.util.List;


public class SlackMessageBuilder {
    private final Logger logger = LoggerFactory.getLogger(SlackMessageBuilder.class);


    public void sendMessage(String scenarioTitle, String scenarioStatus, String error, String screenshotPath) {


        Long startTime = System.currentTimeMillis() / 1000;
        Field field = Field.builder()
            .title(SlackEnum.PROJECT.value)
            .value(ConfigFactory.getConfig().getProjectName())
            .shortValue(true)
            .build();

        Field field1 = Field.builder()
            .title(SlackEnum.ENVIRONMENT.value)
            .value(ConfigFactory.getConfig().getEnv())
            .shortValue(true)
            .build();

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(field);
        fieldList.add(field1);

        Attachment attachment = Attachment.builder()
            .fallback(SlackEnum.PRETEXT.value)
            .callbackId(SlackEnum.CALL_BACK.value)
            .color(NotifierHelper.getThemeColor(scenarioStatus))
            .pretext(SlackEnum.PRETEXT.value)
            .authorName(SlackEnum.AUTHOR.value)
            .authorLink(SlackEnum.TECHYWORKS.value)
            .authorIcon(SlackEnum.AUTHOR_ICON.value)
            .title(scenarioTitle)
            .titleLink(SlackEnum.TECHYWORKS.value)
            .text(error)
            .fields(fieldList)
            .imageUrl(screenshotPath)
            .footer(SlackEnum.FOOTER_TEXT.value)
            .footerIcon(SlackEnum.FOOTER_ICON.value)
            .timeStamp(startTime)
            .build();

        List<Attachment> attachmentList = new ArrayList<>();

        attachmentList.add(attachment);


        SlackMessage slackMessage = SlackMessage.builder()
            .attachments(attachmentList).build();

        HttpClient client = IncomingWebHookRequest.forUrl(getSlackWebHookUrl());
        client.post(slackMessage);
        client.shutdown();
    }

    private String getSlackWebHookUrl() {
        StringBuilder slackWebHookUrl = new StringBuilder();
        slackWebHookUrl.append(SlackEnum.WEB_HOOKS_URL.value)
            .append(ConfigFactory.getConfig().getSlack().get("webhook-token"));

        return slackWebHookUrl.toString();
    }


}
