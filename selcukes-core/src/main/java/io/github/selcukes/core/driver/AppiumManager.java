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
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.helper.FileHelper;
import lombok.CustomLog;
import org.openqa.selenium.Capabilities;

@CustomLog
public class AppiumManager implements RemoteManager {
    AppiumDriver driver;
    AppiumDriverLocalService service;

    @Override
    public AppiumDriver createDriver() {

        if (null == driver) {
            try {
                logger.info(() -> "Initiating New Mobile Session...");
                startAppiumService();
                Capabilities capabilities = DesktopOptions.getUserOptions();
                if (capabilities == null) {
                    String app = FileHelper.loadThreadResource(ConfigFactory.getConfig()
                        .getMobile().get("app")).getAbsolutePath();
                    capabilities = DesktopOptions.getAndroidOptions(app);
                }
                driver = new AndroidDriver(service.getUrl(), capabilities);
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
        stopAppiumService();
    }

    public void startAppiumService() {
        service = new AppiumServiceBuilder()
            .withIPAddress("127.0.0.1")
            .usingAnyFreePort()
            .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
            .withArgument(GeneralServerFlag.BASEPATH, "/wd/")
            .build();
        logger.info(() -> "Starting Appium server...");
        service.start();
    }

    public void stopAppiumService() {
        if (service != null)
            service.stop();
    }
}
