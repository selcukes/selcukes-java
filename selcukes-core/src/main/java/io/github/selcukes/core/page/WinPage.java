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

package io.github.selcukes.core.page;

import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.core.driver.AppiumOptions;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.enums.DeviceType;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@CustomLog
public class WinPage implements Page {
    private WindowsDriver driver;

    public WinPage(WindowsDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public WinPage switchWindow(Object locator) {
        WebElement newWindowElement = find(locator);
        String windowId = newWindowElement.getAttribute("NativeWindowHandle");
        String windowIdToHex = Integer.toHexString(Integer.parseInt(windowId));
        logger.info(() -> "Window Id: " + windowId + "After: " + windowIdToHex);
        driver = DriverManager.createDriver(DeviceType.DESKTOP, AppiumOptions.setAppTopLevelWindow(windowIdToHex));
        driver.switchTo().activeElement();
        return this;
    }
}
