package io.github.selcukes.core.page;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WinPage implements Page {
    private final WindowsDriver driver;

    public WinPage(WindowsDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public String read(String name) {
        return find(name).getText();
    }

    public WebElement find(String locator) {
        return getDriver().findElement(new AppiumBy.ByAccessibilityId(locator));
    }
}
