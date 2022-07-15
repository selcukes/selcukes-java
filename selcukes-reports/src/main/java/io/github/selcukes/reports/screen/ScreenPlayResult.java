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
import io.github.selcukes.commons.fixer.TestResult;
import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.reports.enums.TestType;
import lombok.Getter;

@Getter
class ScreenPlayResult {

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
            failed = !testResult.getStatus().equalsIgnoreCase("PASS");
        }
    }


    private String getError(TestResult result) {
        if (!result.getStatus().equalsIgnoreCase("PASS")) {
            return "Exception: " + ExceptionHelper.getExceptionTitle(result.getThrowable());
        }
        return null;
    }

}