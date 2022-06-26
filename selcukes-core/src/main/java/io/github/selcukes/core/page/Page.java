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

import io.github.selcukes.commons.http.WebClient;
import io.github.selcukes.core.enums.SwipeDirection;
import io.github.selcukes.core.page.ui.Locator;
import io.github.selcukes.core.validation.PageValidations;
import io.github.selcukes.core.wait.WaitCondition;
import io.github.selcukes.core.wait.WaitManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    default Page maximize() {
        getDriver().manage().window().maximize();
        return this;
    }

    default Page implicitlyWait() {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        return this;
    }

    default String title() {
        return getDriver().getTitle();
    }

    default String currentUrl() {
        return getDriver().getCurrentUrl();
    }

    default String text(Object locator) {
        return find(locator).getText();
    }

    default Page enter(Object locator, CharSequence text) {
        find(locator, WaitCondition.VISIBLE).sendKeys(text);
        return this;
    }

    default Page clear(Object locator) {
        find(locator, WaitCondition.VISIBLE).clear();
        return this;
    }

    default Page click(Object locator) {
        click(locator, WaitCondition.CLICKABLE);
        return this;
    }

    default Page selectText(Object locator, String text) {
        select(locator).selectByVisibleText(text);
        return this;
    }

    default Page selectValue(Object locator, String value) {
        select(locator).selectByValue(value);
        return this;
    }

    default Select select(Object locator) {
        return new Select(find(locator));
    }

    default Page click(Object locator, final WaitCondition condition) {
        find(locator, condition).click();
        return this;
    }

    default Page doubleClick(Object locator) {
        actions().doubleClick(find(locator)).perform();
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

    default Page swipe(Object target, SwipeDirection swipeDirection) {
        return this;
    }

    default Page swipe(Object source, Object target, SwipeDirection swipeDirection) {
        return this;
    }

    default Actions actions() {
        return new Actions(getDriver());
    }

    default Page dragAndDrop(WebElement source, WebElement target) {
        actions().dragAndDrop(source, target).perform();
        return this;
    }

    default Page dragAndDrop(By source, By target) {
        dragAndDrop(find(source, WaitCondition.VISIBLE), find(target, WaitCondition.VISIBLE));
        return this;
    }

    default Alert alert() {
        waitFor(ExpectedConditions.alertIsPresent(), TIMEOUT);
        return getDriver().switchTo().alert();
    }

    default Page acceptAlert() {
        alert().accept();
        return this;
    }

    default Page dismissAlert() {
        alert().dismiss();
        return this;
    }

    default String getAlertText() {
        return alert().getText();
    }

    default Page enterTextInAlert(String text) {
        alert().sendKeys(text);
        return this;
    }

    default List<String> getWindows() {
        return new ArrayList<>(getDriver().getWindowHandles());
    }

    default Page parentWindow() {
        getDriver().switchTo().defaultContent();
        return this;
    }

    default Page switchWindow(int index) {
        getDriver().switchTo().window(getWindows().get(index));
        return this;
    }

    default void openNewWindow() {
        getDriver().switchTo().newWindow(WindowType.WINDOW);
    }

    default void openNewTab() {
        getDriver().switchTo().newWindow(WindowType.TAB);
    }

    default Page switchFrame(String name) {
        getDriver().switchTo().frame(name);
        return this;
    }

    default Page parentFrame() {
        getDriver().switchTo().parentFrame();
        return this;
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

    default <X> X screenshotAs(OutputType<X> target) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(target);
    }

    default WebElement find(Object locator) {
        return getDriver().findElement(Locator.resolve(locator));
    }

    default List<WebElement> findAll(Object locator) {
        return getDriver().findElements(Locator.resolve(locator));
    }

    default WebElement find(Object locator, final WaitCondition condition) {
        return waitFor(Locator.resolve(locator), "", condition);
    }

    default List<WebElement> findAll(Object locator, final WaitCondition condition) {
        return waitFor(Locator.resolve(locator), "", condition);
    }

    default List<WebElement> findAllChildren(By parent, By child) {
        return find(parent, WaitCondition.VISIBLE).findElements(child);
    }

    default WebElement findChild(By parent, By child) {
        return find(parent, WaitCondition.VISIBLE).findElement(child);
    }

    default WebElement findChild(WebElement parent, By child) {
        return parent.findElement(child);
    }

    default WebElement findShadowChild(WebElement parent, By child) {
        return parent.getShadowRoot().findElement(child);
    }

    default WebElement findShadowChild(By parent, By child) {
        return find(parent, WaitCondition.VISIBLE).getShadowRoot().findElement(child);
    }

    default Object executeScript(String script, Object... args) {
        JavascriptExecutor exe = (JavascriptExecutor) getDriver();
        return exe.executeScript(script, args);
    }

    default Page waitForPageLoading() {
        new WaitManager(this);
        return this;
    }

    default Page waitFor(ExpectedCondition<?> condition, int timeoutSeconds) {
        waiter(timeoutSeconds).until(condition);
        return this;
    }

    default WebDriverWait waiter(int timeoutSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    default WebDriverWait waiter() {
        return waiter(TIMEOUT);
    }

    default PageValidations assertThat() {
        return new PageValidations(this, false);
    }

    default PageValidations verifyThat() {
        return new PageValidations(this, true);
    }

    @SuppressWarnings("unchecked")
    default <T, V, R> R waitFor(final T locator, final V arg, final WaitCondition condition) {
        return (R) waiter()
            .pollingEvery(Duration.ofMillis(100))
            .ignoreAll(List.of(StaleElementReferenceException.class, NoSuchElementException.class))
            .until((Function<WebDriver, ?>) condition.getType().apply(locator, arg));
    }

    default WebClient request(String url) {
        return new WebClient(url);
    }

}
