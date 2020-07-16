/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

public final class DriverFactory<D extends RemoteWebDriver> {

    private static ThreadLocal<Object> DRIVER_THREAD = new ThreadLocal<>();

    private static List<Object> STORED_DRIVER = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> STORED_DRIVER.forEach(d -> ((RemoteWebDriver) d).quit())));
    }

    private DriverFactory() {
    }

    public static <D> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    public static <D> void setDriver(D driveThread) {
        STORED_DRIVER.add(driveThread);
        DRIVER_THREAD.set(driveThread);
    }

    public static void removeDriver() {
        STORED_DRIVER.remove(DRIVER_THREAD.get());
        DRIVER_THREAD.remove();
    }
}
