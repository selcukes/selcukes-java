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

import io.github.selcukes.commons.exception.ExcelConfigException;
import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.excel.ExcelMapper;
import io.github.selcukes.databind.utils.StringHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractExcelDataProvider implements ExcelDataProvider {
    static final String NAME_SEPARATOR = "::";
    static final String TEST = "Test";
    static final String RUN = "Run";
    static final String HYPHEN = " - ";
    static final String EXAMPLE = "Example";

    protected static DataTable<String, String> excelSuite = new DataTable<>();
    protected static final Map<String, Map<String, DataTable<String, String>>> runtimeDataMap = new ConcurrentHashMap<>();

    public abstract void init();

    public abstract List<String> getScenariosToRun();

    public abstract Map<String, String> getTestDataAsMap(String testName);

    public Map<String, String> getTestDataAsMap() {
        return getTestDataAsMap(ScenarioContext.getTestName());
    }

    protected Map<String, DataTable<String, String>> getCachedTestData(String testDataFile) {
        return runtimeDataMap.computeIfAbsent(testDataFile, file -> {
            var testData = ExcelMapper.parse(file);
            testData.forEach((key, value) -> modifyFirstColumnData(value, TEST, EXAMPLE));
            return testData;
        });
    }

    protected Map<String, String> getTestData(String testName, DataTable<String, String> sheetData) {
        Objects.requireNonNull(sheetData, String.format("Unable to read sheet data for [%s]", testName));
        return sheetData.findFirst(row -> row.get(TEST).equalsIgnoreCase(testName))
                .orElseThrow(
                    () -> new ExcelConfigException(String.format("Unable to read [%s] Test Data Row", testName)));
    }

    protected void modifyFirstColumnData(DataTable<String, String> sheetData, String firstColumn, String secondColumn) {
        String testName = "";
        Map<String, String> previousRow = new LinkedHashMap<>();
        for (var row : sheetData) {
            var currentTestName = row.get(firstColumn);
            if (StringHelper.isNullOrEmpty(currentTestName)) {
                var newTestName = testName;
                if (!StringHelper.isNullOrEmpty(secondColumn)) {
                    if (!previousRow.isEmpty()
                            && !previousRow.get(firstColumn).startsWith(testName + HYPHEN + EXAMPLE)) {
                        previousRow.replace(firstColumn,
                            testName + HYPHEN + previousRow.getOrDefault(secondColumn, ""));

                    }
                    newTestName = testName + HYPHEN + row.getOrDefault(secondColumn, "");
                }
                row.replace(firstColumn, newTestName);
            } else {
                testName = currentTestName;
            }
            previousRow = row;
        }
    }
}
