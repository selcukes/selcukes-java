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

package io.github.selcukes.core.listener;

import io.github.selcukes.core.page.ui.PageElement;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@CustomLog
public class EventCapture implements WebDriverListener {

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        logger.info(() -> String.format("Page title is [%s]", result));
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        logger.info(() -> String.format("Opening URL [%s]", url));
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        logger.info(() -> String.format("Navigating to [%s]", url));
    }

    @Override
    public void beforeClick(WebElement element) {
        logger.info(() -> "Clicking on " + PageElement.labelName(element));
    }

    @Override
    public void beforeClear(WebElement element) {
        logger.info(() -> "Clearing " + PageElement.labelName(element));
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        if (keysToSend == null) {
            return;
        }
        String text = Arrays.stream(keysToSend)
                .filter(Objects::nonNull)
                .map(key -> PageElement.toLogMessage(element, key))
                .collect(Collectors.joining("\n And "));
        logger.info(() -> text);
    }

    @Override
    public void afterQuit(WebDriver driver) {
        logger.info(() -> "Driver closed");
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
        logger.info(() -> "Screen Refreshed");
    }

    @Override
    public void afterMaximize(WebDriver.Window window) {
        logger.info(() -> "Screen Maximized");
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
        logger.info(() -> "Switched to window " + result);
    }
}
