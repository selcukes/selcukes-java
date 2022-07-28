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

@UtilityClass
public class Maps {
    public static Map<String, String> of(Properties properties) {
        return properties.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> String.valueOf(entry.getKey()),
                        entry -> String.valueOf(entry.getValue()),
                        (prev, next) -> next, LinkedHashMap::new
                ));
    }

    public static Map<String, String> of(List<String> keys, List<String> values) {
        return IntStream.range(0, keys.size()).boxed()
                .filter(i -> !StringHelper.isNullOrEmpty(keys.get(i))).collect(Collectors.toMap(keys::get, values::get));
    }

    public static <V> Map<String, V> caseInsensitive(Map<String, V> map) {
        var newMap = new TreeMap<String, V>(CASE_INSENSITIVE_ORDER);
        newMap.putAll(map);
        return newMap;
    }
}
