/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.driver;

import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.commons.config.ConfigFactory;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Objects;

@CustomLog
public class DesktopManager implements RemoteManager {

    private WindowsDriver windowsDriver;
    private Process winProcess;

    public synchronized WindowsDriver createDriver() {
        if (null == windowsDriver) {
            startWinAppDriver();
            String app=ConfigFactory.getConfig().getWindows().get("app");
            windowsDriver = new WindowsDriver(Objects.requireNonNull(getServiceUrl()),
                DriverOptions.setCapabilities(app));
        }
        return windowsDriver;
    }

    public void destroyDriver() {
        windowsDriver.closeApp();
        killWinAppDriver();
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

    public WindowsDriver switchWindow(String name) {
        WebElement newWindowElement = windowsDriver.findElement(By.name(name));
        String windowId = newWindowElement.getAttribute("NativeWindowHandle");
        logger.info(() -> "Window Id: " + windowId + "After: " + Integer.toHexString(Integer.parseInt(windowId)));
        windowsDriver = new WindowsDriver(getServiceUrl(), DriverOptions.setAppTopLevelWindow(Integer.toHexString(Integer.parseInt(windowId))));
        windowsDriver.switchTo().activeElement();
        return windowsDriver;
    }


}
