package io.github.selcukes.reports.notification.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Getter
@Setter
public class SlackMessage implements Serializable {
    private String username;
    private String text;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("icon_emoji")
    private String iconEmoji;
    private String channel;
    private List<Attachment> attachments;
}