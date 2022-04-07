package io.github.selcukes.core.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public interface Page {
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
        return  find(by).getText();
    }

    default Page write(By by, CharSequence text) {
        find(by).sendKeys(text);
        return this;
    }

    default Page click(By by) {
        find(by).click();
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

    default Page switchWindow(String name) {
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

    default Page screenshot() {
        return this;
    }

    default WebElement find(By by) {
        return getDriver().findElement(by);
    }

    default List<WebElement> findAll(By by) {
        return getDriver().findElements(by);
    }

    default Object executeScript(String script, Object... args) {
        JavascriptExecutor exe = (JavascriptExecutor) getDriver();
        return exe.executeScript(script, args);
    }

    default WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(5));
    }
}
