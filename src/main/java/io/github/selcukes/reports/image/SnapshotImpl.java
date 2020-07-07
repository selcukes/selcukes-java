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

package io.github.selcukes.reports.image;


import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.exception.RecorderException;
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SnapshotImpl extends NativeScreenshot implements Snapshot {
    private final WebDriver driver;

    public SnapshotImpl(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public String shootPage() {
        File destFile = getScreenshotPath();
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            throw new RecorderException("Failed Capturing Screenshot..", e);
        }
        return destFile.getAbsolutePath();
    }

    @Override
    public String shootFullPage() {
        File destFile = getScreenshotPath();
        return (isChrome()) ?
            Screenshot.captureFullPage(getDevTools(), destFile.getAbsolutePath()) : captureNativeScreenshot();
    }

    @Override
    public byte[] shootFullPageAsBytes() {
        byte[] screenshot;
        try {
            screenshot = (isChrome()) ? Screenshot.captureFullPageAsBytes(getDevTools()) :
                Files.readAllBytes(Paths.get(captureNativeScreenshot()));
        } catch (IOException e) {
            throw new RecorderException("Failed Capturing Screenshot..", e);
        }
        return screenshot;
    }

    private ChromeDevToolsService getDevTools() {
        return DevToolsService.getDevToolsService(driver);
    }

    private boolean isChrome() {
        return (ConfigFactory.getConfig().getBrowserName().equalsIgnoreCase("CHROME"));
    }
}
