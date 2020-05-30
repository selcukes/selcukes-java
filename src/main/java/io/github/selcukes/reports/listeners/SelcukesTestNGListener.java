package io.github.selcukes.reports.listeners;


import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class SelcukesTestNGListener implements ITestListener {
    private final Logger LOGGER = LoggerFactory.getLogger(SelcukesTestNGListener.class);

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info(() -> "PASSED TEST CASES");
        context.getPassedTests()
            .getAllResults()
            .forEach(result -> LOGGER.info(result::getName));
        LOGGER.info(() -> "FAILED TEST CASES");
        context.getFailedTests()
            .getAllResults()
            .forEach(result -> LOGGER.info(result::getName));
        LOGGER.info(() -> "Test completed on: " + context.getEndDate());
    }

    @Override
    public void onStart(ITestContext arg0) {
        LOGGER.info(() -> "Started testing on: " + arg0.getStartDate());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {


    }

    @Override
    public void onTestFailure(ITestResult arg0) {
        LOGGER.info(() -> "Failed : " + arg0.getName());

    }

    @Override
    public void onTestSkipped(ITestResult arg0) {
        LOGGER.info(() -> "Skipped Test: " + arg0.getName());

    }

    @Override
    public void onTestStart(ITestResult arg0) {
        LOGGER.info(() -> "Testing: " + arg0.getName());

    }

    @Override
    public void onTestSuccess(ITestResult arg0) {
        long timeTaken = ((arg0.getEndMillis() - arg0.getStartMillis()));
        LOGGER.info(() -> "Tested: " + arg0.getName() + " Time taken:" + timeTaken + " ms");

    }

}