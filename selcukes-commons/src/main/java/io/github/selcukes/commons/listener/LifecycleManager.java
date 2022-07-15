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

import io.github.selcukes.commons.fixer.TestResult;
import lombok.CustomLog;

import java.util.List;
import java.util.function.BiConsumer;

@CustomLog

public class LifecycleManager implements TestLifecycleListener {

    private final List<TestLifecycleListener> testListeners;

    public LifecycleManager(final List<TestLifecycleListener> testListeners) {
        this.testListeners = testListeners;
    }

    @Override
    public void beforeSuite(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::beforeSuite, result);
    }

    @Override
    public void afterSuite(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::afterSuite, result);
    }

    @Override
    public void beforeTest(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::beforeTest, result);
    }

    @Override
    public void afterTest(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::afterTest, result);
    }

    @Override
    public void beforeStep(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::beforeStep, result);
    }

    @Override
    public void afterStep(TestResult result) {
        runSafely(testListeners, TestLifecycleListener::afterStep, result);
    }

    private void runSafely(List<TestLifecycleListener> testListeners,
                           BiConsumer<TestLifecycleListener, TestResult> method,
                           TestResult result) {
        testListeners.forEach(listener -> {
            try {
                method.accept(listener, result);
            } catch (Exception e) {
                logger.error(e, () -> "Could not invoke listener method");
            }
        });
    }


}
