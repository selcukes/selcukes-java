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

import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.notification.slack.Slack;
import io.github.selcukes.reports.notification.teams.MicrosoftTeams;
import lombok.experimental.UtilityClass;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@UtilityClass
public class NotifierFactory {
    public synchronized Notifier getNotifier(NotifierType notifierType) {

        Map<NotifierType, Supplier<Notifier>> notifierSupplier = new EnumMap<NotifierType, Supplier<Notifier>>(NotifierType.class);
        notifierSupplier.put(NotifierType.SLACK, Slack::new);
        notifierSupplier.put(NotifierType.TEAMS, MicrosoftTeams::new);

        return notifierSupplier.getOrDefault(notifierType, MicrosoftTeams::new).get();
    }

}
