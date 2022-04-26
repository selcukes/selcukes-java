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

package io.github.selcukes.core.tests.grid;

import io.github.selcukes.core.driver.BrowserOptions;
import io.github.selcukes.core.enums.DriverType;
import io.github.selcukes.wdb.WebDriverBinary;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URL;

@CustomLog
public class ClassGridTest {
    private static final ThreadLocal<WebDriver> LOCAL_DRIVER = new InheritableThreadLocal<>();
    private static int HUB_PORT;

    @BeforeSuite
    static void beforeSuite() {
        WebDriverBinary.chromeDriver().setup();
        WebDriverBinary.edgeDriver().setup();
        HUB_PORT = PortProber.findFreePort();
        logger.debug(() -> "Using Free Hub Port: " + HUB_PORT);
        Main.main(new String[]{"standalone", "--port", String.valueOf(HUB_PORT)});
    }

    @DataProvider(parallel = true)
    public Object[][] driverTypes() {

        return new Object[][]{{DriverType.CHROME}, {DriverType.EDGE}};
    }

    @SneakyThrows
    @Test(dataProvider = "driverTypes")
    public void parallelBrowserTest(DriverType driverType) {
        BrowserOptions browserOptions = new BrowserOptions();
        LOCAL_DRIVER.set(new RemoteWebDriver(new URL("http://localhost:" + HUB_PORT),
            browserOptions.getBrowserOptions(driverType)));
        getDriver().get("https://www.google.com/");
        Assert.assertEquals(getDriver().getTitle(), "Google");
    }

    public WebDriver getDriver() {
        return LOCAL_DRIVER.get();
    }

    @AfterMethod
    public void tearDown() {
        getDriver().quit();
    }

    @AfterClass
    void terminate() {
        LOCAL_DRIVER.remove();
    }
}
