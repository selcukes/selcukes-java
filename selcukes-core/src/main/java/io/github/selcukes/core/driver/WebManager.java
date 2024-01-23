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

import io.github.selcukes.collections.Resources;
import io.github.selcukes.commons.config.ConfigFactory;
import lombok.CustomLog;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static io.github.selcukes.core.driver.GridRunner.isSeleniumServerNotRunning;
import static io.github.selcukes.core.driver.RunMode.isCloudBrowser;
import static io.github.selcukes.core.driver.RunMode.isRemoteBrowser;
import static java.util.Optional.ofNullable;

@CustomLog
class WebManager implements RemoteManager {

    public synchronized WebDriver createDriver(Capabilities customCapabilities) {
        String browser = ConfigFactory.getConfig().getWeb().getBrowser().toUpperCase();
        logger.debug(() -> "Initiating New Browser Session...");

        var capabilities = ofNullable(customCapabilities)
                .orElseGet(() -> {
                    var driverOptions = BrowserOptions
                            .getBrowserOptions(BrowserOptions.valueOf(browser), "", RunMode.isHeadlessWeb());
                    return isCloudBrowser() ? driverOptions
                            .merge(CloudOptions.getBrowserStackOptions(false)) : driverOptions;
                });

        var driverBuilder = RemoteWebDriver.builder().oneOf(capabilities);
        if (isRemoteBrowser()) {
            logger.info(() -> "Starting Remote WebDriver session...");
            driverBuilder.address(getServiceUrl());
        } else {
            logger.info(() -> "Starting Local WebDriver session...");
        }

        return driverBuilder.build();
    }

    public URL getServiceUrl() {
        var serviceUrl = isCloudBrowser()
                ? CloudOptions.browserStackUrl()
                : ConfigFactory.getConfig().getWeb().getServiceUrl();

        if (isSeleniumServerNotRunning()) {
            logger.warn(() -> """
                    The Selenium server is not running.
                    Please use the 'GridRunner.startSelenium()' method to start it automatically.
                    If you have started it manually or are executing in the cloud, you can ignore this message.""");
        } else {
            return GridRunner.getLocalServiceUrl();
        }

        return Resources.toURL(serviceUrl);
    }

}
