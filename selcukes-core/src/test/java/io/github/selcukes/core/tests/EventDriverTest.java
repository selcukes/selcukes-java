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

package io.github.selcukes.core.tests;

import io.github.selcukes.core.listener.DriverEventHandler;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EventDriverTest {
    WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        WebDriverBinary.chromeDriver().checkBrowserVersion().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        DriverEventHandler eventListener = new DriverEventHandler();
        EventFiringWebDriver eventFiringDriver =
            new EventFiringWebDriver(new ChromeDriver(options));
        eventFiringDriver.register(eventListener);
        driver = eventFiringDriver;
    }

    @Test
    public void eventDriverTest() {
        driver.get("https://techyworks.blogspot.com/");
    }

    @AfterTest
    public void afterTest() {

        driver.quit();
    }
}
