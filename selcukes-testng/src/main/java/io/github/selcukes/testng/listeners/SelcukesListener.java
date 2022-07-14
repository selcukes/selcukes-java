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

package io.github.selcukes.testng.listeners;

import io.github.selcukes.commons.listener.LifecycleManager;
import io.github.selcukes.commons.result.TestResult;
import org.testng.*;

import static io.github.selcukes.commons.SelcukesLifecycle.getDefaultLifecycle;

public class SelcukesListener implements ISuiteListener, IInvokedMethodListener {
    LifecycleManager lifecycleManager;

    @Override
    public void onStart(ISuite suite) {
        lifecycleManager = getDefaultLifecycle();
        var result = TestResult.builder()
            .name(suite.getName())
            .build();
        lifecycleManager.beforeSuite(result);
    }

    @Override
    public void onFinish(ISuite suite) {
        var result = TestResult.builder()
            .name(suite.getName())
            .build();
        lifecycleManager.afterSuite(result);
    }
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isConfigurationMethod()) {
            var result = TestResult.builder()
                .name(method.getTestMethod().getMethodName())
                .status(testResult.getStatus() + "")
                .build();
            lifecycleManager.beforeTest(result);
        }
    }
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isConfigurationMethod()) {
            var result = TestResult.builder()
                .name(method.getTestMethod().getMethodName())
                .status(testResult.getStatus() + "")
                .build();
            lifecycleManager.afterTest(result);
        }
    }
}
