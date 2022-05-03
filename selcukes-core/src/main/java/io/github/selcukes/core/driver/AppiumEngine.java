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

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.selcukes.commons.exception.DriverSetupException;
import lombok.CustomLog;

import java.net.URL;

@CustomLog
class AppiumEngine {
    private AppiumDriverLocalService service;
    private static AppiumEngine appiumEngine;

    public static AppiumEngine getInstance() {
        if (appiumEngine == null)
            appiumEngine = new AppiumEngine();
        return appiumEngine;
    }

    URL getServiceUrl() {
        if (service == null) {
            throw new DriverSetupException("Appium server not started...");
        }
        return service.getUrl();
    }

    void startLocalServer() {
        service = new AppiumServiceBuilder()
            .withIPAddress("127.0.0.1")
            .usingAnyFreePort()
            .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
            .withArgument(GeneralServerFlag.BASEPATH, "/wd/")
            .build();
        logger.info(() -> "Starting Appium server...");
        service.start();
    }

    void stopServer() {
        if (service != null) {
            service.stop();
            logger.info(() -> "Stopped Appium server...");
        }
    }
}