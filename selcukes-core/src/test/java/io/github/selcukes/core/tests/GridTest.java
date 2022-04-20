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

import io.github.selcukes.wdb.WebDriverBinary;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;

@CustomLog
public class GridTest {
    WebDriver driver;
    private static int HUB_PORT;

    @BeforeSuite
    static void beforeSuite() {
        WebDriverBinary.chromeDriver().setup();
        HUB_PORT = PortProber.findFreePort();
        logger.debug(() -> "Using Free Hub Port: " + HUB_PORT);
        Main.main(new String[]{"standalone", "--port", String.valueOf(HUB_PORT)});
    }

    @SneakyThrows
    @BeforeTest
    void beforeTest() {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        driver = new RemoteWebDriver(new URL("http://localhost:" + HUB_PORT), options);

    }

    @Test
    public void remoteTest() {
        driver.get("https://www.google.com/");
        Assert.assertEquals(driver.getTitle(), "Google");
    }

    @AfterTest
    void afterTest() {
        driver.quit();
    }
}
