/*
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
 */

package io.github.selcukes.notifier.slack;

import io.github.selcukes.notifier.AbstractNotifier;
import io.github.selcukes.notifier.Notifier;

public class Slack extends AbstractNotifier {

    @Override
    public Notifier pushNotification(String scenarioTitle, String scenarioStatus, String message, String error, String screenshotPath) {
        SlackMessageBuilder slackMessageBuilder = new SlackMessageBuilder();
        slackMessageBuilder.sendMessage(scenarioTitle, scenarioStatus, error, screenshotPath);
        SlackUploader slackUploader = new SlackUploader();
        slackUploader.uploadFile(screenshotPath);
        return this;
    }
}
