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
import io.github.selcukes.testng.SelcukesTestNGRunner;
import lombok.CustomLog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.selcukes.excel.ExcelUtils.NAME_SEPARATOR;
import static io.github.selcukes.excel.ExcelUtils.runScenarios;

@CustomLog
public class ExcelTestRunner extends SelcukesTestNGRunner {
    @Override
    public Object[][] filter(Object[][] scenarios, Predicate<Pickle> accept) {
        Object[][] filteredScenarios = super.filter(scenarios, accept);
        if (runScenarios.isEmpty()) {
            logger.info(() -> "No scenario is selected to execute.");
            return filteredScenarios;
        } else {
            List<String> excelSuiteScenarios = runScenarios.stream()
                .map(s -> s.split(NAME_SEPARATOR)[1]).collect(Collectors.toList());
            List<Object[]> allScenarios = Arrays.asList(filteredScenarios);
            List<Object[]> excelFilteredScenarios = excelSuiteScenarios.stream()
                .map(excelScenario -> allScenarios.stream()
                    .filter(scenario -> scenario[0].toString().replace("\"", "")
                        .equalsIgnoreCase(excelScenario)).findAny())
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

            return excelFilteredScenarios.toArray(Object[][]::new);
        }
    }

}

