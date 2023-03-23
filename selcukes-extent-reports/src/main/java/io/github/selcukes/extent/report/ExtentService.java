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

package io.github.selcukes.extent.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.ReporterConfigurable;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.github.selcukes.databind.collections.Maps;
import io.github.selcukes.databind.properties.PropertiesMapper;
import io.github.selcukes.databind.utils.Clocks;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.THUMBNAIL_REPORT;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.TIMESTAMP_REPORT;
import static io.github.selcukes.databind.utils.Clocks.DATE_TIME_FILE_FORMAT;
import static io.github.selcukes.databind.utils.StringHelper.isNullOrEmpty;

public class ExtentService {

    private static final String SYS_INFO_MARKER = "systeminfo.";
    private static final String OUTPUT_PATH = "target/";
    private static final String INIT_SPARK_KEY = "extent.reporter.spark.start";
    private static final String CONFIG_SPARK_KEY = "extent.reporter.spark.config";
    private static final String OUT_SPARK_KEY = "extent.reporter.spark.out";
    private static final String VIEW_ORDER_SPARK_KEY = "extent.reporter.spark.vieworder";
    private static final String REPORT_NAME = "Automation Report";
    private static final String REPORT_TITLE = "Automation Report";
    private final ExtentReports extentReports;
    private Map<String, String> propertiesMap;

    public ExtentService(ExtentReports extentReports) {
        this.extentReports = extentReports;
        initProperties();
        if ("true".equals(getProperty(INIT_SPARK_KEY))) {
            initSpark();
        }
        addSystemInfo();
    }

    public ExtentReports getExtentReports() {
        return extentReports;
    }

    private void initProperties() {
        try {
            propertiesMap = PropertiesMapper.parse("extent.properties");
        } catch (Exception e) {
            propertiesMap = Maps.of(System.getProperties());
        }
    }

    private String getProperty(String propertyKey) {
        return (System.getProperty(propertyKey) != null) ? System.getProperty(propertyKey)
                : propertiesMap.get(propertyKey);
    }

    private String getOutputPath() {
        String out = getProperty(OUT_SPARK_KEY);
        return isNullOrEmpty(out) ? OUTPUT_PATH + OUT_SPARK_KEY.split("\\.")[2] + "/" : out;
    }

    private boolean getBooleanProperty(String propertyKey) {
        String value = getProperty(propertyKey);
        return (!isNullOrEmpty(value)
                && value.equalsIgnoreCase("true"));
    }

    private void initSpark() {
        String out = getOutputPath();
        if (getBooleanProperty(TIMESTAMP_REPORT)) {
            out = Objects.requireNonNull(out).replace(".", Clocks.dateTime(DATE_TIME_FILE_FORMAT) + ".");
        }
        ExtentSparkReporter spark = new ExtentSparkReporter(out);
        spark.config().setReportName(REPORT_NAME);
        spark.config().setDocumentTitle(REPORT_TITLE);
        spark.config().thumbnailForBase64(getBooleanProperty(THUMBNAIL_REPORT));
        sparkReportViewOrder(spark);
        attach(spark);
    }

    private void sparkReportViewOrder(ExtentSparkReporter spark) {
        try {
            List<ViewName> viewOrder = Arrays.stream(getProperty(VIEW_ORDER_SPARK_KEY).split(","))
                    .map(v -> ViewName.valueOf(v.toUpperCase())).collect(Collectors.toList());
            spark.viewConfigurer().viewOrder().as(viewOrder).apply();
        } catch (Exception ignored) {
            // Gobble exception
        }
    }

    private void attach(ReporterConfigurable reporterConfigurable) {
        String configPath = getProperty(CONFIG_SPARK_KEY);
        if (!isNullOrEmpty(configPath)) {
            try {
                reporterConfigurable.loadXMLConfig(configPath);
            } catch (IOException ignored) {
                // Gobble exception
            }
        }
        extentReports.attachReporter((ExtentObserver<?>) reporterConfigurable);
    }

    private void addSystemInfo() {
        propertiesMap.forEach((k, v) -> {
            String key = String.valueOf(k);
            if (key.startsWith(SYS_INFO_MARKER)) {
                key = key.substring(key.indexOf('.') + 1);
                extentReports.setSystemInfo(key, String.valueOf(v));
            }
        });
    }
}
