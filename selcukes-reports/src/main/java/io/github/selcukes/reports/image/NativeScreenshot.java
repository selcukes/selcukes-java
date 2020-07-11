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

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.ImageUtil;
import io.github.selcukes.devtools.DevToolsService;
import io.github.selcukes.devtools.core.Screenshot;
import io.github.selcukes.devtools.services.ChromeDevToolsService;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpMethod;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

abstract class NativeScreenshot {
    private final WebDriver driver;

    public NativeScreenshot(WebDriver driver) {
        this.driver = driver;
    }

    public byte[] shootPageAsBytes() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getFullHeight() {
        return executeJS("return document.body.scrollHeight").toString();
    }

    protected int getFullWidth() {
        return ((Long) executeJS("return window.innerWidth",
            new Object[0])).intValue();
    }

    protected int getWindowHeight() {
        return ((Long) executeJS("return window.innerHeight",
            new Object[0])).intValue();
    }


    protected void waitForScrolling() {
        Await.until(TimeUnit.MILLISECONDS, 1);
    }

    private BufferedImage defaultFullPageScreenshot() {
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
            BufferedImage part = ImageUtil.toBufferedImage(shootPageAsBytes());
            graphics.drawImage(part, 0, n * winH, null);
        }
        if (tail > 0) {
            executeJS("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = ImageUtil.toBufferedImage(shootPageAsBytes());
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();
        return finalImage;
    }

    public Object executeCustomCommand(String commandName) {
        try {
            Method execute = RemoteWebDriver.class.getDeclaredMethod("execute", String.class);
            execute.setAccessible(true);
            Response res = (Response) execute.invoke(this.driver, commandName);
            return res.getValue();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SnapshotException(e);
        }
    }

    public BufferedImage nativeScreenshotForFF() {
        defineCustomCommand("mozFullPageScreenshot", new CommandInfo("/session/:sessionId/moz/screenshot/full", HttpMethod.GET));
        Object result = this.executeCustomCommand("mozFullPageScreenshot");
        String base64EncodedPng;
        if (result instanceof String) {
            base64EncodedPng = (String) result;
        } else if (result instanceof byte[]) {
            base64EncodedPng = new String((byte[]) result);
        } else {
            throw new SnapshotException(String.format("Unexpected result for /moz/screenshot/full command: %s",
                result == null ? "null" : result.getClass().getName() + "instance"));
        }

        return ImageUtil.toBufferedImage(OutputType.BYTES.convertFromBase64Png(base64EncodedPng));
    }

    private void defineCustomCommand(String name, CommandInfo info) {
        try {
            Method defineCommand = HttpCommandExecutor.class.getDeclaredMethod("defineCommand", String.class, CommandInfo.class);
            defineCommand.setAccessible(true);
            defineCommand.invoke(((RemoteWebDriver) this.driver).getCommandExecutor(), name, info);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new SnapshotException(e);
        }
    }

    protected byte[] fullPageNativeScreenshotAsBytes() {
        try {
            BufferedImage screenshotImage = getBufferedImage();
            BufferedImage textImage = ImageUtil.generateImageWithLogo("URL: " + getCurrentUrl(), screenshotImage);
            return ImageUtil.toByteArray(textImage);
        } catch (IOException e) {
            throw new SnapshotException(e);
        }
    }

    private BufferedImage getBufferedImage() throws IOException {
        if (driver instanceof ChromeDriver)
            return ImageUtil.toBufferedImage(Screenshot.captureFullPageAsBytes(getDevTools()));
        else if (driver instanceof FirefoxDriver)
            return nativeScreenshotForFF();
        else
            return defaultFullPageScreenshot();
    }

    private ChromeDevToolsService getDevTools() {
        return DevToolsService.getDevToolsService(driver);
    }

    protected Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected Path getScreenshotPath() {
        File reportDirectory = new File("screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";
        return Paths.get(filePath);
    }
}

