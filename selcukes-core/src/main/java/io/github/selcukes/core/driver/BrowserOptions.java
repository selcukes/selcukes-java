/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.core.driver;

import io.github.selcukes.core.enums.DriverType;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import static org.openqa.selenium.remote.CapabilityType.HAS_NATIVE_EVENTS;
import static org.openqa.selenium.remote.CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR;

public class BrowserOptions {
    public Capabilities getBrowserOptions(DriverType driverType) {
        switch (driverType) {
            case EDGE:
                WebDriverBinary.edgeDriver().setup();
                return new EdgeOptions();
            case FIREFOX:
                WebDriverBinary.firefoxDriver().setup();
                return new FirefoxOptions();
            case IEXPLORER:
                WebDriverBinary.ieDriver().setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions().requireWindowFocus();
                ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                ieOptions.setCapability(HAS_NATIVE_EVENTS, false);
                ieOptions.setCapability(UNEXPECTED_ALERT_BEHAVIOUR, "accept");
                ieOptions.setCapability("ignoreProtectedModeSettings", true);
                ieOptions.setCapability("disable-popup-blocking", true);
                ieOptions.setCapability("enablePersistentHover", true);
                return ieOptions;
            default:
                WebDriverBinary.chromeDriver().checkBrowserVersion().setup();
                return new ChromeOptions();
        }

    }
}
