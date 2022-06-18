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
import io.github.selcukes.core.enums.DeviceType;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;

@UtilityClass
public class Pages {
    public static synchronized WebPage webPage() {
        WebDriver driver = DriverManager.createDriver(DeviceType.BROWSER);
        return new WebPage(driver);
    }

    public static synchronized WinPage winPage() {
        WindowsDriver driver = DriverManager.createDriver(DeviceType.DESKTOP);
        return new WinPage(driver);
    }

    public static synchronized MobilePage mobilePage() {
        WebDriver driver = DriverManager.createDriver(DeviceType.MOBILE);
        return new MobilePage(driver);
    }
}
