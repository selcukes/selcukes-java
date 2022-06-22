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

package io.github.selcukes.core.listener;

import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.driver.GridRunner;
import lombok.CustomLog;
import org.testng.ISuite;
import org.testng.ISuiteListener;

@CustomLog
public class TestLifecycle implements ISuiteListener {
    public void onStart(ISuite suite) {
        logger.debug(() -> "Test Suite Execution started...");
    }

    public void onFinish(ISuite suite) {
        DriverManager.removeAllDrivers();
        GridRunner.stopAppium();
        logger.debug(() -> "Test Suite Execution finished...");
    }
}
