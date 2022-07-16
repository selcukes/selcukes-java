/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.reports.listeners;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.fixture.TestResult;
import io.github.selcukes.commons.listener.TestLifecycleListener;
import io.github.selcukes.reports.screen.ScreenPlay;
import io.github.selcukes.reports.screen.ScreenPlayBuilder;
import lombok.CustomLog;

import static io.github.selcukes.commons.fixture.DriverFixture.getDriverFixture;

@CustomLog
public class ReportServiceProvider implements TestLifecycleListener {
    ScreenPlay play;

    @Override
    public void beforeTest(TestResult result) {
        logger.debug(() -> "Initiating Screenplay..");
        if (getDriverFixture() != null && play == null) {
            play = ScreenPlayBuilder.getScreenPlay()
                .start();
            logger.debug(() -> "Initiated Screenplay..");
        }
    }

    @Override
    public void beforeAfterTest(TestResult result) {
        logger.debug(() -> "Performing Screenplay Actions..");
        if (getDriverFixture() != null && play != null) {
            play.withResult(result)
                .ignoreCondition()
                .attachScreenshot();
            if (ConfigFactory.getConfig().getNotifier().isNotification())
                play.sendNotification(result.getName());

            play.attachVideo();
            logger.debug(() -> "Completed Screenplay Actions..");
        }

    }
}
