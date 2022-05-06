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
import static io.github.selcukes.databind.utils.StringHelper.isNullOrEmpty;

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
            String features = properties.getSubstitutedConfigProperty(FEATURES);
            String glue = properties.getCucumberProperty(GLUE);
            String tag = properties.getCucumberProperty(TAGS);
            String additionalPlugin = properties.getCucumberProperty(PLUGIN);
            String reportsPath = properties.getReportsProperty(REPORTS_PATH);
            String timestampReport = properties.getReportsProperty(TIMESTAMP_REPORT);
            String emailReport = properties.getReportsProperty(EMAIL_REPORT);
            String reportsFile = properties.getReportsProperty(REPORTS_FILE);
            if (isNullOrEmpty(reportsFile))
                reportsFile = "TestReport";

            if (isNullOrEmpty(reportsPath)) {
                reportsPath = "target";
            }
            String cucumberReportPath = reportsPath + "/cucumber-reports";
            String extentReportPath = reportsPath + "/extent-reports";

            String timestamp = timestampReport.equalsIgnoreCase("true") ?
                "-" + DateHelper.get().dateTime() : "";


            String plugin = String.format("html:%s/%s%s.html, json:%s/cucumber%s.json",
                cucumberReportPath, reportsFile, timestamp, cucumberReportPath, timestamp);

            if (!isNullOrEmpty(additionalPlugin)) {
                plugin = plugin + "," + additionalPlugin;
            }
            if (!isNullOrEmpty(emailReport) && !emailReport.equalsIgnoreCase("false")) {
                setSystemProperty("extent.reporter.spark.start", "true");
                setSystemProperty("extent.reporter.spark.out", String.format("%s/%s.html", extentReportPath, reportsFile));
                setSystemProperty(TIMESTAMP_REPORT, timestampReport);
                plugin = plugin + "," + "io.github.selcukes.extent.report.SelcukesExtentAdapter:";
            }
            setSystemProperty("cucumber.plugin", plugin);
            setSystemProperty("cucumber.features", features);
            setSystemProperty("cucumber.filter.tags", tag);
            setSystemProperty("cucumber.glue", glue);
            setSystemProperty("cucumber.publish.quiet", "true");

            logger.debug(() -> String.format("Using Runtime Cucumber Options:\nFeatures : [%s]\nGlue     : [%s]\nTags     : [%s] " +
                "\n ", features, glue, tag));
        } catch (Exception exception) {
            logger.warn(() -> "Failed loading selcukes properties. Using default CucumberOptions to execute...");
        }
    }


}
