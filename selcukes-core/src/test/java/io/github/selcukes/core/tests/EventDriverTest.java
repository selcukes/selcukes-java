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
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.core.listener.EventCapture;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

public class EventDriverTest {
    WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        LocalDriver localDriver = new LocalDriver();
        driver = localDriver.createWebDriver(DriverType.EDGE, Platform.isLinux());
        WebDriverListener eventCapture = new EventCapture();
        driver = new EventFiringDecorator(eventCapture).decorate(driver);
    }

    @Test
    public void eventDriverTest() {

        driver.get("https://techyworks.blogspot.com/");
        driver.getTitle();

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
            ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='show-search' or @class='show-mobile-search']"))
        );
        driver.findElement(By.xpath("//span[@class='show-search' or @class='show-mobile-search']")).click();
        Await.until(2);
        driver.findElement(By.xpath("//input[@class='search-input' or @class='mobile-search-input']")).sendKeys("selenium");
    }

    @AfterTest
    public void afterTest() throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File reportDirectory = new File("target/screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";

        FileUtils.copyFile(srcFile, Paths.get(filePath).toFile());
        driver.quit();
    }
}
