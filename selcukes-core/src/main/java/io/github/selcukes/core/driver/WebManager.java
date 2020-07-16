/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
