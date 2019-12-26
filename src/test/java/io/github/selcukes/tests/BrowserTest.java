package io.github.selcukes.tests;

import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;

public class BrowserTest {
    private Logger logger = Logger.getLogger(BrowserTest.class.getName());


    private String baseUrl = "https://www.google.com/";
    private WebDriver driver;

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

    @Test
    public void chromeDriverTest() {
        WebDriverBinary.chromeDriver().setup();
        driver = new ChromeDriver();
        driver.get(baseUrl);
        logger.info(driver.getTitle());
        assertEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void firefoxDriverTest() {
        WebDriverBinary.firefoxDriver().setup();
        driver = new FirefoxDriver();
        driver.get(baseUrl);
        logger.info(driver.getTitle());
        assertEquals(driver.getCurrentUrl(), baseUrl);
    }
}
