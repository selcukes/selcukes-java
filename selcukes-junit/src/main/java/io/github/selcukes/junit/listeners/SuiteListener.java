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

import io.github.selcukes.commons.fixture.SelcukesFixture;
import io.github.selcukes.commons.properties.SelcukesRuntime;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.junit.platform.launcher.LauncherDiscoveryListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;

@CustomLog
public class SuiteListener implements LauncherDiscoveryListener {
    @SneakyThrows
    @Override
    public void launcherDiscoveryStarted(LauncherDiscoveryRequest request) {
        SelcukesFixture.setValidator(Class.forName("org.junit.jupiter.api.Assertions"));
        SelcukesRuntime.loadOptions();
    }
}
