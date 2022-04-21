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
import io.github.selcukes.core.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriverBuilder;

import java.net.URL;

@CustomLog
public class WebManager implements RemoteManager {

    private WebDriver driver;

    public synchronized WebDriver createDriver() {
        String browser = ConfigFactory.getConfig().getWeb().get("browserName");
        if (null == driver) {
            try {
                logger.info(() -> "Initiating New Browser Session...");

                BrowserOptions browserOptions = new BrowserOptions();
                Capabilities capabilities = browserOptions.getBrowserOptions(DriverType.valueOf(browser));

                RemoteWebDriverBuilder webDriverBuilder = RemoteWebDriver.builder().oneOf(capabilities);
                if (ConfigFactory.getConfig().getWeb().get("remote").equalsIgnoreCase("true")) {
                    Main.main(new String[]{"standalone", "--port", "4444"});
                    webDriverBuilder.address(getServiceUrl());
                }
                driver = webDriverBuilder.build();
            } catch (Exception e) {
                throw new DriverSetupException("Driver was not setup properly.", e);
            }
        }
        return driver;
    }

    @Override
    public void destroyDriver() {
        if (driver != null)
            driver.quit();
    }

    @SneakyThrows
    public URL getServiceUrl() {
        String serviceUrl = ConfigFactory.getConfig().getWeb().get("serviceUrl");
        return new URL(serviceUrl);
    }
}
