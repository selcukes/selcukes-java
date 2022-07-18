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
import io.github.selcukes.core.page.ui.DropDown;
import io.github.selcukes.core.validation.PageValidations;
import io.github.selcukes.core.wait.WaitCondition;
import io.github.selcukes.core.wait.WaitManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.selcukes.core.page.ui.Locator.resolve;
import static io.github.selcukes.core.wait.WaitCondition.CLICKABLE;
import static io.github.selcukes.core.wait.WaitCondition.VISIBLE;
import static org.openqa.selenium.Keys.ESCAPE;
import static org.openqa.selenium.WindowType.TAB;
import static org.openqa.selenium.WindowType.WINDOW;

/**
 * The interface Page.
 */
public interface Page {
    /**
     * The constant TIMEOUT.
     */
    int TIMEOUT = 2;

    /**
     * Gets driver.
     *
     * @return the driver
     */
    WebDriver getDriver();

    /**
     * Open page.
     *
     * @param url the url
     * @return the page
     */
    default Page open(String url) {
        getDriver().get(url);
        return this;
    }

    /**
     * Navigate to page.
     *
     * @param url the url
     * @return the page
     */
    default Page navigateTo(String url) {
        getDriver().navigate().to(url);
        return this;
    }

    /**
     * Maximize page.
     *
     * @return the page
     */
    default Page maximize() {
        getDriver().manage().window().maximize();
        return this;
    }

    /**
     * Implicitly wait page.
     *
     * @return the page
     */
    default Page implicitlyWait() {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        return this;
    }

    /**
     * Title string.
     *
     * @return the string
     */
    default String title() {
        return getDriver().getTitle();
    }

    /**
     * Current url string.
     *
     * @return the string
     */
    default String currentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Text string.
     *
     * @param locator the locator
     * @return the string
     */
    default String text(Object locator) {
        return find(locator).getText();
    }

    /**
     * Enter page.
     *
     * @param locator the locator
     * @param text    the text
     * @return the page
     */
    default Page enter(Object locator, CharSequence text) {
        find(locator, VISIBLE).sendKeys(text);
        return this;
    }

    /**
     * Clear page.
     *
     * @param locator the locator
     * @return the page
     */
    default Page clear(Object locator) {
        find(locator, VISIBLE).clear();
        return this;
    }

    /**
     * Click page.
     *
     * @param locator the locator
     * @return the page
     */
    default Page click(Object locator) {
        click(locator, CLICKABLE);
        return this;
    }

    /**
     * Selects an option of an element.
     *
     * @param locator  the locator
     * @param consumer the consumer
     * @return the page
     */
    default Page select(Object locator, Consumer<Select> consumer) {
        consumer.accept(select(locator));
        return this;
    }

    /**
     * Selects an option of an element.
     *
     * @param locator       the locator
     * @param optionLocator the option locator to be "index:indexOfOption" or "value:OptionValue" or "label:visibleText"
     * @return the page
     */
    default Page select(Object locator, String optionLocator) {
        DropDown.select(select(locator), optionLocator);
        return this;
    }

    /**
     * Select select.
     *
     * @param locator the locator
     * @return the select
     */
    default Select select(Object locator) {
        return new Select(find(locator));
    }

    /**
     * Click page.
     *
     * @param locator   the locator
     * @param condition the condition
     * @return the page
     */
    default Page click(Object locator, final WaitCondition condition) {
        find(locator, condition).click();
        return this;
    }

    /**
     * Double click page.
     *
     * @param locator the locator
     * @return the page
     */
    default Page doubleClick(Object locator) {
        actions().doubleClick(find(locator)).perform();
        return this;

    }

    /**
     * Select menu page.
     *
     * @param menu    the menu
     * @param subMenu the sub menu
     * @return the page
     */
    default Page selectMenu(String menu, String subMenu) {
        return this;
    }

    /**
     * Select menu page.
     *
     * @param menu    the menu
     * @param subMenu the sub menu
     * @return the page
     */
    default Page selectMenu(By menu, By subMenu) {
        WebElement menuOption = find(menu);
        actions().moveToElement(menuOption).perform();
        click(subMenu);
        return this;
    }

    /**
     * Swipe page.
     *
     * @param target         the target
     * @param swipeDirection the swipe direction
     * @return the page
     */
    default Page swipe(Object target, SwipeDirection swipeDirection) {
        return this;
    }

    /**
     * Swipe page.
     *
     * @param source         the source
     * @param target         the target
     * @param swipeDirection the swipe direction
     * @return the page
     */
    default Page swipe(Object source, Object target, SwipeDirection swipeDirection) {
        return this;
    }

    /**
     * Actions actions.
     *
     * @return the actions
     */
    default Actions actions() {
        return new Actions(getDriver());
    }

