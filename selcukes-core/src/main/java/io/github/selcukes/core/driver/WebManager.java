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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriverBuilder;

import java.net.URL;

import static io.github.selcukes.core.driver.GridRunner.*;

@CustomLog
public class WebManager implements RemoteManager {

    public synchronized WebDriver createDriver() {
        String browser = ConfigFactory.getConfig().getWeb().get("browserName");
        WebDriver driver;
        try {
            logger.debug(() -> "Initiating New Browser Session...");
            Capabilities capabilities = DesktopOptions.getUserOptions();
            if (capabilities == null) {
                BrowserOptions browserOptions = new BrowserOptions();
                capabilities = browserOptions.getBrowserOptions(DriverType.valueOf(browser), isGrid());
            }
            RemoteWebDriverBuilder driverBuilder = RemoteWebDriver.builder().oneOf(capabilities);
            if (isGrid())
                driverBuilder.address(getServiceUrl());

            driver = driverBuilder.build();
        } catch (Exception e) {
            throw new DriverSetupException("Driver was not setup properly.", e);
        }

        return driver;
    }

    @SneakyThrows
    public URL getServiceUrl() {
        URL serviceUrl = new URL(ConfigFactory.getConfig().getWeb().get("serviceUrl"));
        if (isGridNotRunning()) {
            logger.warn(() -> "Selenium server not started...\n" +
                "Please use 'GridRunner.startSeleniumServer' method to start automatically.\n" +
                " Ignore this message if you have started manually...");
            return serviceUrl;
        }
        String urlString = String.format("%s://%s:%s", serviceUrl.getProtocol(), serviceUrl.getHost(), HUB_PORT);
        return new URL(urlString);
    }
}
