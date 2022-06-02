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

import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.utils.StringHelper;
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

    private BrowserStack getBrowserStack() {
        if (browserStack == null) {
            try {
                browserStack = DataMapper.parse(BrowserStack.class);
            } catch (Exception e) {
                throw new DriverSetupException("Failed loading BrowserStackOptions. Please make sure 'browser_stack.yaml or json' file is placed in test resource file.");
            }
        }

        return browserStack;
    }

    public Capabilities getBrowserStackOptions() {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("bstack:options", getBrowserStack().getCapabilities());
        return capabilities;
    }

    @SneakyThrows
    public String browserStackUrl() {
        return StringHelper.interpolate(getBrowserStack().getUrl(), matcher -> System.getProperty(matcher.group(1)));
    }

    @Data
    @DataFile
    static class BrowserStack {
        String url;
        Map<String, String> capabilities;
    }
}
