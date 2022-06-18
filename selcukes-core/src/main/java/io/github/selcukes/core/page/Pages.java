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

package io.github.selcukes.core.page;

import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.enums.DeviceType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Pages {
    public static WebPage webPage() {
        DriverManager.createDriver(DeviceType.BROWSER);
        return new WebPage(DriverManager.getDriver());
    }

    public static WinPage winPage() {
        DriverManager.createDriver(DeviceType.DESKTOP);
        return new WinPage(DriverManager.getDriver());
    }

    public static MobilePage mobilePage() {
        DriverManager.createDriver(DeviceType.MOBILE);
        return new MobilePage(DriverManager.getDriver());
    }
}
