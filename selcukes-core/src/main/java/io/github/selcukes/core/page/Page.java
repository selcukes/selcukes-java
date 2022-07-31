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
import io.github.selcukes.core.page.ui.Dropdown;
import io.github.selcukes.core.validation.PageValidations;
import io.github.selcukes.core.wait.WaitCondition;
import io.github.selcukes.core.wait.WaitManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    // Declaring a constant variable called TIMEOUT and assigning it the value
    // 2.
    int TIMEOUT = 2;

    /**
     * This function returns a WebDriver object.
     *
     * @return The driver object.
     */
    WebDriver getDriver();

    /**
     * Open the given URL in the browser.
     *
     * @param  url The URL to open.
     * @return     The page object itself.
     */
    default Page open(String url) {
        getDriver().get(url);
        return this;
    }

    /**
     * Navigate to the given URL.
     *
     * @param  url The URL to navigate to.
     * @return     The page object itself.
     */
    default Page navigateTo(String url) {
        getDriver().navigate().to(url);
        return this;
    }

    /**
     * Maximize the browser window.
     *
     * @return The current page.
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
     * Return the title of the current page.
     *
     * @return The title of the page
     */
    default String title() {
        return getDriver().getTitle();
    }

    /**
     * Return the current URL of the page.
     *
     * @return The current URL of the page.
     */
    default String currentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Return the text of the element located by the locator.
     *
     * @param  locator The locator of the element to be found.
     * @return         The text of the element.
     */
    default String text(Object locator) {
        return find(locator).getText();
    }

    /**
     * Enter text into a visible element.
     *
     * @param  locator The locator of the element to enter text into.
     * @param  text    The text to enter into the field.
     * @return         The page object itself.
     */
    default Page enter(Object locator, CharSequence text) {
        find(locator, VISIBLE).sendKeys(text);
        return this;
    }

    /**
     * Clear the text from the element identified by the locator.
     *
     * @param  locator The locator of the element to clear.
     * @return         The page object itself.
     */
    default Page clear(Object locator) {
        find(locator, VISIBLE).clear();
        return this;
    }

    /**
     * Click on the element that matches the locator, and wait until it is
     * clickable.
     *
     * @param  locator The locator of the element you want to click.
     * @return         The page object itself.
     */
    default Page click(Object locator) {
        click(locator, CLICKABLE);
        return this;
    }

    /**
     * Select the element with the given locator, and then perform the given
     * action on the selected element.
     *
     * @param  locator  The locator of the element to be selected.
     * @param  consumer A lambda expression that accepts a Select object and
     *                  returns void.
     * @return          The page object itself.
     */
    default Page select(Object locator, Consumer<Select> consumer) {
        consumer.accept(select(locator));
        return this;
    }

    /**
     * Selects an option from a dropdown list.
     *
     * @param  locator       The locator of the dropdown.
     * @param  optionLocator an option locator to be "index:indexOfOption" or
     *                       "value:OptionValue" or "label:visibleText"
     * @return               The page object itself.
     */
    default Page select(Object locator, String optionLocator) {
        Dropdown.select(select(locator), optionLocator);
        return this;
    }

    /**
     * Return a new Select object, which is a Selenium object that allows you to
     * select an option from a dropdown list.
     *
     * @param  locator The locator of the element to be selected.
     * @return         A Select object
     */
    default Select select(Object locator) {
        return new Select(find(locator));
    }

    /**
     * Click on the element that matches the locator, and wait until the
     * condition is met.
     *
     * @param  locator   This is the locator of the element you want to click.
     * @param  condition The condition to wait for before clicking the element.
     * @return           The page object itself.
     */
    default Page click(Object locator, final WaitCondition condition) {
        find(locator, condition).click();
        return this;
    }

    /**
     * Double-click on the element found by the locator.
     *
     * @param  locator The locator of the element to double-click.
     * @return         The page object itself.
     */
    default Page doubleClick(Object locator) {
        actions().doubleClick(find(locator)).perform();
        return this;

    }

    /**
     * Select a menu item from the menu bar.
     *
     * @param  menu    The name of the menu to select.
     * @param  subMenu The sub menu to select.
     * @return         The page object itself.
     */
    default Page selectMenu(String menu, String subMenu) {
        return this;
    }

    /**
     * Click on a menu option and then click on a submenu option.
     *
     * @param  menu    The menu option you want to hover over
     * @param  subMenu The submenu you want to click on.
     * @return         The page object itself.
     */
    default Page selectMenu(By menu, By subMenu) {
        WebElement menuOption = find(menu);
        actions().moveToElement(menuOption).perform();
        click(subMenu);
        return this;
    }

    /**
     * Swipe the target in the specified direction.
     *
     * @param  target         The target element to swipe on.
     * @param  swipeDirection The direction of the swipe.
     * @return                The page object itself.
     */
    default Page swipe(Object target, SwipeDirection swipeDirection) {
        return this;
    }

    /**
     * Swipe from source to target in the specified direction
     *
     * @param  source         The source element to swipe from.
     * @param  target         The target element to swipe on.
     * @param  swipeDirection The direction of the swipe.
     * @return                The page object itself.
     */
    default Page swipe(Object source, Object target, SwipeDirection swipeDirection) {
        return this;
    }

    /**
     * Return a new Actions object that is associated with the driver that was
     * used to create this page object.
     *
     * @return An instance of the Actions class.
     */
    default Actions actions() {
        return new Actions(getDriver());
    }

    /**
     * This function performs a drag and drop operation on the source element to
     * the target element.
     *
     * @param  source The element to be dragged.
     * @param  target The target element to drop the source element on.
     * @return        The page object itself.
     */
    default Page dragAndDrop(WebElement source, WebElement target) {
        actions().dragAndDrop(source, target).perform();
        return this;
    }

    /**
     * Drag and drop the element found by the source locator onto the element
     * found by the target locator
     *
     * @param  source The element you want to drag
     * @param  target The target element to drop the source element on.
     * @return        The page object itself.
     */
    default Page dragAndDrop(By source, By target) {
        dragAndDrop(find(source, VISIBLE), find(target, VISIBLE));
        return this;
    }

    /**
     * Wait for an alert to be present, then switch to it.
     *
     * @return The alert object
     */
    default Alert alert() {
        waitFor(ExpectedConditions.alertIsPresent(), TIMEOUT);
        return getDriver().switchTo().alert();
    }

    /**
     * Accept the alert and return the current page.
     *
     * @return The page object itself.
     */
    default Page acceptAlert() {
        alert().accept();
        return this;
    }

    /**
     * Dismiss the alert and return the page.
     *
     * @return The page object itself.
     */
    default Page dismissAlert() {
        alert().dismiss();
        return this;
    }

    /**
     * Get the text of the alert.
     *
     * @return The text of the alert.
     */
    default String getAlertText() {
        return alert().getText();
    }

    /**
     * Enter text in the alert.
     *
     * @param  text The text to enter into the alert.
     * @return      The page object itself.
     */
    default Page enterTextInAlert(String text) {
        alert().sendKeys(text);
        return this;
    }

    /**
     * Get a list of all the windows that are currently open.
     *
     * @return A list of strings
     */
    default List<String> getWindows() {
        return new ArrayList<>(getDriver().getWindowHandles());
    }

    /**
     * Switch to the parent window of the current window.
     *
     * @return The Page object itself.
     */
    default Page parentWindow() {
        getDriver().switchTo().defaultContent();
        return this;
    }

    /**
     * Switch to the window at the given index.
     *
     * @param  index The index of the window to switch to.
     * @return       The page object itself.
     */
    default Page switchToWindow(int index) {
        getDriver().switchTo().window(getWindows().get(index));
        return this;
    }

    /**
     * Switch to the window with the given name or handle.
     *
     * @param  nameOrHandle The name or window handle of the window to switch
     *                      focus to.
     * @return              The page object itself.
     */
    default Page switchToWindow(String nameOrHandle) {
        getDriver().switchTo().window(nameOrHandle);
        return this;
    }

    /**
     * Open a new window and switch to it.
     *
     * @return The page object itself.
     */
    default Page openNewWindow() {
        getDriver().switchTo().newWindow(WINDOW);
        return this;
    }

    /**
     * Open a new tab and switch to it.
     *
     * @return The current page.
     */
    default Page openNewTab() {
        getDriver().switchTo().newWindow(TAB);
        return this;
    }

    /**
     * Switch to the frame with the given name.
     *
     * @param  name The name of the frame to switch to.
     * @return      The page object itself.
     */
    default Page switchToFrame(String name) {
        getDriver().switchTo().frame(name);
        return this;
    }

    /**
     * Switch to the parent frame of the current frame.
     *
     * @return The current page.
     */
    default Page parentFrame() {
        getDriver().switchTo().parentFrame();
        return this;
    }

    /**
     * Navigate back to the previous page.
     *
     * @return The page object itself.
     */
    default Page back() {
        getDriver().navigate().back();
        return this;
    }

    /**
     * Navigate forward in the browser history, if possible.
     *
     * @return The current page.
     */
    default Page forward() {
        getDriver().navigate().forward();
        return this;
    }

    /**
     * Refresh the page.
     *
     * @return The page object itself.
     */
    default Page refresh() {
        getDriver().navigate().refresh();
        return this;
    }

    /**
     * Takes a screenshot of the current page and returns it as the specified
     * output type.
     *
     * @param  target The target type, @see OutputType
     * @return        The screenshot is being returned as a file.
     */
    default <X> X screenshotAs(OutputType<X> target) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(target);
    }

    /**
     * Find an element using the given locator.
     *
     * @param  locator The locator to find.
     * @return         A WebElement
     */
    default WebElement find(Object locator) {
        return getDriver().findElement(resolve(locator));
    }

    /**
     * Find all elements matching the given locator.
     *
     * @param  locator The locator to find the element(s)
     * @return         A list of WebElements
     */
    default List<WebElement> findAll(Object locator) {
        return getDriver().findElements(resolve(locator));
    }

    /**
     * Wait for the element to be present and visible, and then return it.
     *
     * @param  locator   The locator to find the element.
     * @param  condition The condition to wait for.
     * @return           WebElement
     */
    default WebElement find(Object locator, final WaitCondition condition) {
        return waitFor(resolve(locator), "", condition);
    }

    /**
     * Wait for the element to be found, then return it.
     *
     * @param  locator   The locator to find the element(s)
     * @param  condition The condition to wait for.
     * @return           A list of WebElements
     */
    default List<WebElement> findAll(Object locator, final WaitCondition condition) {
        return waitFor(resolve(locator), "", condition);
    }

    /**
     * "Find all the children of the parent element that match the child
     * locator."
     * <p>
     * The first line of the function is a JavaDoc comment. This is a special
     * comment that is used to document the function. It is not required, but it
     * is a good practice to document your functions
     *
     * @param  parent The parent element to search within.
     * @param  child  The child element to find.
     * @return        A list of WebElements
     */
    default List<WebElement> findAllChildren(Object parent, Object child) {
        return find(parent, VISIBLE).findElements(resolve(child));
    }

    /**
     * "Find the child element of the parent element, where the parent element
     * is visible."
     * <p>
     * The first line of the function is a comment. The second line is the
     * function declaration. The third line is the function body
     *
     * @param  parent The parent element to search within.
     * @param  child  The child element to find.
     * @return        A WebElement
     */
    default WebElement findChild(Object parent, Object child) {
        return find(parent, VISIBLE).findElement(resolve(child));
    }

    /**
     * "Find the child element of the parent element using the child locator."
     * <p>
     * The first line of the function is a JavaDoc comment. This is a special
     * comment that is used to document the function. The JavaDoc comment is
     * used by the JavaDoc tool to generate documentation for the function
     *
     * @param  parent The parent element to search within.
     * @param  child  The child element to find.
     * @return        A WebElement
     */
    default WebElement findChild(WebElement parent, Object child) {
        return parent.findElement(resolve(child));
    }

    /**
     * "Find a child element of a shadow root element."
     * <p>
     * The first parameter is the parent element, and the second parameter is
     * the child element. The second parameter can be a string, a By object, or
     * a WebElement
     *
     * @param  parent The parent element to search within.
     * @param  child  The child element to find. This can be a By object, a
     *                WebElement, or a String.
     * @return        A WebElement
     */
    default WebElement findShadowChild(WebElement parent, Object child) {
        return parent.getShadowRoot().findElement(resolve(child));
    }

    /**
     * Find the shadow child of a shadow parent.
     *
     * @param  parent The parent element to find.
     * @param  child  The child element to find.
     * @return        The WebElement that is found by the child locator.
     */
    default WebElement findShadowChild(By parent, By child) {
        return find(parent, VISIBLE).getShadowRoot().findElement(child);
    }

    /**
     * Execute a JavaScript script on the current page.
     *
     * @param  script The JavaScript to execute.
     * @return        The return value of the last executed statement.
     */
    @SuppressWarnings("unchecked")
    default <T> T executeScript(String script, Object... args) {
        var jsExecutor = (JavascriptExecutor) getDriver();
        return (T) jsExecutor.executeScript(script, args);
    }

    /**
     * Wait for the page to load.
     *
     * @return The page object itself.
     */
    default Page waitForPageLoading() {
        new WaitManager(this);
        return this;
    }

    /**
     * Wait for the condition to be true, or until the timeout is reached.
     *
     * @param  condition      The ExpectedCondition to wait for.
     * @param  timeoutSeconds The maximum amount of time to wait for the
     *                        condition to be true.
     * @return                The return type is T.
     */
    default <T> T waitFor(ExpectedCondition<T> condition, int timeoutSeconds) {
        return waiter(timeoutSeconds).until(condition);
    }

    /**
     * Create a WebDriverWait object with a timeout of the specified number of
     * seconds.
     *
     * @param  timeoutSeconds The number of seconds to wait for the condition to
     *                        be true.
     * @return                A WebDriverWait object
     */
    default WebDriverWait waiter(int timeoutSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * If the default timeout is not set, set it to 10 seconds.
     *
     * @return A WebDriverWait object.
     */
    default WebDriverWait waiter() {
        return waiter(TIMEOUT);
    }

    /**
     * If the page is not valid, throw an exception.
     *
     * @return A new instance of PageValidations
     */
    default PageValidations assertThat() {
        return new PageValidations(this, false);
    }

    /**
     * If the page is valid, return a new PageValidations object, otherwise
     * throw an exception.
     *
     * @return A new instance of the PageValidations class.
     */
    default PageValidations verifyThat() {
        return new PageValidations(this, true);
    }

    /**
     * It waits for a condition to be met.
     *
     * @param  locator   The locator to be used to find the element.
     * @param  arg       The argument to pass to the condition.
     * @param  condition The condition to wait for.
     * @return           The return type is R.
     */
    @SuppressWarnings("unchecked")
    default <T, V, R> R waitFor(final T locator, final V arg, final WaitCondition condition) {
        return (R) waiter()
                .pollingEvery(Duration.ofMillis(100))
                .ignoreAll(List.of(StaleElementReferenceException.class, NoSuchElementException.class))
                .until((Function<WebDriver, ?>) condition.getType().apply(locator, arg));
    }

    /**
     * Create a new WebClient object with the given url and return it.
     *
     * @param  url The URL to request.
     * @return     A new instance of the WebClient class.
     */
    default WebClient request(String url) {
        return new WebClient(url);
    }

    private List<String> getValues(Object locator, Function<WebElement, String> function) {
        return findAll(locator).stream().map(function).collect(Collectors.toList());
    }

    /**
     * "Get the text values of all elements matching the given locator."
     * <p>
     * The first line of the function is a Java 8 default method. It's a method
     * that can be overridden by subclasses, but doesn't have to be. The second
     * line is a Java 8 lambda expression. It's a function that can be passed as
     * an argument to another function
     *
     * @param  locator The locator of the elements to get the values from.
     * @return         A list of strings
     */
    default List<String> textValues(Object locator) {
        return getValues(locator, WebElement::getText);
    }

    /**
     * "Get the values of the attribute named 'name' for all elements matching
     * the locator."
     * <p>
     * The first line of the function is a JavaDoc comment. It's a good idea to
     * include a JavaDoc comment for every function you write. It's also a good
     * idea to include a one sentence summary of the function
     *
     * @param  locator The locator of the elements to get the attribute values
     *                 from.
     * @param  name    The name of the attribute.
     * @return         A list of strings.
     */
    default List<String> attributeValues(Object locator, String name) {
        return getValues(locator, e -> e.getAttribute(name));
    }

    /**
     * Returns the currently active element on the page.
     *
     * @return The active element on the page.
     */
    default WebElement activeElement() {
        return getDriver().switchTo().activeElement();
    }

    /**
     * Send the escape key to the active element.
     *
     * @return The page object itself.
     */
    default Page removeFocus() {
        activeElement().sendKeys(ESCAPE);
        return this;
    }

    /**
     * It takes a WebElement and returns its color as a hex string
     *
     * @param  element The WebElement to get the color of.
     * @return         The hex value of the color of the element.
     */
    default String getColor(WebElement element) {
        String color = element.getCssValue("color");
        return Color.fromString(color).asHex();
    }

    /**
     * Delete all cookies from the current page.
     *
     * @return The page object itself.
     */
    default Page deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
        return this;
    }

    /**
     * Delete the cookie with the given name.
     *
     * @param  name The name of the cookie to delete.
     * @return      The page object itself.
     */
    default Page deleteCookie(String name) {
        getDriver().manage().deleteCookieNamed(name);
        return this;
    }

    /**
     * Get the cookies from the browser, map them to their names, and return the
     * list of names.
     *
     * @return A list of cookies
     */
    default List<String> cookies() {
        return getDriver().manage().getCookies()
                .stream()
                .map(Cookie::getName)
                .collect(Collectors.toList());
    }

    /**
     * Return the cookie with the given name
     *
     * @param  name The name of the cookie to retrieve.
     * @return      A cookie with the given name.
     */
    default Cookie cookie(String name) {
        return getDriver().manage().getCookieNamed(name);
    }
}
