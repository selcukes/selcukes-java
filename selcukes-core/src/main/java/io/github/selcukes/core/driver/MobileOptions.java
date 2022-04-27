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

import io.appium.java_client.android.options.EspressoOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.gecko.options.GeckoOptions;
import io.appium.java_client.mac.options.Mac2Options;
import io.appium.java_client.safari.options.SafariOptions;
import io.appium.java_client.windows.options.WindowsOptions;
import io.github.selcukes.core.enums.AppiumDriverType;
import org.openqa.selenium.Capabilities;

public class MobileOptions extends BrowserOptions {

    public Capabilities getMobileOptions(AppiumDriverType appiumDriverType) {
        switch (appiumDriverType) {
            case SAFARI:
                return new SafariOptions();
            case GECKO:
                return new GeckoOptions();
            case MAC:
                return new Mac2Options();
            case WINDOWS:
                return new WindowsOptions();
            case ESPRESSO:
                return new EspressoOptions();
            case UIAUTOMATOR:
                return new UiAutomator2Options();
            default:
                throw new RuntimeException("Not Supported option" + appiumDriverType);

        }

    }
}
