package io.github.selcukes.core.caps;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

public class CapabilitiesProvider {
    public Capabilities getCapabilities(WebDriver driver) {
        WebDriver currentDriver = driver;
        Capabilities capabilities = capabilitiesOf(currentDriver);
        while (currentDriver instanceof WrapsDriver && capabilities == null) {
            currentDriver = ((WrapsDriver) currentDriver).getWrappedDriver();
            capabilities = capabilitiesOf(currentDriver);
        }
        return capabilities;
    }

    private Capabilities capabilitiesOf(WebDriver currentDriver) {
        return currentDriver instanceof HasCapabilities
            ? ((HasCapabilities) currentDriver).getCapabilities()
            : null;
    }
}
