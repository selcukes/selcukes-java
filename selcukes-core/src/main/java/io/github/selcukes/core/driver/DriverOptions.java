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

import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import static org.openqa.selenium.remote.CapabilityType.*;
@UtilityClass
public class DriverOptions {
    private static final ThreadLocal<MutableCapabilities> threadLocalCapabilities = new InheritableThreadLocal<>();

    private static void getDefaultOptions() {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(ACCEPT_INSECURE_CERTS, true);
        capabilities.setCapability(SUPPORTS_JAVASCRIPT, true);
        capabilities.setCapability(TAKES_SCREENSHOT, true);
        capabilities.setCapability(SUPPORTS_ALERTS, true);
        threadLocalCapabilities.set(capabilities);
    }

    public static Capabilities getOptions() {
        if (threadLocalCapabilities.get() == null) getDefaultOptions();
        return threadLocalCapabilities.get();
    }

    public static Capabilities setAppTopLevelWindow(String windowId) {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("appTopLevelWindow", windowId);
        return capabilities;
    }

    public static MutableCapabilities setCapabilities(String app) {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(PLATFORM_NAME, "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        capabilities.setCapability("app",app);
        return capabilities;
    }

}
