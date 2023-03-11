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

import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A generic data table that stores data in rows and columns.
 * <p>
 * The data table is implemented as a list of maps where each map represents a
 * row in the table,
 * <p>
 * and the keys of the maps represent the column names.
 *
 * @param <K> the type of column keys in the data table
 * @param <T> the type of column values in the data table
 */
@Getter
public class DataTable<K, T> {

    /**
     * The list of maps representing the data table.
     */
    @NonNull
    private final List<Map<K, T>> rows;

    /**
     * Constructs a new, empty DataTable.
     */
    public DataTable() {
        rows = new ArrayList<>();
    }

    /**
     * Returns a list of column keys from the first row of the data table.
     * 
     * @return                       a list of column keys
     * @throws IllegalStateException if the data table is empty
     */
    public List<K> getColumns() {
        if (rows.isEmpty()) {
            throw new IllegalStateException("Data table is empty");
        }
        return List.copyOf(rows.get(0).keySet());
    }

    /**
     * Adds a new row to the data table.
     *
     * @param row the map representing the new row to add
     */
    @Synchronized
    public void addRow(@NonNull Map<K, T> row) {
        rows.add(Map.copyOf(row));
    }

    /**
     * Adds multiple rows to the data table.
     *
     * @param rows the list of maps representing the rows to add
     */
    @Synchronized
    public void addRows(@NonNull List<Map<K, T>> rows) {
        this.rows.addAll(List.copyOf(rows));
    }

    /**
     * Returns an unmodifiable list of all the rows in the data table.
     *
     * @return an unmodifiable list of rows
     */
    @Synchronized
    public List<Map<K, T>> getRows() {
        return List.copyOf(rows);
    }

    /**
     * Returns the first row that matches the given predicate.
     *
     * @param  predicate                the predicate to match against rows
     * @return                          the first row that matches the predicate
     * @throws IllegalArgumentException if no row matches the predicate
     */
    @Synchronized
    public Map<K, T> getRow(Predicate<Map<K, T>> predicate) {
        return rows.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Row not found for predicate: " + predicate));
    }

    /**
     * Groups the rows by the value in the specified column and returns a map
     * where the keys are the unique column values and the values are the lists
     * of rows that contain those values.
     *
     * @param  key the column key to group rows by
     * @return     a map of rows grouped by the specified column value
     */
    @Synchronized
    public Map<T, List<Map<K, T>>> getRowsGroupedByColumn(@NonNull K key) {
        return getRows().stream()
                .filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(map -> map.get(key),
                    Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList)));
    }

    /**
     * Updates each row in the data table with the result of applying the given
     * function to the row.
     *
     * @param function the function to apply to each row
     */
    @Synchronized
    public void updateRows(Function<Map<K, T>, Map<K, T>> function) {
        rows.replaceAll(function::apply);
    }

    /**
     * Returns a list of values for a given column.
     *
     * @param  columnName               the column name
     * @return                          a list of values for the given column
     * @throws IllegalArgumentException if the column name is not found in the
     *                                  table
     */
    @Synchronized
    public List<T> getColumnValues(@NonNull K columnName) {
        int columnIndex = getColumns().indexOf(columnName);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return List.copyOf(rows).stream()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the data table contains the expected row.
     *
     * @param  expectedRow          the map representing the expected row
     * @return                      true if the data table contains the expected
     *                              row, false otherwise
     * @throws NullPointerException if the expected row is null
     */
    @Synchronized
    public boolean contains(@NonNull Map<K, T> expectedRow) {
        return rows.stream().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
    }

    /**
     * Returns a new DataTable instance initialized with the data provided as a
     * List of Map objects.
     *
     * @param  dataList             a list of Maps representing the data rows to
     *                              add to the new DataTable instance
     * @param  <K>                  the type of the column keys
     * @param  <V>                  the type of the column values
     * @return                      a new DataTable instance
     * @throws NullPointerException if dataList is null
     */
    public static <K, V> DataTable<K, V> of(@NonNull List<Map<K, V>> dataList) {
        var dataTable = new DataTable<K, V>();
        dataTable.addRows(dataList);
        return dataTable;
    }
}
