package io.github.selcukes.core.tests;

import io.github.selcukes.commons.Await;
import io.github.selcukes.core.page.WebPage;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    WebDriver driver;
    WebPage page;

    @BeforeMethod
    public void setup() {
        driver = new LocalDriver().createWebDriver(DriverType.CHROME, true);
        page = new WebPage(driver);
    }

    @AfterMethod
    public void teardown(){
        Await.until(3);
        driver.quit();
    }
}
