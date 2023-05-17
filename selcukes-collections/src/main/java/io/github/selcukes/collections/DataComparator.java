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

package io.github.selcukes.collections;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@UtilityClass
public class DataComparator {

    /**
     * Compares the data in two tables and returns a table of differences.
     *
     * @param  expected      the expected table data
     * @param  actual        the actual table data
     * @param  foreignKey    the foreign key to use for comparing rows
     * @param  ignoreColumns a list of column keys to ignore when comparing
     * @param  <K>           the type of the key
     * @param  <V>           the type of the value
     * @return               a table of differences between the expected and
     *                       actual tables
     */
    public static <K, V> DataTable<String, String> diff(
            final DataTable<K, V> expected, final DataTable<K, V> actual, final K foreignKey,
            final List<K> ignoreColumns
    ) {
        return expected.stream()
                .map(expectedMap -> ofNullable(expectedMap.get(foreignKey))
                        .map(foreignKeyValue -> actual
                                .findFirst(actualMap -> foreignKeyValue.equals(actualMap.get(foreignKey)))
                                .map(actualMap -> diff(expectedMap, actualMap, ignoreColumns))
                                .orElseGet(() -> DataTable
                                        .of(rowStatus(foreignKey.toString(), foreignKeyValue.toString(), "", "Fail"))))
                        .orElseThrow(() -> new IllegalArgumentException(String.format(
                            "The expected table does not contain the foreign key [%s] column.", foreignKey))))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(DataTable::new));
    }

    /**
     * Compares the data in two tables using a default empty list of ignored
     * columns and returns a table of differences.
     *
     * @param  expected   the expected table data
     * @param  actual     the actual table data
     * @param  foreignKey the foreign key to use for comparing rows
     * @param  <K>        the type of the key
     * @param  <V>        the type of the value
     * @return            a table of differences between the expected and actual
     *                    tables
     */
    public static <K, V> DataTable<String, String> diff(
            DataTable<K, V> expected, DataTable<K, V> actual, K foreignKey
    ) {
        return diff(expected, actual, foreignKey, Collections.emptyList());
    }

    /**
     * Compares the data in two rows and returns a table of differences.
     *
     * @param  expected      the expected row data
     * @param  actual        the actual row data
     * @param  ignoreColumns a list of column keys to ignore when comparing
     * @param  <K>           the type of the key
     * @param  <V>           the type of the value
     * @return               a table of differences between the expected and
     *                       actual rows
     */
    public static <K, V> DataTable<String, String> diff(
            final Map<K, V> expected, final Map<K, V> actual, final List<K> ignoreColumns
    ) {
        return expected.entrySet().stream()
                .filter(entry -> !ignoreColumns.contains(entry.getKey()))
                .map(entry -> {
                    var expectedValue = entry.getValue();
                    var actualValue = actual.get(entry.getKey());
                    String status = Objects.equals(expectedValue, actualValue) ? "Pass" : "Fail";
                    return rowStatus(entry.getKey().toString(), expectedValue.toString(),
                        actualValue != null ? actualValue.toString() : "", status);
                })
                .collect(Collectors.toCollection(DataTable::new));
    }

    /**
     * Compares the data in two rows using a default empty list of ignored
     * columns and returns a table of differences.
     *
     * @param  expected the expected row data
     * @param  actual   the actual row data
     * @param  <K>      the type of the key
     * @param  <V>      the type of the value
     * @return          a table of differences between the expected and actual
     *                  rows
     */
    public static <K, V> DataTable<String, String> diff(Map<K, V> expected, Map<K, V> actual) {
        return diff(expected, actual, Collections.emptyList());
    }

    /**
     * Compares two lists of column data in a DataTable and returns a DataTable
     * containing the differences.
     *
     * @param  expected a List of expected values
     * @param  actual   a List of actual values
     * @param  <V>      the value type of the DataTable
     * @return          a DataTable containing the differences between the two
     *                  input lists
     */
    public static <V> DataTable<String, String> diff(
            final List<V> expected, final List<V> actual
    ) {
        List<V> sortedExpected = Lists.sortWithNulls(expected);
        List<V> sortedActual = Lists.sortWithNulls(actual);

        return Streams.of(0, Math.max(sortedExpected.size(), sortedActual.size()))
                .mapToObj(i -> {
                    var expectedValue = i < sortedExpected.size() ? sortedExpected.get(i) : null;
                    var actualValue = i < sortedActual.size() ? sortedActual.get(i) : null;
                    String status = Objects.equals(expectedValue, actualValue) ? "Pass" : "Fail";
                    return rowStatus("Row " + i, expectedValue != null ? expectedValue.toString() : "",
                        actualValue != null ? actualValue.toString() : "", status);
                })
                .collect(Collectors.toCollection(DataTable::new));
    }

    private Map<String, String> rowStatus(String fieldName, String expected, String actual, String status) {
        var row = new LinkedHashMap<String, String>();
        row.put("Field", fieldName);
        row.put("Expected", expected);
        row.put("Actual", actual);
        row.put("Status", status);
        return row;
    }

}
