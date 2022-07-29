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

package io.github.selcukes.notifier;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.notifier.enums.NotifierType;
import io.github.selcukes.notifier.slack.Slack;
import io.github.selcukes.notifier.teams.MicrosoftTeams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifierFactory {
    /**
     * If the notifier type is Slack, return a Slack notifier, otherwise return
     * a Microsoft Teams notifier.
     *
     * @param  notifierType The type of notifier you want to use.
     * @return              A Notifier object.
     */
    public synchronized Notifier getNotifier(final NotifierType notifierType) {
        return notifierType.equals(NotifierType.SLACK) ? new Slack() : new MicrosoftTeams();
    }

    /**
     * If the notifier type is Slack, return a Slack object, otherwise return a
     * MicrosoftTeams object
     *
     * @return A Notifier object.
     */
    public synchronized Notifier getNotifier() {
        return ConfigFactory.getConfig().getNotifier().getType().equalsIgnoreCase("SLACK") ? new Slack()
                : new MicrosoftTeams();
    }

}
