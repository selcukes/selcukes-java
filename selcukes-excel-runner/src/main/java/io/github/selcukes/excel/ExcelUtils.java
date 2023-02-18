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
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.databind.excel.ExcelMapper;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@UtilityClass
@CustomLog
public class ExcelUtils {
    static final String NAME_SEPARATOR = "::";
    static List<String> runScenarios = new ArrayList<>();
    private static final String TEST = "Test";
    private static final String RUN = "Run";
    private static final String HYPHEN = " - ";
    private static final String EXAMPLE = " - Example";
    private static final String TEST_SUITE_RUNNER_SHEET = ConfigFactory.getConfig().getExcel().get("suiteName");
    private static final List<String> IGNORE_SHEETS = new ArrayList<>(Arrays.asList("Master", "Smoke", "Regression", "StaticData"));
    private static Map<String, List<Map<String, String>>> excelData = new LinkedHashMap<>();

    public static void initTestRunner() {

        var filePath = FileHelper.loadResource(ConfigFactory.getConfig().getExcel().get("fileName"));
        excelData = ExcelMapper.parse(filePath);
        IGNORE_SHEETS.remove(TEST_SUITE_RUNNER_SHEET);
        logger.debug(() -> "Using excel runner sheet : " + TEST_SUITE_RUNNER_SHEET);

        excelData.entrySet().stream()
                .filter(entry -> !IGNORE_SHEETS.contains(entry.getKey()))
                .forEach(entry -> modifyFirstColumnData(entry.getValue(),
                        entry.getKey().equals(TEST_SUITE_RUNNER_SHEET) ? "Screen" : TEST,
                        entry.getKey().equals(TEST_SUITE_RUNNER_SHEET) ? "" : "Example"));
        runScenarios = getScenariosToRun();
    }

    private static List<String> getScenariosToRun() {
        var masterList = new ArrayList<String>();
        var dataList = new ArrayList<String>();
        for (var entry : excelData.entrySet()) {
            if (!IGNORE_SHEETS.contains(entry.getKey())) {
                for (var map : entry.getValue()) {
                    if (map.get(RUN).equalsIgnoreCase("Yes")) {
                        if (entry.getKey().equalsIgnoreCase(TEST_SUITE_RUNNER_SHEET)) {
                            masterList.add(map.get("Feature") + NAME_SEPARATOR + map.get(TEST));
                        } else {
                            dataList.add(map.get(TEST));
                        }
                    }
                }
            }
        }
        return dataList.stream().filter(name -> anyMatch(masterList, name))
                .collect(Collectors.toList());
    }

    public Map<String, String> getTestDataAsMap(String testName) {

        logger.debug(() -> "TestName:" + testName);
        String testSheetName = testName.split(NAME_SEPARATOR)[0];
        logger.debug(() -> "TestSheetName:" + testSheetName);
        var rowTestData = excelData.get(testSheetName).parallelStream()
                .filter(row -> row.get(TEST).equalsIgnoreCase(testName))
                .findFirst();
        if (rowTestData.isPresent()) {
            return rowTestData.get();
        } else {
            throw new ExcelConfigException(String.format("Unable to read Test Data Row for [%s]", testName));
        }
    }

    private boolean anyMatch(List<String> masterList, String testName) {
        return masterList.stream().anyMatch(name -> {
            String scenarioName = name + EXAMPLE;
            return testName.startsWith(scenarioName) || testName.equalsIgnoreCase(name);
        });
    }

    private void modifyFirstColumnData(List<Map<String, String>> sheetData, String firstColumn, String secondColumn) {
        String testName = "";
        for (int i = 0; i < sheetData.size(); i++) {
            if (StringHelper.isNullOrEmpty(sheetData.get(i).get(firstColumn))) {
                String newTestName;
                if (!StringHelper.isNullOrEmpty(secondColumn)) {
                    if (!sheetData.get(i - 1).get(firstColumn).startsWith(testName + EXAMPLE)) {
                        sheetData.get(i - 1).put(firstColumn, testName + HYPHEN + ofNullable(sheetData.get(i - 1).get(secondColumn)).orElse(""));
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