    /**
     * Drag and drop page.
     *
     * @param source the source
     * @param target the target
     * @return the page
     */
    default Page dragAndDrop(WebElement source, WebElement target) {
        actions().dragAndDrop(source, target).perform();
        return this;
    }

    /**
     * Drag and drop page.
     *
     * @param source the source
     * @param target the target
     * @return the page
     */
    default Page dragAndDrop(By source, By target) {
        dragAndDrop(find(source, VISIBLE), find(target, VISIBLE));
        return this;
    }

    /**
     * Alert alert.
     *
     * @return the alert
     */
    default Alert alert() {
        waitFor(ExpectedConditions.alertIsPresent(), TIMEOUT);
        return getDriver().switchTo().alert();
    }

    /**
     * Accept alert page.
     *
     * @return the page
     */
    default Page acceptAlert() {
        alert().accept();
        return this;
    }

    /**
     * Dismiss alert page.
     *
     * @return the page
     */
    default Page dismissAlert() {
        alert().dismiss();
        return this;
    }

    /**
     * Gets alert text.
     *
     * @return the alert text
     */
    default String getAlertText() {
        return alert().getText();
    }

    /**
     * Enter text in alert page.
     *
     * @param text the text
     * @return the page
     */
    default Page enterTextInAlert(String text) {
        alert().sendKeys(text);
        return this;
    }

    /**
     * Gets windows.
     *
     * @return the windows
     */
    default List<String> getWindows() {
        return new ArrayList<>(getDriver().getWindowHandles());
    }

    /**
     * Parent window page.
     *
     * @return the page
     */
    default Page parentWindow() {
        getDriver().switchTo().defaultContent();
        return this;
    }

    /**
     * Switch window page.
     *
     * @param index the index
     * @return the page
     */
    default Page switchToWindow(int index) {
        getDriver().switchTo().window(getWindows().get(index));
        return this;
    }

    /**
     * Switch to window page.
     *
     * @param nameOrHandle the name or handle
     * @return the page
     */
    default Page switchToWindow(String nameOrHandle) {
        getDriver().switchTo().window(nameOrHandle);
        return this;
    }

    /**
     * Open new window.
     */
    default void openNewWindow() {
        getDriver().switchTo().newWindow(WINDOW);
    }

    /**
     * Open new tab.
     */
    default void openNewTab() {
        getDriver().switchTo().newWindow(TAB);
    }

    /**
     * Switch frame page.
     *
     * @param name the name
     * @return the page
     */
    default Page switchToFrame(String name) {
        getDriver().switchTo().frame(name);
        return this;
    }

    /**
     * Parent frame page.
     *
     * @return the page
     */
    default Page parentFrame() {
        getDriver().switchTo().parentFrame();
        return this;
    }

    /**
     * Back page.
     *
     * @return the page
     */
    default Page back() {
        getDriver().navigate().back();
        return this;
    }

    /**
     * Forward page.
     *
     * @return the page
     */
    default Page forward() {
        getDriver().navigate().forward();
        return this;
    }

    /**
     * Refresh page.
     *
     * @return the page
     */
    default Page refresh() {
        getDriver().navigate().refresh();
        return this;
    }

