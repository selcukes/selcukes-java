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
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.excel.ExcelMapper;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.selcukes.excel.SingleExcelData.EXAMPLE;
import static io.github.selcukes.excel.SingleExcelData.NAME_SEPARATOR;
import static io.github.selcukes.excel.SingleExcelData.RUN;
import static io.github.selcukes.excel.SingleExcelData.TEST;
import static java.lang.String.format;

@CustomLog
@UtilityClass
public class MultiExcelData {

    private static DataTable<String, String> excelSuite = new DataTable<>();
    private static final Map<String, Map<String, DataTable<String, String>>> runtimeDataMap = new LinkedHashMap<>();

    public static void init() {
        var filePath = ConfigFactory.getConfig().getExcel().get("suiteFile");
        var excelData = ExcelMapper.parse(filePath);
        var suiteName = ConfigFactory.getConfig().getExcel().get("suiteName");
        excelSuite = excelData.get(suiteName);
        SingleExcelData.modifyFirstColumnData(excelSuite, "Screen", "");
    }

    /**
     * Returns a list of scenarios that should be executed based on the 'RUN'
     * flag in the Excel test data sheet.
     *
     * @return a list of scenario names in the format
     *         'FeatureName::ScenarioName'
     */
    public static List<String> getScenariosToRun() {
        return excelSuite
                .filter(map -> map.get(RUN).equalsIgnoreCase("yes"))
                .map(map -> map.get("Feature") + NAME_SEPARATOR + map.get(TEST))
                .collect(Collectors.toList());
    }

    /**
     * Returns the test data for the current test as a map of key-value pairs.
     * The current test name is obtained from the ScenarioContext.
     *
     * @return                          a map of key-value pairs containing the
     *                                  test data
     * @throws ExcelConfigException     if the test data file cannot be found
     *                                  for the current test
     * @throws IllegalArgumentException if the current test name is not in the
     *                                  correct format
     */
    public Map<String, String> getTestDataAsMap() {
        return getTestDataAsMap(ScenarioContext.getTestName());
    }

    /**
     * Returns the test data for the given test name as a map of key-value
     * pairs.
     * <p>
     * The test name should be in the format 'FeatureName::ScenarioName'.
     *
     * @param  testName                 the name of the test in the format
     *                                  'FeatureName::ScenarioName'
     * @return                          a map of key-value pairs containing the
     *                                  test data
     * @throws ExcelConfigException     if the test data file cannot be found
     *                                  for the given test name
     * @throws IllegalArgumentException if the test name is not in the correct
     *                                  format
     */
    public Map<String, String> getTestDataAsMap(String testName) {
        logger.debug(() -> "TestName: " + testName);
        Preconditions.checkArgument(testName.contains(NAME_SEPARATOR),
            format("Invalid Test Name [%s], TestName should be in the format 'FeatureName::ScenarioName'", testName));
        String testSheetName = testName.split(NAME_SEPARATOR)[0];
        String scenarioName = testName.split(NAME_SEPARATOR)[1];
        logger.debug(() -> "TestSheetName: " + testSheetName);

        var testDataFile = excelSuite
                .findFirst(map -> map.get("Feature").equalsIgnoreCase(testSheetName)
                        && map.get(TEST).equalsIgnoreCase(scenarioName))
                .map(map -> map.get("DataFile"))
                .orElseThrow(
                    () -> new ExcelConfigException(String.format("Unable to find Test Data File for [%s]", testName)));

        logger.debug(() -> "Using Test Data File: " + testDataFile);
        var testData = runtimeDataMap.computeIfAbsent(testDataFile,
            MultiExcelData::readAndCacheTestData);
        return SingleExcelData.getTestData(testName, testData.get(testSheetName));
    }

    private Map<String, DataTable<String, String>> readAndCacheTestData(String testDataFile) {
        var testData = ExcelMapper.parse(testDataFile);
        testData.forEach((key, value) -> SingleExcelData.modifyFirstColumnData(value, TEST, EXAMPLE));
        runtimeDataMap.put(testDataFile, testData);
        return testData;
    }

}
