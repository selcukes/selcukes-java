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
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        return properties.entrySet().stream().collect(of(
            entry -> String.valueOf(entry.getKey()),
            entry -> String.valueOf(entry.getValue())));
    }

    /**
     * Given a list of keys and a list of values, return a map of the keys to
     * the values, skipping any keys that are null or empty.
     * <p>
     * The method first creates a stream of the keys, then filters out any keys
     * that are null or empty. It then collects the stream into a map, using the
     * keys as the keys and the values as the values. If a key has no
     * corresponding value in the values list, or if the corresponding value is
     * null, the default value provided is used instead. The order of the keys
     * is preserved in the resulting map.
     * </p>
     *
     * @param  keys         the list of keys
     * @param  values       the list of values to be put into the map
     * @param  defaultValue the default value to use for missing or null values
     * @return              a map of the keys and values
     */
    public <T> Map<String, T> of(List<String> keys, List<T> values, T defaultValue) {
        return Streams.of(keys).boxed()
                .filter(i -> !StringHelper.isNullOrEmpty(keys.get(i)))
                .collect(of(keys::get, i -> i < values.size() && values.get(i) != null ? values.get(i) : defaultValue));
    }

    /**
     * It takes a map, creates a new map with a case-insensitive comparator, and
     * then puts all the entries from the original map into the new map.
     *
     * @param  map The map to be wrapped.
     * @return     A new TreeMap with the same keys and values as the original
     *             map, but with the keys in case-insensitive order.
     */
    public <V> Map<String, V> ofIgnoreCase(Map<String, V> map) {
        var newMap = new TreeMap<String, V>(CASE_INSENSITIVE_ORDER);
        newMap.putAll(map);
        return newMap;
    }

    /**
     * "Collects the elements of a stream into a TreeMap whose keys are case
     * insensitive."
     * <p>
     * The first two parameters are the same as the ones for the `toMap`
     * collector. The third parameter is a function that takes two values and
     * returns one of them. In this case, we're just returning the first value.
     * The fourth parameter is a supplier that creates a new instance of the
     * map. In this case, we're creating a new TreeMap with a custom comparator
     * that ignores case
     *
     * @param  keyMapper   A function that maps the input elements to keys.
     * @param  valueMapper a function that maps the input elements to the values
     *                     of the map
     * @return             A Collector that will collect the elements of a
     *                     stream into a TreeMap.
     */
    public <T, V> Collector<T, ?, TreeMap<String, V>> ofIgnoreCase(
            Function<? super T, String> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return Collectors.toMap(keyMapper, valueMapper,
            (u, v) -> u,
            () -> new TreeMap<>(CASE_INSENSITIVE_ORDER));
    }

    /**
     * Given a stream of elements, create a LinkedHashMap where the key is the
     * result of applying the keyMapper function to each element, and the value
     * is the result of applying the valueMapper function to each element.
     *
     * @param  keyMapper   A function that maps the input elements to keys.
     * @param  valueMapper a function that maps the input elements to the values
     *                     of the map
     * @return             A Collector that accumulates the input elements into
     *                     a new LinkedHashMap.
     */
    public <K, T, V> Collector<T, ?, LinkedHashMap<K, V>> of(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return Collectors.toMap(keyMapper, valueMapper,
            (u, v) -> u,
            LinkedHashMap::new);
    }

}
