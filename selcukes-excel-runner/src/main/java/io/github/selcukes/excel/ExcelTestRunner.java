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

import io.cucumber.testng.Pickle;
import io.github.selcukes.commons.properties.SelcukesTestProperties;
import io.github.selcukes.testng.SelcukesTestNGRunner;
import lombok.CustomLog;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.EXCEL_RUNNER;

@CustomLog
public class ExcelTestRunner extends SelcukesTestNGRunner {
    private List<String> runScenarios = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setUpExcel(ITestContext context) {
        var testProperties = new SelcukesTestProperties();
        ExcelDataFactory.excelData().init();
        if (testProperties.getExcelProperty(EXCEL_RUNNER).equalsIgnoreCase("true")) {
            runScenarios = ExcelDataFactory.excelData().getScenariosToRun();
        }
    }

    @Override
    public Object[][] filter(Object[][] scenarios, Predicate<Pickle> accept) {
        var filteredScenarios = super.filter(scenarios, accept);
        return SingleExcelData.filteredScenarios(filteredScenarios, runScenarios);
    }
}
