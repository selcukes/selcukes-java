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

package io.github.selcukes.reports.listeners;


import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class SelcukesTestNGListener implements ITestListener {
    private final Logger logger = LoggerFactory.getLogger(SelcukesTestNGListener.class);

    @Override
    public void onFinish(ITestContext context) {
        logger.info(() -> "PASSED TEST CASES");
        context.getPassedTests()
            .getAllResults()
            .forEach(result -> logger.info(result::getName));
        logger.info(() -> "FAILED TEST CASES");
        context.getFailedTests()
            .getAllResults()
            .forEach(result -> logger.info(result::getName));
        logger.info(() -> "Test completed on: " + context.getEndDate());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info(() -> "Started testing on: " + context.getStartDate());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info(() -> "Failed : " + result.getName());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info(() -> "Skipped Test: " + result.getName());
        onTestFailure(result);

    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info(() -> "Testing: " + result.getName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long timeTaken = result.getEndMillis() - result.getStartMillis();
        logger.info(() -> "Tested: " + result.getName() + " Time taken:" + timeTaken + " ms");

    }

}