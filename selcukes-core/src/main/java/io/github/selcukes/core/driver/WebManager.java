package io.github.selcukes.core.driver;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class WebManager {
    final Logger logger = LoggerFactory.getLogger(WebManager.class);
    private WebDriver driver;

    public WebManager() {
    }

    /**
     * Get browser
     *
     * @return Webdriver
     */
    public synchronized WebDriver createDriver() {

        String browser = ConfigFactory.getConfig().getBrowserName();
        if (browser == null) {
            browser = DriverType.CHROME.getName();
        }

        if (null == driver) {

            try {
                logger.info(() -> "Initiating New Browser Session...");

                driver = new ChromeDriver();
                /*EventFiringWebDriver wd=new EventFiringWebDriver(driver);
                wd.register(new DriverEventHandler());*/
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            } catch (Exception e) {
                throw new DriverSetupException("Driver was not setup properly.", e);
            } finally {
                logger.info(() -> "Closing Browser...");

            }
        }
        return driver;

    }
}
