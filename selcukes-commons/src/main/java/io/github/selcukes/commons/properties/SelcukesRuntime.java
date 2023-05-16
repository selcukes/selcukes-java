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

import io.github.selcukes.collections.Clocks;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.os.Platform;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import static io.github.selcukes.collections.Clocks.DATE_TIME_FILE_FORMAT;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.EMAIL_REPORT;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.FEATURES;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.GLUE;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.PLUGIN;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.REPORTS_FILE;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.REPORTS_PATH;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.TAGS;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.TIMESTAMP_REPORT;
import static io.github.selcukes.commons.properties.SelcukesTestProperties.setSystemProperty;
import static io.github.selcukes.databind.utils.StringHelper.isNonEmpty;

@CustomLog
@UtilityClass
public class SelcukesRuntime {
    public void loadOptions() {
        try {
            var properties = new SelcukesTestProperties();
            String features = properties.getSubstitutedConfigProperty(FEATURES);
            String glue = properties.getCucumberProperty(GLUE);
            String tag = properties.getCucumberProperty(TAGS);
            String additionalPlugin = properties.getCucumberProperty(PLUGIN);
            String reportsPath = properties.getReportsProperty(REPORTS_PATH).orElse("target");
            String timestampReport = properties.getReportsProperty(TIMESTAMP_REPORT).orElse("");
            String emailReport = properties.getReportsProperty(EMAIL_REPORT).orElse("");
            String reportsFile = properties.getReportsProperty(REPORTS_FILE).orElse("TestReport");

            String cucumberReportPath = reportsPath + "/cucumber-reports";
            String extentReportPath = reportsPath + "/extent-reports";

            String timestamp = timestampReport.equalsIgnoreCase("true") ? "-" + Clocks.dateTime(DATE_TIME_FILE_FORMAT)
                    : "";

            String plugin = String.format("html:%s/%s%s.html, json:%s/cucumber%s.json",
                cucumberReportPath, reportsFile, timestamp, cucumberReportPath, timestamp);

            if (isNonEmpty(additionalPlugin)) {
                plugin += "," + additionalPlugin;
            }
            if (isNonEmpty(emailReport) && !emailReport.equalsIgnoreCase("false")) {
                setSystemProperty("extent.reporter.spark.start", "true");
                setSystemProperty("extent.reporter.spark.out",
                    String.format("%s/%s.html", extentReportPath, reportsFile));
                setSystemProperty(TIMESTAMP_REPORT, timestampReport);
                setSystemProperty("extent.reporter.spark.vieworder",
                    "dashboard,test,category,exception,author,device,log");
                setSystemProperty("systeminfo.Platform", Platform.getPlatform().getOsName());
                setSystemProperty("systeminfo.Environment", ConfigFactory.getConfig().getEnv());
                plugin += "," + "io.github.selcukes.extent.report.SelcukesExtentAdapter";
            }
            setSystemProperty("cucumber.plugin", plugin);
            setSystemProperty("cucumber.features", features);
            setSystemProperty("cucumber.filter.tags", tag);
            setSystemProperty("cucumber.glue", glue);
            setSystemProperty("cucumber.publish.quiet", "true");

            logger.debug(() -> String
                    .format("Using Runtime Cucumber Options:\nFeatures : [%s]\nGlue     : [%s]\nTags     : [%s] " +
                            "\n ",
                        features, glue, tag));
        } catch (Exception exception) {
            logger.warn(() -> "Failed to load selcukes properties. Using default CucumberOptions to execute...");
        }
    }
}
