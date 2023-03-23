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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.config.Environment;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.CustomLog;

import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CustomLog
public class SelcukesTestProperties {
    public static final String EXCEL_RUNNER = "selcukes.excel.runner";
    public static final String EXCEL_SUITE_NAME = "selcukes.excel.suiteName";
    public static final String EXCEL_SUITE_FILE = "selcukes.excel.suiteFile";
    public static final String FEATURES = "selcukes.features";
    public static final String GLUE = "selcukes.glue";
    public static final String TAGS = "selcukes.tags";
    public static final String PLUGIN = "selcukes.plugin";
    public static final String EMAIL_REPORT = "selcukes.reports.emailReport";
    public static final String HTML_REPORT = "selcukes.reports.htmlReport";
    public static final String REPORTS_PATH = "selcukes.reports.path";
    public static final String REPORTS_FILE = "selcukes.reports.fileName";
    public static final String TIMESTAMP_REPORT = "selcukes.reports.timestamp";
    public static final String THUMBNAIL_REPORT = "selcukes.reports.thumbnail";
    public static final String CRYPTO_KEY = "selcukes.crypto.key";

    public static void setSystemProperty(String key, String value) {
        ofNullable(value).ifPresent(v -> System.setProperty(key, v));
    }

    private String getProperty(String propertyKey, Function<Environment, Map<String, String>> mapGetter) {
        return ofNullable(System.getProperty(propertyKey))
                .orElseGet(() -> {
                    String key = propertyKey.substring(propertyKey.lastIndexOf(".") + 1);
                    return mapGetter.apply(ConfigFactory.getConfig()).getOrDefault(key, "");
                });
    }

    public String getExcelProperty(String propertyKey) {
        return getProperty(propertyKey, Environment::getExcel);
    }

    public String getCucumberProperty(String propertyKey) {
        return getProperty(propertyKey, Environment::getCucumber);
    }

    public String getReportsProperty(String propertyKey) {
        return getProperty(propertyKey, Environment::getReports);
    }

    public String getSubstitutedConfigProperty(String propertyKey) {
        return StringHelper.interpolate(getCucumberProperty(propertyKey),
            this::getCucumberProperty);
    }
}
