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
import io.github.selcukes.commons.exception.ExcelConfigException;
import io.github.selcukes.databind.excel.ExcelMapper;
import io.github.selcukes.databind.utils.Maps;
import io.github.selcukes.databind.utils.Streams;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@UtilityClass
@CustomLog
public class SingleExcelData {
    static final String NAME_SEPARATOR = "::";
    static final String TEST = "Test";
    static final String RUN = "Run";
    static final String HYPHEN = " - ";
    static final String EXAMPLE = "Example";
    private static final String TEST_SUITE_RUNNER_SHEET = ConfigFactory.getConfig().getExcel().get("suiteName");
    private static final List<String> IGNORE_SHEETS = new ArrayList<>(
        Arrays.asList("Master", "Smoke", "Regression", "StaticData"));
    private static Map<String, List<Map<String, String>>> excelData = new LinkedHashMap<>();

    public static void init() {
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

    public static List<String> getScenariosToRun() {
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

        return testScenarios.stream()
                .filter(name -> anyMatch(suiteScenarios, name))
                .collect(Collectors.toList());
    }

    public Map<String, String> getTestDataAsMap() {
        return getTestDataAsMap(ScenarioContext.getTestName());
    }

    public Map<String, String> getTestDataAsMap(String testName) {
        logger.debug(() -> "TestName: " + testName);
        String testSheetName = testName.split(NAME_SEPARATOR)[0];
        logger.debug(() -> "TestSheetName: " + testSheetName);
        return getTestData(testName, excelData.get(testSheetName));
    }

    Map<String, String> getTestData(String testName, List<Map<String, String>> sheetData) {
        Objects.requireNonNull(sheetData, String.format("Unable to read sheet data for [%s]", testName));
        return Streams.findFirst(sheetData, row -> row.get(TEST).equalsIgnoreCase(testName))
                .orElseThrow(
                    () -> new ExcelConfigException(String.format("Unable to read [%s] Test Data Row", testName)));
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

    void modifyFirstColumnData(List<Map<String, String>> sheetData, String firstColumn, String secondColumn) {
        String testName = "";
        for (int i = 0; i < sheetData.size(); i++) {
            if (StringHelper.isNullOrEmpty(sheetData.get(i).get(firstColumn))) {
                String newTestName;
                if (!StringHelper.isNullOrEmpty(secondColumn)) {
                    if (!sheetData.get(i - 1).get(firstColumn).startsWith(testName + HYPHEN + EXAMPLE)) {
                        sheetData.get(i - 1).put(firstColumn,
                            testName + HYPHEN + ofNullable(sheetData.get(i - 1).get(secondColumn)).orElse(""));
                    }
                    newTestName = testName + HYPHEN + ofNullable(sheetData.get(i).get(secondColumn)).orElse("");
                } else {
                    newTestName = testName;
                }
                sheetData.get(i).put(firstColumn, newTestName);
            } else {
                testName = sheetData.get(i).get(firstColumn);
            }
        }
    }
}
