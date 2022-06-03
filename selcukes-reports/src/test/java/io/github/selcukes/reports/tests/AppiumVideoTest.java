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

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.selcukes.reports.screen.ScreenPlay;
import io.github.selcukes.reports.screen.ScreenPlayBuilder;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

@CustomLog
public class AppiumVideoTest {

    private WebDriver driver;
    private ScreenPlay screenPlay;
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

        UiAutomator2Options options = new UiAutomator2Options();
        String app = Paths.get("src/test/resources/android-app.apk").toFile().getAbsolutePath();
        System.out.println(app);
        options.setApp(app);
        driver = new AndroidDriver(service.getUrl(), options);

    }

    @Test(enabled = false)
    public void mobileVideoTest() {
        screenPlay = ScreenPlayBuilder
            .getScreenPlay(driver)
            .start();

        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Expandable Lists")).click();
        driver.findElement(AppiumBy.accessibilityId("3. Simple Adapter")).click();
    }


    @AfterMethod
    public void afterTest(ITestResult result) {
        try {
            screenPlay
                .withResult(result)
                .ignoreCondition()
                .attachVideo();
            if (driver != null)
                driver.quit();

        } finally {
            if (service != null) {
                service.stop();
            }
        }

    }
}