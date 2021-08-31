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

import io.github.selcukes.commons.exception.DriverSetupException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class AbstractDriverControl implements DriverControl {
    @Override
    public final WebDriver createDriver(String serviceUrl) {
        try {
            URL url = new URL(serviceUrl);
            DesiredCapabilities capabilities = createCapabilities();
            return new RemoteWebDriver(url, capabilities);
        } catch (Exception e) {
            throw new DriverSetupException(e);
        }
    }

    protected abstract DesiredCapabilities createCapabilities();
}
