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

import io.github.selcukes.core.page.ui.Locator;
import lombok.CustomLog;
import lombok.Setter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.util.Arrays;
import java.util.Optional;

@CustomLog
public class EventCapture implements WebDriverListener {
    @Setter
    public static String fieldAttribute = "placeholder";

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        logger.info(() -> String.format("Page title is [%s]", result));
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        logger.info(() -> String.format("Opening URL[%s]", url));
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        logger.info(() -> String.format("Navigating to [%s]", url));
    }

    @Override
    public void beforeClick(WebElement element) {
        String elementName = element.getText();
        logger.info(() -> "Clicking on " + (elementName.isBlank() ? Locator.of(element) : elementName));
    }

    @Override
    public void beforeClear(WebElement element) {
        logger.info(() -> "Clearing " + element.getAttribute(fieldAttribute));
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        logger.debug(() -> "Entering Text using locator " + Locator.of(element));
        if (keysToSend != null) {
            Optional<CharSequence> keyChar = Arrays.stream(keysToSend).filter(Keys.class::isInstance).findFirst();

            if (keyChar.isPresent()) {
                Arrays.stream(Keys.values()).filter(key -> key.equals(keyChar.get()))
                        .findFirst().ifPresent(key -> logger.info(() -> key.name() + " Key Pressed"));
            } else {
                logger.info(() -> (String.format("Entering Text %s in %s Field", Arrays.toString(keysToSend),
                    element.getAttribute(fieldAttribute))));
            }
        }
    }

    @Override
    public void afterQuit(WebDriver driver) {
        logger.info(() -> "Browser closed");
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
        logger.info(() -> "Browser Refreshed");
    }

    @Override
    public void afterMaximize(WebDriver.Window window) {
        logger.info(() -> "Browser Maximized");
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
        logger.info(() -> "Switched to window " + result);
    }
}
