/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private final WebDriver driver;
    private CapabilitiesProvider caps;

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
            WebElement target = element.getWebElement();
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
