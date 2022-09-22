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

package io.github.selcukes.databind.utils;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * It's a wrapper around a `{@literal HashMap<String, Object>}` that allows you
 * to access the values in the map using a dot notation
 */
@UtilityClass
public class Maps {
    /**
     * It takes a `Properties` object and returns a
     * `{@literal Map<String, String>}` object
     *
     * @param  properties The properties object to convert to a map.
     * @return            A map of the properties.
     */
    public static Map<String, String> of(Properties properties) {
        return properties.entrySet().stream().collect(
            Collectors.toMap(
                entry -> String.valueOf(entry.getKey()),
                entry -> String.valueOf(entry.getValue()),
                (prev, next) -> next, LinkedHashMap::new));
    }

    /**
     * It takes two lists of strings, and returns a map of the non-empty keys to
     * their corresponding values
     *
     * @param  keys   List of keys
     * @param  values The values to be put into the map.
     * @return        A map of the keys and values.
     */
    public static Map<String, String> of(List<String> keys, List<String> values) {
        return IntStream.range(0, keys.size()).boxed()
                .filter(i -> !StringHelper.isNullOrEmpty(keys.get(i)))
                .collect(Collectors.toMap(keys::get, values::get));
    }

    /**
     * It takes a map, creates a new map with a case-insensitive comparator, and
     * then puts all the entries from the original map into the new map.
     *
     * @param  map The map to be wrapped.
     * @return     A new TreeMap with the same keys and values as the original
     *             map, but with the keys in case-insensitive order.
     */
    public <V> Map<String, V> caseInsensitive(Map<String, V> map) {
        var newMap = new TreeMap<String, V>(CASE_INSENSITIVE_ORDER);
        newMap.putAll(map);
        return newMap;
    }
}
