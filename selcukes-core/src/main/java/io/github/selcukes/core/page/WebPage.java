package io.github.selcukes.core.page;

import io.github.selcukes.core.listener.EventCapture;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

public class WebPage extends WebAuthenticator {
    WebDriver driver;

    public WebPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void enableDriverEvents() {
        WebDriverListener eventCapture = new EventCapture();
        driver = new EventFiringDecorator(eventCapture).decorate(driver);
    }

}
