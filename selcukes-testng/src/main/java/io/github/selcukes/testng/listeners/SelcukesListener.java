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

import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.commons.fixture.SelcukesFixture;
import io.github.selcukes.commons.fixture.TestResult;
import io.github.selcukes.commons.listener.LifecycleManager;
import org.testng.*;

import static io.github.selcukes.commons.SelcukesLifecycle.getDefaultLifecycle;
import static io.github.selcukes.commons.SelcukesLifecycle.getLifecycleType;

public class SelcukesListener implements ISuiteListener, IInvokedMethodListener, IClassListener {
    LifecycleManager lifecycleManager;

    @Override
    public void onStart(ISuite suite) {
        lifecycleManager = getDefaultLifecycle();
        SelcukesFixture.setValidator("org.testng.Assert");
        SelcukesFixture.setReporter(Reporter.class);
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

    @SuppressWarnings("all")
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isConfigurationMethod()) {
            var type = getLifecycleType(method.getTestMethod().getRealClass());
            if (type == Lifecycle.Type.METHOD) {
                var result = TestResult.builder()
                    .name(method.getTestMethod().getMethodName())
                    .status(getTestStatus(testResult))
                    .build();
                lifecycleManager.beforeTest(result);
            }
        }

    }

    @SuppressWarnings("all")
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isConfigurationMethod()) {
            Reporter.setCurrentTestResult(testResult);
            var type = getLifecycleType(method.getTestMethod().getRealClass());
            if (type == Lifecycle.Type.METHOD) {
                var result = TestResult.builder()
                    .name(method.getTestMethod().getMethodName())
                    .status(getTestStatus(testResult))
                    .build();
                lifecycleManager.beforeAfterTest(result);
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        if (!method.isConfigurationMethod()) {
            Reporter.setCurrentTestResult(testResult);
            var type = getLifecycleType(method.getTestMethod().getRealClass());
            if (type == Lifecycle.Type.METHOD) {
                var result = TestResult.builder()
                    .name(method.getTestMethod().getMethodName())
                    .status(getTestStatus(testResult))
                    .build();
                lifecycleManager.afterTest(result);
            }
        }
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        var type = getLifecycleType(testClass.getRealClass());
        if (type == Lifecycle.Type.CLASS) {
            var result = TestResult.builder()
                .name(testClass.getName())
                .build();
            lifecycleManager.beforeTest(result);
        }
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        var type = getLifecycleType(testClass.getRealClass());
        if (type == Lifecycle.Type.CLASS) {
            var result = TestResult.builder()
                .name(testClass.getName())
                .build();
            lifecycleManager.afterTest(result);
        }
    }

    private String getTestStatus(ITestResult result) {
        String testStatus;
        if (result.isSuccess()) {
            testStatus = "PASSED";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            testStatus = "FAILED";
        } else {
            testStatus = "SKIPPED";
        }
        return testStatus;
    }
}
