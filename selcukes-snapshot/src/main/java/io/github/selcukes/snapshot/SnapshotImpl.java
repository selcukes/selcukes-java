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

package io.github.selcukes.snapshot;

import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SnapshotImpl extends PageSnapshot implements Snapshot {
    private final Logger logger = LoggerFactory.getLogger(SnapshotImpl.class);

    public SnapshotImpl(WebDriver driver) {
        super(driver);
    }

    @Override
    public String shootPage() {
        return copyFile(false);
    }

    @Override
    public String shootVisiblePage() {
        return copyFile(true);
    }

    private String copyFile(boolean isVisible) {
        File destFile = getScreenshotPath().toFile();
        logger.debug(() -> "Capturing screenshot...");
        try {
            File srcFile = isVisible ? takeScreenshot(OutputType.FILE) : getScreenshot(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
            logger.debug(() -> "Screenshot saved at  :" + destFile.getAbsolutePath());
        } catch (IOException e) {
            throw new SnapshotException("Failed Capturing Screenshot..", e);
        }
        return destFile.getAbsolutePath();
    }

    @Override
    public byte[] shootPageAsBytes() {
        return getScreenshot(OutputType.BYTES);
    }

    @Override
    public byte[] shootVisiblePageAsBytes() {
        return takeScreenshot();
    }

    private <X> X getScreenshot(OutputType<X> outputType) {
        return screenshotText != null || isAddressBar ? getScreenshotWithText(outputType) : getFullScreenshotAs(outputType);
    }

    private Path getScreenshotPath() {
        Path reportDirectory = FileHelper.createDirectory("target/screenshots");
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + "." + "png";
        return Paths.get(filePath);
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
}
