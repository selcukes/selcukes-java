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

package io.github.selcukes.reports.tests;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.options.WindowsOptions;
import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.commons.fixture.DriverFixture;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@CustomLog
@Lifecycle
public class NotepadTest {
    private WebDriver driver;
    private AppiumDriverLocalService service;

    @SneakyThrows
    @BeforeMethod
    public void beforeTest() {
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/")
                .build();
        service.start();

        WindowsOptions options = new WindowsOptions();
        String app = "C:\\Windows\\System32\\notepad.exe";
        options.setApp(app);
        driver = new WindowsDriver(service.getUrl(), options);

        DriverFixture.setDriverFixture(driver);
    }

    @Test(enabled = false)
    public void noteTest() {
        WebElement edit = driver.findElement(By.className("Edit"));
        edit.sendKeys("Welcome to Selcukes !!!");

        edit.sendKeys(Keys.CONTROL + "w" + Keys.CONTROL);
        driver.findElement(By.name("Don't Save")).click();

    }


    @AfterMethod
    public void afterTest() {
        try {
            DriverFixture.removeDriverFixture();
            if (driver != null) {
                driver.quit();
            }

        } finally {
            if (service != null) {
                service.stop();
            }
        }

    }
}
