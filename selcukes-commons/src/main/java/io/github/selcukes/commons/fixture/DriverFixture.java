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

@UtilityClass
public class DriverFixture {

    private static final ThreadLocal<Object> DRIVER_THREAD = new InheritableThreadLocal<>();

    public static Object getDriverFixture() {
        return DRIVER_THREAD.get();
    }

    public static void setDriverFixture(Object driver) {
        DRIVER_THREAD.set(driver);
    }

    public static void removeDriverFixture() {
        DRIVER_THREAD.remove();
    }
}
