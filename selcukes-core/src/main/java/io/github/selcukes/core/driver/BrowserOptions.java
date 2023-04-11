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

import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;

import static io.github.selcukes.databind.utils.StringHelper.isNonEmpty;
import static org.openqa.selenium.remote.Browser.CHROME;
import static org.openqa.selenium.remote.Browser.EDGE;
import static org.openqa.selenium.remote.Browser.FIREFOX;

@UtilityClass
public class BrowserOptions {
    private static final String HEADLESS = "--headless";
    private static final String NEW_HEADLESS = "--headless=new";

    public static Capabilities getBrowserOptions(Browser browser, String platform) {
        boolean isHeadless = RunMode.isHeadless();
        if (EDGE.equals(browser)) {
            var edgeOptions = new EdgeOptions();
            if (isHeadless) {
                edgeOptions.addArguments(HEADLESS);
            }
            if (isNonEmpty(platform)) {
                edgeOptions.setPlatformName(platform);
            }
            return edgeOptions;
        } else if (FIREFOX.equals(browser)) {
            var firefoxOptions = new FirefoxOptions();
            if (isHeadless) {
                firefoxOptions.addArguments(HEADLESS);
            }
            return firefoxOptions;
        } else {
            var chromeOptions = new ChromeOptions();
            if (isHeadless) {
                chromeOptions.addArguments(NEW_HEADLESS);
            }
            if (isNonEmpty(platform)) {
                chromeOptions.setPlatformName(platform);
            }
            return chromeOptions;
        }
    }

    public Browser valueOf(String browserName) {
        if (browserName.equalsIgnoreCase("MicroSoftEdge")) {
            return EDGE;
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            return FIREFOX;
        } else {
            return CHROME;
        }
    }
}
