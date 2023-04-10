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

import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.listener.EventCapture;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.Map;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * A factory class for creating instances of the WebDriver. This class uses a
 * map of device types to driver managers to determine which driver manager to
 * use for creating the appropriate WebDriver.
 */
@CustomLog
@UtilityClass
public class DriverFactory {

    /**
     * A map of device types to driver managers.
     */
    private static final Map<DeviceType, Supplier<RemoteManager>> DRIVER_MANAGER_MAP = Map.of(
        DeviceType.BROWSER, WebManager::new,
        DeviceType.DESKTOP, DesktopManager::new,
        DeviceType.MOBILE, AppiumManager::new);

    /**
     * Creates a new instance of the Selenium WebDriver for the given device
     * type and capabilities.
     *
     * @param  deviceType           the type of device to create the driver for
     * @param  capabilities         the custom capabilities for the driver
     * @return                      the created WebDriver instance
     * @throws DriverSetupException if a driver session cannot be created for
     *                              the given device type
     */
    public synchronized WebDriver create(DeviceType deviceType, Capabilities capabilities) {
        // Log the creation of a new session
        logger.info(() -> format("Creating new %s session...", deviceType));

        // Get the appropriate driver manager for the given device type
        var remoteManager = ofNullable(DRIVER_MANAGER_MAP.get(deviceType))
                .map(Supplier::get)
                .orElseThrow(() -> new DriverSetupException(
                    "Unable to create new driver session for Driver Type[" + deviceType + "]"));

        // Create the WebDriver using the selected driver manager
        var webDriver = remoteManager.createDriver(capabilities);

        // Decorate the WebDriver with the EventFiringDecorator and EventCapture
        // if it's not a WindowsDriver
        return (webDriver instanceof WindowsDriver ? webDriver
                : new EventFiringDecorator<>(new EventCapture()).decorate(webDriver));
    }
}
