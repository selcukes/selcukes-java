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

import java.util.List;
import java.util.Map;

/**
 * Interface that provides methods to read test data from an Excel file.
 */
public interface ExcelDataProvider {

    /**
     * Initializes the data provider by reading the test data from the Excel
     * file.
     */
    void init();

    /**
     * Returns a list of scenarios that should be executed based on the 'RUN'
     * flag in the Excel test data sheet.
     * <p>
     * The list includes both individual scenarios and scenarios listed in the
     * test suite runner sheet.
     *
     * @return a list of scenario names in the format
     *         'FeatureName::ScenarioName'
     */
    List<String> getScenariosToRun();

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
    default Map<String, String> getTestDataAsMap() {
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
    Map<String, String> getTestDataAsMap(String testName);
}
