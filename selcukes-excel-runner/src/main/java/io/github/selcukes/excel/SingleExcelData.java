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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.collections.Lists;
import io.github.selcukes.databind.collections.Maps;
import io.github.selcukes.databind.excel.ExcelMapper;
import lombok.CustomLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.String.format;

@CustomLog
class SingleExcelData extends AbstractExcelDataProvider {

    private static final String TEST_SUITE_RUNNER_SHEET = ConfigFactory.getConfig().getExcel().get("suiteName");
    private static final List<String> IGNORE_SHEETS = Lists.of("Master", "Smoke", "Regression", "StaticData");
    private Map<String, DataTable<String, String>> excelData = new LinkedHashMap<>();

    @Override
    public void init() {
        var filePath = ConfigFactory.getConfig().getExcel().get("dataFile");
        excelData = ExcelMapper.parse(filePath);
        IGNORE_SHEETS.remove(TEST_SUITE_RUNNER_SHEET);
        logger.debug(() -> "Using excel runner sheet : " + TEST_SUITE_RUNNER_SHEET);

        excelData.entrySet().stream()
                .filter(entry -> !IGNORE_SHEETS.contains(entry.getKey()))
                .forEach(entry -> modifyFirstColumnData(entry.getValue(),
                    entry.getKey().equals(TEST_SUITE_RUNNER_SHEET) ? "Screen" : TEST,
                    entry.getKey().equals(TEST_SUITE_RUNNER_SHEET) ? "" : EXAMPLE));
    }

    @Override
    public List<String> getScenariosToRun() {
        var suiteScenarios = new ArrayList<String>();
        var testScenarios = new ArrayList<String>();

        excelData.forEach((key, value) -> {
            if (!IGNORE_SHEETS.contains(key)) {
                value.stream()
                        .filter(entry -> entry.get(RUN).equalsIgnoreCase("Yes"))
                        .forEach(entry -> {
                            var testName = entry.get(TEST);
                            if (key.equalsIgnoreCase(TEST_SUITE_RUNNER_SHEET)) {
                                suiteScenarios.add(entry.get("Feature") + NAME_SEPARATOR + testName);
                            } else {
                                testScenarios.add(testName);
                            }
                        });
            }
        });
        return Lists.retainIf(testScenarios, name -> anyMatch(suiteScenarios, name));
    }

    @Override
    public Map<String, String> getTestDataAsMap(String testName) {
        logger.debug(() -> "TestName: " + testName);
        Preconditions.checkArgument(testName.contains(NAME_SEPARATOR),
            format("Invalid Test Name [%s], TestName should be in the format 'FeatureName::ScenarioName'", testName));
        String testSheetName = testName.split(NAME_SEPARATOR)[0];
        logger.debug(() -> "TestSheetName: " + testSheetName);
        return getTestData(testName, excelData.get(testSheetName));
    }

    private boolean anyMatch(List<String> scenarios, String testName) {
        return scenarios.stream().anyMatch(name -> {
            String scenarioName = name + HYPHEN + EXAMPLE;
            return testName.startsWith(scenarioName) || testName.equalsIgnoreCase(name);
        });
    }

    static Object[][] filteredScenarios(Object[][] cucumberScenarios, List<String> runScenarios) {
        if (runScenarios.isEmpty()) {
            logger.info(() -> "No scenario is selected to execute.");
            return cucumberScenarios;
        } else {
            var scenarioMap = Stream.of(cucumberScenarios)
                    .collect(Maps.ofIgnoreCase(s -> s[0].toString().replace("\"", ""), s -> s));
            return runScenarios.stream()
                    .map(s -> s.split(NAME_SEPARATOR)[1])
                    .map(scenarioMap::get)
                    .filter(Objects::nonNull)
                    .toArray(Object[][]::new);
        }
    }
}
