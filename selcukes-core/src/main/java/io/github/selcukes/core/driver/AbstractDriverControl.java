package io.github.selcukes.core.driver;

import io.github.selcukes.commons.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class AbstractDriverControl implements DriverControl {
    @Override
    public final WebDriver getDriver(String serviceUrl) {
        try {
            URL url = new URL(serviceUrl);
            DesiredCapabilities capabilities = createCapabilities();
            return new RemoteWebDriver(url, capabilities);
        } catch (Exception e) {
            return ExceptionHelper.rethrow(e);
        }
    }

    protected abstract DesiredCapabilities createCapabilities();
}
