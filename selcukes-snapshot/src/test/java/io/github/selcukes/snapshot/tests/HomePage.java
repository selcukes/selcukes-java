package io.github.selcukes.snapshot.tests;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.snapshot.Snapshot;
import io.github.selcukes.snapshot.SnapshotImpl;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String url = "https://techyworks.blogspot.com/";
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHomePage() {
        String browser = driver.getClass().getSimpleName().replace("Driver", "");
        logger.info(() -> String.format("Initiated %s browser", browser));
        driver.get(url);
        logger.info(() -> "Navigated to " + url);
        Snapshot snapshot = new SnapshotImpl(driver);
        String screenshotFilePath = snapshot
            .withText("This sample Text Message\nMake it simple Make it simple Make it simple Make it simple Make it simple")
            .shootPage();
        logger.info(() -> String.format("Captured full page screenshot for %s browser and placed at %s ",
            browser, screenshotFilePath));
    }

}
