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

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.commons.config.ConfigFactory;
import lombok.CustomLog;
import lombok.SneakyThrows;

import java.util.Objects;

@CustomLog
public class DesktopManager implements RemoteManager {
    private AppiumDriverLocalService service;
    private WindowsDriver windowsDriver;
    private Process winProcess;

    public synchronized WindowsDriver createDriver() {
        if (null == windowsDriver) {
            startAppiumService();
            startWinAppDriver();
            String app = ConfigFactory.getConfig().getWindows().get("app");
            windowsDriver = new WindowsDriver(Objects.requireNonNull(service.getUrl()),
                DesktopOptions.setCapabilities(app));
        }
        return windowsDriver;
    }

    public void destroyDriver() {
        if (windowsDriver != null) {
            windowsDriver.closeApp();
        }
        killWinAppDriver();
        stopAppiumService();
    }

    @SneakyThrows
    private void startWinAppDriver() {
        ProcessBuilder processBuilder = new ProcessBuilder(ConfigFactory.getConfig().getWindows().get("winApp-path"));
        processBuilder.inheritIO();
        winProcess = processBuilder.start();
        logger.info(() -> "WinAppDriver started...");
    }

    private void killWinAppDriver() {
        winProcess.destroy();
        logger.info(() -> "WinAppDriver killed...");
    }

    public void startAppiumService() {
        service = new AppiumServiceBuilder()
            .withIPAddress("127.0.0.1")
            .usingPort(4723)
            .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
            .withArgument(GeneralServerFlag.BASEPATH, "/wd/")
            .build();
        service.start();
    }

    public void stopAppiumService() {
        if (service != null)
            service.stop();
    }
}
