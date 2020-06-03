/*
 *
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
 *
 */

package io.github.selcukes.reports.tests;

import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.notification.Notifier;
import io.github.selcukes.reports.notification.teams.MicrosoftTeams;
import io.github.selcukes.reports.video.FFmpegRecorder;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RecorderTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    WebDriver driver;
    Recorder recorder;

    @BeforeClass
    public void beforeClass() {
        ConfigFactory.loadLoggerProperties();
        WebDriverBinary.chromeDriver().setup();
        driver = new ChromeDriver();
        recorder = new FFmpegRecorder();
        recorder.start();
    }

    @Test
    public void loginTest() {

        driver.get("http://www.princexml.com/samples/");
        logger.debug(driver::getTitle);
        Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href,'dictionary.pdf')]")).isDisplayed());
        driver.findElement(By.xpath("//a[contains(@href,'dictionary.pdf')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains(".pdf"));
    }

    public void notificationTest() {
        System.setProperty("selcukes.teams.hooksUrl", "");
        Notifier notifier = new MicrosoftTeams();
        notifier.pushNotification("Test Name", "FAILED", "Test Exception in Step", "http://connectorsdemo.azurewebsites.net/images/WIN14_Jan_04.jpg");
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
        recorder.stopAndSave("SampleTest");
    }
}
