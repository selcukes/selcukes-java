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

import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import java.net.URL;
import java.util.Map;

@UtilityClass
public class DesktopOptions {
    URL serviceUrl;
    Capabilities caps;

    public URL getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(URL url) {
        serviceUrl = url;
    }

    public Capabilities setAppTopLevelWindow(String windowId) {
        return setCapability("appTopLevelWindow", windowId);
    }

    public MutableCapabilities getWinAppOptions(String app) {
        return new MutableCapabilities(Map.of("platformName", "Windows",
            "deviceName", "WindowsPC", "app", app));
    }

    public MutableCapabilities getAndroidOptions(String app) {
        return setCapability("app", app);
    }

    public MutableCapabilities setCapability(String capabilityName, String value) {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(capabilityName, value);
        return capabilities;
    }

    public void setUserOptions(Capabilities capabilities) {
        caps = capabilities;
    }

    public Capabilities getUserOptions() {
        return caps;
    }

}