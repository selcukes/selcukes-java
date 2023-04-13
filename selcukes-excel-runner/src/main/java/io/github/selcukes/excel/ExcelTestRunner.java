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

package io.github.selcukes.excel;

import io.cucumber.testng.Pickle;
import io.github.selcukes.commons.properties.SelcukesTestProperties;
import io.github.selcukes.databind.collections.Maps;
import io.github.selcukes.testng.SelcukesTestNGRunner;
import lombok.CustomLog;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.EXCEL_RUNNER;
import static io.github.selcukes.excel.AbstractExcelDataProvider.NAME_SEPARATOR;

@CustomLog
public class ExcelTestRunner extends SelcukesTestNGRunner {
    private final List<String> runScenarios = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setupExcel(ITestContext context) {
        var testProperties = new SelcukesTestProperties();
        ExcelDataFactory.getInstance().init();
        if (testProperties.getExcelProperty(EXCEL_RUNNER).equalsIgnoreCase("true")) {
            runScenarios.addAll(ExcelDataFactory.getInstance().getScenariosToRun());
            if (runScenarios.isEmpty()) {
                logger.debug(() -> "No scenario is selected to execute.");
            }
        }
    }

    @Override
    public Object[][] filter(Object[][] scenarios, Predicate<Pickle> accept) {
        var filteredScenarios = super.filter(scenarios, accept);
        if (runScenarios.isEmpty()) {
            return filteredScenarios;
        } else {
            var scenarioMap = Stream.of(filteredScenarios)
                    .collect(Maps.ofIgnoreCase(s -> s[0].toString().replace("\"", ""), s -> s));
            return runScenarios.stream()
                    .map(s -> s.split(NAME_SEPARATOR)[1])
                    .map(scenarioMap::get)
                    .filter(Objects::nonNull)
                    .toArray(Object[][]::new);
        }
    }
}
