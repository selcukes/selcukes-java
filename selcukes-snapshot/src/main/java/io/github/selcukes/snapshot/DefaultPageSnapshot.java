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

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.ImageUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class DefaultPageSnapshot {
    protected String screenshotText;
    protected boolean isAddressBar = false;
    private final WebDriver driver;
    protected Map<String, Object> screenOptions;

    public DefaultPageSnapshot(WebDriver driver) {
        this.driver = driver;
    }

    protected <X> X getDefaultPageSnapshot(OutputType<X> outputType) {
        screenOptions = getScreenOptions();
        BufferedImage defaultPageScreenshot = defaultPageScreenshot();
        return outputType.convertFromPngBytes(ImageUtil.toByteArray(defaultPageScreenshot));
    }

    @SuppressWarnings("unchecked")
    protected <T> T executeJS(String script, Object... args) {
        return (T) ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected Map<String, Object> getScreenOptions() {
        String js = "return " + FileHelper.readContent("screen-size.js");
        return executeJS(js);
    }

    protected int getFullHeight() {
        Long fullHeight = (Long) screenOptions.get("fullHeight");
        return fullHeight.intValue();
    }

    protected boolean isNotViewport() {
        return (boolean) screenOptions.get("exceedViewport");
    }

    protected int getFullWidth() {
        Long fullWidth = (Long) screenOptions.get("fullWidth");
        return fullWidth.intValue();
    }

    protected int getWindowHeight() {
        Long windowHeight = (Long) screenOptions.get("viewHeight");
        return windowHeight.intValue();
    }

    protected void waitForScrolling() {
        Await.until(TimeUnit.MILLISECONDS, 1);
    }

    protected byte[] takeScreenshot() {
        return takeScreenshot(OutputType.BYTES);
    }

    protected <X> X takeScreenshot(OutputType<X> outputType) {
        return ((TakesScreenshot) driver).getScreenshotAs(outputType);
    }

    private String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getScreenshotText() {
        String pageUrl = "Current Page Url: " + getCurrentUrl();
        if (screenshotText == null)
            return pageUrl;
        return pageUrl + "\n" + screenshotText;
    }

    private BufferedImage defaultPageScreenshot() {
        int allH = getFullHeight();
        int allW = getFullWidth();
        int winH = getWindowHeight();
        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;
        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();
        for (int n = 0; n < scrollTimes; n++) {
            executeJS("scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = ImageUtil.toBufferedImage(takeScreenshot());
            graphics.drawImage(part, 0, n * winH, null);
        }
        if (tail > 0) {
            executeJS("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = ImageUtil.toBufferedImage(takeScreenshot());
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();
        return finalImage;
    }

    public BufferedImage captureAddressBar(int width) {
        Rectangle screenRectangle = new Rectangle(0, 0, width, 70);
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(screenRectangle);
        } catch (AWTException e) {
            throw new SnapshotException("Failed capturing Browser address bar...", e);
        }
    }

}
