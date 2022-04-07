package io.github.selcukes.core.page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

public class MobilePage implements Page {
    private final AppiumDriver driver;
    public MobilePage(AppiumDriver driver)
    {
        this.driver=driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
