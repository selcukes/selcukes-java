/*
 *
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
 *
 */

package io.github.selcukes.reports.listeners;


import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
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
    public void onStart(ITestContext results) {
        logger.info(() -> "Started testing on: " + results.getStartDate());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult results) {

        logger.info(() -> "Started testing on: " + results.getName());
    }

    @Override
    public void onTestFailure(ITestResult results) {
        logger.info(() -> "Failed : " + results.getName());

    }

    @Override
    public void onTestSkipped(ITestResult results) {
        logger.info(() -> "Skipped Test: " + results.getName());

    }

    @Override
    public void onTestStart(ITestResult results) {
        logger.info(() -> "Testing: " + results.getName());

    }

    @Override
    public void onTestSuccess(ITestResult results) {
        long timeTaken = results.getEndMillis() - results.getStartMillis();
        logger.info(() -> "Tested: " + results.getName() + " Time taken:" + timeTaken + " ms");

    }

}