
package io.github.selcukes.reports.screen;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class ScreenPlayBuilder {
    public static ScreenPlayImpl getScreenPlay(WebDriver driver, Scenario scenario) {
        return new ScreenPlayImpl(driver, scenario);
    }
}
