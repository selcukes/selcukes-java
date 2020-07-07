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

public class NativeScreenshot {

    private final String ERROR_WHILE_CONVERTING_IMAGE = "Error while converting image";


    private String getFullHeight(WebDriver driver) {
        return executeJS(driver, "return document.body.scrollHeight").toString();
    }

    private int getFullWidth(WebDriver driver) {
        return ((Long) executeJS(driver, "return window.innerWidth",
            new Object[0])).intValue();
    }

    private int getWindowHeight(WebDriver driver) {
        return ((Long) executeJS(driver, "return window.innerHeight",
            new Object[0])).intValue();
    }

    private void waitForScrolling() {
        Await.until(1);
    }

    private BufferedImage getScreenshotNative(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        try (ByteArrayInputStream imageArrayStream = new ByteArrayInputStream(screenshot)) {
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new SelcukesException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    private BufferedImage getScreenshot(WebDriver driver) {
        int allH = Integer.parseInt(getFullHeight(driver));
        int allW = getFullWidth(driver);
        int winH = getWindowHeight(driver);
        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;
        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();
        for (int n = 0; n < scrollTimes; n++) {
            executeJS(driver, "scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = getScreenshotNative(driver);
            graphics.drawImage(part, 0, n * winH, null);
        }
        if (tail > 0) {
            executeJS(driver, "scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = getScreenshotNative(driver);
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();
        return finalImage;
    }

    public void captureNativeScreenshot(WebDriver driver, String filename) {
        try {
            ImageIO.write(getScreenshot(driver), "PNG", new File(filename));
        } catch (IOException e) {
            throw new SelcukesException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    private Object executeJS(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    public File getScreenshotPath() {
        File reportDirectory = new File("screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";
        return new File(filePath);
    }
}