    /**
     * Screenshot as x.
     *
     * @param <X>    the type parameter
     * @param target the target
     * @return the x
     */
    default <X> X screenshotAs(OutputType<X> target) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(target);
    }

    /**
     * Find web element.
     *
     * @param locator the locator
     * @return the web element
     */
    default WebElement find(Object locator) {
        return getDriver().findElement(resolve(locator));
    }

    /**
     * Find all list.
     *
     * @param locator the locator
     * @return the list
     */
    default List<WebElement> findAll(Object locator) {
        return getDriver().findElements(resolve(locator));
    }

    /**
     * Find web element.
     *
     * @param locator   the locator
     * @param condition the condition
     * @return the web element
     */
    default WebElement find(Object locator, final WaitCondition condition) {
        return waitFor(resolve(locator), "", condition);
    }

    /**
     * Find all list.
     *
     * @param locator   the locator
     * @param condition the condition
     * @return the list
     */
    default List<WebElement> findAll(Object locator, final WaitCondition condition) {
        return waitFor(resolve(locator), "", condition);
    }

    /**
     * Find all children list.
     *
     * @param parent the parent
     * @param child  the child
     * @return the list
     */
    default List<WebElement> findAllChildren(Object parent, Object child) {
        return find(parent, VISIBLE).findElements(resolve(child));
    }

    /**
     * Find child web element.
     *
     * @param parent the parent
     * @param child  the child
     * @return the web element
     */
    default WebElement findChild(Object parent, Object child) {
        return find(parent, VISIBLE).findElement(resolve(child));
    }

    /**
     * Find child web element.
     *
     * @param parent the parent
     * @param child  the child
     * @return the web element
     */
    default WebElement findChild(WebElement parent, Object child) {
        return parent.findElement(resolve(child));
    }

    /**
     * Find shadow child web element.
     *
     * @param parent the parent
     * @param child  the child
     * @return the web element
     */
    default WebElement findShadowChild(WebElement parent, Object child) {
        return parent.getShadowRoot().findElement(resolve(child));
    }

    /**
     * Find shadow child web element.
     *
     * @param parent the parent
     * @param child  the child
     * @return the web element
     */
    default WebElement findShadowChild(By parent, By child) {
        return find(parent, VISIBLE).getShadowRoot().findElement(child);
    }

    /**
     * Execute script object.
     *
     * @param script the script
     * @param args   the args
     * @return the object
     */
    default Object executeScript(String script, Object... args) {
        JavascriptExecutor exe = (JavascriptExecutor) getDriver();
        return exe.executeScript(script, args);
    }

    /**
     * Wait for page loading page.
     *
     * @return the page
     */
    default Page waitForPageLoading() {
        new WaitManager(this);
        return this;
    }

    /**
     * Wait for page.
     *
     * @param condition      the condition
     * @param timeoutSeconds the timeout seconds
     * @return the page
     */
    default Page waitFor(ExpectedCondition<?> condition, int timeoutSeconds) {
        waiter(timeoutSeconds).until(condition);
        return this;
    }

    /**
     * Waiter web driver wait.
     *
     * @param timeoutSeconds the timeout seconds
     * @return the web driver wait
     */
    default WebDriverWait waiter(int timeoutSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waiter web driver wait.
     *
     * @return the web driver wait
     */
    default WebDriverWait waiter() {
        return waiter(TIMEOUT);
    }

    /**
     * Assert that page validations.
     *
     * @return the page validations
     */
    default PageValidations assertThat() {
        return new PageValidations(this, false);
    }

    /**
     * Verify that page validations.
     *
     * @return the page validations
     */
    default PageValidations verifyThat() {
        return new PageValidations(this, true);
    }

    /**
     * Wait for r.
     *
     * @param <T>       the type parameter
     * @param <V>       the type parameter
     * @param <R>       the type parameter
     * @param locator   the locator
     * @param arg       the arg
     * @param condition the condition
     * @return the r
     */
    @SuppressWarnings("unchecked")
    default <T, V, R> R waitFor(final T locator, final V arg, final WaitCondition condition) {
        return (R) waiter()
            .pollingEvery(Duration.ofMillis(100))
            .ignoreAll(List.of(StaleElementReferenceException.class, NoSuchElementException.class))
            .until((Function<WebDriver, ?>) condition.getType().apply(locator, arg));
    }

    /**
     * Request web client.
     *
     * @param url the url
     * @return the web client
     */
    default WebClient request(String url) {
        return new WebClient(url);
    }

    private List<String> getValues(Object locator, Function<WebElement, String> function) {
        return findAll(locator).stream().map(function).collect(Collectors.toList());
    }

    /**
     * Text values list.
     *
     * @param locator the locator
     * @return the list
     */
    default List<String> textValues(Object locator) {
        return getValues(locator, WebElement::getText);
    }

    /**
     * Attribute values list.
     *
     * @param locator the locator
     * @param name    the name
     * @return the list
     */
    default List<String> attributeValues(Object locator, String name) {
        return getValues(locator, e -> e.getAttribute(name));
    }

    /**
     * Active element web element.
     *
     * @return the web element
     */
    default WebElement activeElement() {
        return getDriver().switchTo().activeElement();
    }

    /**
     * Remove focus.
     */
    default void removeFocus() {
        activeElement().sendKeys(ESCAPE);
    }

    /**
     * Gets color of an element.
     *
     * @param element the element
     * @return the color
     */
    default String getColor(WebElement element) {
        String color = element.getCssValue("color");
        return Color.fromString(color).asHex();
    }

    /**
     * Delete all cookies.
     */
    default void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    /**
     * Delete cookie.
     *
     * @param name the name
     */
    default void deleteCookie(String name) {
        getDriver().manage().deleteCookieNamed(name);
    }

    /**
     * Get all Cookies.
     *
     * @return the list of cookies
     */
    default List<String> cookies() {
        return getDriver().manage().getCookies()
            .stream()
            .map(Cookie::getName)
            .collect(Collectors.toList());
    }

    /**
     * Get Cookie.
     *
     * @param name the name
     * @return the cookie
     */
    default Cookie cookie(String name) {
        return getDriver().manage().getCookieNamed(name);
    }
}
