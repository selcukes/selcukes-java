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

package io.github.selcukes.core.tests;

import io.appium.java_client.AppiumDriver;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.page.MobilePage;
import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MobileTest {
    DriverManager<AppiumDriver> driverManager;

    @BeforeTest
    void beforeTest() {
        driverManager = new DriverManager<>();
    }

    @Test(enabled = true)
    public void remoteTest() {
        AppiumDriver driver = driverManager.createDriver(DeviceType.MOBILE);
        MobilePage page = new MobilePage(driver);
        page.enableDriverEvents();
        page.click(By.xpath("//android.widget.TextView[contains(@text,'Views')]"));
    }

    @AfterTest
    void afterTest() {
        if (driverManager.getManager() != null)
            driverManager.getManager().destroyDriver();
    }
}
