
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

