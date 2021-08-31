/*
 *
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
 *
 */

package io.github.selcukes.core.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class SelcukesDriverPluginLoader {
    private final List<DriverPlugin> pluginList;

    public SelcukesDriverPluginLoader() {
        pluginList = new ArrayList<>();
        for (DriverPlugin driverPlugin : ServiceLoader.load(DriverPlugin.class)) {
            pluginList.add(driverPlugin);
        }
    }

    public <T extends DriverPlugin> List<T> getMatchingPlugins(final Class<T> requestedPluginInterface) {
        List<T> matchingPlugins = new ArrayList<>();
        for (DriverPlugin driverPlugin : pluginList) {
            if (requestedPluginInterface.isAssignableFrom(driverPlugin.getClass())) {
                T plugin = requestedPluginInterface.cast(driverPlugin);
                matchingPlugins.add(plugin);
            }
        }
        return matchingPlugins;
    }
}
