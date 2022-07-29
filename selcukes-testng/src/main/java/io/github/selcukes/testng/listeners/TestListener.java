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

package io.github.selcukes.testng.listeners;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.properties.SelcukesRuntime;
import lombok.CustomLog;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.xml.XmlTest;

@CustomLog
public class TestListener implements ITestListener {
    @Override
    public void onFinish(final ITestContext context) {
        logger.debug(() -> context.getName() + " Execution finished...");
    }

    @Override
    public void onStart(final ITestContext context) {
        logger.debug(() -> context.getName() + " Execution started...");
        XmlTest currentXmlTest = context.getCurrentXmlTest();
        currentXmlTest.getAllParameters().forEach((key, value) -> {
            if (key.startsWith("selcukes")) {
                String prop = key.substring(key.lastIndexOf(".") + 1);
                if (key.startsWith("selcukes.reports")) {
                    ConfigFactory.getConfig().getReports().put(prop, value);
                } else if (key.startsWith("selcukes.excel")) {
                    ConfigFactory.getConfig().getExcel().put(prop, value);
                } else {
                    ConfigFactory.getConfig().getCucumber().put(prop, value);
                }
            }
        });
        SelcukesRuntime.loadOptions();
    }
}
