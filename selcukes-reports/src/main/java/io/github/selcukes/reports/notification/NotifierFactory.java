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

package io.github.selcukes.reports.notification;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.notification.slack.Slack;
import io.github.selcukes.reports.notification.teams.MicrosoftTeams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifierFactory {
    public synchronized Notifier getNotifier(NotifierType notifierType) {
        return notifierType.equals(NotifierType.SLACK) ? new Slack() : new MicrosoftTeams();
    }

    public synchronized Notifier getNotifier() {
        return ConfigFactory.getConfig().getNotifier().get("type").equalsIgnoreCase("SLACK") ?
            new Slack() : new MicrosoftTeams();
    }

}
