/*
 *
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
 *
 */

package io.github.selcukes.core.driver;

import io.github.selcukes.wdb.WebDriverBinary;
import io.github.selcukes.wdb.enums.DriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.selcukes.wdb.enums.DriverType.*;

public class RemoteWebManager {
    private Map<DriverType, DriverControl> factories;
    public RemoteWebManager()
    {
        initDefaultFactories();
    }

    private void initDefaultFactories() {
        factories = new ConcurrentHashMap<>();
        factories.put(CHROME, new AbstractDriverControl() {

            protected DesiredCapabilities createCapabilities() {
                WebDriverBinary.chromeDriver().setup();
                return DesiredCapabilities.chrome();
            }
        });
        factories.put(FIREFOX, new AbstractDriverControl() {

            protected DesiredCapabilities createCapabilities() {
                WebDriverBinary.firefoxDriver().setup();
                return DesiredCapabilities.firefox();
            }
        });
        factories.put(EDGE, new AbstractDriverControl() {

            protected DesiredCapabilities createCapabilities() {
                WebDriverBinary.edgeDriver().setup();
                return DesiredCapabilities.edge();
            }
        });
    }
    public  WebDriver getDriver()
    {
        DriverControl control=factories.get(CHROME);
        return control.createDriver("");
    }
}
