/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.junit.tests;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.CustomLog;

@CustomLog
public class Steps {
    @When("the Maker starts a game")
    public void theMakerStartsAGame() {
        logger.info(() -> "the Maker starts a game");

    }

    @Then("the Maker waits for a Breaker to join")
    public void theMakerWaitsForABreakerToJoin() {
        logger.info(() -> "the Maker waits for a Breaker to join");
    }
}
