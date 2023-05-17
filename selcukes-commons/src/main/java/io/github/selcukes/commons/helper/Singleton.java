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

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.selcukes.collections.Reflections.newInstance;

/**
 * The `Singleton` class provides a thread-safe way to create a single instance
 * of a class. It uses a `ConcurrentHashMap` to store the instances and ensures
 * that only one instance is created for a given class and constructor
 * arguments.
 */
@SuppressWarnings("java:S6548")
public class Singleton {

    private Singleton() {
        // private constructor to prevent instantiation from outside the class
    }

    /**
     * Returns an instance of the specified class with the given constructor
     * arguments. If an instance with the same constructor arguments exists in
     * the map, that instance is returned. Otherwise, a new instance is created,
     * stored in the map, and returned.
     *
     * @param  clazz    the class to be instantiated
     * @param  initArgs the constructor arguments for the class
     * @return          an instance of the specified class with the given
     *                  constructor arguments
     */
    public static <T> T instanceOf(final Class<T> clazz, final Object... initArgs) {
        return SingletonHolder.INSTANCE.getOrCreate(clazz, initArgs);
    }

    private static class SingletonHolder {
        private final ConcurrentHashMap<String, Object> mapHolder = new ConcurrentHashMap<>();

        private static final SingletonHolder INSTANCE = new SingletonHolder();

        /**
         * Returns an instance of the specified class with the given constructor
         * arguments. If an instance with the same constructor arguments exists
         * in the map, that instance is returned. Otherwise, a new instance is
         * created, stored in the map, and returned.
         *
         * @param  clazz    the class to be instantiated
         * @param  initArgs the constructor arguments for the class
         * @return          an instance of the specified class with the given
         *                  constructor arguments
         */
        public <T> T getOrCreate(Class<T> clazz, Object... initArgs) {
            String className = clazz.getName();
            String key = className + Arrays.hashCode(initArgs);
            return clazz.cast(mapHolder.computeIfAbsent(key, k -> newInstance(clazz, initArgs)));
        }
    }
}
