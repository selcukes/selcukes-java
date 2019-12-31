package io.github.selcukes.reports.slack;

import io.github.selcukes.core.config.ConfigFactory;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;

public class SlackUploader {

    public void uploadFile(String filePath) {
        SlackFileUploader slackFileUploader = SlackFileUploader.builder()
                .channel(ConfigFactory.getConfig().getSlack().get("channel"))
                .token(ConfigFactory.getConfig().getSlack().get("api-token"))
                .filePath(filePath)
                .fileName("Sample")
                .build();

        StringBuilder url = new StringBuilder();
        url.append(SlackEnum.SLACK_API_URL.value)
                .append(slackFileUploader.getToken())
                .append("&channels=")
                .append(slackFileUploader.getChannel())
                .append("&pretty=1");

        File fileToUpload = new File(slackFileUploader.getFilePath());
        FileBody fileBody = new FileBody(fileToUpload);

        SlackWebHookClient webHookClient = SlackClientFactory.createWebHookClient(url.toString());
        webHookClient.post(fileBody);
        webHookClient.shutdown();

    }


}
