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

package io.github.selcukes.commons.helper;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.selcukes.databind.utils.StringHelper.isNullOrEmpty;

@UtilityClass
public class CollectionUtils {
    public String[][] toArray(List<List<String>> cells) {
        return cells.stream()
            .map(row -> row.toArray(String[]::new))
            .toArray(String[][]::new);
    }

    public Map<String, String> toMap(List<String> keys, List<String> values) {
        return IntStream.range(0, keys.size()).boxed()
            .filter(i -> isNullOrEmpty(keys.get(i))).collect(Collectors.toMap(keys::get, values::get));
    }

    public List<String> trim(List<String> list) {
        return list.stream()
            .map(String::trim)
            .collect(Collectors.toList());
    }

    public List<String> toList(Class<? extends Enum<?>> enumData) {
        return Arrays.stream(enumData.getEnumConstants()).map(Enum::toString).collect(Collectors.toList());
    }
}
