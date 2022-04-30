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
import lombok.CustomLog;

import java.net.URL;
import java.util.Objects;

@CustomLog
public class DesktopManager extends AppiumManager {
    private WindowsDriver windowsDriver;

    @Override
    public synchronized WindowsDriver createDriver() {
        if (null == windowsDriver) {

            String app = ConfigFactory.getConfig().getWindows().get("app");
            URL serviceUrl = Objects.requireNonNull(getServiceUrl());
            DesktopOptions.setServiceUrl(serviceUrl);
            windowsDriver = new WindowsDriver(serviceUrl, DesktopOptions.getWinAppOptions(app));
        }
        return windowsDriver;
    }

    public void destroyDriver() {
        if (windowsDriver != null) {
            windowsDriver.quit();
        }
    }
}
