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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.core.driver.DriverManager;
import lombok.CustomLog;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

@CustomLog
public class TestLifecyclePerMethod extends TestLifecycle implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.debug(() -> "Before invocation of " + method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        logger.debug(() -> "After invocation of " + method.getTestMethod().getMethodName());
        if (!method.isConfigurationMethod()) {
            logger.debug(() -> method.getTestMethod().getMethodName() + " is Success? " + result.isSuccess());
            logger.debug(() -> "Cleanup Resource");
            DriverManager.removeDriver();
            ConfigFactory.cleanupConfig();
        }
    }

}