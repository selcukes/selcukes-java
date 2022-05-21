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

package io.github.selcukes.reports.html;

import io.github.selcukes.commons.helper.RandomUtils;
import io.github.selcukes.commons.os.Platform;
import lombok.experimental.UtilityClass;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.selcukes.commons.config.ConfigFactory.getConfig;

@UtilityClass
public class HtmlReporter {
    public void generateReports(String cucumberJsonPath) {
        File reportOutputDirectory = new File(getConfig().getReports().get("path"));
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(cucumberJsonPath);

        String buildNumber = RandomUtils.randomNumeric(4);
        String projectName = getConfig().getProjectName();
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setBuildNumber(buildNumber);

        configuration.addClassifications("Platform", Platform.getPlatform().getOsName());
        configuration.addClassifications("Environment", getConfig().getEnv());

        configuration.setSortingMethod(SortingMethod.NATURAL);
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }

}
