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

package io.github.selcukes.reports.screen;

import io.cucumber.java.Scenario;
import io.github.selcukes.commons.fixture.TestResult;
import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.reports.enums.TestType;
import lombok.Getter;

import java.util.function.Predicate;

@Getter
class ScreenPlayResult {

    private static final Predicate<String> IS_NOT_PASS = s -> !s.equalsIgnoreCase("PASSED");
    private String testName;
    private String status;
    private TestType testType;
    private String errorMessage;
    private boolean failed;

    public <T> ScreenPlayResult(T result) {
        extractResult(result);
    }

    private <T> void extractResult(T result) {
        if (result instanceof Scenario) {
            testName = ((Scenario) result).getName().replace(" ", "_");
            status = ((Scenario) result).getStatus().toString();
            testType = TestType.CUCUMBER;
            failed = ((Scenario) result).isFailed();
        } else if (result instanceof TestResult) {
            var testResult = ((TestResult) result);
            testName = testResult.getName().replace(" ", "_");
            status = testResult.getStatus();
            testType = TestType.TESTNG;
            errorMessage = getError(testResult);
            failed = IS_NOT_PASS.test(testResult.getStatus());
        }
    }

    private String getError(TestResult result) {
        if (IS_NOT_PASS.test(result.getStatus())) {
            return "Exception: " + ExceptionHelper.getExceptionTitle(result.getThrowable());
        }
        return null;
    }

}
