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

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.github.selcukes.excel.ExcelUtils.NAME_SEPARATOR;
import static io.github.selcukes.excel.ExcelUtils.runScenarios;

@CustomLog
public class ExcelTestRunner extends SelcukesTestNGRunner {
    @BeforeClass(alwaysRun = true)
    public void setUpExcel(ITestContext context) {
        SelcukesTestProperties testProperties = new SelcukesTestProperties();
        if (!testProperties.getExcelProperty(SelcukesTestProperties.EXCEL_RUNNER).equalsIgnoreCase("false")) {
            ExcelUtils.initTestRunner();
        }
    }

    @Override
    public Object[][] filter(Object[][] scenarios, Predicate<Pickle> accept) {
        Object[][] filteredScenarios = super.filter(scenarios, accept);
        if (runScenarios.isEmpty()) {
            logger.info(() -> "No scenario is selected to execute.");
            return filteredScenarios;
        } else {
            return runScenarios.stream()
                    .map(s -> s.split(NAME_SEPARATOR)[1])
                    .map(excelScenario -> Stream.of(filteredScenarios)
                            .filter(scenario -> scenario[0].toString().replace("\"", "")
                                    .equalsIgnoreCase(excelScenario))
                            .findAny())
                    .filter(Optional::isPresent).map(Optional::get).toArray(Object[][]::new);
        }
    }

}
