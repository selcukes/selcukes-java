package io.github.selcukes.wdb.driver;

import io.github.selcukes.wdb.WebDriverBinary;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class LocalDriver {
    public WebDriver createWebDriver(DriverType driverType, boolean headless) {
        switch (driverType) {
            case EDGE:
                WebDriverBinary.edgeDriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setHeadless(headless);
                return new EdgeDriver(edgeOptions);
            case FIREFOX:
                WebDriverBinary.firefoxDriver().setup();
                return new FirefoxDriver();
            case IEXPLORER:
                WebDriverBinary.ieDriver().setup();
                return new InternetExplorerDriver();
            default:
                WebDriverBinary.chromeDriver().checkBrowserVersion().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(headless);
                return new ChromeDriver(chromeOptions);
        }

    }

    public WebDriver createWebDriver(DriverType driverType) {
        return createWebDriver(driverType, true);
    }
}
