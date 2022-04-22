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

import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.core.enums.DeviceType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Arrays;


public class DriverManager<D extends RemoteWebDriver> {
    private final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private RemoteManager remoteManager;

    public D createDriver(DeviceType deviceType, Capabilities... capabilities) {
        Arrays.stream(capabilities).findAny().ifPresent(DesktopOptions::setUserOptions);
        if (DriverFactory.getDriver() == null) {
            logger.info(() -> String.format("Creating new %s session...", deviceType));
            switch (deviceType) {
                case BROWSER:
                    remoteManager = new WebManager();
                    DriverFactory.setDriver(remoteManager.createDriver());
                    break;
                case DESKTOP:
                    remoteManager = new DesktopManager();
                    DriverFactory.setDriver(remoteManager.createDriver());
                    break;
                case MOBILE:
                    remoteManager = new AppiumManager();
                    DriverFactory.setDriver(remoteManager.createDriver());
                    break;
                default:
                    throw new DriverSetupException("Unable to create new driver session for Driver Type[" + deviceType + "]");
            }
        }
        return DriverFactory.getDriver();
    }

    public RemoteManager getManager() {
        return remoteManager;
    }

}