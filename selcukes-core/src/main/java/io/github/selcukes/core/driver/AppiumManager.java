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
import io.github.selcukes.databind.utils.Resources;
import lombok.CustomLog;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.nio.file.Path;

import static io.github.selcukes.core.driver.RunMode.isCloudAppium;
import static io.github.selcukes.core.driver.RunMode.isLocalAppium;
import static java.util.Optional.ofNullable;

@CustomLog
class AppiumManager implements RemoteManager {

    @Override
    public WebDriver createDriver(Capabilities customCapabilities) {
        String target = ConfigFactory.getConfig().getMobile().getBrowser().toUpperCase();
        return target.equals("APP") ? createAppDriver(customCapabilities)
                : createBrowserDriver(customCapabilities, target);
    }

    public URL getServiceUrl() {
        var serviceUrl = ofNullable(serviceUrl())
                .flatMap(Resources::toURL)
                .orElseThrow(() -> new IllegalStateException("Service URL is null"));

        logger.debug(() -> String.format("Using ServiceUrl[%s://%s:%s]", serviceUrl.getProtocol(), serviceUrl.getHost(),
            serviceUrl.getPort()));
        return serviceUrl;
    }

    private String serviceUrl() {
        return isLocalAppium() ? AppiumEngine.getInstance().getServiceUrl().toString()
                : isCloudAppium() ? CloudOptions.browserStackUrl()
                        : ConfigFactory.getConfig().getMobile().getServiceUrl();
    }

    public WebDriver createBrowserDriver(Capabilities capabilities, String browser) {
        logger.debug(() -> "Initiating New Mobile Browser Session...");
        var options = ofNullable(capabilities)
                .orElseGet(() -> {
                    String platform = ConfigFactory.getConfig().getMobile().getPlatform();
                    var driverOptions = BrowserOptions.getBrowserOptions(BrowserOptions.valueOf(browser), platform);
                    return isCloudAppium() ? driverOptions.merge(CloudOptions.getBrowserStackOptions(false))
                            : driverOptions;
                });
        return new RemoteWebDriver(getServiceUrl(), options);
    }

    public WebDriver createAppDriver(Capabilities capabilities) {
        logger.debug(() -> "Initiating New Mobile App Session...");
        var options = ofNullable(capabilities)
                .orElseGet(() -> {
                    if (isCloudAppium()) {
                        return CloudOptions.getBrowserStackOptions(true);
                    } else {
                        var app = ConfigFactory.getConfig().getMobile().getApp();
                        String appPath = Path.of(app).toAbsolutePath().toString();
                        logger.info(() -> "Using APP: " + appPath);
                        return AppiumOptions.getAndroidOptions(appPath);
                    }
                });
        return new AndroidDriver(getServiceUrl(), options);
    }

}
