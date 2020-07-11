package io.github.selcukes.core;

import io.github.selcukes.core.caps.CapabilitiesProvider;
import io.github.selcukes.core.element.Element;
import io.github.selcukes.core.listener.DriverEventHandler;
import io.github.selcukes.core.script.JavaScriptImpl;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class SelcukesDriver {
    private CapabilitiesProvider caps;
    private final WebDriver driver;

    public SelcukesDriver(WebDriver driver) {
        if (driver instanceof EventFiringWebDriver) {
            EventFiringWebDriver wd = new EventFiringWebDriver(driver);
            this.driver = wd.register(new DriverEventHandler());
        } else this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void switchTo(Element element) {
        if (null == element || !"iframe".equals(element.tagName())) {
            getDriver().switchTo().defaultContent();
        } else {
            WebElement target = element.getElement();
            while (target instanceof WrapsElement && target != element.getWrappedElement(target)) {
                target = element.getWrappedElement(target);
            }
            getDriver().switchTo().frame(target);
        }
    }

    public String pageSource() {
        return getDriver().getPageSource();
    }

    public Capabilities capabilities() {
        return caps.getCapabilities(getDriver());
    }

    public JavaScriptImpl executeScript(String script, Object... args) {
        return new JavaScriptImpl(getDriver()).execute(script, args);
    }

    public void quit() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}
