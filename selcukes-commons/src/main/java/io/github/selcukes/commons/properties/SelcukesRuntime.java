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
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.os.Platform;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.*;
import static io.github.selcukes.databind.utils.StringHelper.isNullOrEmpty;
import static java.util.Optional.ofNullable;

@CustomLog
@UtilityClass
public class SelcukesRuntime {
    public static void loadOptions() {
        try {
            SelcukesTestProperties properties = new SelcukesTestProperties();
            String features = ofNullable(properties.getSubstitutedConfigProperty(FEATURES)).orElse("");
            String glue = ofNullable(properties.getCucumberProperty(GLUE)).orElse("");
            String tag = ofNullable(properties.getCucumberProperty(TAGS)).orElse("");
            String additionalPlugin = properties.getCucumberProperty(PLUGIN);
            String reportsPath = ofNullable(properties.getReportsProperty(REPORTS_PATH)).orElse("target");
            String timestampReport = properties.getReportsProperty(TIMESTAMP_REPORT);
            String emailReport = properties.getReportsProperty(EMAIL_REPORT);
            String reportsFile = ofNullable(properties.getReportsProperty(REPORTS_FILE)).orElse("TestReport");

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
                setSystemProperty("extent.reporter.spark.vieworder", "dashboard,test,category,exception,author,device,log");
                setSystemProperty("systeminfo.Platform", Platform.getPlatform().getOsName());
                setSystemProperty("systeminfo.Environment", ConfigFactory.getConfig().getEnv());
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
