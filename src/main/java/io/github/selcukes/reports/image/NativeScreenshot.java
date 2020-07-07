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

import io.github.selcukes.core.commons.Await;
import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.helper.FileHelper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

abstract class NativeScreenshot {
    private final WebDriver driver;
    private final String ERROR_WHILE_CONVERTING_IMAGE = "Error while converting image";

    public NativeScreenshot(WebDriver driver) {
        this.driver = driver;
    }

    public byte[] shootPageAsBytes() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private String getFullHeight() {
        return executeJS("return document.body.scrollHeight").toString();
    }

    private int getFullWidth() {
        return ((Long) executeJS("return window.innerWidth",
            new Object[0])).intValue();
    }

    private int getWindowHeight() {
        return ((Long) executeJS("return window.innerHeight",
            new Object[0])).intValue();
    }

    private void waitForScrolling() {
        Await.until(1);
    }

    private BufferedImage toBufferedImage() {

        try (ByteArrayInputStream imageArrayStream = new ByteArrayInputStream(shootPageAsBytes())) {
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new SelcukesException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    private BufferedImage getScreenshot() {
        int allH = Integer.parseInt(getFullHeight());
        int allW = getFullWidth();
        int winH = getWindowHeight();
        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;
        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();
        for (int n = 0; n < scrollTimes; n++) {
            executeJS("scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = toBufferedImage();
            graphics.drawImage(part, 0, n * winH, null);
        }
        if (tail > 0) {
            executeJS("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = toBufferedImage();
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();
        return finalImage;
    }

    protected String captureNativeScreenshot() {
        try {
            File file = getScreenshotPath();
            ImageIO.write(getScreenshot(), "PNG", file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new SelcukesException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    private Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected File getScreenshotPath() {
        File reportDirectory = new File("screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";
        return new File(filePath);
    }
}

