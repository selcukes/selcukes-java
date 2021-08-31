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
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WebManager {
    private final Logger logger = LoggerFactory.getLogger(WebManager.class);
    private WebDriver driver;

    public synchronized WebDriver createDriver() {

        Optional<String> browser = Optional.empty();
        try {
            browser = Optional.ofNullable(ConfigFactory.getConfig().getBrowserName());
        } catch (Exception ignored) {
                //ignore
        }

        if (null == driver) {

            try {
                logger.info(() -> "Initiating New Browser Session...");
                switch (browser.orElse("")) {
                    case "edge":
                        WebDriverBinary.edgeDriver().setup();
                        driver = new EdgeDriver();
                        break;
                    case "firefox":
                        WebDriverBinary.firefoxDriver().setup();
                        driver = new FirefoxDriver();
                        break;
                    default:
                        WebDriverBinary.chromeDriver().checkBrowserVersion().setup();
                        driver = new ChromeDriver();
                }
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
