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
import io.github.selcukes.core.enums.DriverType;
import org.openqa.selenium.Capabilities;

public class MobileOptions extends BrowserOptions{

    public Capabilities getMobileOptions(DriverType mobileType) {
        switch (mobileType) {
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions();
                return safariOptions;
            case GECKO:
                GeckoOptions geckoOptions = new GeckoOptions();
                return geckoOptions;
            case MAC:
                Mac2Options mac2Options = new Mac2Options();
                return mac2Options;
            case WINDOWS:
                WindowsOptions windowsOptions = new WindowsOptions();
                return windowsOptions;
            case ESPRESSO:
                EspressoOptions espressoOptions = new EspressoOptions();
                return espressoOptions;
            case UIAUTOMATOR:
                UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
                return uiAutomator2Options;
            default:
                return getBrowserOptions(mobileType);

        }

    }
}
