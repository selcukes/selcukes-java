/*
 *
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
 *
 */

package io.github.selcukes.reports.listeners;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverEventHandler implements WebDriverEventListener {

    private static final Logger logger = Logger.getLogger(DriverEventHandler.class.getName());

    private static String getLocatorFromElement(WebElement element) {
        String str = element.toString();
        Pattern p = Pattern.compile("->\\s(.*)(?=\\])");
        Matcher m = p.matcher(str);
        return m.find() && m.groupCount() > 0
                ? m.group(1)
                : str;
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysSent) {
        logger.warning("Changed value of element with " + getLocatorFromElement(element));
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        logger.warning("Clicked on element with " + getLocatorFromElement(element));
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {

        logger.warning("Element found using [" + by + "]");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        logger.warning("Navigated backward...");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        logger.warning("Navigated forward...");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {
        logger.warning("Refreshing the page...");
    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {
        logger.warning("Page refreshed...");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        logger.warning("Navigated to url [" + url + "]");
    }

    @Override
    public void afterScript(String script, WebDriver driver) {

        logger.warning("ran script " + StringUtils.abbreviate(script, 128));
    }

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver) {
        logger.warning("Switching to window [" + windowName + "]");
    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver) {
        logger.warning("after switch to window " + windowName);
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        logger.warning("Change value of element with " + getLocatorFromElement(element));
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        logger.warning("click element with " + getLocatorFromElement(element));
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        logger.warning("Finding element by [" + by + "]");
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        logger.warning("Navigating back...");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        logger.warning("Navigating forward...");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        logger.warning("Navigating to [" + url + "]");
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {

        logger.warning("Executing script [" + StringUtils.abbreviate(script, 512) + "]");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {

        logger.severe("Exception occurred: " + throwable.getMessage());
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {
        logger.warning("Capturing screenshot...");
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {
        logger.warning("Successfully captured screenshot...");
    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {
        logger.warning("Getting text from element...");
    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String text) {
        logger.warning("Got text [" + text + "] from element...");
    }

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {
        logger.warning("Accepting Alert pop-up...");
    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {
        logger.warning("Alert dialog accepted...");
    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {
        logger.warning("Dismissing Alert pop-up...");
    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {
        logger.warning("Alert dialog dismissed...");
    }
}
