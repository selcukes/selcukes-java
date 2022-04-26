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
 *//*


package io.github.selcukes.wdb.driver;


import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriverBuilder;

public class WebDriverBuilder {
    boolean headless = false;
    Capabilities capabilities;
    DriverType driverType;

    public WebDriverBuilder(DriverType driverType) {
        this.driverType = driverType;
    }

    public WebDriver create() {
        if (capabilities == null) {
            capabilities = getCapabilities();
        }
        RemoteWebDriverBuilder driverBuilder = RemoteWebDriver.builder().oneOf(capabilities);

        return driverBuilder.build();
    }

    public WebDriverBuilder setHeadless() {
        headless = true;
        return this;
    }

    public WebDriverBuilder setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    private Capabilities getCapabilities() {
        if (Platform.isLinux())
            headless = true;
        switch (driverType) {
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setHeadless(headless);
                return edgeOptions;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(headless);
                return firefoxOptions;
            case IEXPLORER:
                return new InternetExplorerOptions();
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(headless);
                return chromeOptions;
        }
    }

}
*/
