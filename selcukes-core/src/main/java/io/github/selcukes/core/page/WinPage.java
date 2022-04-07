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

import io.appium.java_client.AppiumBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WinPage implements Page {
    private final WindowsDriver driver;

    public WinPage(WindowsDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public String read(String name) {
        return find(name).getText();
    }

    public WebElement find(String locator) {
        return getDriver().findElement(new AppiumBy.ByAccessibilityId(locator));
    }
}
