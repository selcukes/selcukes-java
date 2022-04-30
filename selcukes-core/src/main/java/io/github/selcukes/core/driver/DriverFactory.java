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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class DriverFactory {

    private static final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();

    private static final Set<Object> STORED_DRIVER = new HashSet<>();

    private DriverFactory() {
    }

    public static <D extends WebDriver> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    public static <D extends WebDriver> void setDriver(D driveThread) {

        DRIVER_THREAD.set(driveThread);
    }

    public static void removeDriver() {
        try {
            STORED_DRIVER.remove(DRIVER_THREAD.get());
            getDriver().quit();
        } finally {
            DRIVER_THREAD.remove();
        }
    }

    public static void removeAllDrivers() {
        STORED_DRIVER.stream()
            .filter(Objects::nonNull).forEach(d -> {
                    try {
                        ((RemoteWebDriver) d).quit();
                    } finally {
                        STORED_DRIVER.remove(d);
                    }
                }
            );
        DRIVER_THREAD.remove();
    }
}
