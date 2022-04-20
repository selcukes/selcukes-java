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

import io.github.selcukes.commons.config.ConfigFactory;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import java.net.URL;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@UtilityClass
public class DesktopOptions {

    @SneakyThrows
    public static URL getServiceUrl() {
        String serviceUrl = ConfigFactory.getConfig().getWindows().get("serviceUrl");
        return new URL(serviceUrl);
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
        capabilities.setCapability("app", app);
        return capabilities;
    }

}
