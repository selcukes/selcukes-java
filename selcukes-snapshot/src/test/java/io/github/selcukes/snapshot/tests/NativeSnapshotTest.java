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

package io.github.selcukes.snapshot.tests;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.snapshot.ScreenGrabber;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class NativeSnapshotTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String url = "https://techyworks.blogspot.com/";
    private static final ThreadLocal<WebDriver> LOCAL_DRIVER = new InheritableThreadLocal<>();

    @Test(enabled = false)
    public void nativeScreenshotTestForFirefox() {
        setDriver(DriverType.FIREFOX);
        new HomePage(getDriver()).navigateToHomePage();

    }

    @Test
    public void nativeScreenshotTestForChrome() {
        setDriver(DriverType.CHROME);
        new HomePage(getDriver()).navigateToHomePage();

    }

    @Test
    public void nativeScreenshotTestForEdge() {
        setDriver(DriverType.EDGE);
        new HomePage(getDriver()).navigateToHomePage();

    }

    @Test
    public void nativeScreenshotTestForChromeWithText() {
        setDriver(DriverType.CHROME);
        logger.info(() -> "Initiated Chrome browser");
        getDriver().get(url);
        logger.info(() -> "Navigated to " + url);
        long startTime = System.currentTimeMillis();
        String screenshotFilePath = ScreenGrabber
            .shoot(getDriver())
            .withText("This sample Text Message\nMake it simple Make it simple Make it simple Make it simple Make it simple")
            .fullPage()
            .save();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        logger.info(() -> "Time Taken to capture screenshot: " + duration + "ms");
        logger.info(() -> "Chrome full page screenshot captured : " + screenshotFilePath);

    }

    @AfterTest
    public void tearDown() {
        LOCAL_DRIVER.get().quit();
        LOCAL_DRIVER.remove();
    }

    private WebDriver getDriver() {
        return LOCAL_DRIVER.get();
    }

    private void setDriver(DriverType driverType) {
        LOCAL_DRIVER.set(new LocalDriver().createWebDriver(driverType, true));
    }
}
