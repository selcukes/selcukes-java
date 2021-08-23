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
