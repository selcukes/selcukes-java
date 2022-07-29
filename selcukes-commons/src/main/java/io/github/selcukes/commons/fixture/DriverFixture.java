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

package io.github.selcukes.commons.fixture;

import lombok.experimental.UtilityClass;

/**
 * It's a fixture that can be used to drive the application under test
 */
@UtilityClass
public class DriverFixture {

    private final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();

    /**
     * If the current thread has a driver fixture, return it. Otherwise, return
     * null.
     *
     * @return The driver object that is currently being used by the thread.
     */
    public Object getDriverFixture() {
        return DRIVER_THREAD.get();
    }

    /**
     * This function sets the driver object to the current thread.
     *
     * @param driver The driver object that is being used to run the tests.
     */
    public void setDriverFixture(final Object driver) {
        DRIVER_THREAD.set(driver);
    }

    /**
     * This function removes the driver from the thread local storage.
     */
    public void removeDriverFixture() {
        DRIVER_THREAD.remove();
    }
}
