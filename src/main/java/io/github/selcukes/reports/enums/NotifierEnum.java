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

package io.github.selcukes.reports.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum NotifierEnum {
    PRETEXT("Selcukes Automation Report"),
    CALL_BACK("Callback"),
    AUTHOR("Ramesh"),
    AUTHOR_ICON("https://avatars0.githubusercontent.com/u/2510294?s=400&u=5d6412ba1dd13052992ff66317ae28d007a971d3&v=4"),
    TECHYWORKS("https://techyworks.blogspot.com/"),
    FOOTER_TEXT("Test Start Time"),
    FOOTER_ICON("https://techyworks.blogspot.com/favicon.ico"),
    WEB_HOOKS_URL("https://hooks.slack.com/services/"),
    SLACK_API_URL("https://slack.com/api/files.upload?token="),
    PROJECT("Project"),
    ENVIRONMENT("Environment"),
    ATTACHMENT("Attachments"),
    MESSAGE_CARD("MessageCard"),
    TIME_STAMP("Time Stamp");
    @Getter String value;
}
