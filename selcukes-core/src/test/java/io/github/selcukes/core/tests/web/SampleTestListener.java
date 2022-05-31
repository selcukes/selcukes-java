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

package io.github.selcukes.core.tests.web;

import lombok.CustomLog;
import org.testng.*;

@CustomLog
public class SampleTestListener implements ITestListener, IInvokedMethodListener {
    public void onStart(ITestContext context) {
        logger.info(() -> "Before Test: " + context.getName());
    }

    public void onFinish(ITestContext context) {
        logger.info(() -> "After Test: " + context.getName());
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.info(() -> "beforeInvocation: " + method.getTestMethod().getMethodName());
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.info(() -> "afterInvocation: " +  method.getTestMethod().getMethodName());
    }
}
