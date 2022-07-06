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

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.windows.options.WindowsOptions;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

@UtilityClass
public class AppiumOptions {
    Capabilities caps;

    public Capabilities setAppTopLevelWindow(String windowId) {
        return setCapability("appTopLevelWindow", windowId);
    }

    public Capabilities appRoot() {
        return setCapability("app", "Root");
    }

    public MutableCapabilities getWinAppOptions(String app) {
        WindowsOptions windowsOptions = new WindowsOptions();
        windowsOptions.setApp(app);
        return merge(windowsOptions);
    }

    public MutableCapabilities getAndroidOptions(String app) {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setApp(app);
        return merge(uiAutomator2Options);
    }

    public MutableCapabilities setCapability(String capabilityName, String value) {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(capabilityName, value);
        return capabilities;
    }

    private MutableCapabilities merge(Capabilities capabilities) {
        return new MutableCapabilities().merge(capabilities);
    }

    public void setUserOptions(Capabilities capabilities) {
        caps = capabilities;
    }

    public Capabilities getUserOptions() {
        return caps;
    }

}
