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

import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Objects;

@CustomLog
public class DesktopManager extends AppiumManager {

    @Override
    public synchronized WebDriver createDriver() {
        WebDriver windowsDriver;
        try {
            logger.debug(() -> "Initiating New Desktop Session...");
            String app = ConfigFactory.getConfig().getWindows().getApp();
            URL serviceUrl = Objects.requireNonNull(getServiceUrl());
            AppiumOptions.setServiceUrl(serviceUrl);
            windowsDriver = new WindowsDriver(serviceUrl, AppiumOptions.getWinAppOptions(app));
        } catch (Exception e) {
            throw new DriverSetupException("Driver was not setup properly.", e);
        }
        return windowsDriver;
    }

    @SneakyThrows
    @Override
    public URL getServiceUrl() {
        return AppiumEngine.getInstance().getServiceUrl();
    }
}
