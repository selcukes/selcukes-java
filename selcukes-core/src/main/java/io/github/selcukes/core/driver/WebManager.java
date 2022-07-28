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
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriverBuilder;

import java.net.URL;

import static io.github.selcukes.core.driver.GridRunner.hubPort;
import static io.github.selcukes.core.driver.GridRunner.isSeleniumServerNotRunning;
import static io.github.selcukes.core.driver.RunMode.isCloudBrowser;
import static io.github.selcukes.core.driver.RunMode.isLocalBrowser;

@CustomLog
public class WebManager implements RemoteManager {

    public synchronized WebDriver createDriver() {
        String browser = ConfigFactory.getConfig().getWeb().getBrowser().toUpperCase();
        logger.debug(() -> "Initiating New Browser Session...");
        Capabilities capabilities = AppiumOptions.getUserOptions();
        if (capabilities == null) {
            capabilities = BrowserOptions.getBrowserOptions(DriverType.valueOf(browser),
                    !(isLocalBrowser() || isCloudBrowser()));
            if (isCloudBrowser()) {
                capabilities = capabilities.merge(CloudOptions.getBrowserStackOptions(false));
            }
        }
        RemoteWebDriverBuilder driverBuilder = RemoteWebDriver.builder().oneOf(capabilities);
        if (!isLocalBrowser()) {
            logger.info(() -> "Starting Remote WebDriver session...");
            driverBuilder.address(getServiceUrl());
        } else {
            logger.info(() -> "Starting Local WebDriver session...");
        }

        return driverBuilder.build();
    }

    @SneakyThrows
    public URL getServiceUrl() {
        URL serviceUrl;
        if (isCloudBrowser())
            serviceUrl = new URL(CloudOptions.browserStackUrl());
        else {
            serviceUrl = new URL(ConfigFactory.getConfig().getWeb().getServiceUrl());
            if (isSeleniumServerNotRunning()) {
                logger.warn(() -> "Selenium server not started...\n" +
                        "Please use 'GridRunner.startSeleniumServer' method to start automatically.\n" +
                        " Ignore this message if you have started manually or executing in Cloud...");
            } else {
                String urlString = String.format("%s://%s:%s", serviceUrl.getProtocol(), serviceUrl.getHost(), hubPort);
                serviceUrl = new URL(urlString);
            }
        }
        return serviceUrl;
    }
}
