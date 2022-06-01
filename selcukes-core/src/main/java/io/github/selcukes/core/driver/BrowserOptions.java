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

import io.github.selcukes.wdb.WebDriverBinary;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

@UtilityClass
public class BrowserOptions {
    public static synchronized Capabilities getBrowserOptions(DriverType driverType, boolean ignoreBinarySetup) {
        return getBrowserOptions(driverType, ignoreBinarySetup, "");
    }

    public static synchronized Capabilities getBrowserOptions(DriverType driverType, boolean ignoreBinarySetup, String platform) {
        boolean headless = RunMode.isHeadless();
        if (!ignoreBinarySetup) {
            setBinaries(driverType);
        }
        switch (driverType) {
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setHeadless(headless);
                edgeOptions.setPlatformName(platform);
                return edgeOptions;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(headless);
                return firefoxOptions;
            case IEXPLORER:
                InternetExplorerOptions ieOptions = new InternetExplorerOptions().requireWindowFocus();
                ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                ieOptions.setCapability("ignoreProtectedModeSettings", true);
                ieOptions.setCapability("disable-popup-blocking", true);
                ieOptions.setCapability("enablePersistentHover", true);
                return ieOptions;
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(headless);
                chromeOptions.setPlatformName(platform);
                return chromeOptions;
        }

    }

    public static synchronized void setBinaries(DriverType driverType) {
        switch (driverType) {
            case EDGE:
                WebDriverBinary.edgeDriver().setup();
                break;
            case FIREFOX:
                WebDriverBinary.firefoxDriver().setup();
                break;
            case IEXPLORER:
                WebDriverBinary.ieDriver().setup();
                break;
            default:
                WebDriverBinary.chromeDriver().setup();
        }
    }
}
