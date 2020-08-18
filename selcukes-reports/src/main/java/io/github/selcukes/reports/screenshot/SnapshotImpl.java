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

package io.github.selcukes.reports.screenshot;

import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SnapshotImpl extends NativeSnapshot implements Snapshot {
    private final Logger logger = LoggerFactory.getLogger(SnapshotImpl.class);
    private final WebDriver driver;

    public SnapshotImpl(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public String shootPage() {

        File destFile = getScreenshotPath().toFile();
        logger.debug(() -> "Capturing screenshot...");
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
            logger.debug(() -> "Screenshot saved at  :" + destFile.getAbsolutePath());
        } catch (IOException e) {
            throw new SnapshotException("Failed Capturing Screenshot..", e);
        }
        return destFile.getAbsolutePath();
    }

    @Override
    public String shootFullPage() {
        try {
            logger.debug(() -> "Capturing screenshot...");
            String filePath = Files.write(getScreenshotPath(), shootFullPageAsBytes()).toString();
            logger.debug(() -> "Screenshot saved at  :" + filePath);
            return filePath;
        } catch (IOException e) {
            throw new SnapshotException("Failed Capturing Screenshot..", e);
        }
    }

    @Override
    public byte[] shootFullPageAsBytes() {
        return fullPageNativeScreenshotAsBytes();
    }

    @Override
    public Snapshot withAddressBar() {
        isAddressBar = true;
        return this;
    }

    @Override
    public Snapshot withText(String text) {
        screenshotText = text;
        return this;
    }

    @Override
    public String save() {
        return null;
    }
}
