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

package io.github.selcukes.reports.tests.steps;

import io.cucumber.java.*;
import lombok.CustomLog;

@CustomLog
public class ReporterHooks {

    @Before
    public void beforeTest(Scenario scenario) {

        logger.info(() -> "Starting Scenario .." + scenario.getName());
    }

    @BeforeStep
    public void beforeStep() {
        logger.info(() -> "Before Step");

    }

    @AfterStep
    public void afterStep() {
        logger.info(() -> "After Step");
    }

    @After
    public void afterTest(Scenario scenario) {
        logger.info(() -> "Completed Scenario .." + scenario.getName());
    }

}
