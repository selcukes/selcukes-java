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
import io.github.selcukes.core.driver.DriverManager;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

@UtilityClass
public class Pages {
    public synchronized WebPage webPage(Capabilities... capabilities) {
        WebDriver driver = DriverManager.createWebDriver(capabilities);
        return new WebPage(driver);
    }

    public synchronized WinPage winPage(Capabilities... capabilities) {
        WindowsDriver driver = DriverManager.createWinDriver(capabilities);
        return new WinPage(driver);
    }

    public synchronized MobilePage mobilePage(Capabilities... capabilities) {
        WebDriver driver = DriverManager.createMobileDriver(capabilities);
        return new MobilePage(driver);
    }

    public static synchronized ApiPage apiPage() {
        return new ApiPage();
    }
}
