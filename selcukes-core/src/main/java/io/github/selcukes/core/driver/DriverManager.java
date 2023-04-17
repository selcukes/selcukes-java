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

import io.github.selcukes.commons.exception.DriverConnectionException;
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
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * The DriverManager class manages a pool of WebDriver instances and provides
 * methods for creating, switching, and removing WebDriver instances for
 * different devices and capabilities.
 */
@CustomLog
@UtilityClass
public class DriverManager {

    private static final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();
    private static final SingletonContext<DevicePool> DEVICE_POOL = SingletonContext.with(DevicePool::new);

    /**
     * Creates a new driver instance for the specified device type and
     * capabilities, adds it to the DevicePool, sets it as the current driver
     * for the current thread, and returns it.
     *
     * @param  deviceType   the device type for which the driver instance should
     *                      be created
     * @param  capabilities the capabilities for the driver instance
     * @return              the newly created driver instance
     */
    public synchronized <D extends WebDriver> D createDriver(DeviceType deviceType, Capabilities... capabilities) {
        createDevice(deviceType, capabilities);
        var devices = getDevicePool().getDevices(deviceType);
        if (devices.isEmpty()) {
            throw new DriverConnectionException("No devices available for " + deviceType);
        }
        int deviceIndex = devices.size() - 1;
        setDriver(getDevicePool().getDevice(deviceType, deviceIndex));
        return getDriver();
    }

    /**
     * Returns the device pool.
     *
     * @return the device pool
     */
    public synchronized DevicePool getDevicePool() {
        return DEVICE_POOL.get();
    }

    /**
     * Creates a new device with the specified device type and capabilities, and
     * adds it to the device pool if it does not already exist.
     *
     * @param deviceType   the type of device to create
     * @param capabilities the capabilities to use when creating the device
     */
    private synchronized void createDevice(DeviceType deviceType, Capabilities... capabilities) {
        Stream.ofNullable(capabilities)
                .flatMap(Arrays::stream)
                .map(options -> DriverFactory.create(deviceType, options))
                .forEach(device -> getDevicePool().addDevice(deviceType, device));
        if (getDevicePool().getDevices(deviceType).isEmpty()) {
            getDevicePool().addDevice(deviceType, DriverFactory.create(deviceType, null));
        }
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @param  <D> the type of WebDriver to return
     * @return     the WebDriver instance for the current thread
     */
    @SuppressWarnings("unchecked")
    public synchronized <D extends WebDriver> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    /**
     * Sets the WebDriver instance for the specified device type and index as
     * the current driver for the current thread.
     *
     * @param deviceType the device type for which the driver instance should be
     *                   switched
     * @param index      the zero-based index of the driver instance to be
     *                   switched
     */
    public synchronized void switchDriver(DeviceType deviceType, int index) {
        setDriver(getDevicePool().getDevice(deviceType, index));
    }

    /**
     * Sets the specified WebDriver instance as the current driver for the
     * current thread.
     *
     * @param driver the WebDriver instance to be set as the current driver
     */
    public synchronized void setDriver(Object driver) {
        DRIVER_THREAD.set(driver);
        DriverFixture.setDriverFixture(driver);
    }

    /**
     * Returns the underlying WebDriver instance wrapped by any
     * {@link WrapsDriver} instance currently set as the driver for the current
     * thread. If the current driver does not implement the {@link WrapsDriver}
     * interface, the current driver instance is returned.
     *
     * @return the underlying WebDriver instance wrapped by any
     *         {@link WrapsDriver} instance currently set as the driver for the
     *         current thread, or the current driver instance if it does not
     *         implement {@link WrapsDriver}.
     */
    public synchronized WebDriver getWrappedDriver() {
        if (getDriver() instanceof WrapsDriver) {
            return ((WrapsDriver) getDriver()).getWrappedDriver();
        }
        return getDriver();
    }

    /**
     * Removes the current driver instance from the current thread, closing any
     * associated WebDriver instances and removing the device from the device
     * pool.
     */
    public synchronized void removeDriver() {
        try {
            if (getDriver() != null) {
                getDriver().quit();
                getDevicePool().removeDevice(getDriver());
            }
        } finally {
            DRIVER_THREAD.remove();
        }
    }

    /**
     * Removes all WebDriver instances from the DevicePool and quits each
     * driver.
     * <p>
     * If a driver fails to quit, a warning message is logged with details of
     * the driver and the exception that occurred.
     * </p>
     */
    public synchronized void removeAllDrivers() {
        getDevicePool().getAllDevices().values().stream()
                .flatMap(List::stream)
                .filter(WebDriver.class::isInstance)
                .map(WebDriver.class::cast)
                .forEach(webDriver -> {
                    try {
                        webDriver.quit();
                        getDevicePool().removeDevice(webDriver);
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
