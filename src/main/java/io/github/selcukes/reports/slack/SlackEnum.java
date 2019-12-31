package io.github.selcukes.reports.slack;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SlackEnum {
    PRETEXT("Selcukes Automation Report"),
    CALL_BACK("Callback"),
    AUTHOR("Ramesh"),
    AUTHOR_ICON("https://media.licdn.com/dms/image/C5103AQGjKEdaFRp0cw/profile-displayphoto-shrink_200_200/0?e=1580342400&v=beta&t=1vh2MFbwgnzBs9l96pujGYQxOpAifbqx2n9WOrsbSpo"),
    TECHYWORKS("https://techyworks.blogspot.com/"),
    FOOTER_TEXT("Test Start Time"),
    FOOTER_ICON("https://techyworks.blogspot.com/favicon.ico"),
    WEB_HOOKS_URL("https://hooks.slack.com/services/"),
    SLACK_API_URL("https://slack.com/api/files.upload?token="),
    PROJECT("Project"),
    ENVIRONMENT("Environment");
    String value;

}
