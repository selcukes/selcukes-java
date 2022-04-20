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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.core.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;

import java.net.URL;

@CustomLog
public class MobileManager implements RemoteManager {
    AppiumDriver driver;
    AppiumDriverLocalService service;

    @Override
    public AppiumDriver createDriver() {
        String browser = ConfigFactory.getConfig().getWeb().get("browserName");
        if (null == driver) {
            try {
                logger.info(() -> "Initiating New Browser Session...");
                service = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
                service.start();

                MobileOptions browserOptions = new MobileOptions();
                Capabilities capabilities = browserOptions.getBrowserOptions(DriverType.valueOf(browser));
                driver = new AppiumDriver(getServiceUrl(), capabilities);
            } catch (Exception e) {
                throw new DriverSetupException("Driver was not setup properly.", e);
            }
        }
        return driver;
    }

    @Override
    public void destroyDriver() {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }
    }

    @SneakyThrows
    @Override
    public URL getServiceUrl() {
        String serviceUrl = ConfigFactory.getConfig().getMobile().get("serviceUrl");
        return new URL(serviceUrl);
    }
}
