package io.github.selcukes.reports.slack;

public class SlackUtils implements Slack{


    @Override
    public Slack pushNotification(String scenarioTitle, String scenarioStatus, String message, String screenshotPath) {
        SlackMessageBuilder slackMessageBuilder=new SlackMessageBuilder();
        slackMessageBuilder.sendMessage(scenarioTitle,scenarioStatus,message,screenshotPath);
        SlackUploader slackUploader=new SlackUploader();
        slackUploader.uploadFile(screenshotPath);
        return this;
    }
}