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

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.exception.ConfigurationException;
import io.github.selcukes.databind.utils.Streams;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

@UtilityClass
public class ServiceLoaderUtils {
    public <T> T loadFirst(final Class<T> type, final ClassLoader classLoader) {
        return Streams.of(ServiceLoader.load(type, classLoader).iterator())
                .findFirst()
                .orElseThrow(
                        () -> new ConfigurationException("Could not load {" + type + "}")
                );
    }

    public <T> List<T> load(final Class<T> type, final ClassLoader classLoader) {
        return Streams.of(ServiceLoader.load(type, classLoader).iterator())
                .collect(Collectors.toList());
    }
}
