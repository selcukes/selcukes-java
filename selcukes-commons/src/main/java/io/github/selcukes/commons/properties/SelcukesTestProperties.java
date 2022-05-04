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

package io.github.selcukes.commons.properties;

import io.github.selcukes.databind.utils.StringHelper;
import lombok.CustomLog;

import java.util.Map;

@CustomLog
public class SelcukesTestProperties {
    public static final String EXCEL_RUNNER = "selcukes.excel.runner";
    public static final String EXCEL_SUITE_NAME = "selcukes.excel.suiteName";
    public static final String EXCEL_SUITE_FILE = "selcukes.excel.suiteFile";
    public static final String EXTENT_REPORT = "selcukes.extent.report";
    public static final String FEATURES = "selcukes.features";
    public static final String GLUE = "selcukes.glue";
    public static final String TAGS = "selcukes.tags";
    public static final String PLUGIN = "selcukes.plugin";
    public static final String REPORTS_PATH = "selcukes.reports.path";
    public static final String TIMESTAMP_REPORT = "selcukes.timestamp.report";
    private final Map<String, String> properties;

    public SelcukesTestProperties() {
        properties = PropertiesMapper
            .readAsMap("selcukes.properties");
    }

    public String getProperty(String propertyKey) {
        if (System.getProperty(propertyKey) != null)
            return System.getProperty(propertyKey);
        return properties.getOrDefault(propertyKey, "");
    }

    public String getSubstitutedProperty(String propertyKey) {
        return StringHelper.interpolate(getProperty(propertyKey),
            matcher -> getProperty(matcher.group(1)));
    }


    public static void setSystemProperty(String key, String value) {
        if (!value.isBlank()) {
            System.setProperty(key, value);
        }
    }
}
