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
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
@CustomLog
public class ExcelUtils {
    static final String NAME_SEPARATOR = "::";
    static final List<String> runScenarios = new ArrayList<>();
    private static final String RUN = "Run";
    private static final String HYPHEN = " - ";
    private static final String EXAMPLE = " - Example";
    private static final Map<String, List<List<String>>> allSheetsDataMap = new LinkedHashMap<>();
    private static final String TEST_SUITE_RUNNER_SHEET = ConfigFactory.getConfig().getExcel().get("suiteName");
    private static final List<String> IGNORE_SHEETS = new ArrayList<>(
        Arrays.asList("Master", "Smoke", "Regression", "StaticData"));
    private static Map<String, List<List<String>>> allSheetsMap = new LinkedHashMap<>();

    public static void initTestRunner() {
        ExcelReader excelReader = new ExcelReader(
            ConfigFactory.getConfig().getExcel().get("fileName"));

        // Store all sheets data
        allSheetsMap = excelReader.getAllSheetsDataMap();

        // Replace Empty test name with previous row test name and if it is examples test then add Example row
        allSheetsMap.keySet().forEach(sheet -> allSheetsDataMap.put(sheet, modifySheetFirstColumn(sheet)));

        IGNORE_SHEETS.remove(TEST_SUITE_RUNNER_SHEET);

        logger.debug(() -> "Using excel runner sheet : " + TEST_SUITE_RUNNER_SHEET);

        // Filter runOnly Tests
        Map<String, List<List<String>>> allSheetsModifiedMap = allSheetsDataMap.keySet().stream().filter(s -> !IGNORE_SHEETS.contains(s))
            .collect(Collectors.toMap(sheet -> sheet, sheet -> allSheetsDataMap.get(sheet).stream().skip(1)
                .filter(row -> {
                    int exeStatus = allSheetsDataMap.get(sheet).get(0).indexOf(RUN);
                    return row.get(exeStatus).equalsIgnoreCase("yes");
                }).collect(Collectors.toList())));

        // Stores FeatureName::Tests from master sheet
        List<String> masterList = allSheetsModifiedMap.get(TEST_SUITE_RUNNER_SHEET).stream()
            .map(row -> row.get(1) + NAME_SEPARATOR + row.get(2)).collect(Collectors.toList());

        if (TEST_SUITE_RUNNER_SHEET.equalsIgnoreCase("Master")) {
            allSheetsModifiedMap.keySet().stream().skip(1).forEach(
                sheet -> allSheetsModifiedMap.get(sheet).stream().filter(row -> anyMatch(masterList, row.get(0)))
                    .forEach(row -> runScenarios.add(row.get(0))));
        } else {
            runScenarios.addAll(masterList);
        }
    }

    public static Map<String, String> getTestDataAsMap(String testName) {
        logger.debug(() -> "TestName:" + testName);
        String testSheetName = getTestSheetName(testName);
        logger.debug(() -> "TestSheetName:" + testSheetName);

        List<List<String>> listRow = allSheetsDataMap.get(testSheetName);
        int testRowIndex = getColumnData(listRow, 0).indexOf(testName);
        Map<String, String> testDataRowMap = new LinkedHashMap<>();
        if (testRowIndex < 0) {
            throw new ExcelConfigException("Unable to read Test Row for the test name:" + testName);
        }
        for (int i = 0; i < getRowData(listRow, 0).size(); i++) {
            // Adding Key as Column Header and Value as Test Data Row value
            testDataRowMap.put(getRowData(allSheetsDataMap.get(testSheetName), 0).get(i),
                getRowData(listRow, testRowIndex).get(i));
        }
        return testDataRowMap;
    }

    private static List<String> getRowData(List<List<String>> rowList, int rowIndex) {
        return new ArrayList<>(rowList.get(rowIndex));
    }

    private static String getTestSheetName(String testName) {
        String tests = testName.split(NAME_SEPARATOR)[1];

        List<List<String>> masterSheetList = allSheetsDataMap.get(TEST_SUITE_RUNNER_SHEET);
        int index = getColumnData(masterSheetList, 2).indexOf(tests);
        if (index < 0) {
            int i = tests.lastIndexOf(HYPHEN.trim());
            if (i > 0 && tests.substring(i).trim().startsWith(EXAMPLE.trim())) {
                tests = tests.substring(0, i - 1);
            }
            index = getColumnData(masterSheetList, 2).indexOf(tests);
            if (index < 0) {
                throw new ExcelConfigException("Unable to read SheetName for the given testName:" + testName);
            }
        }

        return masterSheetList.get(index).get(0);
    }

    private static List<String> getColumnData(List<List<String>> sheetDataList, int columnIndex) {
        return sheetDataList.stream().map(row -> row.get(columnIndex)).collect(Collectors.toList());
    }

    private static boolean anyMatch(List<String> masterList, String testName) {
        return masterList.stream().anyMatch(name -> {
            String scenarioName = name + EXAMPLE;
            return testName.startsWith(scenarioName) || testName.equalsIgnoreCase(name);
        });
    }

    private static List<List<String>> modifySheetFirstColumn(String sheetName) {
        List<List<String>> sheetDataList = new ArrayList<>(allSheetsMap.get(sheetName));
        String testName = "";
        for (int i = 0; i < sheetDataList.size(); i++) {
            if (sheetDataList.get(i).get(0).isEmpty()) {
                String newTestName;
                if (!sheetName.equalsIgnoreCase(TEST_SUITE_RUNNER_SHEET)) {
                    if (!sheetDataList.get(i - 1).get(0).startsWith(testName + EXAMPLE)) {
                        sheetDataList.get(i - 1).set(0, testName + HYPHEN + sheetDataList.get(i - 1).get(1));
                    }
                    newTestName = testName + HYPHEN + sheetDataList.get(i).get(1);
                } else
                    newTestName = testName;
                sheetDataList.get(i).set(0, newTestName);
            } else {
                testName = sheetDataList.get(i).get(0);
            }
        }
        return sheetDataList;
    }
}
