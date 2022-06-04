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

package io.github.selcukes.reports.cucumber;

import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Status;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.reports.screen.ScreenPlay;
import io.github.selcukes.reports.screen.ScreenPlayBuilder;

public class CucumberAdapter implements CucumberService {
    ScreenPlay play;
    private String stepInfo;
    Scenario scenario;

    @Override
    public void beforeTest() {
        play = ScreenPlayBuilder.getScreenPlay();
    }

    @Override
    public void beforeScenario() {
        play.start();
    }

    @Override
    public void beforeStep() {
        // Nothing required
    }

    @Override
    public void afterStep(String step, boolean status) {
        if (status) stepInfo = step;
        play.withResult(scenario)
            .attachScreenshot();
        if (ConfigFactory.getConfig().getNotifier().isNotification())
            play.sendNotification(stepInfo);
    }

    @Override
    public void afterScenario(String scenarioName, Status status) {
        play.attachVideo();
    }

    @Override
    public void afterTest() {
        //Do Nothing
    }
}

