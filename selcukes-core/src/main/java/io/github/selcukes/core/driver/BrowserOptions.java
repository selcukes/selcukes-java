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

import io.github.selcukes.databind.utils.StringHelper;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.Browser;

import static org.openqa.selenium.remote.Browser.CHROME;
import static org.openqa.selenium.remote.Browser.EDGE;
import static org.openqa.selenium.remote.Browser.FIREFOX;
import static org.openqa.selenium.remote.Browser.IE;

@UtilityClass
public class BrowserOptions {
    public static final String HEADLESS = "--headless";

    public static Capabilities getBrowserOptions(Browser browser, String platform) {
        boolean isHeadless = RunMode.isHeadless();
        if (EDGE.equals(browser)) {
            EdgeOptions edgeOptions = new EdgeOptions();
            if (isHeadless) {
                edgeOptions.addArguments(HEADLESS);
            }
            if (!StringHelper.isNullOrEmpty(platform)) {
                edgeOptions.setPlatformName(platform);
            }
            return edgeOptions;
        } else if (FIREFOX.equals(browser)) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (isHeadless) {
                firefoxOptions.addArguments(HEADLESS);
            }
            return firefoxOptions;
        } else if (IE.equals(browser)) {
            InternetExplorerOptions ieOptions = new InternetExplorerOptions().requireWindowFocus();
            ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
            ieOptions.setCapability("ignoreProtectedModeSettings", true);
            ieOptions.setCapability("disable-popup-blocking", true);
            ieOptions.setCapability("enablePersistentHover", true);
            return ieOptions;
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        if (isHeadless) {
            chromeOptions.addArguments(HEADLESS);
        }
        if (!StringHelper.isNullOrEmpty(platform)) {
            chromeOptions.setPlatformName(platform);
        }
        return chromeOptions;

    }

    public Browser valueOf(String browserName) {
        if (browserName.equalsIgnoreCase("MicroSoftEdge")) {
            return EDGE;
        } else if (browserName.equalsIgnoreCase("IE")) {
            return IE;
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            return FIREFOX;
        } else {
            return CHROME;
        }
    }
}
