package io.github.selcukes.core.driver;

import org.openqa.selenium.WebDriver;

public interface DriverControl {
    WebDriver getDriver(String serviceUrl);
    WebDriver getDriver();
}
