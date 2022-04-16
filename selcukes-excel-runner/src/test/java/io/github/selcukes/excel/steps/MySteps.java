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

package io.github.selcukes.excel.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.selcukes.excel.page.CommonPage;
import lombok.CustomLog;

@CustomLog
public class MySteps {
    private final CommonPage commonPage;

    public MySteps(CommonPage commonPage) {
        this.commonPage = commonPage;
    }

    @Given("I open {} page")
    public void openPage(String page) {
        logger.info(() -> page);

    }

    @Then("I see {string} in the title")
    public void title(String page) {
        logger.info(() -> page);
    }

    @Given("I kinda open {} page")
    public void kinda(String page) {
        logger.info(() -> page);
    }

    @Then("I am very happy")
    public void happy() {
        commonPage.getScenarioData()
            .forEach((k, v) -> logger.info(() -> String.format("Key: [%s] Values: [%s]%n", k, v)));
    }
}
