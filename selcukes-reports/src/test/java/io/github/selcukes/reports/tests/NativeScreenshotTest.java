/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package io.github.selcukes.reports.tests;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.reports.screenshot.Snapshot;
import io.github.selcukes.reports.screenshot.SnapshotImpl;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class NativeScreenshotTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String url="https://techyworks.blogspot.com/";
    // @Test
    public void nativeScreenshotTestForFirefox() {
        WebDriverBinary.firefoxDriver().setup();
        WebDriver driver = new FirefoxDriver();
        logger.info(() -> "Initiated Firefox browser");
        driver.get(url);
        logger.info(() -> "Navigated to "+url);
        Snapshot snapshot = new SnapshotImpl(driver);
        logger.info(() -> "Firefox full page screenshot captured : " + snapshot.shootFullPage());
        driver.quit();
    }

    @Test
    public void nativeScreenshotTestForChrome() {
        WebDriverBinary.chromeDriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        logger.info(() -> "Initiated Chrome browser");
        driver.get(url);
        logger.info(() -> "Navigated to "+url);
        Snapshot snapshot = new SnapshotImpl(driver);
        logger.info(() -> "Chrome full page screenshot captured : " + snapshot.shootFullPage());
        driver.quit();
    }
}
