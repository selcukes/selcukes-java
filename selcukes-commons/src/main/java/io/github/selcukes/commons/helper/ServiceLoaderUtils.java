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

import io.github.selcukes.collections.Streams;
import io.github.selcukes.commons.exception.ConfigurationException;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * "Load the first service of the given type from the given class loader."
 * <p>
 * The function is generic, so it can be used to load any type of service. It
 * uses the Java ServiceLoader to load the service. The ServiceLoader is a Java
 * class that loads services from a file called
 * META-INF/services/{@literal <service-type>}. The file contains a list of
 * fully qualified class names of the services
 */
@UtilityClass
public class ServiceLoaderUtils {
    /**
     * "Load the first service of the given type from the given class loader."
     * <p>
     * The function is generic, so it can be used to load any type of service.
     * It uses the Java ServiceLoader to load the service. The ServiceLoader is
     * a Java class that loads services from a file called
     * META-INF/services/{@literal <service-type>}. The file contains a list of
     * fully qualified class names of the services
     *
     * @param  type        The type of the service to load.
     * @param  classLoader The class loader to use for loading the service.
     * @return             A single instance of the class that is passed in.
     */
    public <T> T loadFirst(final Class<T> type, final ClassLoader classLoader) {
        return Streams.of(ServiceLoader.load(type, classLoader).iterator())
                .findFirst()
                .orElseThrow(
                    () -> new ConfigurationException("Could not load {" + type + "}"));
    }

    /**
     * "Load all services of a given type from the given class loader."
     * <p>
     * The first thing to notice is that the function is generic. This is
     * because the function is designed to work with any type of service
     *
     * @param  type        The type of the service to load.
     * @param  classLoader The class loader to use for loading the service.
     * @return             A list of all the services of the given type.
     */
    public <T> List<T> load(final Class<T> type, final ClassLoader classLoader) {
        return Streams.of(ServiceLoader.load(type, classLoader).iterator())
                .collect(Collectors.toList());
    }
}
