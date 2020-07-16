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

package io.github.selcukes.core.caps;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

public class CapabilitiesProvider {
    public Capabilities getCapabilities(WebDriver driver) {
        WebDriver currentDriver = driver;
        Capabilities capabilities = capabilitiesOf(currentDriver);
        while (currentDriver instanceof WrapsDriver && capabilities == null) {
            currentDriver = ((WrapsDriver) currentDriver).getWrappedDriver();
            capabilities = capabilitiesOf(currentDriver);
        }
        return capabilities;
    }

    private Capabilities capabilitiesOf(WebDriver currentDriver) {
        return currentDriver instanceof HasCapabilities
            ? ((HasCapabilities) currentDriver).getCapabilities()
            : null;
    }
}
