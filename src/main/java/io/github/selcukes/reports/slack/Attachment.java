
package io.github.selcukes.reports.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Attachment {
    private String fallback;
    @JsonProperty("callback_id")
    private String callbackId;
    private String color;
    private String pretext;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("author_link")
    private String authorLink;
    @JsonProperty("author_icon")
    private String authorIcon;
    private String title;
    @JsonProperty("title_link")
    private String titleLink;
    private String text;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("thumb_url")
    private String thumbUrl;
    private String footer;
    @JsonProperty("footer_icon")
    private String footerIcon;
    @JsonProperty("ts")
    private Long timeStamp;
    private List<Field> fields;
}

