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

package io.github.selcukes.core.driver;

import io.appium.java_client.android.AndroidDriver;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static io.github.selcukes.core.driver.RunMode.isCloudAppium;
import static io.github.selcukes.core.driver.RunMode.isLocalAppium;

@CustomLog
public class AppiumManager implements RemoteManager {

    @Override
    public WebDriver createDriver() {
        String browser = ConfigFactory.getConfig().getMobile().get("browserName");
        return browser.equalsIgnoreCase("APP") ? createAppDriver() : createBrowserDriver(browser);
    }

    @SneakyThrows
    public URL getServiceUrl() {
        URL serviceUrl;

        if (isLocalAppium()) {
            serviceUrl = AppiumEngine.getInstance().getServiceUrl();
        } else if (isCloudAppium()) {
            serviceUrl = new URL(CloudOptions.browserStackUrl());
        } else
            serviceUrl = new URL(ConfigFactory.getConfig().getMobile().get("serviceUrl"));
        logger.debug(() -> String.format("Using ServiceUrl[%s://%s:%s]", serviceUrl.getProtocol(), serviceUrl.getHost(), serviceUrl.getPort()));
        return serviceUrl;
    }

    public WebDriver createBrowserDriver(String browser) {
        WebDriver driver;
        try {
            logger.debug(() -> "Initiating New Mobile Browser Session...");
            Capabilities capabilities = AppiumOptions.getUserOptions();
            if (capabilities == null) {
                capabilities = BrowserOptions.getBrowserOptions(DriverType.valueOf(browser), isCloudAppium());
                if (isCloudAppium()) {
                    capabilities = capabilities.merge(CloudOptions.getBrowserStackOptions());
                }

            }
            driver = new RemoteWebDriver(getServiceUrl(), capabilities);
        } catch (Exception e) {
            throw new DriverSetupException("Driver was not setup properly.", e);
        }
        return driver;
    }

    public WebDriver createAppDriver() {

        WebDriver driver;
        try {
            logger.debug(() -> "Initiating New Mobile App Session...");
            Capabilities capabilities = AppiumOptions.getUserOptions();
            if (capabilities == null) {
                String app = FileHelper.loadThreadResource(ConfigFactory.getConfig()
                    .getMobile().get("app")).getAbsolutePath();
                capabilities = AppiumOptions.getAndroidOptions(app);
                if (isCloudAppium()) {
                    capabilities = capabilities.merge(CloudOptions.getBrowserStackOptions());
                }
            }
            driver = new AndroidDriver(getServiceUrl(), capabilities);
        } catch (Exception e) {
            throw new DriverSetupException("Driver was not setup properly.", e);
        }
        return driver;
    }

}
