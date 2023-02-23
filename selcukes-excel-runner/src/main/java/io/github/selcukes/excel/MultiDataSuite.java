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
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.selcukes.excel.ExcelUtils.EXAMPLE;
import static io.github.selcukes.excel.ExcelUtils.NAME_SEPARATOR;
import static io.github.selcukes.excel.ExcelUtils.RUN;
import static io.github.selcukes.excel.ExcelUtils.TEST;

@CustomLog
@UtilityClass
public class MultiDataSuite {

    private static List<Map<String, String>> excelSuite = new ArrayList<>();

    public List<String> start() {
        var filePath = FileHelper.loadResource(ConfigFactory.getConfig().getExcel().get("suiteFile"));
        var excelData = ExcelMapper.parse(filePath);
        var suiteName = ConfigFactory.getConfig().getExcel().get("suiteFile");
        excelSuite = excelData.get(suiteName);
        ExcelUtils.modifyFirstColumnData(excelSuite, "Screen", "");
        return excelSuite.parallelStream()
                .filter(map -> map.get(RUN).equalsIgnoreCase("yes"))
                .map(map -> map.get(TEST))
                .collect(Collectors.toList());
    }

    public Map<String, String> getTestDataAsMap(String testName) {
        logger.debug(() -> "TestName: " + testName);
        String testSheetName = testName.split(NAME_SEPARATOR)[0];
        String scenarioName = testName.split(NAME_SEPARATOR)[1];
        logger.debug(() -> "TestSheetName: " + testSheetName);

        var testDataFile = excelSuite.stream()
                .filter(map -> map.get("Feature").equalsIgnoreCase(testSheetName)
                        && map.get(TEST).equalsIgnoreCase(scenarioName))
                .map(map -> map.get("DataFile"))
                .findFirst().orElseThrow(
                    () -> new ExcelConfigException(String.format("Unable to find Test Data File for [%s]", testName)));

        logger.debug(() -> "Using Test Data File: " + testDataFile);

        var testData = readTestData(testDataFile);

        return ExcelUtils.getTestData(testName, testData.get(testSheetName));
    }

    private Map<String, List<Map<String, String>>> readTestData(String testDataFile) {
        var filePath = FileHelper.loadResource(testDataFile);
        var testData = ExcelMapper.parse(filePath);
        testData.forEach((key, value) -> ExcelUtils.modifyFirstColumnData(value, TEST, EXAMPLE));
        return testData;
    }

}
