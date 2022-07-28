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

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.notifier.enums.NotifierType;
import io.github.selcukes.reports.screen.ScreenPlay;
import io.github.selcukes.reports.screen.ScreenPlayBuilder;
import io.github.selcukes.video.enums.RecorderType;
import io.github.selcukes.wdb.driver.LocalDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.github.selcukes.wdb.enums.DriverType.CHROME;

@Test(enabled = false)
public class RecorderTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private WebDriver driver;
    private ScreenPlay screenPlay;

    @BeforeTest
    public void beforeTest() {
        driver = new LocalDriver().createWebDriver(CHROME);
        driver.manage().window().maximize();
        screenPlay = ScreenPlayBuilder
                .getScreenPlay(driver)
                .withRecorder(RecorderType.FFMPEG)
                .start();
    }


    public void loginTest() {
        driver.get("http://www.princexml.com/samples/");
        logger.debug(driver::getTitle);
        Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href,'dictionary.pdf')]")).isDisplayed());
        driver.findElement(By.xpath("//a[contains(@href,'dictionary.pdf')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains(".pdf"));
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        screenPlay
                .withResult(result)
                .ignoreCondition()
                .attachScreenshot()
                .withNotifier(NotifierType.SLACK) //Default Notifier is TEAMS
                .sendNotification("This is sample Test Step");

    }

    @AfterTest
    public void afterTest() {
        if (driver != null)
            driver.quit();
        screenPlay
                .attachVideo()
                .attachLogs();
    }
}
