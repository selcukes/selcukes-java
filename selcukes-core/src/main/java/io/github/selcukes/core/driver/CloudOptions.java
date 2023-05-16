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

import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.commons.exception.DriverConnectionException;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.annotation.DataFile;
import lombok.CustomLog;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import java.util.Map;

@UtilityClass
@CustomLog
public class CloudOptions {
    private static BrowserStack browserStack;

    private static BrowserStack getBrowserStack() {
        if (browserStack == null) {
            try {
                browserStack = DataMapper.parse(BrowserStack.class);
            } catch (Exception e) {
                throw new DriverConnectionException(
                    "Failed loading BrowserStackOptions. Please make sure 'browser_stack.yaml or json' file is placed in test resource file.");
            }
        }

        return browserStack;
    }

    public Capabilities getBrowserStackOptions(boolean isApp) {
        MutableCapabilities capabilities = new MutableCapabilities();
        if (isApp) {
            getBrowserStack().getEnvironments().forEach(capabilities::setCapability);
        }
        capabilities.setCapability("bstack:options", getBrowserStack().getCapabilities());
        return capabilities;
    }

    @SneakyThrows
    public String browserStackUrl() {
        return StringHelper.interpolate(getBrowserStack().getUrl(), System::getProperty);
    }

    @Data
    @DataFile
    private static class BrowserStack {
        private String url;
        private Map<String, String> capabilities;
        private Map<String, String> environments;
    }
}
