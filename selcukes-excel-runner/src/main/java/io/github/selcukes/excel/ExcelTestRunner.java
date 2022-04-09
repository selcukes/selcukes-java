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
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExcelTestRunner extends SelcukesTestNGRunner {

    @DataProvider
    public Object[][] serialScenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }

        return filterScenarios(testNGCucumberRunner.provideScenarios(), isParallel.negate());
    }

    @DataProvider(parallel = true)
    public Object[][] parallelScenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }
        return filterScenarios(testNGCucumberRunner.provideScenarios(), isParallel);
    }

    public Object[][] filterScenarios(Object[][] scenarios, Predicate<Pickle> accept) {
        List<String> excelSuiteScenarios = ExcelUtils.runScenarios;
        excelSuiteScenarios = excelSuiteScenarios.stream().map(s -> s.split("::")[1]).collect(Collectors.toList());

        Object[][] scenarios1 = super.filter(scenarios, accept);
        List<Object[]> allScenarios = Arrays.asList(scenarios1);
        List<Object[]> modifiedList = excelSuiteScenarios.stream()
            .map(excelScenario -> allScenarios.stream()
                .filter(a -> a[0].toString().replaceAll("\"", "").equalsIgnoreCase(excelScenario)).findAny())
            .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        return modifiedList.toArray(Object[][]::new);
    }

}

