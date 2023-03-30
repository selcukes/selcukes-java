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
import io.github.selcukes.commons.helper.Singleton;
import io.github.selcukes.databind.utils.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * Interface that provides methods to read test data from an Excel file.
 */
public interface ExcelDataProvider {

    /**
     * Initializes the data provider by reading the test data from the Excel file.
     */
    void init();

    /**
     * Returns a list of scenarios to run based on the data in the Excel file.
     *
     * @return a list of scenario names
     */
    List<String> getScenariosToRun();

    /**
     * Returns the test data as a map of key-value pairs for all the scenarios in the Excel file.
     *
     * @return a map of key-value pairs representing the test data
     */
    Map<String, String> getTestDataAsMap();

    /**
     * Returns the test data as a map of key-value pairs for a specific test scenario in the Excel file.
     *
     * @param testName the name of the test scenario to retrieve the data for
     * @return a map of key-value pairs representing the test data for the specified scenario
     */
    Map<String, String> getTestDataAsMap(String testName);

    /**
     * Returns an instance of the ExcelDataProvider implementation based on the configuration specified in the
     * application properties file.
     *
     * @return an instance of SingleExcelData or MultiExcelData, depending on the configuration
     */
    static ExcelDataProvider getInstance() {
        String suiteFile = ConfigFactory.getConfig().getExcel().get("suiteFile");
        return StringHelper.isNullOrEmpty(suiteFile) ? Singleton.instanceOf(SingleExcelData.class)
                : Singleton.instanceOf(MultiExcelData.class);
    }
}

