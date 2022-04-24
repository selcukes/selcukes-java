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

package io.github.selcukes.testng;

import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.properties.SelcukesTestProperties;
import lombok.CustomLog;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.*;

@CustomLog
public class SelcukesRuntimeAdapter implements SelcukesRuntimeOptions {
    private static SelcukesRuntimeOptions runtimeOptions;

    public static SelcukesRuntimeOptions getInstance() {
        if (runtimeOptions == null)
            runtimeOptions = new SelcukesRuntimeAdapter();
        return runtimeOptions;
    }

    @Override
    public void perform() {
        try {
            SelcukesTestProperties properties = new SelcukesTestProperties();
            String features = properties.getProperty(FEATURES);
            String glue = properties.getProperty(GLUE);
            String tag = properties.getProperty(TAGS);
            String additionalPlugin = properties.getProperty(PLUGIN);
            String reportsPath = properties.getProperty(REPORTS_PATH);
            String timestampReport = properties.getProperty(TIMESTAMP_REPORT);
            String extentReport = properties.getProperty(EXTENT_REPORT);

            String extentReportPath = reportsPath;

            if (reportsPath.isBlank()) {
                reportsPath = "target/cucumber-reports";
                extentReportPath = "target/extent-reports";
            }
            String timestamp = timestampReport.equalsIgnoreCase("true") ?
                "-" + DateHelper.get().dateTime() : "";


            String plugin = String.format("html:%s/cucumber%s.html, json:%s/cucumber%s.json",
                reportsPath, timestamp, reportsPath, timestamp);

            if (!additionalPlugin.isBlank()) {
                plugin = plugin + "," + additionalPlugin;
            }
            if (!extentReport.equalsIgnoreCase("false")) {
                setSystemProperty("extent.reporter.spark.start", "true");
                setSystemProperty("extent.reporter.spark.out", extentReportPath + "/TestReport.html");
                setSystemProperty(TIMESTAMP_REPORT, timestampReport);
                plugin=plugin + "," + "io.github.selcukes.extent.report.SelcukesExtentAdapter:";
            }
            setSystemProperty("cucumber.plugin", plugin);
            setSystemProperty("cucumber.features", features);
            setSystemProperty("cucumber.filter.tags", tag);
            setSystemProperty("cucumber.glue", glue);
            setSystemProperty("cucumber.publish.quiet", "true");

            logger.debug(() -> String.format("Using Runtime Cucumber Options:\nFeatures : [%s]\nGlue     : [%s]\nTags     : [%s] " +
                "\n ", features, glue, tag));
        } catch (Exception exception) {
            logger.warn(() -> "Failed loading selcukes-test properties. Using default CucumberOptions to execute...");
        }
    }

}
