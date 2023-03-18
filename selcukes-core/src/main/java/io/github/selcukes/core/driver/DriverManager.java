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
import io.github.selcukes.commons.fixture.DriverFixture;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.listener.EventCapture;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@CustomLog
@UtilityClass
public class DriverManager {

    private static final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();
    private static final Map<Integer, Object> STORED_DRIVER = new ConcurrentHashMap<>();

    public synchronized <D extends WebDriver> D createDriver(DeviceType deviceType, Capabilities... capabilities) {
        Arrays.stream(capabilities).findAny().ifPresent(AppiumOptions::setUserOptions);
        if (getDriver() == null) {
            System.setProperty("webdriver.http.factory", "jdk-http-client");
            logger.info(() -> String.format("Creating new %s session...", deviceType));
            Map<DeviceType, Supplier<RemoteManager>> driverManagerMap = Map.of(
                DeviceType.BROWSER, WebManager::new,
                DeviceType.DESKTOP, DesktopManager::new,
                DeviceType.MOBILE, AppiumManager::new);
            var remoteManager = ofNullable(driverManagerMap.get(deviceType))
                    .map(Supplier::get)
                    .orElseThrow(
                        () -> new DriverSetupException(
                            "Unable to create new driver session for Driver Type[" + deviceType + "]"));
            var webDriver = remoteManager.createDriver();
            setDriver(webDriver instanceof WindowsDriver ? webDriver
                    : new EventFiringDecorator<>(new EventCapture()).decorate(webDriver));
        }
        return getDriver();
    }

    @SuppressWarnings("unchecked")
    public static <D extends WebDriver> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    public static <D extends WebDriver> void setDriver(D driver) {
        DRIVER_THREAD.set(driver);
        DriverFixture.setDriverFixture(driver);
        STORED_DRIVER.putIfAbsent(driver.hashCode(), driver);
    }

    public static WebDriver getWrappedDriver() {
        if (getDriver() instanceof WrapsDriver) {
            return ((WrapsDriver) getDriver()).getWrappedDriver();
        }
        return getDriver();
    }

    public static synchronized void removeDriver() {
        try {
            if (getDriver() != null) {
                STORED_DRIVER.remove(getDriver().hashCode());
                getDriver().quit();
            }
        } finally {
            DRIVER_THREAD.remove();
        }
    }

    public static synchronized void removeAllDrivers() {
        logger.debug(() -> format("Closing [%d] stored drivers..", STORED_DRIVER.size()));

        STORED_DRIVER.values().stream()
                .filter(WebDriver.class::isInstance)
                .map(WebDriver.class::cast)
                .forEach(webDriver -> {
                    try {
                        webDriver.quit();
                    } catch (Exception e) {
                        logger.warn(
                            () -> format("Failed to close driver %d: %s", webDriver.hashCode(), e.getMessage()));
                    }
                });

        STORED_DRIVER.clear();
        DRIVER_THREAD.remove();
    }
}
