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

package io.github.selcukes.commons.listener;

import io.github.selcukes.commons.fixture.TestResult;

public interface TestLifecycleListener {
    default void beforeSuite(TestResult result) {
        // do nothing
    }

    default void afterSuite(TestResult result) {
        // do nothing
    }

    default void beforeTest(TestResult result) {
        // do nothing
    }

    default void beforeAfterTest(TestResult result) {
        // do nothing
    }

    default void afterTest(TestResult result) {
        // do nothing
    }

    default void beforeStep(TestResult result) {
        // do nothing
    }

    default void afterStep(TestResult result) {
        // do nothing
    }
}
