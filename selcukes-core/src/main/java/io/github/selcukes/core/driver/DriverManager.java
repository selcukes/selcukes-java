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

import io.github.selcukes.commons.fixture.DriverFixture;
import io.github.selcukes.commons.helper.SingletonContext;
import io.github.selcukes.core.enums.DeviceType;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

@CustomLog
@UtilityClass
public class DriverManager {

    private static final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();
    private static final SingletonContext<DevicePool> DEVICE_POOL = SingletonContext.with(DevicePool::new);

    public synchronized <D extends WebDriver> D createDriver(DeviceType deviceType, Capabilities... capabilities) {
        createDevice(deviceType, capabilities);
        setDriver(DEVICE_POOL.get().getDevice(deviceType, 0));
        return getDriver();
    }

    public synchronized void createDevice(DeviceType deviceType, Capabilities... capabilities) {
        if (capabilities.length > 0) {
            Arrays.stream(capabilities).forEach(
                options -> DEVICE_POOL.get().addDevice(deviceType, DriverFactory.create(deviceType, options)));
        }
        if (DEVICE_POOL.get().getDevices(deviceType).isEmpty()) {
            DEVICE_POOL.get().addDevice(deviceType, DriverFactory.create(deviceType, null));
        }
    }

    @SuppressWarnings("unchecked")
    public static <D extends WebDriver> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    public static void switchDriver(DeviceType deviceType, int index) {
        setDriver(DEVICE_POOL.get().getDevice(deviceType, index));
    }

    public static void setDriver(Object driver) {
        DRIVER_THREAD.set(driver);
        DriverFixture.setDriverFixture(driver);
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
                getDriver().quit();
                DEVICE_POOL.get().removeDevice(getDriver());
            }
        } finally {
            DRIVER_THREAD.remove();
        }
    }

    public static synchronized void removeAllDrivers() {
        DEVICE_POOL.get().getAllDevices().values().stream()
                .flatMap(List::stream)
                .filter(WebDriver.class::isInstance)
                .map(WebDriver.class::cast)
                .forEach(webDriver -> {
                    try {
                        webDriver.quit();
                        DEVICE_POOL.get().removeDevice(webDriver);
                        logger.debug(
                            () -> format("Closed driver %d and removed from DevicePool.", webDriver.hashCode()));
                    } catch (Exception e) {
                        logger.warn(
                            () -> format("Failed to close driver %d: %s", webDriver.hashCode(), e.getMessage()));
                    }
                });
        DRIVER_THREAD.remove();
    }

}
