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

package io.github.selcukes.reports.screenshot;

import com.google.common.collect.ImmutableMap;
import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.ImageUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract class NativeSnapshot {
    private final static String PNG = "png";
    private final WebDriver driver;
    protected boolean isAddressBar = false;
    protected String screenshotText;

    public NativeSnapshot(WebDriver driver) {
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

    private String getScreenshotText() {
        String pageUrl = "Current Page Url: " + getCurrentUrl();
        if (screenshotText == null)
            return pageUrl;
        return pageUrl + "\n" + screenshotText;
    }

    protected byte[] fullPageNativeScreenshotAsBytes() {
        BufferedImage screenshotImage = getBufferedImage();
        BufferedImage textImage = ImageUtil.generateImageWithLogo(getScreenshotText(), screenshotImage);
        BufferedImage addressBarImage = ImageUtil.generateImageWithAddressBar(captureAddressBar(textImage.getWidth()), textImage);
        return (isAddressBar) ? ImageUtil.toByteArray(addressBarImage) : ImageUtil.toByteArray(textImage);
    }

    private BufferedImage getBufferedImage() {
        if (driver instanceof ChromeDriver)
            return nativeScreenshotForCH();
        else if (driver instanceof FirefoxDriver)
            return nativeScreenshotForFF();
        else
            return defaultFullPageScreenshot();
    }

    protected BufferedImage nativeScreenshotForCH() {
        defineCustomCommand("sendCommand", new CommandInfo("/session/:sessionId/chromium/send_command_and_get_result", HttpMethod.POST));
        Object metrics = sendEvaluate(
            "({" +
                "width: Math.max(window.innerWidth,document.body.scrollWidth,document.documentElement.scrollWidth)|0," +
                "height: Math.max(window.innerHeight,document.body.scrollHeight,document.documentElement.scrollHeight)|0," +
                "deviceScaleFactor: window.devicePixelRatio || 1," +
                "mobile: typeof window.orientation !== 'undefined'" +
                "})");
        sendCommand("Emulation.setDeviceMetricsOverride", metrics);
        Await.until(TimeUnit.MILLISECONDS, 500);
        Object result = sendCommand("Page.captureScreenshot", ImmutableMap.of("format", PNG, "fromSurface", true));
        sendCommand("Emulation.clearDeviceMetricsOverride", ImmutableMap.of());
        String base64EncodedPng = (String) ((Map<String, ?>) result).get("data");
        return ImageUtil.toBufferedImage(OutputType.BYTES.convertFromBase64Png(base64EncodedPng));
    }

    private Object sendCommand(String cmd, Object params) {
        try {
            Method execute = RemoteWebDriver.class.getDeclaredMethod("execute", String.class, Map.class);
            execute.setAccessible(true);
            Response res = (Response) execute.invoke(driver, "sendCommand", ImmutableMap.of("cmd", cmd, "params", params));
            return res.getValue();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage captureAddressBar(int width) {
        Rectangle screenRectangle = new Rectangle(0, 0, width, 70);
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(screenRectangle);
        } catch (AWTException e) {
            throw new SnapshotException("Failed capturing Address bar..");
        }
    }

    protected Object sendEvaluate(String script) {
        Object response = sendCommand("Runtime.evaluate", ImmutableMap.of("returnByValue", true, "expression", script));
        Object result = ((Map<String, ?>) response).get("result");
        return ((Map<String, ?>) result).get("value");
    }

    protected Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected Path getScreenshotPath() {
        File reportDirectory = new File("target/screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + "." + PNG;
        return Paths.get(filePath);
    }
}

