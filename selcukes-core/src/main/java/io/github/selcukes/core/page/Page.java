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

package io.github.selcukes.core.page;

import io.github.selcukes.core.wait.WaitCondition;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface Page {
    int TIMEOUT = 2;

    WebDriver getDriver();

    default Page open(String url) {
        getDriver().get(url);
        return this;
    }

    default Page navigateTo(String url) {
        getDriver().navigate().to(url);
        return this;
    }

    default String title() {
        return getDriver().getTitle();
    }

    default String read(By by) {
        return find(by).getText();
    }

    default Page enter(By by, CharSequence text) {
        find(by, WaitCondition.VISIBLE).sendKeys(text);
        return this;
    }

    default Page clear(By by) {
        find(by, WaitCondition.VISIBLE).clear();
        return this;
    }

    default Page click(By by) {
        click(by, WaitCondition.CLICKABLE);
        return this;
    }

    default Page selectText(By by, String text) {
        select(by).selectByVisibleText(text);
        return this;
    }

    default Page selectValue(By by, String value) {
        select(by).selectByValue(value);
        return this;
    }

    default Select select(By by) {
        return new Select(find(by));
    }

    default Page click(By by, final WaitCondition condition) {
        find(by, condition).click();
        return this;
    }

    default Page doubleClick(By by) {
        actions().doubleClick(find(by)).perform();
        return this;

    }

    default Page selectMenu(String menu, String subMenu) {
        return this;
    }

    default Page selectMenu(By menu, By subMenu) {
        WebElement menuOption = find(menu);
        actions().moveToElement(menuOption).perform();
        return this;
    }

    default Actions actions() {
        return new Actions(getDriver());
    }

    default TouchActions touchActions() {
        return new TouchActions(getDriver());
    }

    default Page tap(By by) {
        touchActions().singleTap(find(by)).perform();
        return this;
    }

    default Page doubleTap(By by) {
        touchActions().doubleTap(find(by)).perform();
        return this;
    }

    default Page swipe(By target) {
        Point elementLocation = find(target).getLocation();
        touchActions().scroll(elementLocation.getX(), elementLocation.getY()).perform();
        return this;
    }

    default List<String> getWindows() {
        return new ArrayList<>(getDriver().getWindowHandles());
    }

    default Page switchWindow(int index) {
        getDriver().switchTo().window(getWindows().get(index));
        return this;
    }

    default void openNewBrowserWindow() {
        getDriver().switchTo().newWindow(WindowType.WINDOW);
    }

    default void openNewBrowserTab() {
        getDriver().switchTo().newWindow(WindowType.TAB);
    }

    default Page back() {
        getDriver().navigate().back();
        return this;
    }

    default Page forward() {
        getDriver().navigate().forward();
        return this;
    }

    default Page refresh() {
        getDriver().navigate().refresh();
        return this;
    }

    default Page screenshot() {
        return this;
    }

    default WebElement find(By by) {
        return getDriver().findElement(by);
    }

    default List<WebElement> findAll(By by) {
        return getDriver().findElements(by);
    }

    default WebElement find(By by, final WaitCondition condition) {
        return waitFor(by, "", condition);
    }

    default List<WebElement> findAll(By by, final WaitCondition condition) {
        return waitFor(by, "", condition);
    }

    default Object executeScript(String script, Object... args) {
        JavascriptExecutor exe = (JavascriptExecutor) getDriver();
        return exe.executeScript(script, args);
    }

    default WebDriverWait getWait() {
        return getWait(TIMEOUT);
    }

    default WebDriverWait getWait(int seconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
    }

    @SuppressWarnings("unchecked")
    default <T, V, R> R waitFor(final T locator, final V arg, final WaitCondition condition) {
        return (R) getWait()
            .pollingEvery(Duration.ofMillis(100))
            .ignoreAll(List.of(StaleElementReferenceException.class, NoSuchElementException.class))
            .until((Function<WebDriver, ?>) condition.getType().apply(locator, arg));
    }

}
