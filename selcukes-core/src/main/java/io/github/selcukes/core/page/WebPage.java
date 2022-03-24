package io.github.selcukes.core.page;

import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.core.listener.EventCapture;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;

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

    public void capturePageAsPdf(String filePath) {
        PrintsPage pg = (PrintsPage) driver;
        PrintOptions printOptions = new PrintOptions();
        Pdf pdf = pg.print(printOptions);
        String pdfContent = pdf.getContent();
        Assert.assertTrue(pdfContent.contains("JVBER"));
        FileHelper.createFile(pdfContent, filePath);
    }

}
