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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@CustomLog
class MultiExcelData extends AbstractExcelDataProvider {
    private DataTable<String, String> excelSuite = new DataTable<>();

    @Override
    public void init() {
        var filePath = ConfigFactory.getConfig().getExcel().get("suiteFile");
        var excelData = ExcelMapper.parse(filePath);
        var suiteName = ConfigFactory.getConfig().getExcel().get("suiteName");
        excelSuite = ofNullable(excelData.get(suiteName))
                .orElseThrow(
                    () -> new ExcelConfigException(format("The Excel suite name [%s] is invalid or empty", suiteName)));
        modifyFirstColumnData(excelSuite, "Screen", "");
    }

    @Override
    public List<String> getScenariosToRun() {
        return excelSuite
                .filter(map -> map.get(RUN).equalsIgnoreCase("yes"))
                .map(map -> map.get("Feature") + NAME_SEPARATOR + map.get(TEST))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getScenarioData(String testName) {
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
                    () -> new ExcelConfigException(format("Unable to find Test Data File for [%s]", testName)));

        logger.debug(() -> "Using Test Data File: " + testDataFile);
        var testData = getCachedTestData(testDataFile);
        return getTestData(testName, testData.get(testSheetName));
    }
}
