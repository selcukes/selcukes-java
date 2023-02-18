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

import io.github.selcukes.commons.os.Platform;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.Browser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.openqa.selenium.remote.Browser.CHROME;
import static org.openqa.selenium.remote.Browser.EDGE;

@CustomLog
public class NativeSnapshotTest {
    private static final ThreadLocal<WebDriver> LOCAL_DRIVER = new InheritableThreadLocal<>();

    @DataProvider
    public Object[][] driverTypes() {
        return new Object[][]{{CHROME}, {EDGE}};
    }

    @Test(dataProvider = "driverTypes")
    public void browserTest(Browser browser) {
        logger.debug(() -> "Browser : " + browser);
        setDriver(browser);
        new HomePage(getDriver()).navigateToHomePage();

    }

    @AfterMethod
    public void tearDown() {
        getDriver().quit();
    }

    @AfterClass
    void terminate() {
        LOCAL_DRIVER.remove();
    }

    private WebDriver getDriver() {
        return LOCAL_DRIVER.get();
    }

    private void setDriver(Browser browser) {
        LOCAL_DRIVER.set(createWebDriver(browser, Platform.isLinux()));
    }

    private WebDriver createWebDriver(Browser browser, boolean headless) {
        if (browser.equals(EDGE)) {
            EdgeOptions edgeOptions = new EdgeOptions();
            if (headless) {
                edgeOptions.addArguments("--headless");
            }
            return new EdgeDriver(edgeOptions);
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            if (headless) {
                chromeOptions.addArguments("--headless");
            }
            return new ChromeDriver(chromeOptions);
        }
    }
}
