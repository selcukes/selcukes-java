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
import io.github.selcukes.reports.notification.IncomingWebHookRequest;
import io.github.selcukes.reports.enums.NotifierEnum;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;

public class SlackUploader {

    public void uploadFile(String filePath) {
        SlackFileUploader slackFileUploader = SlackFileUploader.builder()
            .channel(ConfigFactory.getConfig().getNotifier().get("channel"))
            .token(ConfigFactory.getConfig().getNotifier().get("api-token"))
            .filePath(filePath)
            .fileName("Sample")
            .build();

        StringBuilder url = new StringBuilder();
        url.append(NotifierEnum.SLACK_API_URL.getValue())
            .append(slackFileUploader.getToken())
            .append("&channels=")
            .append(slackFileUploader.getChannel())
            .append("&pretty=1");

        File fileToUpload = new File(slackFileUploader.getFilePath());
        FileBody fileBody = new FileBody(fileToUpload);

       IncomingWebHookRequest.forUrl(url.toString())
        .post(fileBody);

    }


}
