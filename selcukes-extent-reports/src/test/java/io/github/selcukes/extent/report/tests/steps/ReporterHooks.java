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

package io.github.selcukes.extent.report.tests.steps;

import io.cucumber.java.*;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;

import static io.github.selcukes.extent.report.Reporter.getReport;

public class ReporterHooks {
    Logger logger = LoggerFactory.getLogger(ReporterHooks.class);

    @Before
    public void beforeTest(Scenario scenario) {
        getReport().start(); //Initialise Extent Report and start recording logRecord
        //  .initSnapshot(driver); //Initialise Full page screenshot
        logger.info(() -> "Starting Scenario .." + scenario.getName());
        getReport().attachAndRestart(); // Attach INFO logs and restart logRecord

    }

    @BeforeStep
    public void beforeStep() {
        logger.info(() -> "Before Step");
        getReport().attachAndRestart(); // Attach INFO logs and restart logRecord
    }

    @AfterStep
    public void afterStep() {
        getReport().attachAndRestart(); // Attach INFO logs and restart logRecord
        // getReport().attachScreenshot(); //Attach Full page screenshot

    }

    @After
    public void afterTest(Scenario scenario) {
        logger.info(() -> "Completed Scenario .." + scenario.getName());
        getReport().attachAndClear(); // Attach INFO logs and clear logRecord
    }

}
