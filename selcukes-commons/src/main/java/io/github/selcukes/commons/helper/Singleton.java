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

import java.util.HashMap;
import java.util.Map;

import static io.github.selcukes.databind.utils.Reflections.newInstance;

/**
 * It's a singleton that holds a map of class names to instances of those classes
 */
public class Singleton {

    private static final Singleton INSTANCE = new Singleton();

    private final Map<String, Object> mapHolder = new HashMap<>();

    private Singleton() {
    }

    /**
     * If the class is not in the map, create a new instance of the class and put it in the map.  If it is in the map,
     * return the instance from the map.
     *
     * @param clazz The class to be instantiated.
     * @return An instance of the class passed in.
     */
    @SuppressWarnings("unchecked")
    public static <T> T instanceOf(final Class<T> clazz, final Object... initArgs) {
        if (!INSTANCE.mapHolder.containsKey(clazz.getName())) {
            T obj = newInstance(clazz, initArgs);
            INSTANCE.mapHolder.put(clazz.getName(), obj);
        }
        return (T) INSTANCE.mapHolder.get(clazz.getName());
    }
}
