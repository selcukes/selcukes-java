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

import io.github.selcukes.commons.helper.ImageUtil;
import io.github.selcukes.commons.os.Platform;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Map;

class PageSnapshot extends DefaultPageSnapshot {
    private WebDriver driver;

    public PageSnapshot(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public <X> X getFullScreenshotAs(OutputType<X> outputType) {
        unwrapDriver();
        if (driver instanceof HasCdp) {
            return getFullScreenshot(outputType);
        } else if (driver instanceof FirefoxDriver) {
            return ((FirefoxDriver) driver).getFullPageScreenshotAs(outputType);
        } else {
            return getDefaultPageSnapshot(outputType);
        }

    }

    public <X> X getScreenshotWithText(OutputType<X> outputType) {
        var screenshotImage = ImageUtil.toBufferedImage(getFullScreenshotAs(OutputType.BYTES));
        var textImage = ImageUtil.generateImageWithLogo(getScreenshotText(), screenshotImage);
        var finalImage = (!Platform.isLinux() && isAddressBar)
                ? ImageUtil.generateImageWithAddressBar(captureAddressBar(textImage.getWidth()), textImage)
                : textImage;

        return outputType.convertFromPngBytes(ImageUtil.toByteArray(finalImage));
    }

    private <X> X getFullScreenshot(OutputType<X> outputType) {
        screenOptions = getScreenOptions();
        var screenViewOptions = Map.of(
            "clip", Map.of(
                "x", 0,
                "y", 0,
                "width", getFullWidth(),
                "height", getFullHeight(),
                "scale", 1),
            "captureBeyondViewport", isNotViewport());
        var result = ((HasCdp) driver).executeCdpCommand("Page.captureScreenshot", screenViewOptions);
        var base64EncodedPng = (String) result.get("data");
        return outputType.convertFromBase64Png(base64EncodedPng);
    }

    private void unwrapDriver() {
        WebDriver webDriver = driver;
        if (driver instanceof WrapsDriver) {
            driver = ((WrapsDriver) webDriver).getWrappedDriver();
        }
        if (driver instanceof RemoteWebDriver) {
            driver = new Augmenter().augment(driver);
        }
    }
}
