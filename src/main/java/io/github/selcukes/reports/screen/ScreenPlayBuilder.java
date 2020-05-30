package io.github.selcukes.reports.screen;

import io.cucumber.java.Scenario;
import lombok.Builder;
import org.openqa.selenium.WebDriver;


public class ScreenPlayBuilder {

    @Builder(builderMethodName = "set")
    public static ScreenPlayImpl set(WebDriver driver, Scenario scenario) {
        return new ScreenPlayImpl(driver, scenario);
    }
}
