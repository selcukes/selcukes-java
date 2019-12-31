package io.github.selcukes.reports.slack;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.runtime.HttpClientUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SlackMessageBuilder {
    private final Logger logger = Logger.getLogger(SlackMessageBuilder.class.getName());


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
                .color(getMessageColor(scenarioStatus))
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

        HttpClientUtils webHookClient = SlackClientFactory.createWebHookClient(getSlackWebHookUrl());
        webHookClient.post(slackMessage);
        webHookClient.shutdown();
    }

    private String getSlackWebHookUrl() {
        StringBuilder slackWebHookUrl = new StringBuilder();
        slackWebHookUrl.append(SlackEnum.WEB_HOOKS_URL.value)
                .append(ConfigFactory.getConfig().getSlack().get("webhook-token"));

        return slackWebHookUrl.toString();
    }

    private String getMessageColor(String stepStatus) {
        String messageColor = null;
        switch (stepStatus) {
            case "FAILED":
                messageColor = "FF0000"; // Red
                break;
            case "PASSED":
                messageColor = "36a64f"; // Green
                break;
            case "SKIPPED":
                messageColor = "FFA500"; // Orange
                break;
        }
        return messageColor;
    }
}
