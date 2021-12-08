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

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.core.listener.EventCapture;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EventDriverTest {
    WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        LocalDriver localDriver = new LocalDriver();
        driver = localDriver.createWebDriver(DriverType.CHROME, Platform.isLinux());
        WebDriverListener eventCapture = new EventCapture();
        driver = new EventFiringDecorator(eventCapture).decorate(driver);
    }

    @Test
    public void eventDriverTest() {

        driver.get("https://techyworks.blogspot.com/");
        driver.getTitle();
        Await.until(2);
        driver.findElement(By.xpath("//span[@class='show-search']")).click();
        Await.until(2);
        driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("selenium");
    }

    @AfterTest
    public void afterTest() {

        driver.quit();
    }
}
