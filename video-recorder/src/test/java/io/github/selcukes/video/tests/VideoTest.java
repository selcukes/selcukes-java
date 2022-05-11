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

package io.github.selcukes.video.tests;

import io.github.selcukes.video.Recorder;
import io.github.selcukes.video.RecorderFactory;
import io.github.selcukes.video.enums.RecorderType;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class VideoTest {
    @Test
    public void recordVideo() {
        System.setProperty("java.awt.headless", "true");
        Recorder recorder = RecorderFactory.getRecorder(RecorderType.MONTE);
        recorder.start();
        LocalDriver localDriver = new LocalDriver();
        WebDriver driver = localDriver.createWebDriver(DriverType.CHROME);
        driver.get("https://www.google.com/");
        driver.findElement(By.name("q")).sendKeys("Welcome to Selcukes...", Keys.ENTER);
        File file = recorder.stopAndSave("test");
        Assert.assertTrue(file.getAbsolutePath().contains("mp4"));

    }
}
