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
import io.appium.java_client.ios.IOSDriver;
import io.github.selcukes.collections.Resources;
import io.github.selcukes.commons.config.ConfigFactory;
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
        String serviceUrl;
        if (isLocalAppium()) {
            serviceUrl = AppiumEngine.getInstance().getServiceUrl().toString();
        } else if (isCloudAppium()) {
            serviceUrl = CloudOptions.browserStackUrl();
        } else {
            serviceUrl = ConfigFactory.getConfig().getMobile().getServiceUrl();
        }
        var url = Resources.toURL(serviceUrl);
        logger.debug(() -> String.format("Using ServiceUrl[%s://%s:%s]", url.getProtocol(), url.getHost(),
            url.getPort()));
        return url;
    }

    public WebDriver createBrowserDriver(Capabilities capabilities, String browser) {
        logger.debug(() -> "Creating New Mobile Browser Session...");
        var options = ofNullable(capabilities)
                .orElseGet(() -> {
                    String platform = ConfigFactory.getConfig().getMobile().getPlatform();
                    var driverOptions = BrowserOptions.getBrowserOptions(BrowserOptions.valueOf(browser), platform,
                        RunMode.isHeadlessMobile());
                    return isCloudAppium() ? driverOptions.merge(CloudOptions.getBrowserStackOptions(false))
                            : driverOptions;
                });
        return new RemoteWebDriver(getServiceUrl(), options);
    }

    public WebDriver createAppDriver(Capabilities capabilities) {
        var options = getAppiumAppOptions(capabilities);
        var platform = options.getPlatformName().toString();
        logger.debug(() -> String.format("Creating New %s App Session...", platform));
        return platform.equalsIgnoreCase("IOS") ? new IOSDriver(getServiceUrl(), options)
                : new AndroidDriver(getServiceUrl(), options);

    }

    private Capabilities getAppiumAppOptions(Capabilities capabilities) {
        return ofNullable(capabilities)
                .orElseGet(() -> {
                    if (isCloudAppium()) {
                        return CloudOptions.getBrowserStackOptions(true);
                    } else {
                        var platform = ConfigFactory.getConfig().getMobile().getPlatform();
                        var app = ConfigFactory.getConfig().getMobile().getApp();
                        String appPath = Path.of(app).toAbsolutePath().toString();
                        logger.debug(() -> "Using APP: " + appPath);
                        return platform.equalsIgnoreCase("ios") ? AppiumOptions.getIOSOptions(appPath)
                                : AppiumOptions.getAndroidOptions(appPath);
                    }
                });
    }

}
