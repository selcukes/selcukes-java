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

package io.github.selcukes.junit.listeners;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.testng.SelcukesRuntimeAdapter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


public class SuiteListener implements BeforeAllCallback, AfterAllCallback {
    private final Logger logger = LoggerFactory.getLogger(SuiteListener.class);

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

        logger.debug(() -> "Test Suite Execution started...");
        SelcukesRuntimeAdapter.getInstance().perform();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        logger.debug(() -> "Test Suite Execution finished...");
    }
}
