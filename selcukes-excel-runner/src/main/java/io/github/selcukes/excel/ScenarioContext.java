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

import io.cucumber.java.Scenario;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScenarioContext {
    public static final ThreadLocal<String> testName = new InheritableThreadLocal<>();

    private String getFeatureName(Scenario scenario) {
        String featureName = scenario.getUri().getPath();
        featureName = featureName.substring(featureName.lastIndexOf("/") + 1, featureName.indexOf("."));
        return featureName;
    }

    public void setTestName(Scenario scenario) {
        testName.set(getFeatureName(scenario) + "::" + scenario.getName());
    }

    public String getTestName() {
        return testName.get();
    }

    public void removeTestName() {
        testName.remove();
    }

}
