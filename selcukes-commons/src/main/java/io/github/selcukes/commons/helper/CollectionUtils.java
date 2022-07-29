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
import java.util.stream.Collectors;

/**
 * This class contains a bunch of static methods that operate on collections.
 */
@UtilityClass
public class CollectionUtils {
    /**
     * Convert a list of lists of strings into a 2D array of strings.
     *
     * @param cells The list of lists of strings to convert to a 2D array.
     * @return A 2D array of Strings
     */
    public String[][] toArray(final List<List<String>> cells) {
        return cells.stream()
                .map(row -> row.toArray(String[]::new))
                .toArray(String[][]::new);
    }

    /**
     * Return a list of trimmed strings from the given list of strings.
     *
     * @param list The list to trim.
     * @return A list of strings that have been trimmed.
     */
    public List<String> trim(final List<String> list) {
        return list.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * It takes an enum class and returns a list of all the enum values as strings.
     *
     * @param enumData The enum class that you want to convert to a list.
     * @return A list of strings.
     */
    public List<String> toList(final Class<? extends Enum<?>> enumData) {
        return Arrays.stream(enumData.getEnumConstants()).map(Enum::toString).collect(Collectors.toList());
    }

}
