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
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@CustomLog
public class DesktopManager extends AppiumManager {

    @Override
    public synchronized WebDriver createDriver() {

        logger.debug(() -> "Initiating New Desktop Session...");
        String app = ConfigFactory.getConfig().getWindows().getApp();
        URL serviceUrl = Objects.requireNonNull(getServiceUrl());
        Capabilities capabilities = ofNullable(AppiumOptions.getUserOptions())
                .orElse(AppiumOptions.getWinAppOptions(app));
        return new WindowsDriver(serviceUrl, capabilities);
    }

    @SneakyThrows
    @Override
    public URL getServiceUrl() {
        return AppiumEngine.getInstance().getServiceUrl();
    }
}
